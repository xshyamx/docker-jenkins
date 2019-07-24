#!/bin/sh

if [ "$1" == "" ]; then
  cat <<EOF
USAGE: start-nodes.sh <number of nodes>
EOF
  exit 1
fi
n=$1

JENKINS_URL=http://192.168.99.100:8080
JENKINS_USER=jenkins
JENKINS_PASSWORD=jenkins

for i in $(seq $n); do
  node_name=$(printf "node-%02d" $i)
  cat <<EOF > ${node_name}.env
JENKINS_URL=${JENKINS_URL}
JENKINS_USER=${JENKINS_USER}
JENKINS_PASSWORD=${JENKINS_PASSWORD}
NODE_NAME=${node_name}
http_proxy=http://10.0.2.2:3128
https_proxy=http://10.0.2.2:3128
no_proxy=localhost
JAVA_OPTS=-Dhttp.proxyHost=10.0.2.2 -Dhttp.proxyPort=3128 -Dhttps.proxyHost=10.0.2.2 -Dhttps.proxyPort=3128
EOF
  docker run --name $node_name -d --env-file ./${node_name}.env jenkins/node
done
