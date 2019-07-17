import jenkins.plugins.nodejs.tools.*
import jenkins.model.*
import hudson.security.csrf.*
import jenkins.security.s2m.AdminWhitelistRule

def instance = Jenkins.get()

// enable CSRF protection
instance.setCrumbIssuer(new DefaultCrumbIssuer(true))

// Enable agent access control
Jenkins.instance.getInjector().getInstance(AdminWhitelistRule.class).setMasterKillSwitch(false)

// do not execute anything on the master
instance.setNumExecutors(0)
instance.save()
