##!/bin/bash
if [ -d "/opt/env/" ]; then
  for file in /opt/env/*.sh
  do
    . "$file"
    echo $file
  done
fi   

if [ -d "/sidecar/env/" ]; then
  for file in /sidecar/env/*.sh
  do
    . "$file"
    echo $file
  done
fi

serverPort=$1
nacosAddr=$2
nacosSpace=$3
nacosUsername=$4
nacosPassword=$5
discoveryNamespace=$6





echo $serverPort
echo $nacosAddr
echo $nacosSpace 
echo $JAVA_OPTS
echo $nacosUsername
echo $discoveryNamespace

java $JAVA_OPTS -jar /opt/gproto/api-gproto.jar  --SERVER_PORT=$serverPort --REGISTER_ADDR=$nacosAddr --NAMESPACE=$nacosSpace --NACOS_USERNAME=$nacosUsername --NACOS_PASSWORD=$nacosPassword  --DISCOVERY_NAMESPACE=$discoveryNamespace
