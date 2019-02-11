FROM jenkins/jenkins:lts-alpine

ADD --chown=jenkins:jenkins proxy.xml /var/jenkins_home/

ADD --chown=jenkins:jenkins plugins/*.jpi /var/jenkins_home/plugins/
