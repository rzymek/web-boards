appid=`git rev-parse --abbrev-ref HEAD`
if [ $appid = 'master' ]; then
	appid=web-boards
fi
echo "Updating $appid... $*"
mvn clean appengine:update -Pprod -Dappid=$appid $*
