# multivac-ml [![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/multivacplatform/multivac-ml/blob/master/LICENSE) [![Build Status](https://travis-ci.org/multivacplatform/multivac-ml.svg?branch=master)](https://travis-ci.org/multivacplatform/multivac-ml) [![multivac discuss](https://img.shields.io/badge/multivac-discuss-ff69b4.svg)](https://discourse.iscpif.fr/c/multivac) [![multivac channel](https://img.shields.io/badge/multivac-chat-ff69b4.svg)](https://chat.iscpif.fr/channel/multivac) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/0df6364b08e84dadadf83e1bc902a58b)](https://app.codacy.com/app/maziyarpanahi/multivac-ml?utm_source=github.com&utm_medium=referral&utm_content=multivacplatform/multivac-ml&utm_campaign=Badge_Grade_Dashboard)
Pre-trained Apache Spark's ML Pipeline for NLP, Classification, etc.

## Project Structure
-   [models](models) Offline ML Models (for downloads)
    -   [models/word2vec](models/word2vec) (Word2Vec Model)
    -   [models/nlp](models/nlp) (Part of Speech Models)
-   [demo](demo) Demo project


## Facts and Fiures
### POS Tagger models

**Enlgish POS tagger model (UD_English-EWT)**
- SimpleAccuracy: **0.92**
- Precision: **0.91**
- Recall: **0.97**
```
 +----------------+-------------------+-----------------+--------------+---------------+---------------+------------------+------------------+------------------+
|TotalWordsInTest|TotalWordsPredicted|TotalTokenMatches|True_Positives|False_Positives|False_Negatives|    SimpleAccuracy|         Precision|            Recall|
+----------------+-------------------+-----------------+--------------+---------------+---------------+------------------+------------------+------------------+
|           25097|              25334|            24514|         22589|           2059|            633|0.9214734437464306|0.9164638104511522|0.9727413659460856|
+----------------+-------------------+-----------------+--------------+---------------+---------------+------------------+------------------+------------------+

```


## Open Data
**Multivac ML data**: [https://dataverse.harvard.edu/dataset.xhtml?persistentId=doi:10.7910/DVN/WSWU7K](https://dataverse.harvard.edu/dataset.xhtml?persistentId=doi:10.7910/DVN/WSWU7K)

**Multivac Open Data**: [https://dataverse.harvard.edu/dataverse/multivac](https://dataverse.harvard.edu/dataverse/multivac)

## Dataset Citation
> PANAHI, Maziyar;Chavalarias, David, 2018, "Multivac Machine Learning Models", https://doi.org/10.7910/DVN/WSWU7K, Harvard Dataverse, V1

## Code of Conduct
This, and all github.com/multivacplatform projects, are under the [Multivac Platform Open Source Code of Conduct](https://github.com/multivacplatform/code-of-conduct/blob/master/code-of-conduct.md). Additionally, see the [Typelevel Code of Conduct](http://typelevel.org/conduct) for specific examples of harassing behavior that are not tolerated.

## Copyright and License
Code and documentation copyright (c) 2018 [ISCPIF - CNRS](http://iscpif.fr). Code released under the [MIT license](https://github.com/multivacplatform/multivac-ml/blob/master/LICENSE).
