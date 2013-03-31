cd `dirname $0`
mvn $* -pl engine appengine:devserver -DskipTests
