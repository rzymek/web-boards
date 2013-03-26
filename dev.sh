#!/bin/bash
set -e
mvn $* -pl wb-core -am install -DskipTests
mvn $* -pl wb-server appengine:devserver -DskipTests
