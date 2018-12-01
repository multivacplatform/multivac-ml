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
import org.apache.spark.rdd.RDD
import org.multivacplatform.ml.util._

import ResourceHelper.spark.implicits._

class Multivac {

  /** Train
    *
    * @note
    * @param inputDataset input RDD[String] from `spark.sparkContext.textFile`
    * @return Array[String] to be saved for training `Spark-NLP`
    */
  def train(inputDataset: RDD[String], iterationNum: Int, textColName: String): PipelineModel = {

    val spark = ResourceHelper.spark

    val applicationId = spark.sparkContext.applicationId

    val conllOutputPath = s"./data/english_universal_tags/$applicationId"

    val conlluConverterClass = new ConllConverter

    val taggedConnlText = conlluConverterClass.extractingTagsInConllu(inputDataset)
    spark.sparkContext.parallelize(taggedConnlText).repartition(5).saveAsTextFile(conllOutputPath)

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
      .setCorpus(path = conllOutputPath, delimiter = "_", options = posOptions)

    // Finisher
    val tokenFinisher = new Finisher()
      .setInputCols("token")
      .setOutputCols("token_array")
      .setCleanAnnotations(false)
      .setOutputAsArray(true)

    val pipeline = new Pipeline()
      .setStages(Array(
        documentAssembler,
        sentenceDetector,
        tokenizer,
        posTagger,
        tokenFinisher
      ))

    pipeline.fit(Seq.empty[String].toDF(textColName))
  }
}
