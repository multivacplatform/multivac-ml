### NLP (Part of Speech)

## Download

**Dataverse:** 

English
```
wget -O multivac_nlp_pos_UD_English-EWT.tar.xz https://dataverse.harvard.edu/api/access/datafile/:persistentId?persistentId=doi:10.7910/DVN/WSWU7K/J8HDKW
```
French
```
wget -O multivac_nlp_pos_UD_French-GSD.tar.xz https://dataverse.harvard.edu/api/access/datafile/:persistentId?persistentId=doi:10.7910/DVN/WSWU7K/6SWRTT
```

Or simply click on the links to start the download: 

English: https://dataverse.harvard.edu/api/access/datafile/:persistentId?persistentId=doi:10.7910/DVN/WSWU7K/J8HDKW

French: https://dataverse.harvard.edu/api/access/datafile/:persistentId?persistentId=doi:10.7910/DVN/WSWU7K/6SWRTT


**Kaggle (you need to be logged-in):** 
`English(UD_English-EWT)` and `French(UD_French-GSD)`
https://www.kaggle.com/mozzie/apache-spark-universal-part-of-speech/downloads/apache-spark-universal-part-of-speech.zip/2

## Context

Apache Spark POS tagger model trained by [Universal Treebanks Version v2.2][1] datasets. The [Spark-NLP][2] library is used to train the POS tagger models.

<b>English:</b> UD_English-EWT

<b>French:</b> UD_French-GSD

#### Universal POS tags
Alphabetical listing

* ADJ: adjective
* ADP: adposition
* ADV: adverb
* AUX: auxiliary
* CCONJ: coordinating conjunction
* DET: determiner
* INTJ: interjection
* NOUN: noun
* NUM: numeral
* PART: particle
* PRON: pronoun
* PROPN: proper noun
* PUNCT: punctuation
* SCONJ: subordinating conjunction
* SYM: symbol
* VERB: verb
* X: other

#### Usage
You can simply download this model and load it into your Apache Spark ML pipeline:

```scala
import org.apache.spark.ml._

val pipeLinePOSTaggerModel = PipelineModel.read.load("/tmp/multivac_nlp_pos_UD_English-EWT")

//Testing Dataframe
val rawData = List("""What if Google Morphed Into GoogleOS? What if Google expanded on its search-engine (and now e-mail) wares into a full-fledged operating system? [via Microsoft Watch from Mary Jo Foley ]""")    
val testEnglishDF = rawData.toDF("content")

val manualPipelineDF = pipeLinePOSTaggerModel.transform(testEnglishDF)
manualPipelineDF.select("token.result", "pos.result").show(false)

|[What, if, Google, Morphed, Into, GoogleOS, ?, What, if, Google, expanded, on, its, search-engine, (, and, now, e-mail, ), wares, into, a, full-fledged, operating, system, ?, [, via, Microsoft, Watch, from, Mary, Jo, Foley, ]]
|[PRON, SCONJ, PROPN, PROPN, PROPN, PROPN, PUNCT, PRON, SCONJ, PROPN, NOUN, ADP, PRON, NUM, PUNCT, CCONJ, ADV, VERB, PUNCT, VERB, ADP, DET, ADJ, NOUN, NOUN, PUNCT, PUNCT, ADP, PROPN, VERB, ADP, PROPN, PROPN, PROPN, PUNCT]|
```
The schema and what else is in the transformed Dataframe:

```
    root
     |-- id: long (nullable = false)
     |-- content: string (nullable = true)
     |-- document: array (nullable = true)
     |    |-- element: struct (containsNull = true)
     |    |    |-- annotatorType: string (nullable = true)
     |    |    |-- begin: integer (nullable = false)
     |    |    |-- end: integer (nullable = false)
     |    |    |-- result: string (nullable = true)
     |    |    |-- metadata: map (nullable = true)
     |    |    |    |-- key: string
     |    |    |    |-- value: string (valueContainsNull = true)
     |-- sentence: array (nullable = true)
     |    |-- element: struct (containsNull = true)
     |    |    |-- annotatorType: string (nullable = true)
     |    |    |-- begin: integer (nullable = false)
     |    |    |-- end: integer (nullable = false)
     |    |    |-- result: string (nullable = true)
     |    |    |-- metadata: map (nullable = true)
     |    |    |    |-- key: string
     |    |    |    |-- value: string (valueContainsNull = true)
     |-- token: array (nullable = true)
     |    |-- element: struct (containsNull = true)
     |    |    |-- annotatorType: string (nullable = true)
     |    |    |-- begin: integer (nullable = false)
     |    |    |-- end: integer (nullable = false)
     |    |    |-- result: string (nullable = true)
     |    |    |-- metadata: map (nullable = true)
     |    |    |    |-- key: string
     |    |    |    |-- value: string (valueContainsNull = true)
     |-- pos: array (nullable = true)
     |    |-- element: struct (containsNull = true)
     |    |    |-- annotatorType: string (nullable = true)
     |    |    |-- begin: integer (nullable = false)
     |    |    |-- end: integer (nullable = false)
     |    |    |-- result: string (nullable = true)
     |    |    |-- metadata: map (nullable = true)
     |    |    |    |-- key: string
     |    |    |    |-- value: string (valueContainsNull = true)
     |-- token_array: array (nullable = true)
     |    |-- element: string (containsNull = true)
     |-- token_array_filtered: array (nullable = true)
     |    |-- element: string (containsNull = true)
```

#### Environment
* Cloudera CDH 5.15.1
* Apache Spark 2.3.1
* Spark NLP 1.7.x
* Ubuntu 16.4.x

####  Acknowledgements
This work has been done by using ISC-PIF/CNRS(UPS3611) and Multivac Platform infrastructure.


  [1]: http://universaldependencies.org/
  [2]: https://github.com/JohnSnowLabs/spark-nlp


## File Citation
> Panahi, Maziyar;Chavalarias, David, 2018, "multivac_nlp_pos_UD_English-EWT.tar.xz", Multivac Machine Learning Models, https://doi.org/10.7910/DVN/WSWU7K/PSXOAJ, Harvard Dataverse, V2

> Panahi, Maziyar;Chavalarias, David, 2018, "multivac_nlp_pos_UD_French-GSD.tar.xz", Multivac Machine Learning Models, https://doi.org/10.7910/DVN/WSWU7K/6SWRTT, Harvard Dataverse, V2


## Dataset Citation
> Panahi, Maziyar;Chavalarias, David, 2018, "Multivac Machine Learning Models", https://doi.org/10.7910/DVN/WSWU7K, Harvard Dataverse, V2


## Code of Conduct

This, and all github.com/multivacplatform projects, are under the [Multivac Platform Open Source Code of Conduct](https://github.com/multivacplatform/code-of-conduct/blob/master/code-of-conduct.md). Additionally, see the [Typelevel Code of Conduct](http://typelevel.org/conduct) for specific examples of harassing behavior that are not tolerated.

## Copyright and License

Code and documentation copyright (c) 2018 [ISCPIF - CNRS](http://iscpif.fr). Code released under the [MIT license](https://github.com/multivacplatform/multivac-ml/blob/master/LICENSE).
