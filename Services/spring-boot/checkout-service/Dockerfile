FROM openjdk:11

VOLUME /tmp

ARG JAR_FILE=target/CheckoutService.jar

COPY ${JAR_FILE} CheckoutService.jar

ENTRYPOINT ["java","-jar","/CheckoutService.jar"]
