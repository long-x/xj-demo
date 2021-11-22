FROM java:8-jdk-alpine

MAINTAINER shl 409178623@qq.com

COPY ./target/gateway-1.0.0.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch gateway-1.0.0.jar'

EXPOSE 18085

ENTRYPOINT ["java", "-jar", "gateway-1.0.0.jar"]
