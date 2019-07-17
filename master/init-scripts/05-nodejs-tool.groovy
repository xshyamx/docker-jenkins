import hudson.model.*
import hudson.tools.*
import jenkins.plugins.nodejs.tools.*
import jenkins.model.*

def inst = Jenkins.getInstance()
def desc = inst.getDescriptor("jenkins.plugins.nodejs.tools.NodeJSInstallation")
def versions = [
  "node-lts": "10.16.0",
  "node-current": "12.6.0",
]
def installations = desc.getInstallations() as List

for (v in versions) {
  def installer = new NodeJSInstaller(v.value, "", 100)
  def installerProps = new InstallSourceProperty([installer])
  def installation = new NodeJSInstallation(v.key, "", [installerProps])
  installations.add(installation)
}
desc.setInstallations(installations as jenkins.plugins.nodejs.tools.NodeJSInstallation[])
desc.save()

// https://stackoverflow.com/questions/40915072/jenkins-global-tool-installation-auto-install-nodejs-scripts
