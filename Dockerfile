
FROM maven:3.9.3-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven-related files
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .

# ✅ Add permission to execute mvnw
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy project source
COPY src ./src

# Build the application
RUN ./mvnw package -DskipTests

# -----------------
FROM eclipse-temurin:17-jdk-alpine AS stage-1
WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]


