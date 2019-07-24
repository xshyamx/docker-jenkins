#!/bin/sh

for node_env in *.env; do
  node=$(basename $node_env .env)
  # stop container
  if docker stop $node ; then
    if docker rm $node ; then
      ## remove node
      rm -f $node_env
    fi
  fi
done
