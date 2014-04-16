#!/bin/bash
host=wb.meteor.com
host=localhost:3000
wget http://$host/edit/bastogne/path-info.json -O `dirname $0`/public/games/bastogne/path-info.json
