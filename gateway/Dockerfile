FROM gradle:8.6-jdk21-alpine AS builder

WORKDIR /app

ENV GRADLE_USER_HOME=/home/gradle/caches

COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle

RUN --mount=type=cache,target=$GRADLE_USER_HOME gradle build --no-daemon --dry-run

COPY . .

RUN gradle build -x test --no-daemon

FROM amazoncorretto:21-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/Gateway-0.0.1-SNAPSHOT.jar /app/gateway.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "/app/gateway.jar"]
