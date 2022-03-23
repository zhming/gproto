:: -Dmaven.test.skip=true
mvn clean protobuf:compile package -f %3/pom.xml  -Dproto-serviceName=%1 -Dproto-timestamp=%2
cp %3/target/*.jar d:\work\temp\jars