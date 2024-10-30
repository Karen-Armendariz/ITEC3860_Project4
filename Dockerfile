FROM ubuntu:latest
LABEL authors="karme"

ENTRYPOINT ["top", "-b"]

# Use an official OpenJDK image as the base
FROM openjdk:11-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the project files to the container
COPY . .

# Grant execute permissions for the Gradle wrapper
RUN chmod +x gradlew

# Build the application
RUN ./gradlew build

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "build/libs/ITEC_3860_Project_3-0.0.1-SNAPSHOT.jar"]