#!/bin/sh

for node_env in *.env; do
  node=$(basename $node_env .env)
  # stop container
  if docker stop $node ; then
    docker rm $node
  fi
  ## remove node
  rm -f $node_env
done
