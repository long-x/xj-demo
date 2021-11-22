#!/usr/bin/env bash
mvn clean package -Dmaven.test.skip=true -U

docker build -t hub.c.163.com/honglei666/gateway .

#docker push hub.c.163.com/honglei666/gateway

docker save -o ${HOME}/gateway.rar  hub.c.163.com/honglei666/gateway:latest

scp ${HOME}/gateway.rar root@10.10.10.112:/opt/ecdata-cmp

#docker load  -i gateway.rar

#docker run -d -p 18085:18085 --net bridge --name gateway hub.c.163.com/honglei666/gateway:latest
#docker stop $(docker ps -aq)
#docker rm $(docker ps -aq)
#docker rmi $(docker images -aq)
