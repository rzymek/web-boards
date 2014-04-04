#!/bin/bash
cd `dirname $0`
while true;do 
  inotifywait -e modify src
  echo '(function(){' > hooks.js
  awk 'FNR==1{print ""}1' src/*.js >> hooks.js  
  echo '})();' >> hooks.js
done

