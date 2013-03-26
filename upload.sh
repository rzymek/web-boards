appid=`git rev-parse --abbrev-ref HEAD |cut -d- -f1`
if [ $appid = 'master' ]; then
        appid=web-boards
fi
echo "Updating $appid... $ver $* "
mvn -pl wb-core -am clean install -Pprod
mvn -pl wb-server clean appengine:update -Pprod -Dappid=$appid $ver $*

