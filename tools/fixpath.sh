#!/bin/bash
set -e
from="${PWD//\//\\/}"
cat $1 | sed -e "s/\\(file:[/][/]\\)\\?${from}[/]//g" > tmp
mv -v tmp $1

