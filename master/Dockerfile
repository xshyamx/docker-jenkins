FROM jenkins/jenkins:lts-alpine

ADD --chown=jenkins:jenkins proxy.xml /var/jenkins_home/

#ADD --chown=jenkins:jenkins plugins/*.jpi /var/jenkins_home/plugins/

ADD --chown=jenkins:jenkins jenkins-user.groovy /usr/share/jenkins/ref/init.groovy.d/jenkins-user.groovy

ADD --chown=jenkins:jenkins plugins.txt /usr/share/jenkins/ref/plugins.txt

RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
