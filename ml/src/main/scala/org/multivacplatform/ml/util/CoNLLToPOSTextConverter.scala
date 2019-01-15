package org.multivacplatform.ml.util

import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

import scala.collection.mutable.ArrayBuffer

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

class CoNLLToPOSTextConverter extends Serializable {

  private val spark = ResourceHelper.spark

  private var inputCoNNLFilePath: String = ""
  private var posTaggedColName: String = "pos"
  private var lemmaTaggedColName: String = "lemma"

  /** setInputCoNNLFilePath
    *
    * @param value path to ConLL file to train POS Model
    */
  def setInputCoNNLFilePath(value: String): this.type = { inputCoNNLFilePath = value; this }

  /** setPosColName
    *
    * @param value path to ConLL file to train POS Model
    */
  def setPosColName(value: String): this.type = { posTaggedColName = value; this }

  /** setlemmaColName
    *
    * @param value path to ConLL file to train POS Model
    */
  def setlemmaColName(value: String): this.type = { lemmaTaggedColName = value; this }



  /** Convert Conll-U to tagged-based text
    *
    * @note
    * @return Array[String] to be saved for training `Spark-NLP`
    */
  def transform(): DataFrame = {

    import spark.implicits._

    val conf = new org.apache.hadoop.mapreduce.Job().getConfiguration
    conf.set("textinputformat.record.delimiter", "\n\n")

    val usgRDD = spark.sparkContext.newAPIHadoopFile(
      inputCoNNLFilePath, classOf[TextInputFormat], classOf[LongWritable], classOf[Text], conf)
      .map{ case (_, v) => v.toString }

    val conllSentencesDF = usgRDD.map(s => s.split("\n").filter(x => !x.startsWith("#")))
      .filter(x => x.length > 0)
      .toDF("sentence")

    conf.set("textinputformat.record.delimiter", "")

    conllSentencesDF
      .withColumn("pos_tagged", extractPOSTags($"sentence"))
      .withColumn("lemma_tagged", extractLemmaTags($"sentence"))
      .withColumn(posTaggedColName, concat_ws(" ", $"pos_tagged"))
      .withColumn(lemmaTaggedColName, concat_ws(" ", $"lemma_tagged"))
      .select(posTaggedColName, lemmaTaggedColName)

    /* Old way
        val inputCoNNLFileRDD = spark.sparkContext.textFile(inputCoNNLFilePath)

        val originalTokens = inputCoNNLFileRDD.map(s => s.split("\t")
          .filter(x => !x.startsWith("#"))).filter(x => x.length > 0)
          .map{x => if(x.length > 1){x(1) + "_" + x(3)} else{"endOfLine"}}
          .map(x => x.mkString)
     */
    /*
        val lemmaTokens = inputCoNNLFileRDD.map(s => s.split("\t")
          .filter(x => !x.startsWith("#"))).filter(x => x.length > 0)
          .map{x => if(x.length > 1){x(2) + "_" + x(3)} else{"endOfLine"}}
          .map(x => x.mkString)
    */
    /*
        val labeledOriginalTokens = originalTokens.reduce((s1, s2) => s1 + " " + s2).split(" endOfLine | endOfLine")
        val labeledLemmaTokens = lemmaTokens.reduce((s1, s2) => s1 + " " + s2).split(" endOfLine | endOfLine")
        val mergedOriginalLemmaUD = labeledOriginalTokens.union(labeledLemmaTokens)
        mergedOriginalLemmaUD
    */
  }

  private def extractPOSTags = udf { docs: Seq[String] =>
    var posTagsArray = ArrayBuffer[String]()
    for(e <- docs){
      val splitedArray = e.split("\t")
      posTagsArray += splitedArray(1) + "_" + splitedArray(3)
    }
    posTagsArray
  }

  private def extractLemmaTags = udf { docs: Seq[String] =>
    var posTagsArray = ArrayBuffer[String]()
    for(e <- docs){
      val splitedArray = e.split("\t")
      posTagsArray += splitedArray(2) + "_" + splitedArray(3)
    }
    posTagsArray
  }

}
