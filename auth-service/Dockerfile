FROM amazoncorretto:21-alpine

WORKDIR /app

COPY build/libs/AuthService-0.0.1-SNAPSHOT.jar /app/authservice.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/authservice.jar"]
