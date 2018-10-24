import org.apache.log4j.{Level, LogManager}
import org.apache.spark.sql.SparkSession

object SessionBuilder {
  def buildSession(): SparkSession = {


    val spark: SparkSession = SparkSession.builder
      .appName("multivac-ml-demo")
      .master("local[*]")
      .config("spark.driver.memory", "4G")
      .config("spark.kryoserializer.buffer.max","200M")
      .config("spark.serializer","org.apache.spark.serializer.KryoSerializer")
      .getOrCreate


    LogManager.getRootLogger.setLevel(Level.WARN)
    spark.sparkContext.setLogLevel("WARN")

    spark
  }
}
