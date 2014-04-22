#!/bin/bash
set -e -u
cd `dirname $0`
git log > public/log
meteor deploy wb.meteor.com --debug
echo 'Stable? [ENTER / Ctrl+C]'
read
trap "mv tests/developement ." EXIT
mv developement tests/
#meteor deploy wbs.meteor.com 
meteor deploy web-boards.meteor.com
