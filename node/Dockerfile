FROM openjdk:8-jdk-stretch

ARG JENKINS_HOME=/var/jenkins_home

ENV JAVA_OPTS=
ENV JENKINS_URL=http://192.168.99.100:8080
ENV NODE_NAME=node-01
ENV SECRET=

# ensure git and build tools are present
RUN apt-get update \
        && apt-get install -y git build-essential
#VOLUME $JENKINS_HOME

RUN mkdir -p $JENKINS_HOME /usr/share/jenkins \
        && groupadd jenkins \
        && useradd -d "$JENKINS_HOME" -g jenkins -c jenkins -s /bin/bash -M jenkins \
        && chown -Rv jenkins:jenkins $JENKINS_HOME /usr/share/jenkins

USER jenkins

ADD agent.jar /usr/share/jenkins/

WORKDIR $JENKINS_HOME

CMD java $JAVA_OPTS -jar /usr/share/jenkins/agent.jar -jnlpUrl ${JENKINS_URL}/computer/${NODE_NAME}/slave-agent.jnlp -secret $SECRET -workDir .
