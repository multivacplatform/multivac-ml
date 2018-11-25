package org.multivacplatform.ml.model

import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.DataFrame

class Multivac {
  def init(lang: String): PipelineModel = {
    var mlPath: String = ""

    lang match {
      case "en" => mlPath = "src/main/resources/multivac-ml/nlp/multivac_nlp_pos_UD_English-EWT"
      case "fr" => mlPath = "src/main/resources/multivac-ml/nlp/multivac_nlp_pos_UD_French-GSD"
    }

    val pipeLinePOSTaggerModel = PipelineModel.read.load(mlPath)
    pipeLinePOSTaggerModel

  }

  def apply(pipeLinePOSTaggerModel: PipelineModel, inputDataFrame: DataFrame): DataFrame = {
    val transformedDataFrame = pipeLinePOSTaggerModel.transform(inputDataFrame)
    transformedDataFrame
  }

  def testFunc(name: String): String = {
    name + " hello!"
  }
}
