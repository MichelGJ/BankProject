# Stage 1: Build the application
FROM maven:3-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests

# Stage 2: Create the final image
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/client-service-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
