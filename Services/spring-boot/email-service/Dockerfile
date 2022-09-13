FROM openjdk:11

VOLUME /tmp

ARG JAR_FILE=target/EmailService.jar

COPY ${JAR_FILE} EmailService.jar

ENTRYPOINT ["java","-jar","/EmailService.jar"]
