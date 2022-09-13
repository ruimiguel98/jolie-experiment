FROM openjdk:11

VOLUME /tmp

ARG JAR_FILE=target/OrderService.jar

COPY ${JAR_FILE} OrderService.jar

ENTRYPOINT ["java","-jar","/OrderService.jar"]
