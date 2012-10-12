#!/bin/bash
set -e
from="${PWD//\//\\/}"
cat bastogne-opt.svg | sed -e "s/\\(file:[/][/]\\)\\?${from}[/]//g" > tmp
mv -v tmp bastogne-opt.svg 

