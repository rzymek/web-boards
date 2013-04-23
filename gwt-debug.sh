#!/bin/bash
echo $gwt_extraJvmArgs
cd `dirname $0`/engine && mvn gwt:debug -Pdev  -Dgwt.extraJvmArgs="$gwt_extraJvmArgs"
