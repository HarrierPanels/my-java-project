[![CICD](https://img.shields.io/badge/HarrierPanels-CI%2FCD-blue)](./)
[![EPAM](https://img.shields.io/badge/Cloud&DevOps%20UA%20Lab%202nd%20Path-Jenkins%20Task-orange)](./)
[![HitCount](https://hits.dwyl.com/HarrierPanels/my-java-project.svg?style=flat&show=unique)](http://hits.dwyl.com/HarrierPanels/my-java-project)
<br>
## Create a CI pipeline:
1. Looks at the main branch of the repository (main)
2. Triggered by merge into main
3. Clones the repository
4. Running static code analysis, Bugs, Vulnerabilities, Security Hotspots, Code Smells are available on the SonarQube server
5. Build a Docker image
6. Tags the image 2 times (latest and build version)
7. Push the image to Docker Hub
#### Prerequisits
> - agent 'aws2023' by Amazon EC2 Plugin<br>
> - Java based HTTP Server [application](my-java-app/src/main/java/com/example/MyApp.java)<br>
<sup>A simple Java application that creates an HTTP server and listens on port 80. It responds to any incoming HTTP requests with an HTML page. The application uses the com.sun.net.httpserver package to create the server and handle incoming requests. The main method creates an instance of HttpServer, binds it to port 80, sets the executor to null, and starts the server. The MyHandler class implements the HttpHandler interface and overrides the handle method to send the response to the client. The response is an HTML page that includes an image, a message, and the current year. The handle method sends the response headers and body to the client using the HttpExchange object.</sup><br>
> Docker Hub repo <a href="https://hub.docker.com/repository/docker/harrierpanels/myapp">harrierpanels/myapp</a>
#### Project structure:
```
my-java-project/
├── Jenkinsfile
├── pom.xml
└── my-java-app/
    ├── Dockerfile
    └── src/
        └── main/
            └── java/
                └── com/
                    └── example/
                        └── MyApp.java
```
#### Fles:
> [Jenkinsfile](./Jenkinsfile) <br>
> <sup>The Jenkins Pipeline is designed to automate various stages of a CI/CD process for a Java application.</sup><br>


    Agent Configuration:
        The pipeline is configured to run on an agent labeled as 'aws2023', which is 
        an Amazon EC2 instance. This agent is provisioned dynamically using the Amazon EC2 Plugin.

    Stages:

        Checkout: This stage checks out the source code from the version control system.

        Static Code Analysis with SonarQube:
            It runs a SonarQube analysis on the code.
            A SonarQube Docker image is pulled and started, and the analysis is executed.
            This stage ensures that SonarQube is running and accessible before proceeding with the analysis.

        Build Docker Image:
            It builds a Docker image for the Java application.

        Tag and Push Docker Image:
            Tags the Docker image with version information (using the Jenkins build number or 'latest').
            Logs in to Docker Hub using credentials provided via Jenkins credentials.
            Pushes the Docker image to a Docker Hub repository.

> [pom.xml](./pom.xml) <br>
> <sup>The POM file provides essential project information, including project identifiers, dependencies, and build settings. </sup><br>

    modelVersion: Specifies the POM model version, which is set to 4.0.0 in this case.

    groupId: Identifies the group or organization to which the project belongs. In this example, it's "com.example."

    artifactId: Specifies the unique identifier for the project, which is "your-project-name" here.

    version: Indicates the version of the project. The version is "1.0-SNAPSHOT," where "SNAPSHOT" typically signifies a development version.

    properties: This section allows you to define various properties for the project. Notably:
        maven.compiler.source and maven.compiler.target set the Java source and target compatibility versions to 1.8.
        sonar.host.url specifies the URL of a SonarQube server for code analysis. It's set to a local server in this example, but it should be changed to the actual SonarQube server URL.

    dependencies: Lists the project's dependencies. In this case, it includes a JUnit dependency with version 4.12, used for testing purposes.

    build: This section is used for configuring build-related settings, including plugins.
        The maven-compiler-plugin is configured to set the source and target compatibility to 1.8.
        The sonar-maven-plugin is configured for integrating with SonarQube for code analysis.

> [Dockerfile](./my-java-app/Dockerfile) <br>
> <sup>This Dockerfile is designed to create a Docker image that packages a Java application and runs it using OpenJDK 11. It sets up the environment, compiles the Java code, and defines the command to start the application.</sup><br>

    FROM: Specifies the base image for the Docker image. In this case, it uses the "openjdk:11-jre-slim" image, which contains the Java Runtime Environment (JRE) version 11.

    WORKDIR: Sets the working directory within the container to "/app." Subsequent commands will be executed in this directory.

    COPY: Copies the source code from the host machine's "src" directory to the "/app/src" directory inside the container.

    RUN: Executes commands within the container. In this Dockerfile:
        It updates the package list and installs the OpenJDK 11 development kit (JDK).
        It compiles the Java code located at "/app/src/main/java/com/example/MyApp.java."

    EXPOSE: Informs Docker that the container will listen on port 80. However, this instruction doesn't actually publish the port to the host.

    CMD: Specifies the default command to run when the container is started. In this case, it runs the Java application using the "java" command with the classpath and main class provided.

> [MyApp.java](./my-java-app/src/main/java/com/example/MyApp.java)
> <sup></sup>
