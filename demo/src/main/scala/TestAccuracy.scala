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
import org.apache.spark.sql.functions.{col, monotonically_increasing_id, sum, udf}

import scala.collection.mutable.ArrayBuffer

object TestAccuracy {
  def posTaggerEnglish_ml(
                           spark: SparkSession,
                           testPath: String,
                           modelPath: String
                         ): Unit = {
    import spark.implicits._


    val testInput = spark.read.text(testPath).as[String]
    val extractedTokensTags = testInput.map(s => s.split("\t")
      .filter(x => !x.startsWith("#"))).filter(x => x.length > 0)
      .map{x => if(x.length > 1){x(1) + "_" + x(3)} else{"endOfLine"}}
      .map(x => x.mkString)
      .reduce((s1, s2) => s1 + " " + s2).split(" endOfLine | endOfLine")

    val testTokensTagsDF = spark.sparkContext.parallelize(extractedTokensTags)
      .toDF("arrays")
      .withColumn("id", monotonically_increasing_id)
      .withColumn("testTokens", extractTokens($"arrays"))
      .withColumn("testTags", extractTags($"arrays"))
      .drop("arrays")

    println("Count of extracted POS tags DF: ", testTokensTagsDF.count())
    testTokensTagsDF.filter("id=4").show(false)

    //Convert CoNLL-U to Text for training the test dataset
    val testSentencesDF = testInput
      .map(s => s.split("\t").filter(x => x.startsWith("# text")))
      .flatMap(x => x)
      .map(x => x.replace("# text = ", ""))
      .filter(x => x.length > 0)
      .toDF("content")
      .withColumn("id", monotonically_increasing_id)

    println("Count of extracted sentences DF: ", testSentencesDF.count())
    testSentencesDF.filter("id=4").show(false)
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


    val joinedDF = manualPipelineDF
      .join(testTokensTagsDF, Seq("id"))
      .withColumn("predictedTokensLength", calLengthOfArray($"predictedTokens"))
      .withColumn("predictedTagsLength", calLengthOfArray($"predictedTags"))
      .withColumn("testTokensLength", calLengthOfArray($"testTokens"))
      .withColumn("testTagsLength", calLengthOfArray($"testTags"))
      .withColumn("equalTags", col("predictedTagsLength") === col("testTagsLength"))

    joinedDF.show
    joinedDF.filter("id=4").show(false)
    joinedDF.printSchema()

    // Due to different methods of tokenizing, the number of tokens may be different, we need the ones that are equal for accuracy test.
    println("Equal number of tags between test and training:", joinedDF.filter("equalTags = true").count)

    val accuracyDF = joinedDF.filter("equalTags = true")
      .withColumn("correctPredictTags", compareTwoTagsArray($"testTags", $"predictedTags"))

    val sumOfAllTags = accuracyDF.agg(
      sum("testTagsLength").as("sum_testTagsLength"),
      sum("predictedTagsLength").as("sum_predictedTagsLength"),
      sum("correctPredictTags").as("sum_correctPredictTags")
    ).withColumn("accuracy", ($"sum_correctPredictTags" * 100) / $"sum_predictedTagsLength")
    sumOfAllTags.first()
    sumOfAllTags.show()

  }

  def posTaggerFrench_ml(
                          spark: SparkSession,
                          testPath: String,
                          modelPath: String
                        ): Unit = {
    import spark.implicits._


    val testInput = spark.read.text(testPath).as[String]
    val extractedTokensTags = testInput.map(s => s.split("\t")
      .filter(x => !x.startsWith("#"))).filter(x => x.length > 0)
      .map{x => if(x.length > 1){x(1) + "-" + x(3)} else{"endOfLine"}}
      .map(x => x.mkString)
      .reduce((s1, s2) => s1 + " " + s2).split(" endOfLine | endOfLine")

    val testTokensTagsDF = spark.sparkContext.parallelize(extractedTokensTags)
      .toDF("arrays")
      .withColumn("id", monotonically_increasing_id)
      .withColumn("testTokens", extractFrenchTokens($"arrays"))
      .withColumn("testTags", extractFrenchTags($"arrays"))
      .drop("arrays")

    println("Count of extracted POS tags DF: ", testTokensTagsDF.count())
    testTokensTagsDF.show()

    //Convert CoNLL-U to Text for training the test dataset
    val testSentencesDF = testInput
      .map(s => s.split("\t").filter(x => x.startsWith("# text")))
      .flatMap(x => x)
      .map(x => x.replace("# text = ", ""))
      .filter(x => x.length > 0)
      .toDF("content")
      .withColumn("id", monotonically_increasing_id)

    println("Count of extracted sentences DF: ", testSentencesDF.count())
    testSentencesDF.show()
    //Load pre-trained pos model
    val pipeLinePOSTaggerModel = PipelineModel.read.load(modelPath)

    val manualPipelineDF = pipeLinePOSTaggerModel.transform(testSentencesDF)
      .select(
        $"id",
        $"token.result".alias("predictedTokens"),
        $"pos.result".alias("predictedTags")
      )
    println("Count of trained sentences DF: ", manualPipelineDF.count())
    manualPipelineDF.show()

    val joinedDF = manualPipelineDF.join(testTokensTagsDF, Seq("id"))
    //      .withColumn("predictedTokensLength", calLengthOfArray($"predictedTokens"))
    //      .withColumn("predictedTagsLength", calLengthOfArray($"predictedTags"))
    //      .withColumn("testTokensLength", calLengthOfArray($"testTokens"))
    //      .withColumn("testTagsLength", calLengthOfArray($"testTags"))
    //      .withColumn("equalTags", col("predictedTagsLength") === col("testTagsLength"))

    println("Count of joined DF: ", joinedDF.count())
    joinedDF.show(500)
    joinedDF.filter("id=4").show()
    joinedDF.printSchema()
    //    joinedDF.filter("equalTags = true").count
    // Due to different methods of tokenizing, the number of tokens may be different, we need the ones that are equal for accuracy test.
    //    println("Equal number of tags between test and training:", joinedDF.filter("equalTags = true").count)

    //    val accuracyDF = joinedDF.filter("equalTags = true")
    //      .withColumn("correctPredictTags", compareTwoTagsArray($"testTags", $"predictedTags"))
    //
    //    val sumOfAllTags = accuracyDF.agg(
    //      sum("testTagsLength").as("sum_testTagsLength"),
    //      sum("predictedTagsLength").as("sum_predictedTagsLength"),
    //      sum("correctPredictTags").as("sum_correctPredictTags")
    //    ).withColumn("accuracy", ($"sum_correctPredictTags" * 100) / $"sum_predictedTagsLength")
    //    sumOfAllTags.first()
    //    sumOfAllTags.show()

  }

  private def extractTokens= udf { docs: String =>
    var tokensArray = ArrayBuffer[String]()
    val splitedArray = docs.split("\\s+")
    for(e <- splitedArray){
      tokensArray += e.split("_")(0)
    }
    tokensArray
  }
  private def extractTags= udf { docs: String =>
    var tagsArray = ArrayBuffer[String]()
    val splitedArray = docs.split("\\s+")
    for(e <- splitedArray){
      tagsArray += e.split("_")(1)
    }
    tagsArray
  }
  private def extractFrenchTokens= udf { docs: String =>
    var tokensArray = ArrayBuffer[String]()
    val splitedArray = docs.split("\\s+")
    for(e <- splitedArray){
      tokensArray += e.split("-")(0)
    }
    tokensArray
  }
  private def extractFrenchTags= udf { docs: String =>
    var tagsArray = ArrayBuffer[String]()
    val splitedArray = docs.split("\\s+")
    for(e <- splitedArray){
      tagsArray += e.split("-")(1)
    }
    tagsArray
    //    splitedArray
  }
  private def calLengthOfArray= udf { docs: Seq[String] =>
    docs.length
  }
  private def compareTwoTagsArray= udf { (testTags: Seq[String], predictTags: Seq[String]) =>
    var correctTagsCount = 0
    val testTagsWithIndex = testTags.zipWithIndex.map{case (k,v) => (v,k)}
    val predictTagsWithIndex = predictTags.zipWithIndex.map{case (k,v) => (v,k)}

    for (e <- testTagsWithIndex){
      if (predictTagsWithIndex.find(_._1 == e._1).get._2 == e._2){
        correctTagsCount+=1
      }
      correctTagsCount
    }
    /*for (i <- testTags.indices){
      if(i >= predictTags.length && i >= testTags.length) {
        if(testTags(i) == predictTags(i))
          correctTagsCount+=1
      } else{
        correctTagsCount = 0
      }
    }*/
    correctTagsCount
  }
}
