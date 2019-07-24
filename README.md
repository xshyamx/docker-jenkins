# Jenkins #

Bootstrap Jenkins with all plugins and proxy configured. Assumption is that docker is running via docker-machine whose host-only-ip is `192.168.99.100` and the proxy is running on the host in port 3128 so, the proxy configuration will be `http://10.0.2.2:3128`

## Build & Run ##

### Jenkins Master ###

From an existing jenkins installation get the shortnames of all installed plugins from the script console (`http://${JENKINS_HOST}/script`)

```groovy
Jenkins.instance.pluginManager.plugins.each{
  plugin ->
    println ("${plugin.getShortName()}")
}
```

Create the list of shortnames in `plugins.txt` and place it in the `./master` folder

Build the container image of the latest LTS version

``` sh
docker-compose build master
```

Run the Jenkins master

``` sh
docker-compose up -d master
```

### Jenkins Node ###

Jenkins nodes are created using the [Swarm Plugin](https://wiki.jenkins.io/display/JENKINS/Swarm+Plugin) so, ensure that the plugins.txt contains it.

Build the container image

``` sh
docker-compose build node
```

### Add & Run nodes ###

To start a series of nodes use the `start-nodes.sh` script eg.

```sh
./start-nodes.sh 3
```

To clean up the nodes use the `remove-nodes.sh`. Ensure that this run from the same directory from where `start-nodes.sh` was run as it relies on the presence of the `.env` files created by `start-nodes.sh`

```sh
./remove-nodes.sh
```

### Cleanup ###

Ensure that the master node and the associated volume is deleted

```sh
docker-compose down -v
```
