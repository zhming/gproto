echo
mvn clean protobuf:compile package -Dmaven.repo.local=/opt/apache-maven-3.6.0/maven-jar/repository -f $3/pom.xml -Dmaven.test.skip=true -Dproto-serviceName=$1 -Dproto-timestamp=$2


