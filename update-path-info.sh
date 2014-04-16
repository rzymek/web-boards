#!/bin/bash
host=wb.meteor.com
host=localhost:3000
cd `dirname $0`/public/games/bastogne/
wget http://$host/edit/bastogne/path-info.json
