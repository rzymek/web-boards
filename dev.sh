#!/bin/bash
set -e
mvn $* install -DskipTests
cd wb-server
mvn $* appengine:devserver -DskipTests
