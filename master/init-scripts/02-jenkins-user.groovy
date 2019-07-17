import jenkins.model.*
import hudson.security.*

def instance = Jenkins.get()

// Create new user
println '--> Creating admin user'
instance.setSecurityRealm(new HudsonPrivateSecurityRealm(false))
def user = instance.getSecurityRealm().createAccount("jenkins", "jenkins")
user.save()

def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
strategy.setAllowAnonymousRead(false)
instance.setAuthorizationStrategy(strategy)

def jlc = JenkinsLocationConfiguration.get()
jlc.setUrl('http://192.168.99.100:8080/')
jlc.save()

instance.save()
