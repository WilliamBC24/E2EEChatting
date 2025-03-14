FROM amazoncorretto:21-alpine

WORKDIR /app

COPY build/libs/Eureka-0.0.1-SNAPSHOT.jar /app/eureka.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "/app/eureka.jar"]