FROM openjdk:17-jdk-alpine
LABEL authors="jaycesar"
COPY target/message-flow-0.0.1-SNAPSHOT.jar message-flow.jar
EXPOSE 8080/tcp
ENTRYPOINT ["java","-jar","/message-flow.jar"]
