FROM openjdk:8-jdk-stretch

ARG JENKINS_HOME=/var/jenkins_home

ENV JAVA_OPTS=
ENV JENKINS_URL=http://192.168.99.100:8080
ENV NODE_NAME=node-01
ENV SECRET=
ENV SWARM_PLUGIN_VERSION=3.9

# ensure git and build tools are present
RUN apt-get update \
        && apt-get install -y git build-essential
#VOLUME $JENKINS_HOME

RUN mkdir -p $JENKINS_HOME /usr/share/jenkins \
        && groupadd jenkins \
        && useradd -d "$JENKINS_HOME" -g jenkins -c jenkins -s /bin/bash -M jenkins \
        && chown -Rv jenkins:jenkins $JENKINS_HOME /usr/share/jenkins

USER jenkins

#ADD https://repo.jenkins-ci.org/releases/org/jenkins-ci/plugins/swarm-client/${SWARM_PLUGIN_VERSION}/swarm-client-${SWARM_PLUGIN_VERSION}.jar /usr/share/jenkins/swarm-client.jar
ADD swarm-client.jar.sha256 /usr/share/jenkins/swarm-client.jar.sha256

RUN curl -o /usr/share/jenkins/swarm-client.jar \
        -L https://repo.jenkins-ci.org/releases/org/jenkins-ci/plugins/swarm-client/${SWARM_PLUGIN_VERSION}/swarm-client-${SWARM_PLUGIN_VERSION}.jar \
    && sha256sum -c /usr/share/jenkins/swarm-client.jar.sha256

WORKDIR $JENKINS_HOME

CMD java $JAVA_OPTS \
     -jar /usr/share/jenkins/swarm-client.jar \
     -name $NODE_NAME \
     -username $JENKINS_USER \
     -password $JENKINS_PASSWORD \
     -master $JENKINS_URL

