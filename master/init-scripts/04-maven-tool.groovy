import hudson.model.*
import hudson.tools.*
import hudson.tasks.*
import jenkins.model.*
// maven config file
import org.jenkinsci.plugins.configfiles.maven.*; 
import org.jenkinsci.plugins.configfiles.*;


def inst = Jenkins.getInstance()
def desc = inst.getDescriptor("hudson.tasks.Maven")
def versions = [
  "M3": "3.6.1",
]
def installations = desc.getInstallations() as List

for (v in versions) {
  def installer = new Maven.MavenInstaller(v.value)
  def installerProps = new InstallSourceProperty([installer])
  def installation = new Maven.MavenInstallation(v.key, "", [installerProps])
  installations.add(installation)
}
desc.setInstallations(installations as Maven.MavenInstallation[])
desc.save()

// add new settings file
def settingsXml = '''<?xml version="1.0" encoding="UTF-8"?>

<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" 
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">

  <proxies>
    <proxy>
      <id>optional</id>
      <active>true</active>
      <protocol>http</protocol>
      <host>10.0.2.2</host>
      <port>3128</port>
      <nonProxyHosts>localhost</nonProxyHosts>
    </proxy>
  </proxies>

</settings>
'''

def proxyConfig = new MavenSettingsConfig('maven-proxy', 'maven-proxy', 'Maven settings with proxy configured', settingsXml, true, null)

GlobalConfigFiles.get().save(proxyConfig)
