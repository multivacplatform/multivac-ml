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
```
```
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
* Cloudera CDH 6.1
* Apache Spark 2.4.0
* Spark NLP 1.8.0
* Ubuntu 16.4.x / macOS

## Facts and Figures

#### Enlgish POS tagger model (UD_English-EWT)

Only `en_ewt-ud-train.conllu` file was used to train the model:

Precision, Recall and F1-Score against the test dataset `en_ewt-ud-test.conllu`

|tag  |tp_score|fp_score|fn_score|support | Precision          |Recall            |F1-Score          |
|-----|--------|--------|--------|-------|-----------|------------------|------------------|
|PUNCT|3259    |2       |6       |0.999    |0.998 |0.998   |
|CCONJ|758     |4       |6       |0.995    |0.992 |0.993   |
|DET  |2258    |18      |23      |0.992    |0.99  |0.991   |
|PRON |2186    |33      |30      |0.985    |0.986 |0.985   |
|AUX  |1383    |66      |34      |0.954    |0.976 |0.965   |
|INTJ |101     |6       |18      |0.944    |0.849 |0.894   |
|VERB |2271    |141     |146     |0.942    |0.94  |0.941   |
|NUM  |399     |28      |22      |0.934    |0.948 |0.941   |
|PART |640     |48      |70      |0.93     |0.901 |0.915   |
|ADP  |1997    |171     |94      |0.921    |0.955 |0.938   |
|ADV  |1048    |91      |123     |0.92     |0.895 |0.907   |
|SYM  |41      |4       |1       |0.911    |0.976 |0.942   |
|ADJ  |1350    |136     |170     |0.908    |0.888 |0.898   |
|NOUN |3353    |372     |311     |0.9      |0.915 |0.907   |
|SCONJ|321     |43      |63      |0.882    |0.836 |0.858   |
|X    |61      |10      |42      |0.859    |0.592 |0.701   |
|PROPN|1484    |265     |279     |0.848    |0.842 |0.845   |


|Tokens |Precision  |Recall |F1-Score |
|-------|-----------|-------|---------|
| 25831 |0.93       |0.91   |0.92     |


Precision, Recall and F1-Score against the training dataset `en_ewt-ud-train.conllu`

|tag  |tp_score|fp_score|fn_score|support |Precision         |Recall            |F1-Score          |
|-----|--------|--------|--------|---------|---------|------------------|------------------|
|PUNCT|17920   |8       |9       |1.0      |0.999 |0.999   |
|NUM  |2302    |7       |36      |0.997    |0.985 |0.991   |
|CCONJ|4076    |17      |9       |0.996    |0.998 |0.997   |
|DET  |13266   |56      |29      |0.996    |0.998 |0.997   |
|PROPN|9139    |60      |73      |0.993    |0.992 |0.992   |
|NOUN |20021   |157     |112     |0.992    |0.994 |0.993   |
|VERB |12615   |127     |220     |0.99     |0.983 |0.986   |
|PRON |10517   |109     |45      |0.99     |0.996 |0.993   |
|ADJ  |6572    |68      |57      |0.99     |0.991 |0.99    |
|ADV  |5127    |85      |200     |0.984    |0.962 |0.973   |
|X    |524     |10      |60      |0.981    |0.897 |0.937   |
|AUX  |7042    |162     |72      |0.978    |0.99  |0.984   |
|INTJ |460     |17      |8       |0.964    |0.983 |0.973   |
|ADP  |12894   |534     |260     |0.96     |0.98  |0.97    |
|PART |3544    |170     |341     |0.954    |0.912 |0.933   |
|SCONJ|2357    |135     |194     |0.946    |0.924 |0.935   |
|SYM  |136     |9       |6       |0.938    |0.958 |0.948   |


|Tokens |Precision  |Recall |F1-Score |
|-------|-----------|-------|---------|
| 63785 |0.98       |0.98   |0.98     |



> **Precision** is "how useful the POS results are", and **Recall** is "how complete the results are". Precision can be seen as a measure of **exactness or quality**, whereas recall is a measure of **completeness or quantity**. https://en.wikipedia.org/wiki/Precision_and_recall

> The **F1 score** is the harmonic average of the precision and recall, where an F1 score reaches its best value at 1 (perfect precision and recall) and worst at 0. https://en.wikipedia.org/wiki/F1_score

![Precision](https://wikimedia.org/api/rest_v1/media/math/render/svg/26106935459abe7c266f7b1ebfa2a824b334c807)

![Recall](https://wikimedia.org/api/rest_v1/media/math/render/svg/4c233366865312bc99c832d1475e152c5074891b)

![F1 Score](https://wikimedia.org/api/rest_v1/media/math/render/svg/057ffc6b4fa80dc1c0e1f2f1f6b598c38cdd7c23)


##  Acknowledgements
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
