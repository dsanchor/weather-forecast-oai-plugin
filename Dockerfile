# Use a Java base image
FROM openjdk:17-alpine
# Set the working directory to /app
WORKDIR /app
# Copy the Spring Boot application JAR file into the Docker image
COPY target/*.jar /app/weather-forecast-oai-plugin.jar
# expose 8080
EXPOSE 8080
# Run the Spring Boot application when the container starts
CMD ["java", "-jar", "weather-forecast-oai-plugin.jar"]