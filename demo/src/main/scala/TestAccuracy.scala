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
import org.apache.spark.sql.functions.{avg, col, explode, monotonically_increasing_id, sum, udf, round, when}
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.hadoop.io.{LongWritable, Text}

import scala.collection.mutable.ArrayBuffer
import scala.util.control.Breaks._

object TestAccuracy {

  /** evaluatePOSModel
    * @note
    * Evaluating POS Tagger Model: accuracy, precision, recall, f1-score
    * accuracy: `how many you got right`
    * true positives: two tags matched
    * false positives: two tags for a word didn't matched
    * false negatives:
    *
    */
  def evaluatePOSModel(pathCoNNLFile: String, modelPath: String): Unit = {

    val spark = SessionBuilder.buildSession()
    import spark.implicits._

    val debugIsOn = true

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

    if(debugIsOn){
      println("Count of CoNLL extracted sentence from tokens_tags: ", testTokensTagsDF.count())
      testTokensTagsDF.filter("id=4").show(false)
      testTokensTagsDF.show
    }

    // Convert CoNLL-U to Text for training the test Dataframe
    // This DataFrame will be used for testing the POS Model (SentenceDetector, Tokenizer, and POS tagger)
    val testSentencesDF = spark.read.text(pathCoNNLFile).as[String]
      .map(s => s.split("\t").filter(x => x.startsWith("# text")))
      .flatMap(x => x)
      .map(x => x.replace("# text = ", ""))
      .filter(x => x.length > 0)
      .toDF("content")
      .withColumn("id", monotonically_increasing_id)

    if(debugIsOn) {
      println("Count of CoNLL extracted sentences from text DF: ", testSentencesDF.count())
      testSentencesDF.filter("id=4").show(false)
      testSentencesDF.show
      // check if the number of sentences from tokens is equal the number of sentences from the text
    }
    //Load pre-trained pos model
    val pipeLinePOSTaggerModel = PipelineModel.read.load(modelPath)

    val manualPipelineDF = pipeLinePOSTaggerModel.transform(testSentencesDF)
      .select(
        $"id",
        $"token.result".alias("predictedTokens"),
        $"pos.result".alias("predictedTags")
      )
    if(debugIsOn) {
      println("Count of trained sentences DF: ", manualPipelineDF.count())
      manualPipelineDF.filter("id=4").show(false)
      manualPipelineDF.show
    }
    val joinedDF = manualPipelineDF
      .join(testTokensTagsDF, Seq("id"))
      .withColumn("predictedTokensLength", calLengthOfArray($"predictedTokens"))
      .withColumn("predictedTagsLength", calLengthOfArray($"predictedTags"))
      .withColumn("testTokensLength", calLengthOfArray($"testTokens"))
      .withColumn("testTagsLength", calLengthOfArray($"testTags"))
      .withColumn("tokensDiffFromTest", $"testTokensLength" - $"predictedTokensLength")
      .withColumn("missingTokens", extractMissingTokens($"testTokens", $"predictedTokens"))
      .withColumn("equalTags", col("predictedTagsLength") === col("testTagsLength"))

    if(debugIsOn) {
      joinedDF.show
      joinedDF.filter("id=4").show(false)
      joinedDF.printSchema()
      joinedDF.select($"missingTokens", explode($"missingTokens").as("tokens")).groupBy("tokens").count.orderBy($"count".desc).show
    }
    /*
    ADJ: adjective
    ADP: adposition
    ADV: adverb
    AUX: auxiliary
    CCONJ: coordinating conjunction
    DET: determiner
    INTJ: interjection
    NOUN: noun
    NUM: numeral
    PART: particle
    PRON: pronoun
    PROPN: proper noun
    PUNCT: punctuation
    SCONJ: subordinating conjunction
    SYM: symbol
    VERB: verb
    X: other
    */

    val scorePerTagDF = joinedDF.select("testTokens", "testTags", "predictedTokens", "predictedTags")
      .flatMap(row => {
        val newColumns: ArrayBuffer[Seq[TagScore]] = ArrayBuffer()

        var metrics: ArrayBuffer[TagScore] = ArrayBuffer()

        val testTagsWithTokens = row.get(0).asInstanceOf[Seq[String]].zip(row.getSeq(1).asInstanceOf[Seq[String]]).map{case (k,v) => (k,v)}
        var predictTagsWithTokens = row.getSeq(2).asInstanceOf[Seq[String]].zip(row.getSeq(3).asInstanceOf[Seq[String]]).map{case (k,v) => (k,v)}

        var totalTagCount = 0
        var totalTokenCount = 0
        var lastMatchIndex = 0

        // Reset counters
        totalTagCount = 0
        totalTokenCount = 0
        lastMatchIndex = 0

        totalTokenCount += 1
        totalTagCount += 1

        for ((t,i) <- testTagsWithTokens.zipWithIndex) {
          lastMatchIndex = 0
          for ((p,j) <- predictTagsWithTokens.zipWithIndex) {
            //            if(j >= lastMatchIndex){
            breakable {
              if (t == p) {
                // increament True Positive for this tag
                metrics += TagScore(tag = t._2, truePositive = 1, falsePositive = 0, falseNagetive = 0)

                predictTagsWithTokens = predictTagsWithTokens.zipWithIndex.filter(_._2 != j).map(_._1)
                break()
              } else if (t._1 == p._1) {
                // increament False Positive for this tag
                metrics += TagScore(tag = p._2, truePositive = 0, falsePositive = 1, falseNagetive = 0)
                // increament False Negative for the correct tag
                metrics += TagScore(tag = t._2, truePositive = 0, falsePositive = 0, falseNagetive = 1)

                predictTagsWithTokens = predictTagsWithTokens.zipWithIndex.filter(_._2 != j).map(_._1)
                break()
              }
              lastMatchIndex += 1
              if(lastMatchIndex >= 3) break() // after 3 tokens stop looking
            }
          }
        }
        newColumns.append(metrics)
        newColumns
      }).toDF("metrics")
      .select(explode($"metrics").as("tagScores"))
      .filter($"tagScores.tag" =!= "_")
      //   .filter($"tagScores.tag" =!= "X")
      //   .filter($"tagScores.tag" =!= "INTJ")
      .groupBy($"tagScores.tag")
      .agg(
        sum($"tagScores.truePositive").as("tp_score"),
        sum($"tagScores.falsePositive").as("fp_score"),
        sum($"tagScores.falseNagetive").as("fn_score")
      )
      .withColumn("Precision", round($"tp_score" / ($"tp_score" + $"fp_score"), 3))
      .withColumn("Recall", round($"tp_score" / ($"tp_score" + $"fn_score"), 3))
      .withColumn("F1-Score", round((($"Precision" * $"Recall") / ($"Precision" + $"Recall")) * 2, 3))
      .orderBy($"Precision".desc)

    scorePerTagDF.show(false)

    scorePerTagDF.agg(
      avg($"Precision").as("Precision"),
      avg($"Recall").as("Recall"),
      avg($"F1-Score").as("F1-Score")
    ).show(false)
  }

  private def extractTokens= udf { docs: Seq[String] =>
    var posTagsArray = ArrayBuffer[String]()
    var previousSentenceNumber = Array[String]()

    for ((e,i) <- docs.zipWithIndex){
      val splitedArray = e.split("\t")
      val currentSentenceNumber = splitedArray(0).split("-")

      if(currentSentenceNumber.length > 1){
        previousSentenceNumber = currentSentenceNumber
        val nextSentence = docs(i+1).split("\t")
        posTagsArray += splitedArray(1)

      }else if(previousSentenceNumber.contains(currentSentenceNumber(0))){

      }else{
        posTagsArray += splitedArray(1)
      }
    }
    posTagsArray
  }

  private def extractTags= udf { docs: Seq[String] =>
    var posTagsArray = ArrayBuffer[String]()
    var previousSentenceNumber = Array[String]()

    for ((e,i) <- docs.zipWithIndex){
      val splitedArray = e.split("\t")
      val currentSentenceNumber = splitedArray(0).split("-")

      if(currentSentenceNumber.length > 1){
        previousSentenceNumber = currentSentenceNumber
        val nextSentence = docs(i+1).split("\t")
        posTagsArray += nextSentence(3)

      }else if(previousSentenceNumber.contains(currentSentenceNumber(0))){

      }else{
        posTagsArray += splitedArray(3)
      }
    }
    posTagsArray
  }

  private def calLengthOfArray= udf { docs: Seq[String] =>
    docs.length
  }

  private def extractMissingTokens= udf { (testTokens: Seq[String], predictTokens: Seq[String]) =>
    var missingTokensArray = ArrayBuffer[String]()

    for (e <- testTokens) {
      if (!predictTokens.contains(e)) {
        missingTokensArray += e
      }
    }
    missingTokensArray
  }

}
