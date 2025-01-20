FROM amazoncorretto:21-alpine

WORKDIR /app

COPY build/libs/Microservice-0.0.1-SNAPSHOT.jar /app/microservice.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/microservice.jar"]
