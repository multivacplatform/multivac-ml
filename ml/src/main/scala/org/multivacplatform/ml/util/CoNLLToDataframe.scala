package org.multivacplatform.ml.util

import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.hadoop.mapreduce.Job
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

class CoNLLToDataframe extends Serializable {

  def convertToDataFrame(inputCoNNLFilePath: String): DataFrame = {
    val spark = ResourceHelper.spark
    import spark.implicits._

    val conf = new Job().getConfiguration
    conf.set("textinputformat.record.delimiter", "\n\n")

    val usgRDD = spark.sparkContext.newAPIHadoopFile(
      inputCoNNLFilePath, classOf[TextInputFormat], classOf[LongWritable], classOf[Text], conf)
      .map{ case (_, v) => v.toString }

    val conllSentencesDF = usgRDD.map(s => s.split("\n")
      .filter(x => !x.startsWith("# newdoc id"))
    )
      .filter(x => x.length > 0)
      .toDF("sentence")

    conf.set("textinputformat.record.delimiter", "")

    conllSentencesDF
      .withColumn("sent_id", extractCoNLLSentID($"sentence"))
      .withColumn("text", extractCoNLLText($"sentence"))
      .withColumn("ID", extractCoNLL($"sentence", lit(0)))
      .withColumn("FORM", extractCoNLL($"sentence", lit(1)))
      .withColumn("LEMMA", extractCoNLL($"sentence", lit(2)))
      .withColumn("UPOS", extractCoNLL($"sentence", lit(3)))
      .withColumn("XPOS", extractCoNLL($"sentence", lit(4)))
      .withColumn("FEATS", extractCoNLL($"sentence", lit(5)))
      .withColumn("HEAD", extractCoNLL($"sentence", lit(6)))
      .withColumn("DEPREL", extractCoNLL($"sentence", lit(7)))
      .withColumn("DEPS", extractCoNLL($"sentence", lit(8)))
      .withColumn("MISC", extractCoNLL($"sentence", lit(9)))
      .drop("sentence")

  }

  private def extractCoNLL = udf {(docs: Seq[String], index: Int) =>
    var posTagsArray = ArrayBuffer[String]()
    val newFilteredSentences =  docs.filter(x => !x.startsWith("# "))
    for(e <- newFilteredSentences){
      val splitedArray = e.split("\t")
      posTagsArray += splitedArray(index)
    }
    posTagsArray
  }

  private def extractCoNLLSentID = udf {docs: Seq[String] =>
    docs.head.split("# sent_id =").last
  }

  private def extractCoNLLText = udf {docs: Seq[String] =>
    docs(1).split("# text =").last
  }

}
