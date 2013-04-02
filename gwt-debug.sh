#!/bin/bash
echo $gwt_extraJvmArgs
mvn -f engine/pom.xml gwt:debug -Pdev  -Dgwt.extraJvmArgs="$gwt_extraJvmArgs"
