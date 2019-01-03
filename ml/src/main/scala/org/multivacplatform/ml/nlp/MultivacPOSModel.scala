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

package org.multivacplatform.ml.nlp

import com.johnsnowlabs.nlp.annotator.{PerceptronApproach, SentenceDetector, Tokenizer}
import com.johnsnowlabs.nlp.{DocumentAssembler, Finisher}
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.multivacplatform.ml.util.ResourceHelper.spark.implicits._
import org.multivacplatform.ml.util._
import org.apache.spark.ml.PipelineModel
import org.apache.spark.ml.param.{IntParam, Param}
import org.apache.spark.ml.util.{DefaultParamsReadable, Identifiable}
import org.apache.spark.sql.Dataset

import scala.collection.mutable.{Map => MMap}
import scala.util.Random


class MultivacPOSModel extends Serializable {

  private val spark = ResourceHelper.spark
  private val applicationId = spark.sparkContext.applicationId
  private val defaultConllOutputPath = s"./data/universal_tags/$applicationId"

  var inputCoNNLFilePath: String = ""
  var outputConllFilePath: String = defaultConllOutputPath
  var lang: String = "english"
  var includeLemma: Boolean = true
  var iterationCount: Int = 5
  var inputColName: String = ""

  /** setInputCoNNLFilePath
    *
    * @param value path to ConLL file to train POS Model
    */
  def setInputCoNNLFilePath(value: String): this.type = { inputCoNNLFilePath = value; this }

  /** setOutputConllFilePath
    *
    * @param value output path to write converted CoNLL files `default: ./data/english_universal_tags/$applicationId`
    */
  def setOutputConllFilePath(value: String): this.type = { outputConllFilePath = value; this }

  /** setLang
    *
    * @param value path to ConLL file to train POS Model
    */
  def setLang(value: String): this.type = { lang = value; this }

  /** setIncludeLemma
    *
    * @param value path to ConLL file to train POS Model
    */
  def setIncludeLemma(value: Boolean): this.type = { includeLemma = value; this }

  /** setIterationCount
    *
    * @param value number of iteration to train POS model
    */
  def setIterationCount(value: Int): this.type = { iterationCount = value; this }

  /** setInputColName
    *
    * @param value the name of column that contains the text to predict their POS tags
    */
  def setInputColName(value: String): this.type = { inputColName = value; this }

  /** Train
    *
    * @note
    * @return Array[String] to be saved for training `Spark-NLP`
    */
  def train(): PipelineModel = {

    val conlluConverterClass = new CoNLLToPOSTextConverter

    val taggedConnlTextDF = conlluConverterClass.extractingTagsInConllu(inputCoNNLFilePath, posTaggedColName = "pos_tagged", lemmaTaggedColName = "lemma_tagged")

    taggedConnlTextDF.select("pos_tagged").coalesce(1).write.mode("OverWrite").text(outputConllFilePath)
    if(includeLemma)
      taggedConnlTextDF.select("lemma_tagged").coalesce(1).write.mode("Append").text(outputConllFilePath)
    //    spark.sparkContext.parallelize(taggedConnlText).repartition(5).saveAsTextFile(outputConllFilePath)

    val documentAssembler = new DocumentAssembler()
      .setInputCol(inputColName)
      .setOutputCol("document")

    val sentenceDetector = new SentenceDetector()
      .setInputCols(Array("document"))
      .setOutputCol("sentence")

    val tokenizer = new Tokenizer()

    lang match {
      case "english" =>
        tokenizer
          .setInputCols(Array("sentence"))
          .setOutputCol("token")
          .addInfixPattern("(\\w+)([^\\s\\p{L}]{1})+(\\w+)")
          .addInfixPattern("(\\p{L}+)('{1}\\p{L}+)")
          .addInfixPattern("(\\p{L}+)(n't\\b)")
          .addInfixPattern("((?:\\p{L}\\.)+)")
          .addInfixPattern("([\\$#]?\\d+(?:[^\\s\\d]{1}\\d+)*)")
          .addInfixPattern("([\\p{L}\\w]+)")
      case "french" =>
        tokenizer
          .setInputCols(Array("sentence"))
          .setOutputCol("token")
          .addInfixPattern("(\\w+)([^\\s\\p{L}]{1})+(\\w+)")
          .addInfixPattern("(\\w+'{1})(\\w+)") // (l',air) instead of (l, 'air)
          .addInfixPattern("(\\p{L}+)(n't\\b)")
          .addInfixPattern("((?:\\p{L}\\.)+)")
          .addInfixPattern("([\\$#]?\\d+(?:[^\\s\\d]{1}\\d+)*)")
          .addInfixPattern("([\\p{L}\\w]+)")
          .setCompositeTokensPatterns(Array("e-mail"))
    }

    // POS tagging
    val posOptions = Map("format" -> "text", "repartition" -> "1")
    val posTagger = new PerceptronApproach()
      .setNIterations(iterationCount)
      .setInputCols(Array("sentence", "token"))
      .setOutputCol("pos")
      .setCorpus(
        path = s"$outputConllFilePath/*.txt",
        delimiter = "_",
        readAs = "SPARK_DATASET",
        options = posOptions
      )

    // Finishers
    val tokenFinisher = new Finisher()
      .setInputCols("token")
      .setOutputCols("token_array")
      .setCleanAnnotations(false)
      .setOutputAsArray(true)

    val posFinisher = new Finisher()
      .setInputCols("pos")
      .setOutputCols("pos_array")
      .setCleanAnnotations(false)
      .setOutputAsArray(true)

    val pipeline = new Pipeline()
      .setStages(Array(
        documentAssembler,
        sentenceDetector,
        tokenizer,
        posTagger,
        tokenFinisher,
        posFinisher
      ))

    pipeline.fit(Seq.empty[String].toDF(inputColName))
  }
}
