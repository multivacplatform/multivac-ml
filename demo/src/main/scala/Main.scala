object Main {
  def main(args: Array[String]): Unit = {
    val spark = SessionBuilder.buildSession()

    MultivacDemo.word2Vec_ml(spark, "src/main/resources/models/multivac_word2vec_ml_200k")
    MultivacDemo.posTaggerEnglish_ml(spark, "src/main/resources/models/multivac_nlp_pos_UD_English-EWT")
    MultivacDemo.posTaggerFrench_ml(spark, "src/main/resources/models/multivac_nlp_pos_UD_French-GSD")
  }
}
