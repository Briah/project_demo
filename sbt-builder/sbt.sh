#!/bin/bash
set -e

update-ca-certificates

if [ "$#" -eq 0 ]; then
  exec sbt sbtVersion
else
  exec "$@"
fi

