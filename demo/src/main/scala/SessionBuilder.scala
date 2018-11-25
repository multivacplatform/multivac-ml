import org.apache.spark.sql.SparkSession

object SessionBuilder {
  def buildSession(): SparkSession = {

//    Logger.getLogger("com").setLevel(Level.ERROR)
//    Logger.getLogger("org").setLevel(Level.ERROR)
//    Logger.getLogger("akka").setLevel(Level.ERROR)
//    LogManager.getRootLogger.setLevel(Level.ERROR)

    val spark: SparkSession = SparkSession.builder
      .appName("multivac-ml-demo")
      .master("local[*]")
      .config("spark.driver.memory", "4G")
      .config("spark.kryoserializer.buffer.max","200M")
      .config("spark.serializer","org.apache.spark.serializer.KryoSerializer")
      .getOrCreate

    spark.sparkContext.setLogLevel("ERROR")
    // export sparkSession
    spark
  }
}
