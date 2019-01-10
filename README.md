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
|PUNCT|9265    |2       |13      |0.9997841804251646|0.998598835956025 |0.9991911566459962|
|NUM  |1006    |2       |5       |0.998015873015873 |0.9950544015825915|0.9965329370975731|
|CCONJ|2233    |5       |11      |0.9977658623771224|0.9950980392156863|0.9964301651048638|
|DET  |7137    |23      |28      |0.9967877094972067|0.9960921144452198|0.9964397905759163|
|PRON |4974    |52      |49      |0.9896538002387585|0.990244873581525 |0.9899492486814608|
|NOUN |10167   |108     |121     |0.9894890510948905|0.9882387247278382|0.988863492681029 |
|AUX  |3734    |51      |90      |0.9865257595772787|0.9764644351464435|0.9814693126560652|
|PROPN|5369    |74      |68      |0.986404556310858 |0.9874931028140519|0.9869485294117648|
|VERB |6447    |126     |108     |0.9808306709265175|0.9835240274599543|0.9821755027422302|
|ADJ  |3523    |69      |72      |0.9807906458797327|0.9799721835883171|0.98038124391262  |
|INTJ |204     |4       |6       |0.9807692307692307|0.9714285714285714|0.9760765550239234|
|SYM  |193     |6       |4       |0.9698492462311558|0.9796954314720813|0.9747474747474748|
|ADP  |6511    |259     |241     |0.9617429837518464|0.9643068720379147|0.9630232214169501|
|ADV  |2528    |117     |108     |0.955765595463138 |0.9590288315629742|0.957394432872562 |
|PART |1786    |113     |171     |0.9404949973670352|0.912621359223301 |0.9263485477178423|
|SCONJ|1255    |130     |107     |0.9061371841155235|0.92143906020558  |0.9137240626137604|
|X    |226     |63      |2       |0.7820069204152249|0.9912280701754386|0.874274661508704 |


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
