FROM jenkins/jenkins:lts-alpine

ADD --chown=jenkins:jenkins proxy.xml /var/jenkins_home/

ADD --chown=jenkins:jenkins plugins/*.jpi /var/jenkins_home/plugins/

ADD --chown=jenkins:jenkins org.jenkinsci.main.modules.sshd.SSHD.xml /var/jenkins_home/
