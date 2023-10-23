# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory to /app
WORKDIR /app

# Copy the source code into the container
COPY src /app/src

# Compile your Java code
RUN javac /app/src/com/example/MyApp.java

# Run your Java application
CMD ["java", "com.example.MyApp"]
