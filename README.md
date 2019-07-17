# Jenkins #

Bootstrap Jenkins with all plugins and proxy configured

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

**NOTE**: Ensure that the master is running before the following steps

```sh
pushd node
./get-agent.sh
popd
```

Build the container image

``` sh
docker-compose build node
```

### Add & Run nodes ###


Add a few nodes using the `add-node.sh` script

``` sh
./add-node.sh node-01
```

This should create a `node-01.env` file with the required environment variables. Now we can start the jenkins node

```sh
docker run --name node-01 -d --env-file ./node-01.env jenkins/node
```

To start a series of nodes use the `start-nodes.sh` script eg.

```sh
./start-nodes.sh 3
```

To clean up the nodes use the `remove-nodes.sh`. Ensure that this run from the same directory from where `start-nodes.sh` was run as it relies on the presence of the `.env` files created by `start-nodes.sh`

```sh
./remove-nodes.sh
```
