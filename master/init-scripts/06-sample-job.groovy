import jenkins.model.*
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;

def pipelineGroovy = '''
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
    withMaven(maven: 'M3') {
      sh 'mvn -Dmaven.test.failure.ignore=true clean package'
    }
    //alternate
    /*
    withEnv(["MAVEN_HOME=${mvnHome}"]) {
      sh '$MAVEN_HOME/bin/mvn -Dmaven.test.failure.ignore=true clean package'
    }
    */
  }
  stage('Results') {
    junit '**/target/surefire-reports/TEST-*.xml'
    archiveArtifacts 'target/*.jar'
  }
}
'''

def instance = Jenkins.get()
def sample = instance.createProject(WorkflowJob.class, "sample-maven");
sample.setDefinition(new CpsFlowDefinition(pipelineGroovy, true));
instance.getJob('sample-maven').setDescription('A sample pipeline example using maven')
sample.save()

def nodePipeline = '''
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
def nodejs = instance.createProject(WorkflowJob.class, "sample-nodejs");
nodejs.setDefinition(new CpsFlowDefinition(nodePipeline, true));
instance.getJob('sample-nodejs').setDescription('A sample pipeline example using nodejs')
nodejs.save()

instance.queue.schedule(sample WorkflowJob, 0)
instance.queue.schedule(nodejs as WorkflowJob, 0)
