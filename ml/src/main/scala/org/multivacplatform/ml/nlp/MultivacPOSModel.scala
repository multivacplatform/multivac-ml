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
import org.apache.spark.ml.Pipeline
import org.multivacplatform.ml.util.ResourceHelper.spark.implicits._
import org.multivacplatform.ml.util._
import org.apache.spark.ml.PipelineModel

class MultivacPOSModel extends Serializable {

  private val spark = ResourceHelper.spark
  private val applicationId = spark.sparkContext.applicationId
  private val defaultCorpusPath= s"./data/universal_tags/$applicationId"

  var corpusPath: String = defaultCorpusPath
  var lang: String = ""
  var includeLemma: Boolean = true
  var iterationCount: Int = 5
  var inputColName: String = ""

  /** setInputCoNNLFilePath
    *
    * @param value path to ConLL file to train POS Model
    */
  def setCorpus(value: String): this.type = { corpusPath = value; this }

  /** setLang
    *
    * @param value path to ConLL file to train POS Model
    */
  def setLang(value: String): this.type = { lang = value; this }

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
      case "french" | "persian" =>
        tokenizer
          .setInputCols(Array("sentence"))
          .setOutputCol("token")
          .addInfixPattern("(\\w+)([^\\s\\p{L}]{1})+(\\w+)")
          .addInfixPattern("(\\w+'{1})(\\w+)") // (l',air) instead of (l, 'air)
          .addInfixPattern("(\\p{L}+)(n't\\b)")
          .addInfixPattern("((?:\\p{L}\\.)+)")
          .addInfixPattern("([\\$#]?\\d+(?:[^\\s\\d]{1}\\d+)*)")
          .addInfixPattern("([\\p{L}\\w]+)")
      case _ | "default" =>
        tokenizer
          .setInputCols(Array("sentence"))
          .setOutputCol("token")
    }

    // POS tagging
    val posOptions = Map("format" -> "text", "repartition" -> "1")
    val posTagger = new PerceptronApproach()
      .setNIterations(iterationCount)
      .setInputCols(Array("sentence", "token"))
      .setOutputCol("pos")
      .setCorpus(
        path = corpusPath,
        delimiter = "_",
        readAs = "SPARK_DATASET",
        options = posOptions
      )

    val pipeline = new Pipeline()
      .setStages(Array(
        documentAssembler,
        sentenceDetector,
        tokenizer,
        posTagger
      ))

    pipeline.fit(Seq.empty[String].toDF(inputColName))
  }
}
