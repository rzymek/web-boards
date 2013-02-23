appid=`git rev-parse --abbrev-ref HEAD |cut -d- -f1`
ver=`git rev-parse --abbrev-ref HEAD |cut -d- -f2`
if [ $appid = 'master' ]; then
        appid=web-boards
fi
if [ x"$ver" != x ];then
        ver="-Dver=$ver"
fi
echo "Updating $appid... $ver $* "
mvn clean appengine:update -Pprod -Dappid=$appid $ver $*

