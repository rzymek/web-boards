#!/bin/bash
cd `dirname $0`
mvn -f engine/pom.xml $* generate-sources gwt:compile -Pdev -DskipTests 
