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
import org.apache.spark.ml.feature.Word2VecModel
import org.apache.spark.sql.SparkSession

object MultivacDemo {

  def word2Vec_ml(spark: SparkSession, inputPath: String): Unit = {

    val pipeLineWord2VecModel = PipelineModel.read.load(inputPath)
    val word2VecModel = pipeLineWord2VecModel.stages.last.asInstanceOf[Word2VecModel]

    word2VecModel.findSynonyms("climate change", 10).show(false)

  }

  def posTaggerEnglish_ml(spark: SparkSession, inputPath: String): Unit = {
    import spark.implicits._

    val pipeLinePOSTaggerModel = PipelineModel.read.load(inputPath)
    //Testing Dataframe
    val rawData = List("""What if Google Morphed Into GoogleOS? What if Google expanded on its search-engine (and now e-mail) wares into a full-fledged operating system? [via Microsoft Watch from Mary Jo Foley ]""")

    val testEnglishDF = rawData.toDF("content")

    val manualPipelineDF = pipeLinePOSTaggerModel.transform(testEnglishDF)
    manualPipelineDF.select("token.result", "pos.result").show(false)
  }

  def posTaggerFrench_ml(spark: SparkSession, inputPath: String): Unit = {
    import spark.implicits._

    val pipeLinePOSTaggerModel = PipelineModel.read.load(inputPath)
    //Testing Dataframe
    val rawData = List("Les commotions cérébrales sont devenu si courantes dans ce sport qu'on les considére presque comme la routine.")

    val testEnglishDF = rawData.toDF("content")

    val manualPipelineDF = pipeLinePOSTaggerModel.transform(testEnglishDF)
    manualPipelineDF.select("token.result", "pos.result").show(false)
  }
}
