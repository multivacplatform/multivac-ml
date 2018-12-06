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

object Main {
  def main(args: Array[String]): Unit = {
    val spark = SessionBuilder.buildSession()


    MultivacDemo.word2Vec_ml(spark, "demo/src/main/resources/models/multivac_word2vec_ml_200k")
    MultivacDemo.posTaggerEnglish_ml(spark, "demo/src/main/resources/models/multivac_nlp_pos_UD_English-EWT")
    MultivacDemo.posTaggerFrench_ml(spark, "demo/src/main/resources/models/multivac_nlp_pos_UD_French-GSD")

    TrainModel.posTaggerEnglish_ml(
      "./data/ud-treebanks-v2.3/en_ewt-ud-train.conllu",
      5,
      "content"
    )

    TestAccuracyEnglish.posTaggerEnglish_ml(
      spark,
      "./data/ud-treebanks-v2.2/en_ewt-ud-train.conllu",
      "demo/src/main/resources/models/multivac_nlp_pos_UD_English-EWT"
    )

    // Not working as it's expected
    //TestAccuracy.posTaggerFrench_ml(spark, "src/main/resources/data/fr_gsd-ud-test.conllu", "src/main/resources/models/multivac_nlp_pos_UD_French-GSD")
    spark.close()
  }
}
