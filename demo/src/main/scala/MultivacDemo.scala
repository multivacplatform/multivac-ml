import org.apache.spark.sql.SparkSession
import org.apache.spark.ml._
import org.apache.spark.ml.feature.Word2VecModel

object MultivacDemo {

  def word2Vec_ml(
                    spark: SparkSession,
                    inputPath: String
                  ): Unit = {

    val pipeLineWord2VecModel = PipelineModel.read.load(inputPath)
    val word2VecModel = pipeLineWord2VecModel.stages.last.asInstanceOf[Word2VecModel]

    word2VecModel.findSynonyms("climate change", 10).show(false)

  }
}
