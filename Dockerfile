# Build Stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copy project files
COPY . /app

# Build the application
RUN mvn clean package -DskipTests

# Runtime Stage
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the built JAR file from build stage
COPY --from=build /app/target/english_learning_center-0.0.1-SNAPSHOT.jar app.jar

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "app.jar"]
