#!/usr/bin/env bash

echo "Downloading UD_English-EWT"
wget -N https://github.com/UniversalDependencies/UD_English-EWT/raw/master/en_ewt-ud-train.conllu -P data/ud-treebanks-v2.3/
wget -N https://github.com/UniversalDependencies/UD_English-EWT/raw/master/en_ewt-ud-dev.conllu -P data/ud-treebanks-v2.3/
wget -N https://github.com/UniversalDependencies/UD_English-EWT/raw/master/en_ewt-ud-test.conllu -P data/ud-treebanks-v2.3/

echo "Downloading UD_French-GSD"
wget -N https://github.com/UniversalDependencies/UD_French-GSD/raw/master/fr_gsd-ud-train.conllu -P data/ud-treebanks-v2.3/
wget -N https://github.com/UniversalDependencies/UD_French-GSD/raw/master/fr_gsd-ud-dev.conllu -P data/ud-treebanks-v2.3/
wget -N https://github.com/UniversalDependencies/UD_French-GSD/raw/master/fr_gsd-ud-test.conllu -P data/ud-treebanks-v2.3/

echo "Downloading UD_Persian-Seraji"
wget -N https://github.com/UniversalDependencies/UD_Persian-Seraji/raw/master/fa_seraji-ud-train.conllu -P data/ud-treebanks-v2.3/
wget -N https://github.com/UniversalDependencies/UD_Persian-Seraji/raw/master/fa_seraji-ud-dev.conllu -P data/ud-treebanks-v2.3/
wget -N https://github.com/UniversalDependencies/UD_Persian-Seraji/raw/master/fa_seraji-ud-test.conllu -P data/ud-treebanks-v2.3/