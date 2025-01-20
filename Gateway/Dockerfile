FROM amazoncorretto:21-alpine

WORKDIR /app

COPY build/libs/Gateway-0.0.1-SNAPSHOT.jar /app/gateway.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/gateway.jar"]