#!/bin/bash
git log > public/log
meteor deploy wb.meteor.com --debug
echo 'Stable? [ENTER / Ctrl+C]'
read
#meteor deploy wbs.meteor.com 
meteor deploy web-boards.meteor.com
