#!/bin/sh

# requires curl
# this should be the docker machine ip of the host
JENKINS_URL=http://192.168.99.100:8080

# Download the agent jar
curl -o agent.jar $JENKINS_URL/jnlpJars/agent.jar
