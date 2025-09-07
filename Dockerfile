# Use an OpenJDK 21 base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built Spring Boot JAR file into the container
# Assuming your JAR is named target/your-application.jar
COPY target/spring-boot-mysql-crud-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot application listens on (default is 8080)
EXPOSE 9090

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]