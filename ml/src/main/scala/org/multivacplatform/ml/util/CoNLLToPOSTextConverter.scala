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

package org.multivacplatform.ml.util

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

class CoNLLToPOSTextConverter {

  private val spark = ResourceHelper.spark

  /** Convert Conll-U to tagged-based text
    *
    * @note
    * @param inputDataset input RDD[String] from `spark.sparkContext.textFile`
    * @return Array[String] to be saved for training `Spark-NLP`
    */
  def extractingTagsInConllu(inputCoNNLFilePath: String): Array[String] = {

    val inputCoNNLFileRDD = spark.sparkContext.textFile(inputCoNNLFilePath)

    val originalTokens = inputCoNNLFileRDD.map(s => s.split("\t")
      .filter(x => !x.startsWith("#"))).filter(x => x.length > 0)
      .map{x => if(x.length > 1){x(1) + "_" + x(3)} else{"endOfLine"}}
      .map(x => x.mkString)
    /* This did not improve the accuracy!
        val lemmaTokens = inputCoNNLFileRDD.map(s => s.split("\t")
          .filter(x => !x.startsWith("#"))).filter(x => x.length > 0)
          .map{x => if(x.length > 1){x(2) + "_" + x(3)} else{"endOfLine"}}
          .map(x => x.mkString)
    */
    val labeledOriginalTokens = originalTokens.reduce((s1, s2) => s1 + " " + s2).split(" endOfLine | endOfLine")
    val labeledLemmaTokens = lemmaTokens.reduce((s1, s2) => s1 + " " + s2).split(" endOfLine | endOfLine")
    val mergedOriginalLemmaUD = labeledOriginalTokens.union(labeledLemmaTokens)
    mergedOriginalLemmaUD

  }
}
