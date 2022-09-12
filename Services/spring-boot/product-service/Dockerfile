FROM openjdk:11

VOLUME /tmp

ARG JAR_FILE=target/ProductService.jar

COPY ${JAR_FILE} ProductService.jar

ENTRYPOINT ["java","-jar","/ProductService.jar"]
