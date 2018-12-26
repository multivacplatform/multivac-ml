#!/usr/bin/env bash
wget -N https://github.com/UniversalDependencies/UD_English-EWT/raw/master/en_ewt-ud-train.conllu -P data/ud-treebanks-v2.3/
wget -N https://github.com/UniversalDependencies/UD_English-EWT/raw/master/en_ewt-ud-dev.conllu -P data/ud-treebanks-v2.3/
wget -N https://github.com/UniversalDependencies/UD_English-EWT/raw/master/en_ewt-ud-test.conllu -P data/ud-treebanks-v2.3/

wget -N https://github.com/UniversalDependencies/UD_French-GSD/raw/master/fr_gsd-ud-train.conllu -P data/ud-treebanks-v2.3/
wget -N https://github.com/UniversalDependencies/UD_French-GSD/raw/master/fr_gsd-ud-dev.conllu -P data/ud-treebanks-v2.3/
wget -N https://github.com/UniversalDependencies/UD_French-GSD/raw/master/fr_gsd-ud-test.conllu -P data/ud-treebanks-v2.3/