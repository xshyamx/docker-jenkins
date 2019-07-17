FROM jenkins/jenkins:lts-alpine

ADD --chown=jenkins:jenkins init-scripts/ /usr/share/jenkins/ref/init.groovy.d/

ADD --chown=jenkins:jenkins plugins.txt /usr/share/jenkins/ref/plugins.txt

RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
