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

  def posTaggerEnglish_ml(
                   spark: SparkSession,
                   inputPath: String
                 ): Unit = {
    import spark.implicits._

    val pipeLinePOSTaggerModel = PipelineModel.read.load(inputPath)
    //Testing Dataframe
    val rawData = List("""What if Google Morphed Into GoogleOS? What if Google expanded on its search-engine (and now e-mail) wares into a full-fledged operating system? [via Microsoft Watch from Mary Jo Foley ]""")

    val testEnglishDF = rawData.toDF("content")

    val manualPipelineDF = pipeLinePOSTaggerModel.transform(testEnglishDF)
    manualPipelineDF.select("token.result", "pos.result").show(false)

  }

  def posTaggerFrench_ml(
                           spark: SparkSession,
                           inputPath: String
                         ): Unit = {
    import spark.implicits._

    val pipeLinePOSTaggerModel = PipelineModel.read.load(inputPath)
    //Testing Dataframe
    val rawData = List("Les commotions cérébrales sont devenu si courantes dans ce sport qu'on les considére presque comme la routine.")

    val testEnglishDF = rawData.toDF("content")

    val manualPipelineDF = pipeLinePOSTaggerModel.transform(testEnglishDF)
    manualPipelineDF.select("token.result", "pos.result").show(false)

  }
}
