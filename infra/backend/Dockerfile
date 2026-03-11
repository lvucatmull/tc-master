FROM gradle:8.10-jdk17 AS build
WORKDIR /workspace

COPY backend/build.gradle backend/settings.gradle /workspace/backend/
COPY backend/src /workspace/backend/src

WORKDIR /workspace/backend
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /workspace/backend/build/libs/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
