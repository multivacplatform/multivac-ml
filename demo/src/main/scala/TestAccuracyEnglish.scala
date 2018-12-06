/*
 * MIT License
 *
 * Copyright (c) 2018. Maziyar Panahi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, monotonically_increasing_id, sum, udf, when}
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.hadoop.io.{LongWritable, Text}

import scala.collection.mutable.ArrayBuffer

object TestAccuracyEnglish {
  def posTaggerEnglish_ml(spark: SparkSession, pathCoNNLFile: String, modelPath: String): Unit = {
    import spark.implicits._

    val conf = new org.apache.hadoop.mapreduce.Job().getConfiguration
    conf.set("textinputformat.record.delimiter", "\n\n")

    val usgRDD = spark.sparkContext.newAPIHadoopFile(
      pathCoNNLFile, classOf[TextInputFormat], classOf[LongWritable], classOf[Text], conf)
      .map{ case (_, v) => v.toString }

    val conllSentencesDF = usgRDD.map(s => s.split("\n").filter(x => !x.startsWith("#"))).filter(x => x.length > 0).toDF("sentence")

    conf.set("textinputformat.record.delimiter", "")

    val testTokensTagsDF = conllSentencesDF
      .withColumn("id", monotonically_increasing_id)
      .withColumn("testTokens", extractTokens($"sentence"))
      .withColumn("testTags", extractTags($"sentence"))
      .drop("sentence")

    println("Count of CoNLL extracted sentence from tokens_tags: ", testTokensTagsDF.count())
    testTokensTagsDF.filter("id=4").show(false)
    testTokensTagsDF.show

    // Convert CoNLL-U to Text for training the test Dataframe
    // This DataFrame will be used for testing the POS Model (SentenceDetector, Tokenizer, and POS tagger)
    val testSentencesDF = spark.read.text(pathCoNNLFile).as[String]
      .map(s => s.split("\t").filter(x => x.startsWith("# text")))
      .flatMap(x => x)
      .map(x => x.replace("# text = ", ""))
      .filter(x => x.length > 0)
      .toDF("content")
      .withColumn("id", monotonically_increasing_id)

    println("Count of CoNLL extracted sentences from text DF: ", testSentencesDF.count())
    testSentencesDF.filter("id=4").show(false)
    testSentencesDF.show
    // check if the number of sentences from tokens is equal the number of sentences from the text

    //Load pre-trained pos model
    val pipeLinePOSTaggerModel = PipelineModel.read.load(modelPath)

    val manualPipelineDF = pipeLinePOSTaggerModel.transform(testSentencesDF)
      .select(
        $"id",
        $"token.result".alias("predictedTokens"),
        $"pos.result".alias("predictedTags")
      )
    println("Count of trained sentences DF: ", manualPipelineDF.count())
    manualPipelineDF.filter("id=4").show(false)
    manualPipelineDF.show
    val joinedDF = manualPipelineDF
      .join(testTokensTagsDF, Seq("id"))
      .withColumn("predictedTokensLength", calLengthOfArray($"predictedTokens"))
      .withColumn("predictedTagsLength", calLengthOfArray($"predictedTags"))
      .withColumn("testTokensLength", calLengthOfArray($"testTokens"))
      .withColumn("testTagsLength", calLengthOfArray($"testTags"))
      .withColumn("tokensDiffFromTest", $"testTokensLength" - $"predictedTokensLength")
//      .withColumn("missingTokens",  when($"tokensDiffFromTest" < 0, -$"tokensDiffFromTest").otherwise($"tokensDiffFromTest"))
      .withColumn("equalTags", col("predictedTagsLength") === col("testTagsLength"))

    joinedDF.show
    joinedDF.filter("id=4").show(false)
    joinedDF.printSchema()

    val accuracyDF = joinedDF
      .withColumn("correctPredictTokenTag", compareTwoTagsArray($"testTokens", $"testTags", $"predictedTokens", $"predictedTags"))
      .withColumn("correctPredictToken", tokenMatcher($"testTokens", $"predictedTokens"))

    val sumOfAllTags = accuracyDF.agg(
      sum("testTagsLength").as("TotalWordsInTest"),
      sum("predictedTagsLength").as("TotalWordsPredicted"),
      sum("correctPredictToken").as("TotalTokenMatches"),
      sum("correctPredictTokenTag").as("TotalTokenTagMatches")
//      sum("missingTokens").as("TotalTokenMisses")
    )
      .withColumn("accuracy_with_missing_tokens", ($"TotalTokenTagMatches" * 100) / $"TotalWordsInTest")
      .withColumn("accuracy_without_missing_tokens", ($"TotalTokenTagMatches" * 100) / $"TotalWordsPredicted")
      .withColumn("SimpleAccuracy", ($"TotalTokenTagMatches" * 100) / $"TotalTokenMatches")

    sumOfAllTags.first()
    sumOfAllTags.show()
  }

  private def extractTokens= udf { docs: Seq[String] =>
    var tokensArray = ArrayBuffer[String]()
    for(e <- docs){
      val splitedArray = e.split("\t")
      tokensArray += splitedArray(1)
    }
    tokensArray
  }

  private def extractTags= udf { docs: Seq[String] =>
    var tagsArray = ArrayBuffer[String]()
    for(e <- docs){
      val splitedArray = e.split("\t")
      tagsArray += splitedArray(3)
    }
    tagsArray
  }

  private def calLengthOfArray= udf { docs: Seq[String] =>
    docs.length
  }

  private def compareTwoTagsArray= udf { (testTokens: Seq[String], testTags: Seq[String], predictTokens: Seq[String],predictTags: Seq[String]) =>
    var correctTagsCount = 0
    val testTagsWithTokens = testTokens.zip(testTags).map{case (k,v) => (v,k)}
    val predictTagsWithTokens = predictTokens.zip(predictTags).map{case (k,v) => (v,k)}
    for (e <- predictTagsWithTokens) {
      if (testTagsWithTokens.contains(e)) {
        correctTagsCount+=1
      }
      correctTagsCount
    }
    correctTagsCount
  }
  private def tokenMatcher= udf { (testTokens: Seq[String], predictTokens: Seq[String]) =>
    var correctTokensCount = 0
    for (e <- predictTokens) {
      if (testTokens.contains(e)) {
        correctTokensCount+=1
      }
      correctTokensCount
    }
    correctTokensCount
  }
}
