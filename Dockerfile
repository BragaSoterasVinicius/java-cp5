
# Stage 1: Build the Spring Boot application
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/cp5pt-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=80

EXPOSE 80

ENTRYPOINT ["java", "-jar", "app.jar"]
