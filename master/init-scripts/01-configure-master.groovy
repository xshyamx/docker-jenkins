import jenkins.plugins.nodejs.tools.*
import jenkins.model.*

import jenkins.install.InstallState

def instance = Jenkins.get()
// rest install state
if (!instance.installState.isSetupComplete()) {
  // do not show setup wizard 
  instance.setInstallState(InstallState.INITIAL_SETUP_COMPLETED)
}
// do not execute anything on the master
instance.setNumExecutors(0)
instance.save()
