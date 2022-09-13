FROM openjdk:11

VOLUME /tmp

ARG JAR_FILE=target/PaymentService.jar

COPY ${JAR_FILE} PaymentService.jar

ENTRYPOINT ["java","-jar","/PaymentService.jar"]
