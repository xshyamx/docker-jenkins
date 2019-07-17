#!/bin/sh

# requires curl

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


for node_env in *.env; do
  node=$(basename $node_env .env)
  # stop container
  if docker stop $node ; then
    docker rm $node
  fi
  SCRIPT=$(cat <<EOF 
def instance = Jenkins.get();
def node = instance.slaves.find { it.name == "${node}" };
if ( node != null ) {
  node.getComputer().doDoDelete();
} else {
  println("Failed to find ${node}");
}
EOF
           )
  ## remove node
  if $JURL \
       -XPOST \
       -o /dev/null \
       -H "Jenkins-Crumb: $CRUMB" \
       -H "Accept: application/json" \
       -d "script=$SCRIPT" \
       $JENKINS_URL/script
  then
    rm -f $node
  fi
done

