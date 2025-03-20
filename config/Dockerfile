FROM gradle:8.6-jdk21-alpine AS builder

WORKDIR /app

ENV GRADLE_USER_HOME=/home/gradle/caches

COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle/

RUN --mount=type=cache,target=$GRADLE_USER_HOME gradle dependencies --no-daemon

COPY . .

RUN --mount=type=cache,target=$GRADLE_USER_HOME gradle build -x test --no-daemon

FROM amazoncorretto:21-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar /app/config.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/config.jar"]

