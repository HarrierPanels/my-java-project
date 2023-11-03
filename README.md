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
        The pipeline is configured to run on an agent labeled as 'aws2023', which is typically an Amazon EC2 instance. This agent is provisioned dynamically using the Amazon EC2 Plugin.

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
> [Dockerfile](./my-java-app/Dockerfile) <br>
> [MyApp.java](./my-java-app/src/main/java/com/example/MyApp.java)
> <sup></sup>
