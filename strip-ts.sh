#!/bin/bash
find client/ common/ server/ -name *.ts | grep -v -F .d.ts | xargs tsc
meteor remove typescript-compiler

