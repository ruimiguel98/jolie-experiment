FROM openjdk:11

VOLUME /tmp

ARG JAR_FILE=target/UserService.jar

COPY ${JAR_FILE} UserService.jar

ENTRYPOINT ["java","-jar","/UserService.jar"]
