appid=`git rev-parse --abbrev-ref HEAD |cut -d- -f1`
if [ $appid = 'master' ]; then
        appid=web-boards
fi
echo "Updating $appid... $ver $* "
mvn clean appengine:update -Pprod -Dappid=$appid $ver $*

