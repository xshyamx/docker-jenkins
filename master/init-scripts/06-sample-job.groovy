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
    withEnv(["MVN_HOME=$mvnHome"]) {
      configFileProvider([configFile(fileId: 'maven-proxy', variable: 'MAVEN_WITH_PROXY')]) {
        // use maven-proxy configurations
        sh '$MVN_HOME/bin/mvn -s $MAVEN_SETTINGS $MAVEN_WITH_PROXY -Dmaven.test.failure.ignore=true clean package'
      }
    }
  }
  stage('Results') {
    junit '**/target/surefire-reports/TEST-*.xml'
    archiveArtifacts 'target/*.jar'
  }
}
'''

def instance = Jenkins.get()
def simple = instance.createProject(WorkflowJob.class, "simple");
simple.setDefinition(new CpsFlowDefinition(pipelineGroovy, true));
simple.save()
