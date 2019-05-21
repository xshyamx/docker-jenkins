#!/bin/sh

mkdir -p plugins
while read plugin; do
  if [ ! -e plugins/${plugin}.jpi ]; then
    printf "Downloading %s..." $plugin
    curl -s -o plugins/${plugin}.jpi -L "https://updates.jenkins.io/latest/${plugin}.hpi"
    echo done
  else
    echo Skipping $plugin. File already present
  fi
done < plugins.txt
