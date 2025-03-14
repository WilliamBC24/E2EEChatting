FROM amazoncorretto:21-alpine

WORKDIR /app

COPY build/libs/Config-0.0.1-SNAPSHOT.jar /app/config.jar

EXPOSE 8100

ENTRYPOINT ["java", "-jar", "/app/config.jar"]
