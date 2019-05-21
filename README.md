# Jenkins #

Bootstrap Jenkins with all plugins and proxy configured

## Build & Run ##

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
