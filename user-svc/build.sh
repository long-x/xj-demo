#!/usr/bin/env bash
#-X debug查看日志信息 eg: mvn clean  -X package -Dmaven.test.skip=true -U
mvn clean  package -Dmaven.test.skip=true -U

docker build -t hub.c.163.com/honglei666/user-svc .

#docker push hub.c.163.com/honglei666/user-svc

docker save -o ${HOME}/user-svc.rar  hub.c.163.com/honglei666/user-svc:latest

scp ${HOME}/user-svc.rar root@10.10.10.112:/opt/ecdata-cmp

#docker load  -i user-svc.rar

#docker run -d -p 8085:8085 --net bridge --name user-svc hub.c.163.com/honglei666/user-svc:latest
#docker run -p 8085:8085 --net bridge --name user-svc hub.c.163.com/honglei666/user-svc:latest
#docker stop $(docker ps -aq)
#docker rm $(docker ps -aq)
#docker rmi $(docker images -aq)
