# Build Stage
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the jar
RUN mvn clean package -DskipTests

# Run Stage
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/target/supplyChainSystem-0.0.1-SNAPSHOT.jar app.jar

# Expose Spring Boot port
EXPOSE 7070

# Run the jar
ENTRYPOINT ["java","-jar","app.jar"]