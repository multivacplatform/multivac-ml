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

import com.johnsnowlabs.nlp.annotator.PerceptronModel
import com.johnsnowlabs.util.Benchmark
import org.multivacplatform.ml.nlp._
import org.multivacplatform.ml.util.CoNLLToPOSTextConverter

object TrainEnglishPOS extends App {

  private val spark = SessionBuilder.buildSession()
  private val applicationId = spark.sparkContext.applicationId

  /* Convert ConLL-U to labeled text files, ex: The_DET */
  val defaultCorpusPath= s"./data/universal_tags/$applicationId"

  val trainCoNLLDF = new CoNLLToPOSTextConverter()
    .setInputCoNNLFilePath("./data/ud-treebanks-v2.3/en_ewt-ud-train.conllu")
    .setPosColName("pos_tagged")
    .setlemmaColName("lemma_tagged")
    .transform()

  trainCoNLLDF.select("pos_tagged").coalesce(1).write.mode("OverWrite").text(defaultCorpusPath)
  trainCoNLLDF.select("lemma_tagged").coalesce(1).write.mode("Append").text(defaultCorpusPath)

  val devCoNLLDF = new CoNLLToPOSTextConverter()
    .setInputCoNNLFilePath("./data/ud-treebanks-v2.3/en_ewt-ud-dev.conllu")
    .setPosColName("pos_tagged")
    .setlemmaColName("lemma_tagged")
    .transform()

  devCoNLLDF.select("pos_tagged").coalesce(1).write.mode("Append").text(defaultCorpusPath)
  devCoNLLDF.select("lemma_tagged").coalesce(1).write.mode("Append").text(defaultCorpusPath)

  /* Train POS Tagger Model */
  val pipleLineModelEnglish =
    Benchmark.time("Time to train the model") {
      new MultivacPOSModel()
        .setCorpus(s"$defaultCorpusPath/*.txt")
        .setIterationCount(6)
        .setInputColName("content")
        .setLang("default")
        .train()
    }

  pipleLineModelEnglish.write.overwrite.save("models/nlp/pipeline-pos-en_ewt-ud-1.8.0")
  pipleLineModelEnglish.stages(3).asInstanceOf[PerceptronModel].write.overwrite.save("models/nlp/pos-en_ewt-ud-1.8.0")

  /* Evaluate the accuracy of the model */
  TestAccuracy.evaluatePOSModel(
    "./data/ud-treebanks-v2.3/en_ewt-ud-test.conllu",
    "models/nlp/pipeline-pos-en_ewt-ud-1.8.0"
  )

  spark.close()
}