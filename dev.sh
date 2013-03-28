#!/bin/bash
set -e
mvn $* -pl core install -DskipTests
mvn $* -pl engine appengine:devserver -DskipTests
