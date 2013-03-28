#!/bin/bash
set -e
mvn $* -pl core -am install -DskipTests
mvn $* -pl engine appengine:devserver -DskipTests
