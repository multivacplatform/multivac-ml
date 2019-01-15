# multivac-ml [![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/multivacplatform/multivac-ml/blob/master/LICENSE) [![Build Status](https://travis-ci.org/multivacplatform/multivac-ml.svg?branch=master)](https://travis-ci.org/multivacplatform/multivac-ml) [![multivac discuss](https://img.shields.io/badge/multivac-discuss-ff69b4.svg)](https://discourse.iscpif.fr/c/multivac) [![multivac channel](https://img.shields.io/badge/multivac-chat-ff69b4.svg)](https://chat.iscpif.fr/channel/multivac) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/0df6364b08e84dadadf83e1bc902a58b)](https://app.codacy.com/app/maziyarpanahi/multivac-ml?utm_source=github.com&utm_medium=referral&utm_content=multivacplatform/multivac-ml&utm_campaign=Badge_Grade_Dashboard)
Pre-trained Apache Spark's ML Pipeline for NLP, Classification, etc.

## Project Structure
-   [models](models) Offline ML Models (for downloads)
    -   [models/word2vec](models/word2vec) (Word2Vec Model)
    -   [models/nlp](models/nlp) (Part of Speech Models)
-   [demo](demo) Demo project


## Facts and Figures
### POS Tagger models

**Enlgish POS tagger model (UD_English-EWT)**

Precision, Recall and F1-Score against `en_ewt-ud-test.conllu`

|tag  |tp_score|fp_score|fn_score|support | Precision          |Recall            |F1-Score          |
|-----|--------|--------|--------|-------|-----------|------------------|------------------|
|PUNCT|3409    |6       |5       |3420   |0.998    |0.999 |0.998   |
|CCONJ|760     |4       |6       |770    |0.995    |0.992 |0.993   |
|DET  |2262    |22      |18      |2302   |0.99     |0.992 |0.991   |
|PRON |2184    |32      |36      |2252   |0.986    |0.984 |0.985   |
|AUX  |1386    |36      |66      |1488   |0.975    |0.955 |0.965   |
|ADP  |2009    |89      |179     |2277   |0.958    |0.918 |0.938   |
|NUM  |407     |21      |29      |457    |0.951    |0.933 |0.942   |
|SYM  |42      |3       |4       |49     |0.933    |0.913 |0.923   |
|VERB |2264    |166     |156     |2586   |0.932    |0.936 |0.934   |
|NOUN |3359    |328     |358     |4045   |0.911    |0.904 |0.907   |
|PART |640     |70      |53      |763    |0.901    |0.924 |0.912   |
|ADV  |1050    |131     |86      |1267   |0.889    |0.924 |0.906   |
|ADJ  |1358    |175     |132     |1665   |0.886    |0.911 |0.898   |
|INTJ |103     |17      |4       |124    |0.858    |0.963 |0.907   |
|PROPN|1572    |268     |293     |2133   |0.854    |0.843 |0.848   |
|SCONJ|314     |68      |37      |419    |0.822    |0.895 |0.857   |
|X    |43      |38      |12      |93     |0.531    |0.782 |0.633   |


|Tokens |Precision  |Recall |F1-Score |
|-------|-----------|-------|---------|
| 25831 |0.91       |0.94   |0.92     |


Precision, Recall and F1-Score against `en_ewt-ud-train.conllu`

|tag  |tp_score|fp_score|fn_score|support |Precision         |Recall            |F1-Score          |
|-----|--------|--------|--------|---------|---------|------------------|------------------|
|PUNCT|9265    |2       |13      |1.0      |0.999 |0.999   |
|NUM  |1006    |2       |5       |0.998    |0.995 |0.996   |
|CCONJ|2233    |5       |11      |0.998    |0.995 |0.996   |
|DET  |7137    |23      |28      |0.997    |0.996 |0.996   |
|PRON |4974    |52      |49      |0.99     |0.99  |0.99    |
|NOUN |10167   |108     |121     |0.989    |0.988 |0.988   |
|AUX  |3734    |51      |90      |0.987    |0.976 |0.981   |
|PROPN|5369    |74      |68      |0.986    |0.987 |0.986   |
|ADJ  |3523    |69      |72      |0.981    |0.98  |0.98    |
|VERB |6447    |126     |108     |0.981    |0.984 |0.982   |
|INTJ |204     |4       |6       |0.981    |0.971 |0.976   |
|SYM  |193     |6       |4       |0.97     |0.98  |0.975   |
|ADP  |6511    |259     |241     |0.962    |0.964 |0.963   |
|ADV  |2528    |117     |108     |0.956    |0.959 |0.957   |
|PART |1786    |113     |171     |0.94     |0.913 |0.926   |
|SCONJ|1255    |130     |107     |0.906    |0.921 |0.913   |
|X    |226     |63      |2       |0.782    |0.991 |0.874   |


|Tokens |Precision  |Recall |F1-Score |
|-------|-----------|-------|---------|
| 63785 |0.98       |0.97   |0.97     |

> **Precision** is "how useful the POS results are", and **Recall** is "how complete the results are". Precision can be seen as a measure of **exactness or quality**, whereas recall is a measure of **completeness or quantity**. https://en.wikipedia.org/wiki/Precision_and_recall

> The **F1 score** is the harmonic average of the precision and recall, where an F1 score reaches its best value at 1 (perfect precision and recall) and worst at 0. https://en.wikipedia.org/wiki/F1_score

![Precision](https://wikimedia.org/api/rest_v1/media/math/render/svg/26106935459abe7c266f7b1ebfa2a824b334c807)

![Recall](https://wikimedia.org/api/rest_v1/media/math/render/svg/4c233366865312bc99c832d1475e152c5074891b)

![F1 Score](https://wikimedia.org/api/rest_v1/media/math/render/svg/057ffc6b4fa80dc1c0e1f2f1f6b598c38cdd7c23)

## Open Data
**Multivac ML data**: [https://dataverse.harvard.edu/dataset.xhtml?persistentId=doi:10.7910/DVN/WSWU7K](https://dataverse.harvard.edu/dataset.xhtml?persistentId=doi:10.7910/DVN/WSWU7K)

**Multivac Open Data**: [https://dataverse.harvard.edu/dataverse/multivac](https://dataverse.harvard.edu/dataverse/multivac)

## Dataset Citation
> Panahi, Maziyar;Chavalarias, David, 2018, "Multivac Machine Learning Models", https://doi.org/10.7910/DVN/WSWU7K, Harvard Dataverse, V2

## Code of Conduct
This, and all github.com/multivacplatform projects, are under the [Multivac Platform Open Source Code of Conduct](https://github.com/multivacplatform/code-of-conduct/blob/master/code-of-conduct.md). Additionally, see the [Typelevel Code of Conduct](http://typelevel.org/conduct) for specific examples of harassing behavior that are not tolerated.

## Copyright and License
Code and documentation copyright (c) 2018-2019 [ISCPIF - CNRS](http://iscpif.fr). Code released under the [MIT license](https://github.com/multivacplatform/multivac-ml/blob/master/LICENSE).
