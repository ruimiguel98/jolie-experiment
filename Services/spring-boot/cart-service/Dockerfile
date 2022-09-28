FROM openjdk:11

VOLUME /tmp

ARG JAR_FILE=./target/CartService.jar

COPY ${JAR_FILE} CartService.jar

ENTRYPOINT ["java","-jar","/CartService.jar"]
