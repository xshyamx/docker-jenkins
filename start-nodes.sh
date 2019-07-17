#!/bin/sh

if [ "$1" == "" ]; then
  cat <<EOF
USAGE: start-nodes.sh <number of nodes>
EOF
  exit 1
fi
n=$1

for i in $(seq $n); do
  node_name=$(printf "node-%02d" $i)
  ./add-node.sh $node_name
  docker run --name $node_name -d --env-file ./${node_name}.env jenkins/node
done
  
  
