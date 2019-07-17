#!/bin/sh

# requires jq, awk, sed & curl
# Inspired from https://gist.github.com/scarytom/5859142

if [ "$1" == "" ]; then
  cat <<EOF 
USAGE: add-node.sh <node-name>
EOF
  exit 1
fi
NODE_NAME="$1"

JENKINS_USER_ID=jenkins
JENKINS_PASSWORD=jenkins
JENKINS_HOME=/var/jenkins_home

# this should be the docker machine ip of the host
JENKINS_URL=http://192.168.99.100:8080

JURL="curl -s -u $JENKINS_USER_ID:$JENKINS_PASSWORD "

# get crumb
$JURL \
  -o crumb.json \
  $JENKINS_URL/crumbIssuer/api/json

# populate crumb from previous request
CRUMB=$(jq -r .crumb crumb.json)


# create new node
$JURL \
  -XPOST \
  -H "Jenkins-Crumb: $CRUMB" \
  -d name=$NODE_NAME \
  -d type=hudson.slaves.DumbSlave \
  -d 'json={"nodeDescription": "'$NODE_NAME'","labelString": "node linux agent","remoteFS": "'$JENKINS_HOME'"}' \
  $JENKINS_URL/computer/doCreateItem

# verify node is created
$JURL -o ${NODE_NAME}.json \
      $JENKINS_URL/computer/$NODE_NAME/api/json

JENKINS_NODE=$(jq .displayName ${NODE_NAME}.json)

if [ "$JENKINS_NODE" == "$NODE_NAME" ]; then
  echo failed to create node correctly
fi

# Download the jnlp
$JURL -o ${NODE_NAME}.jnlp \
      $JENKINS_URL/computer/$NODE_NAME/slave-agent.jnlp

# extract the secret
SECRET=$(awk -F '<argument>' '{print $2}' ${NODE_NAME}.jnlp | sed 's/<\/argument>//')

cat <<EOF > $NODE_NAME.env
JENKINS_URL=${JENKINS_URL}
NODE_NAME=${NODE_NAME}
SECRET=${SECRET}
http_proxy=http://10.0.2.2:3128
https_proxy=http://10.0.2.2:3128
no_proxy=localhost
EOF

# cleanup

rm -f crumb.json \
   ${NODE_NAME}.json \
   ${NODE_NAME}.jnlp

# print usage
cat <<EOF
Node environment file created at ${NODE_NAME}.env. Run the new node by running

docker run --env-file ./${NODE_NAME}.env --name ${NODE_NAME} jenkins/node
EOF
