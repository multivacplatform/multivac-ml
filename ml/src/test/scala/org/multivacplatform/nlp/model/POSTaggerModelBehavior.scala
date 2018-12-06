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

package org.multivacplatform.nlp.model

import org.apache.spark.sql.Row
import org.multivacplatform.ml.model._
import org.multivacplatform.ml.util.ResourceHelper
import org.scalatest.FlatSpec

trait POSTaggerModelBehavior { this: FlatSpec =>

  def multivacTrainPOSModel(trainingSentencesPath: String, testDataset: String, colName: String): Unit = {

    "Multivac ML Model for POS tagger" should s"successfully train, transform and tag sentences" in {

      val spark = ResourceHelper.spark

      val pipleLineModel = Multivac.nlp.train(inputCoNNLFilePath = trainingSentencesPath, iterationNum = 1, textColName = colName)

      val wordDataFrame = spark.createDataFrame(Seq((0, testDataset))).toDF("id", colName)

      val df = pipleLineModel.transform(wordDataFrame)
      val posCol = df.select("pos")
      val tokensCol = df.select("token")
      val tokens: Seq[String] = tokensCol.collect.flatMap(r => r.getSeq[Row](0)).map(a => a.getString(3))
      val taggedWords: Seq[String] = posCol.collect.flatMap(r => r.getSeq[Row](0)).flatMap(a => a.getMap[String, Any](4).get("word")).map(_.toString)

      tokens.foreach { token: String =>
        assert(taggedWords.contains(token), s"Token $token should be list of tagged words")
      }

      case class IndexedWord(word: String, begin: Int, end:Int) {
        def equals(o: IndexedWord): Boolean = { this.word == o.word && this.begin == o.begin && this.end == o.end }
      }

      val tokensTest2: Seq[IndexedWord] = tokensCol.collect.flatMap(r => r.getSeq[Row](0)).map(a => IndexedWord(a.getString(3), a.getInt(1), a.getInt(2)))
      val taggedWordsTest: Seq[IndexedWord] = posCol.collect.flatMap(r => r.getSeq[Row](0)).map(a => IndexedWord(a.getMap[String, String](4)("word"), a.getInt(1), a.getInt(2)))

      taggedWordsTest.foreach { word: IndexedWord =>
        assert(tokensTest2.exists { token => token.equals(word) }, s"Indexed word $word should be included in ${tokensTest2.filter(t => t.word == word.word)}")
      }
    }
  }
}
