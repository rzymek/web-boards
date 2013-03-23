#!/bin/bash
set -e
mvn $* install
cd server
mvn $* appengine:devserver
