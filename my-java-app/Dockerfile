# Use an appropriate base image with JDK
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the source code into the container
COPY src /app/src

# Install the JDK (javac will be available)
RUN apt-get update && apt-get install -y openjdk-11-jdk

# Compile your Java code
RUN javac /app/src/main/java/com/example/MyApp.java

# Run your Java application
CMD ["java", "-cp", "/app/src/main/java/com/example", "MyApp"]
