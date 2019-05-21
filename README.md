# Jenkins #

Bootstrap Jenkins with all plugins and proxy configured

## Build & Run ##

From an existing jenkins installation get the shortnames of all installed plugins from the script console (`http://${JENKINS_HOST}/script`)

```groovy
Jenkins.instance.pluginManager.plugins.each{
  plugin ->
    println ("${plugin.getShortName()}")
}
```

Download the plugins locally

``` sh
./download-plugins.sh
```

Build the container image

``` sh
docker build -t jenkins-proxy .
```

Run the docker container

```sh
docker run --name jenkins -p 8080:8080 -d jenkins-proxy
```

Stop and remove container

```sh
docker stop jenkins && docker rm jenkins
```
