# multivac-ml
Already trained Apache Spark's ML Pipeline for NLP, Classification, etc.


### Trained ML Models

Plesae download already trained ML models form here:

#### Word2Vec
https://www.kaggle.com/mozzie/apache-spark-word2vec-model/downloads/apache-spark-word2vec-model.zip/1

Apache Spark 2.3 is used to extract more than 6 million phrases from 200,000 English Wikipedia pages. Here is the process of cleaning, extracting keywords, and training Word2Vec model:

* Merging page's Title and its Text
* Sentence detection (spark-nlp)
* Tokenizer (spark-nlp)
* Normalizer (spark-nlp) POS Tagger (spark-nlp) Chuning with grammar rules to detect both uni-grams and multi-grams (spark-nlp)
* Stop words remover (Spark ML)
* Training and transforming Word2Vec Model (Spark ML)


##### TF-IDF
coming soon!
