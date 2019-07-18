import jenkins.model.*
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;

def mavenJob = [
  name: 'sample-maven',
  description: 'A sample maven pipeline example using maven proxy settings',
  script: '''
node {
  def mvnHome
  stage('Preparation') { // for display purposes
    // Get some code from a GitHub repository
    git 'https://github.com/jglick/simple-maven-project-with-tests.git'
    // Get the Maven tool.
    // ** NOTE: This 'M3' Maven tool must be configured
    // **       in the global configuration.           
    mvnHome = tool 'M3'
  }
  stage('Build') {
    // Run the maven build
    withMaven(maven: 'M3', mavenSettingsConfig: 'maven-proxy') {
      sh 'mvn -Dmaven.test.failure.ignore=true clean package'
    }
    //alternate
    /*
    withEnv(["MVN_HOME=$mvnHome"]) {
      configFileProvider([configFile(fileId: 'maven-proxy', variable: 'MAVEN_WITH_PROXY')]) {
      // use maven-proxy configurations
      sh '$MVN_HOME/bin/mvn -s $MAVEN_SETTINGS $MAVEN_WITH_PROXY -Dmaven.test.failure.ignore=true clean package'
    }
    */
  }
  stage('Results') {
    junit '**/target/surefire-reports/TEST-*.xml'
    archiveArtifacts 'target/*.jar'
  }
}
'''
]
def nodejsJob = [
  name: 'sample-nodejs',
  description: 'A sample pipeline example using nodejs',
  script: '''
node {
  stage('Prepare') {
    git 'https://github.com/gustavoapolinario/node-todo-frontend.git'
    nodejsHome = tool 'node-lts'
  }
  stage('Build') {
    withEnv(["PATH=${nodejsHome}/bin:${env.PATH}"]) {
      sh 'npm install'
    }
  }
  stage('Test') {
    withEnv(["PATH=${nodejsHome}/bin:${env.PATH}"]) {
      sh 'npm test'
    }
  }
}'''
]

def instance = Jenkins.get()

[mavenJob, nodejsJob].each {
  def job = instance.createProject(WorkflowJob.class, it.name);
  job.setDefinition(new CpsFlowDefinition(it.script, true))
  instance.getJob(it.name).setDescription(it.description)
  job.save()
  // queue a job
  instance.queue.schedule(job, 0)
}
