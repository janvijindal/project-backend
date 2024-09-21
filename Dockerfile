# Use the official Maven image to build the app
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package

# Use OpenJDK to run the app
FROM openjdk:17-jdk-slim
WORKDIR /Project_Management

# Adjust the path to the JAR file based on your structure
COPY --from=build Project_Management/target/Project_Management-0.0.1-SNAPSHOT.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]