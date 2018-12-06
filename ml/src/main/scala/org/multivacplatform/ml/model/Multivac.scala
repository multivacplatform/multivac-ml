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

package org.multivacplatform.ml.model

import com.johnsnowlabs.nlp.{DocumentAssembler, Finisher}
import com.johnsnowlabs.nlp.annotator.{PerceptronApproach, SentenceDetector, Tokenizer}
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.multivacplatform.ml.util._

import ResourceHelper.spark.implicits._

class Multivac {

  private val spark = ResourceHelper.spark
  private val applicationId = spark.sparkContext.applicationId
  private val defaultConllOutputPath = s"./data/english_universal_tags/$applicationId"
  /** Train
    *
    * @note
    * @param inputCoNNLFilePath path to ConLL file to train POS Model
    * @param outputConllFilePath output path to write converted CoNLL files `default: ./data/english_universal_tags/$applicationId`
    * @param iterationNum number of iteration to train POS model
    * @param textColName the name of column that contains the text to predict their POS tags
    * @return Array[String] to be saved for training `Spark-NLP`
    */
  def train(inputCoNNLFilePath: String, outputConllFilePath: String = defaultConllOutputPath, iterationNum: Int = 5, textColName: String): PipelineModel = {

    val conlluConverterClass = new CoNLLToPOSTextConverter

    val taggedConnlText = conlluConverterClass.extractingTagsInConllu(inputCoNNLFilePath)
    spark.sparkContext.parallelize(taggedConnlText).repartition(5).saveAsTextFile(outputConllFilePath)

    val documentAssembler = new DocumentAssembler()
      .setInputCol(textColName)
      .setOutputCol("document")

    val sentenceDetector = new SentenceDetector()
      .setInputCols(Array("document"))
      .setOutputCol("sentence")

    val tokenizer = new Tokenizer()
      .setInputCols(Array("sentence"))
      .setOutputCol("token")

    // POS tagging
    val posOptions = Map("format" -> "text", "repartition" -> "1")
    val posTagger = new PerceptronApproach()
      .setNIterations(iterationNum)
      .setInputCols(Array("sentence", "token"))
      .setOutputCol("pos")
      .setCorpus(path = outputConllFilePath, delimiter = "_", options = posOptions)

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

    pipeline.fit(Seq.empty[String].toDF(textColName))
  }
}
