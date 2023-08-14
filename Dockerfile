FROM maven:3.9.3-eclipse-temurin-11-alpine as build
WORKDIR /app
COPY pom.xml .
RUN mvn verify --fail-never

COPY src src
RUN mvn package

FROM eclipse-temurin:20.0.2_9-jre-alpine as runtime
COPY --from=build /app/target/com-trivago-trivago-test-0.0.1-jar-with-dependencies.jar /app/app.jar
ENTRYPOINT java -jar /app/app.jar