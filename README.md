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

Accuracy, Precision, Recall and F1-Score against `en_ewt-ud-train.conllu`

|tag  |tp_score|fp_score|fn_score|Precision         |Recall            |F1-Score          |
|-----|--------|--------|--------|------------------|------------------|------------------|
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
| 63292 |0.96       |0.97   |0.97     |

Accuracy, Precision, Recall and F1-Score against `en_ewt-ud-test.conllu`

|tag  |tp_score|fp_score|fn_score|Precision          |Recall            |F1-Score          |
|-----|--------|--------|--------|-------------------|------------------|------------------|
|PUNCT|3515    |2       |6       |0.999    |0.998 |0.998   |
|CCONJ|812     |4       |5       |0.995    |0.994 |0.994   |
|DET  |2399    |24      |23      |0.99     |0.991 |0.99    |
|PRON |2245    |30      |38      |0.987    |0.983 |0.985   |
|AUX  |1456    |37      |72      |0.975    |0.953 |0.964   |
|PART |634     |28      |97      |0.958    |0.867 |0.91    |
|NUM  |384     |22      |17      |0.946    |0.958 |0.952   |
|VERB |2399    |166     |155     |0.935    |0.939 |0.937   |
|ADP  |2039    |141     |138     |0.935    |0.937 |0.936   |
|NOUN |3560    |313     |420     |0.919    |0.894 |0.906   |
|SYM  |34      |4       |1       |0.895    |0.971 |0.931   |
|ADV  |1064    |134     |93      |0.888    |0.92  |0.904   |
|ADJ  |1436    |211     |166     |0.872    |0.896 |0.884   |
|PROPN|1626    |313     |288     |0.839    |0.85  |0.844   |
|SCONJ|314     |72      |40      |0.813    |0.887 |0.848   |
|INTJ |94      |24      |5       |0.797    |0.949 |0.866   |
|X    |36      |47      |8       |0.434    |0.818 |0.567   |


|Tokens |Precision  |Recall |F1-Score |
|-------|-----------|-------|---------|
| 24514 |0.89       |0.93   |0.90     |



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
