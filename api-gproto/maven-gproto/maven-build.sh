echo
mvn clean protobuf:compile package -Dmaven.repo.local=/mnt/c/Users/qianzhm/.m2/repository -f $3/pom.xml -Dmaven.test.skip=true -Dproto-serviceName=$1 -Dproto-timestamp=$2


