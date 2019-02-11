# Jenkins #

Bootstrap Jenkins with all plugins and proxy configured

## Build & Run ##

Build the container image

``` sh
docker build -t jenkins-proxy .
```

Run the docker container

```sh
docker run --name -p 8080:8080 -d jenkins-proxy
```
