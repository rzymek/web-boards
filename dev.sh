cd `dirname $0`
mvn -f engine/pom.xml $* appengine:devserver -DskipTests -Pdev
