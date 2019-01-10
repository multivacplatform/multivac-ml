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
| 63292 |0.964      |0.975  |0.969    |

Accuracy, Precision, Recall and F1-Score against `en_ewt-ud-test.conllu`

|tag  |tp_score|fp_score|fn_score|Precision          |Recall            |F1-Score          |
|-----|--------|--------|--------|-------------------|------------------|------------------|
|PUNCT|3515    |2       |6       |0.9994313335228888 |0.9982959386537915|0.9988633134413185|
|CCONJ|812     |4       |5       |0.9950980392156863 |0.9938800489596084|0.9944886711573792|
|DET  |2399    |24      |23      |0.9900949236483698 |0.990503715937242 |0.9902992776057792|
|PRON |2245    |30      |38      |0.9868131868131869 |0.9833552343407796|0.985081175954366 |
|AUX  |1456    |37      |72      |0.9752176825184193 |0.9528795811518325|0.9639192320423702|
|PART |634     |28      |97      |0.9577039274924471 |0.8673050615595075|0.9102656137832018|
|NUM  |384     |22      |17      |0.9458128078817734 |0.9576059850374065|0.9516728624535316|
|ADP  |2039    |141     |138     |0.9353211009174311 |0.9366100137804317|0.9359651136102823|
|VERB |2399    |166     |155     |0.9352826510721247 |0.9393108848864526|0.9372924399296737|
|NOUN |3560    |313     |420     |0.9191840950167829 |0.8944723618090452|0.9066598752069273|
|SYM  |34      |4       |1       |0.8947368421052632 |0.9714285714285714|0.9315068493150684|
|ADV  |1064    |134     |93      |0.8881469115191987 |0.9196197061365601|0.9036093418259024|
|ADJ  |1436    |211     |166     |0.8718882817243473 |0.8963795255930087|0.883964296706679 |
|PROPN|1626    |313     |288     |0.8385765858690046 |0.8495297805642633|0.8440176485855176|
|SCONJ|314     |72      |40      |0.8134715025906736 |0.8870056497175142|0.8486486486486486|
|INTJ |94      |24      |5       |0.7966101694915254 |0.9494949494949495|0.8663594470046082|
|X    |36      |47      |8       |0.43373493975903615|0.8181818181818182|0.5669291338582677|


|Tokens |Precision  |Recall |F1-Score |
|-------|-----------|-------|---------|
| 24514 |0.892      |0.929  |0.907    |



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
Code and documentation copyright (c) 2018 [ISCPIF - CNRS](http://iscpif.fr). Code released under the [MIT license](https://github.com/multivacplatform/multivac-ml/blob/master/LICENSE).
