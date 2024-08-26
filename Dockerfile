#FROM openjdk:21-jdk-slim AS build
#
#WORKDIR /app
#
#COPY build.gradle settings.gradle gradlew gradlew.bat /app/
#COPY gradle /app/gradle/
#
#COPY src /app/src/
#
#RUN chmod +x ./gradlew
#
#RUN ./gradlew clean build --no-daemon -x test --refresh-dependencies
#
#FROM openjdk:21-jdk-slim
#
#WORKDIR /app
#
#COPY --from=build /app/build/libs/*.jar app.jar
#
#EXPOSE 8080
#
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]



FROM openjdk:21

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
