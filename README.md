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
> - [Dockerfile](my-java-app/Dockerfile)<br>
> <sup>The Dockerfile is used to build a Docker image for a Java application.</sup><br>
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
> <sup>This Dockerfile is designed to create a Docker image that packages a Java application and runs it
> using OpenJDK 11. It sets up the environment, compiles the Java code, and defines the command to start
> the application.</sup><br>

    FROM: Specifies the base image for the Docker image. In this case, it uses the "openjdk:11-jre-slim" image, which contains the Java Runtime Environment (JRE) version 11.

    WORKDIR: Sets the working directory within the container to "/app." Subsequent commands will be executed in this directory.

    COPY: Copies the source code from the host machine's "src" directory to the "/app/src" directory inside the container.

    RUN: Executes commands within the container. In this Dockerfile:
        It updates the package list and installs the OpenJDK 11 development kit (JDK).
        It compiles the Java code located at "/app/src/main/java/com/example/MyApp.java."

    EXPOSE: Informs Docker that the container will listen on port 80. However, this instruction doesn't actually publish the port to the host.

    CMD: Specifies the default command to run when the container is started. In this case, it runs the Java application using the "java" command with the classpath and main class provided.

> [MyApp.java](./my-java-app/src/main/java/com/example/MyApp.java)<br>
> <sup>MyApp.java creates a basic HTTP server that responds with a simple web page when accessed.</sup><br>

    It defines a package named com.example.

    The MyApp class contains the main method, which serves as the entry point of the program. Inside the main method:
        It creates an HttpServer instance.
        Binds the server to listen on port 80.
        Associates a custom MyHandler with the root context ("/").
        Sets the executor to null.
        Starts the server and prints a message indicating that the server has started.

    The MyHandler class is a nested static class that implements the HttpHandler interface, responsible for handling incoming HTTP requests. Inside the handle method:
        It constructs an HTML response as a string.
        Sends an HTTP 200 (OK) response with the response length.
        Writes the response to the output stream and closes it.

    The HTML response includes a simple webpage:
        It displays an image and a message.
        The image is sourced from a URL.
        There's a link to an external webpage.
        The page is generated with Java and includes the current year using Calendar.

#### Set Webhook:
```
[ec2-user@ip-192-168-0-145 ~]$ gh webhook forward \    
    --repo=HarrierPanels/my-java-project    \
    --events=push --url="http://localhost:8080/github-webhook/"
Forwarding Webhook events from GitHub...        
```
```
Started by GitHub push by HarrierPanels
Obtained Jenkinsfile from git https://github.com/HarrierPanels/my-java-project.git
[Pipeline] Start of Pipeline
[Pipeline] node
Still waiting to schedule task
All nodes of label ‘aws2023’ are offline
Running on EC2 (Amazon-EC2) - aws-ec2-agent1 (i-0b3e3a237593d3e1e) in /home/ec2-user/jenkins/workspace/ci-pipeline
[Pipeline] {
[Pipeline] stage
[Pipeline] { (Declarative: Checkout SCM)
[Pipeline] checkout
Selected Git installation does not exist. Using Default
The recommended git tool is: NONE
using credential github-creds-user-token
Cloning the remote Git repository
Cloning repository https://github.com/HarrierPanels/my-java-project.git
 > git init /home/ec2-user/jenkins/workspace/ci-pipeline # timeout=10
Fetching upstream changes from https://github.com/HarrierPanels/my-java-project.git
 > git --version # timeout=10
 > git --version # 'git version 2.40.1'
using GIT_ASKPASS to set credentials github-creds-user-token
 > git fetch --tags --force --progress -- https://github.com/HarrierPanels/my-java-project.git +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git config remote.origin.url https://github.com/HarrierPanels/my-java-project.git # timeout=10
 > git config --add remote.origin.fetch +refs/heads/*:refs/remotes/origin/* # timeout=10
Avoid second fetch
Checking out Revision 41a6a1a2942c6a25e09d5c9494221473e82bc552 (refs/remotes/origin/main)
 > git rev-parse refs/remotes/origin/main^{commit} # timeout=10
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 41a6a1a2942c6a25e09d5c9494221473e82bc552 # timeout=10
 > git branch -a -v --no-abbrev # timeout=10
 > git checkout -b main 41a6a1a2942c6a25e09d5c9494221473e82bc552 # timeout=10
Commit message: "CI Pipeline"
 > git rev-list --no-walk ef602e2048acce680606e0cc5b46465b62cdc5f6 # timeout=10
Cleaning workspace
[Pipeline] }
[Pipeline] // stage
[Pipeline] withEnv
[Pipeline] {
[Pipeline] stage
[Pipeline] { (Checkout)
[Pipeline] checkout
Selected Git installation does not exist. Using Default
The recommended git tool is: NONE
using credential github-creds-user-token
Fetching changes from the remote Git repository
 > git rev-parse --resolve-git-dir /home/ec2-user/jenkins/workspace/ci-pipeline/.git # timeout=10
Cleaning workspace
 > git rev-parse --verify HEAD # timeout=10
Resetting working tree
 > git reset --hard # timeout=10
 > git clean -fdx # timeout=10
Checking out Revision 41a6a1a2942c6a25e09d5c9494221473e82bc552 (refs/remotes/origin/main)
 > git config remote.origin.url https://github.com/HarrierPanels/my-java-project.git # timeout=10
 > git rev-parse --verify HEAD # timeout=10
Resetting working tree
 > git reset --hard # timeout=10
 > git clean -fdx # timeout=10
Fetching upstream changes from https://github.com/HarrierPanels/my-java-project.git
 > git --version # timeout=10
 > git --version # 'git version 2.40.1'
using GIT_ASKPASS to set credentials github-creds-user-token
 > git fetch --tags --force --progress -- https://github.com/HarrierPanels/my-java-project.git +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git rev-parse refs/remotes/origin/main^{commit} # timeout=10
Commit message: "CI Pipeline"
Cleaning workspace
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 41a6a1a2942c6a25e09d5c9494221473e82bc552 # timeout=10
 > git branch -a -v --no-abbrev # timeout=10
 > git branch -D main # timeout=10
 > git checkout -b main 41a6a1a2942c6a25e09d5c9494221473e82bc552 # timeout=10
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Static Code Analysis with SonarQube)
[Pipeline] script
[Pipeline] {
[Pipeline] catchError
 > git rev-parse --verify HEAD # timeout=10
Resetting working tree
 > git reset --hard # timeout=10
 > git clean -fdx # timeout=10
[Pipeline] {
[Pipeline] isUnix
[Pipeline] sh
+ docker run -d --name sonarqube -p 9000:9000 -p 9092:9092 sonarqube:latest
Unable to find image 'sonarqube:latest' locally
latest: Pulling from library/sonarqube
43f89b94cd7d: Pulling fs layer
50431c77a77b: Pulling fs layer
dfd8e860e672: Pulling fs layer
637e2db99ae6: Pulling fs layer
7de1c2853278: Pulling fs layer
d2152ffce821: Pulling fs layer
519cf218564f: Pulling fs layer
637e2db99ae6: Waiting
7de1c2853278: Waiting
d2152ffce821: Waiting
519cf218564f: Waiting
50431c77a77b: Verifying Checksum
50431c77a77b: Download complete
637e2db99ae6: Verifying Checksum
637e2db99ae6: Download complete
7de1c2853278: Verifying Checksum
7de1c2853278: Download complete
43f89b94cd7d: Verifying Checksum
43f89b94cd7d: Download complete
519cf218564f: Verifying Checksum
519cf218564f: Download complete
dfd8e860e672: Verifying Checksum
dfd8e860e672: Download complete
43f89b94cd7d: Pull complete
d2152ffce821: Verifying Checksum
d2152ffce821: Download complete
50431c77a77b: Pull complete
dfd8e860e672: Pull complete
637e2db99ae6: Pull complete
7de1c2853278: Pull complete
d2152ffce821: Pull complete
519cf218564f: Pull complete
Digest: sha256:c6c8096375002d4cb2ef64b89a2736ad572812a87a2917d92e7e59384b9f6f65
Status: Downloaded newer image for sonarqube:latest
[Pipeline] sh
+ curl -s -f -o /dev/null http://localhost:9000
+ sleep 5
+ curl -s -f -o /dev/null http://localhost:9000
+ sleep 5
+ curl -s -f -o /dev/null http://localhost:9000
+ sleep 5
+ curl -s -f -o /dev/null http://localhost:9000
[Pipeline] sh
+ mvn sonar:sonar -X
Apache Maven 3.8.4 (Red Hat 3.8.4-3.amzn2023.0.5)
Maven home: /usr/share/maven
Java version: 17.0.9, vendor: Amazon.com Inc., runtime: /usr/lib/jvm/java-17-amazon-corretto.x86_64
Default locale: en, platform encoding: UTF-8
OS name: "linux", version: "6.1.56-82.125.amzn2023.x86_64", arch: "amd64", family: "unix"
[DEBUG] Created new class realm maven.api
[DEBUG] Importing foreign packages into class realm maven.api
[DEBUG]   Imported: javax.annotation.* < plexus.core
[DEBUG]   Imported: javax.annotation.security.* < plexus.core
[DEBUG]   Imported: javax.inject.* < plexus.core
[DEBUG]   Imported: org.apache.maven.* < plexus.core
[DEBUG]   Imported: org.apache.maven.artifact < plexus.core
[DEBUG]   Imported: org.apache.maven.classrealm < plexus.core
[DEBUG]   Imported: org.apache.maven.cli < plexus.core
[DEBUG]   Imported: org.apache.maven.configuration < plexus.core
[DEBUG]   Imported: org.apache.maven.exception < plexus.core
[DEBUG]   Imported: org.apache.maven.execution < plexus.core
[DEBUG]   Imported: org.apache.maven.execution.scope < plexus.core
[DEBUG]   Imported: org.apache.maven.lifecycle < plexus.core
[DEBUG]   Imported: org.apache.maven.model < plexus.core
[DEBUG]   Imported: org.apache.maven.monitor < plexus.core
[DEBUG]   Imported: org.apache.maven.plugin < plexus.core
[DEBUG]   Imported: org.apache.maven.profiles < plexus.core
[DEBUG]   Imported: org.apache.maven.project < plexus.core
[DEBUG]   Imported: org.apache.maven.reporting < plexus.core
[DEBUG]   Imported: org.apache.maven.repository < plexus.core
[DEBUG]   Imported: org.apache.maven.rtinfo < plexus.core
[DEBUG]   Imported: org.apache.maven.settings < plexus.core
[DEBUG]   Imported: org.apache.maven.toolchain < plexus.core
[DEBUG]   Imported: org.apache.maven.usability < plexus.core
[DEBUG]   Imported: org.apache.maven.wagon.* < plexus.core
[DEBUG]   Imported: org.apache.maven.wagon.authentication < plexus.core
[DEBUG]   Imported: org.apache.maven.wagon.authorization < plexus.core
[DEBUG]   Imported: org.apache.maven.wagon.events < plexus.core
[DEBUG]   Imported: org.apache.maven.wagon.observers < plexus.core
[DEBUG]   Imported: org.apache.maven.wagon.proxy < plexus.core
[DEBUG]   Imported: org.apache.maven.wagon.repository < plexus.core
[DEBUG]   Imported: org.apache.maven.wagon.resource < plexus.core
[DEBUG]   Imported: org.codehaus.classworlds < plexus.core
[DEBUG]   Imported: org.codehaus.plexus.* < plexus.core
[DEBUG]   Imported: org.codehaus.plexus.classworlds < plexus.core
[DEBUG]   Imported: org.codehaus.plexus.component < plexus.core
[DEBUG]   Imported: org.codehaus.plexus.configuration < plexus.core
[DEBUG]   Imported: org.codehaus.plexus.container < plexus.core
[DEBUG]   Imported: org.codehaus.plexus.context < plexus.core
[DEBUG]   Imported: org.codehaus.plexus.lifecycle < plexus.core
[DEBUG]   Imported: org.codehaus.plexus.logging < plexus.core
[DEBUG]   Imported: org.codehaus.plexus.personality < plexus.core
[DEBUG]   Imported: org.codehaus.plexus.util.xml.Xpp3Dom < plexus.core
[DEBUG]   Imported: org.codehaus.plexus.util.xml.pull.XmlPullParser < plexus.core
[DEBUG]   Imported: org.codehaus.plexus.util.xml.pull.XmlPullParserException < plexus.core
[DEBUG]   Imported: org.codehaus.plexus.util.xml.pull.XmlSerializer < plexus.core
[DEBUG]   Imported: org.eclipse.aether.* < plexus.core
[DEBUG]   Imported: org.eclipse.aether.artifact < plexus.core
[DEBUG]   Imported: org.eclipse.aether.collection < plexus.core
[DEBUG]   Imported: org.eclipse.aether.deployment < plexus.core
[DEBUG]   Imported: org.eclipse.aether.graph < plexus.core
[DEBUG]   Imported: org.eclipse.aether.impl < plexus.core
[DEBUG]   Imported: org.eclipse.aether.installation < plexus.core
[DEBUG]   Imported: org.eclipse.aether.internal.impl < plexus.core
[DEBUG]   Imported: org.eclipse.aether.metadata < plexus.core
[DEBUG]   Imported: org.eclipse.aether.repository < plexus.core
[DEBUG]   Imported: org.eclipse.aether.resolution < plexus.core
[DEBUG]   Imported: org.eclipse.aether.spi < plexus.core
[DEBUG]   Imported: org.eclipse.aether.transfer < plexus.core
[DEBUG]   Imported: org.eclipse.aether.version < plexus.core
[DEBUG]   Imported: org.fusesource.jansi.* < plexus.core
[DEBUG]   Imported: org.slf4j.* < plexus.core
[DEBUG]   Imported: org.slf4j.event.* < plexus.core
[DEBUG]   Imported: org.slf4j.helpers.* < plexus.core
[DEBUG]   Imported: org.slf4j.spi.* < plexus.core
[DEBUG] Populating class realm maven.api
[INFO] Error stacktraces are turned on.
[DEBUG] Message scheme: color
[DEBUG] Message styles: debug info warning error success failure strong mojo project
[DEBUG] Reading global settings from /usr/share/maven/conf/settings.xml
[DEBUG] Reading user settings from /home/ec2-user/.m2/settings.xml
[DEBUG] Reading global toolchains from /usr/share/maven/conf/toolchains.xml
[DEBUG] Reading user toolchains from /home/ec2-user/.m2/toolchains.xml
[DEBUG] Using local repository at /home/ec2-user/.m2/repository
[DEBUG] Using manager EnhancedLocalRepositoryManager with priority 10.0 for /home/ec2-user/.m2/repository
[INFO] Scanning for projects...
[DEBUG] Extension realms for project com.example:your-project-name:jar:1.0-SNAPSHOT: (none)
[DEBUG] Looking up lifecycle mappings for packaging jar from ClassRealm[plexus.core, parent: null]
[DEBUG] Resolving plugin prefix sonar from [org.apache.maven.plugins, org.codehaus.mojo]
[DEBUG] Resolving artifact org.apache.maven.plugins:maven-compiler-plugin:pom:3.8.0 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/maven-compiler-plugin-3.8.0.pom
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 0 of 20; total allocated: 0 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] Opening connection {s}->https://repo.maven.apache.org:443
[DEBUG] Connecting to repo.maven.apache.org/146.75.32.215:443
[DEBUG] Connecting socket to repo.maven.apache.org/146.75.32.215:443 with timeout 0
[DEBUG] Enabled protocols: [TLSv1.3, TLSv1.2]
[DEBUG] Enabled cipher suites:[TLS_AES_256_GCM_SHA384, TLS_AES_128_GCM_SHA256, TLS_CHACHA20_POLY1305_SHA256, TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384, TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256, TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384, TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256, TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, TLS_DHE_RSA_WITH_AES_256_GCM_SHA384, TLS_DHE_RSA_WITH_CHACHA20_POLY1305_SHA256, TLS_DHE_DSS_WITH_AES_256_GCM_SHA384, TLS_DHE_RSA_WITH_AES_128_GCM_SHA256, TLS_DHE_DSS_WITH_AES_128_GCM_SHA256, TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384, TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384, TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256, TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256, TLS_DHE_RSA_WITH_AES_256_CBC_SHA256, TLS_DHE_DSS_WITH_AES_256_CBC_SHA256, TLS_DHE_RSA_WITH_AES_128_CBC_SHA256, TLS_DHE_DSS_WITH_AES_128_CBC_SHA256, TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384, TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384, TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256, TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256, TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384, TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384, TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256, TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256, TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA, TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA, TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA, TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA, TLS_DHE_RSA_WITH_AES_256_CBC_SHA, TLS_DHE_DSS_WITH_AES_256_CBC_SHA, TLS_DHE_RSA_WITH_AES_128_CBC_SHA, TLS_DHE_DSS_WITH_AES_128_CBC_SHA, TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA, TLS_ECDH_RSA_WITH_AES_256_CBC_SHA, TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA, TLS_ECDH_RSA_WITH_AES_128_CBC_SHA, TLS_RSA_WITH_AES_256_GCM_SHA384, TLS_RSA_WITH_AES_128_GCM_SHA256, TLS_RSA_WITH_AES_256_CBC_SHA256, TLS_RSA_WITH_AES_128_CBC_SHA256, TLS_RSA_WITH_AES_256_CBC_SHA, TLS_RSA_WITH_AES_128_CBC_SHA, TLS_EMPTY_RENEGOTIATION_INFO_SCSV]
[DEBUG] Starting handshake
[DEBUG] Secure session established
[DEBUG]  negotiated protocol: TLSv1.2
[DEBUG]  negotiated cipher suite: TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256
[DEBUG]  peer principal: CN=repo.maven.apache.org
[DEBUG]  peer alternative names: [repo.maven.apache.org]
[DEBUG]  issuer principal: CN=GlobalSign Atlas R3 DV TLS CA 2023 Q1, O=GlobalSign nv-sa, C=BE
[DEBUG] Connection established 172.31.81.57:36120<->146.75.32.215:443
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/maven-compiler-plugin-3.8.0.pom HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/maven-compiler-plugin-3.8.0.pom HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/maven-compiler-plugin-3.8.0.pom HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 12434[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "9c349c1e290b2ba6a3facd048c1724f7"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/xml[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Thu, 26 Jul 2018 17:14:37 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 9c349c1e290b2ba6a3facd048c1724f7[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: b8847e359d345411fb6b8741e8efb74df550d1ca[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:51 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 603084[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 49[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251812.793320,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 12434
[DEBUG] http-outgoing-0 << ETag: "9c349c1e290b2ba6a3facd048c1724f7"
[DEBUG] http-outgoing-0 << Content-Type: text/xml
[DEBUG] http-outgoing-0 << Last-Modified: Thu, 26 Jul 2018 17:14:37 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 9c349c1e290b2ba6a3facd048c1724f7
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: b8847e359d345411fb6b8741e8efb74df550d1ca
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:51 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 603084
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 49
[DEBUG] http-outgoing-0 << X-Timer: S1698251812.793320,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "<?xml version='1.0' encoding='UTF-8'?>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "<!--[\n]"
[DEBUG] http-outgoing-0 << "Licensed to the Apache Software Foundation (ASF) under one[\n]"
[DEBUG] http-outgoing-0 << "or more contributor license agreements.  See the NOTICE file[\n]"
[DEBUG] http-outgoing-0 << "distributed with this work for additional information[\n]"
[DEBUG] http-outgoing-0 << "regarding copyright ownership.  The ASF licenses this file[\n]"
[DEBUG] http-outgoing-0 << "to you under the Apache License, Version 2.0 (the[\n]"
[DEBUG] http-outgoing-0 << ""License"); you may not use this file except in compliance[\n]"
[DEBUG] http-outgoing-0 << "with the License.  You may obtain a copy of the License at[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  http://www.apache.org/licenses/LICENSE-2.0[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "Unless required by applicable law or agreed to in writing,[\n]"
[DEBUG] http-outgoing-0 << "software distributed under the License is distributed on an[\n]"
[DEBUG] http-outgoing-0 << ""AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY[\n]"
[DEBUG] http-outgoing-0 << "KIND, either express or implied.  See the License for the[\n]"
[DEBUG] http-outgoing-0 << "specific language governing permissions and limitations[\n]"
[DEBUG] http-outgoing-0 << "under the License.[\n]"
[DEBUG] http-outgoing-0 << "-->[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">[\n]"
[DEBUG] http-outgoing-0 << "  <modelVersion>4.0.0</modelVersion>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <parent>[\n]"
[DEBUG] http-outgoing-0 << "    <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "    <artifactId>maven-plugins</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "    <version>32</version>[\n]"
[DEBUG] http-outgoing-0 << "    <relativePath>../../pom/maven/maven-plugins/pom.xml</relativePath>[\n]"
[DEBUG] http-outgoing-0 << "  </parent>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <artifactId>maven-compiler-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "  <version>3.8.0</version>[\n]"
[DEBUG] http-outgoing-0 << "  <packaging>maven-plugin</packaging>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <n"
[DEBUG] http-outgoing-0 << "ame>Apache Maven Compiler Plugin</name>[\n]"
[DEBUG] http-outgoing-0 << "  <description>The Compiler Plugin is used to compile the sources of your project.</description>[\n]"
[DEBUG] http-outgoing-0 << "  <inceptionYear>2001</inceptionYear>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <prerequisites>[\n]"
[DEBUG] http-outgoing-0 << "    <maven>${mavenVersion}</maven>[\n]"
[DEBUG] http-outgoing-0 << "  </prerequisites>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <scm>[\n]"
[DEBUG] http-outgoing-0 << "    <connection>scm:git:https://gitbox.apache.org/repos/asf/maven-compiler-plugin.git</connection>[\n]"
[DEBUG] http-outgoing-0 << "    <developerConnection>scm:git:https://gitbox.apache.org/repos/asf/maven-compiler-plugin.git</developerConnection>[\n]"
[DEBUG] http-outgoing-0 << "    <url>https://github.com/apache/maven-compiler-plugin/tree/${project.scm.tag}</url>[\n]"
[DEBUG] http-outgoing-0 << "    <tag>maven-compiler-plugin-3.8.0</tag>[\n]"
[DEBUG] http-outgoing-0 << "  </scm>[\n]"
[DEBUG] http-outgoing-0 << "  <issueManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <system>JIRA</system>[\n]"
[DEBUG] http-outgoing-0 << "    <url>https://issues.apache.org/jira/browse/MCOMPILER</url>[\n]"
[DEBUG] http-outgoing-0 << "  </issueManagement>[\n]"
[DEBUG] http-outgoing-0 << "  <ciManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <system>Jenkins</system>[\n]"
[DEBUG] http-outgoing-0 << "    <url>https://builds.apache.org/job/maven-box/job/maven-compiler-plugin/</url>[\n]"
[DEBUG] http-outgoing-0 << "  </ciManagement>[\n]"
[DEBUG] http-outgoing-0 << "  <distributionManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <site>[\n]"
[DEBUG] http-outgoing-0 << "      <id>apache.website</id>[\n]"
[DEBUG] http-outgoing-0 << "      <url>scm:svn:https://svn.apache.org/repos/asf/maven/website/components/${maven.site.path}</url>[\n]"
[DEBUG] http-outgoing-0 << "    </site>[\n]"
[DEBUG] http-outgoing-0 << "  </distributionManagement>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <properties>[\n]"
[DEBUG] http-outgoing-0 << "    <mavenVersion>3.0</mavenVersion>[\n]"
[DEBUG] http-outgoing-0 << "    <!--[\n]"
[DEBUG] http-outgoing-0 << "      ! The following property is used in the integration tests MCOMPILER-157[\n]"
[DEBUG] http-outgoing-0 << "    -->[\n]"
[DEBUG] http-outgoing-0 << "    <mavenPluginPluginVersion>3.5</mavenPluginPluginVersion>[\n]"
[DEBUG] http-outgoing-0 << "    <plexusCompilerVersion>2.8.4</plexusCompilerVersion>[\n]"
[DEBUG] http-outgoing-0 << "    <groovyVers"
Progress (1): maven-compiler-plugin-3.8.0.pom (2.8/12 kB)
[DEBUG] http-outgoing-0 << "ion>1.8.0</groovyVersion>[\n]"
[DEBUG] http-outgoing-0 << "    <groovyEclipseCompilerVersion>2.7.0-01</groovyEclipseCompilerVersion>[\n]"
[DEBUG] http-outgoing-0 << "    <groovy-eclipse-batch>2.0.4-04</groovy-eclipse-batch>[\n]"
[DEBUG] http-outgoing-0 << "    <openJpaVersion>2.4.2</openJpaVersion>[\n]"
[DEBUG] http-outgoing-0 << "    <javaVersion>7</javaVersion>[\n]"
[DEBUG] http-outgoing-0 << "    <maven.it.failure.ignore>false</maven.it.failure.ignore>[\n]"
[DEBUG] http-outgoing-0 << "  </properties>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <contributors>[\n]"
[DEBUG] http-outgoing-0 << "    <contributor>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Jan Sievers</name>[\n]"
[DEBUG] http-outgoing-0 << "    </contributor>[\n]"
[DEBUG] http-outgoing-0 << "  </contributors>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <dependencyManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <dependencies>[\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <!-- to remove when using plexus-java-1.0.0 -->[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>com.thoughtworks.qdox</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>qdox</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>2.0-M9</version>[\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    </dependencies>[\n]"
[DEBUG] http-outgoing-0 << "  </dependencyManagement>[\n]"
[DEBUG] http-outgoing-0 << "  <dependencies>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.apache.maven.plugin-tools</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>maven-plugin-annotations</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <scope>provided</scope>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <!-- Maven -->[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.apache.maven</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>maven-plugin-api</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>${mavenVersion}</version>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.apache.maven</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>maven-artifact</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>${mavenVersion}</version>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.apache.maven</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>maven-core</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version"
[DEBUG] http-outgoing-0 << ">${mavenVersion}</version>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.apache.maven.shared</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>maven-shared-utils</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>3.2.1</version>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.apache.maven.shared</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>maven-shared-incremental</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>1.1</version>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>plexus-java</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>0.9.10</version>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>plexus-compiler-api</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>${plexusCompilerVersion}</version>[\n]"
[DEBUG] http-outgoing-0 << "      <exclusions>[\n]"
[DEBUG] http-outgoing-0 << "        <exclusion>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>plexus-component-api</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        </exclusion>[\n]"
[DEBUG] http-outgoing-0 << "      </exclusions>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>plexus-compiler-manager</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>${plexusCompilerVersion}</version>[\n]"
[DEBUG] http-outgoing-0 << "      <exclusions>[\n]"
[DEBUG] http-outgoing-0 << "        <exclusion>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>plexus-component-api</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        </exclusion>[\n]"
[DEBUG] http-outgoing-0 << "      </exclusions>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>plexus-compiler-javac</arti"
Progress (1): maven-compiler-plugin-3.8.0.pom (5.5/12 kB)
[DEBUG] http-outgoing-0 << "factId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>${plexusCompilerVersion}</version>[\n]"
[DEBUG] http-outgoing-0 << "      <scope>runtime</scope>[\n]"
[DEBUG] http-outgoing-0 << "      <exclusions>[\n]"
[DEBUG] http-outgoing-0 << "        <exclusion>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>plexus-component-api</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        </exclusion>[\n]"
[DEBUG] http-outgoing-0 << "      </exclusions>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.apache.maven.plugin-testing</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>maven-plugin-testing-harness</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>2.1</version>[\n]"
[DEBUG] http-outgoing-0 << "      <scope>test</scope>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.apache.maven</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>maven-compat</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>${mavenVersion}</version>[\n]"
[DEBUG] http-outgoing-0 << "      <scope>test</scope>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.mockito</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>mockito-core</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>1.9.5</version>[\n]"
[DEBUG] http-outgoing-0 << "      <scope>test</scope>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>junit</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>junit</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>4.12</version>[\n]"
[DEBUG] http-outgoing-0 << "      <scope>test</scope>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "  </dependencies>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <build>[\n]"
[DEBUG] http-outgoing-0 << "    <pluginManagement>[\n]"
[DEBUG] http-outgoing-0 << "      <plugins>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.rat</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>apache-rat-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <excludes combine.children="append">[\n]"
[DEBUG] http-outgoing-0 << "              <exclude>.java-version</exclude>[\n]"
[DEBUG] http-outgoing-0 << "            </excludes>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "      "
[DEBUG] http-outgoing-0 << "  </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-enforcer-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>1.4.1</version>[\n]"
[DEBUG] http-outgoing-0 << "          <executions>[\n]"
[DEBUG] http-outgoing-0 << "            <execution>[\n]"
[DEBUG] http-outgoing-0 << "              <id>enforce-bytecode-version</id>[\n]"
[DEBUG] http-outgoing-0 << "              <configuration>[\n]"
[DEBUG] http-outgoing-0 << "                <rules>[\n]"
[DEBUG] http-outgoing-0 << "                  <enforceBytecodeVersion>[\n]"
[DEBUG] http-outgoing-0 << "                    <maxJdkVersion>1.6</maxJdkVersion>[\n]"
[DEBUG] http-outgoing-0 << "                    <excludes>[\n]"
[DEBUG] http-outgoing-0 << "                      <exclude>org.ow2.asm:asm</exclude>[\n]"
[DEBUG] http-outgoing-0 << "                    </excludes>[\n]"
[DEBUG] http-outgoing-0 << "                  </enforceBytecodeVersion>[\n]"
[DEBUG] http-outgoing-0 << "                  <requireSameVersions />[\n]"
[DEBUG] http-outgoing-0 << "                </rules>[\n]"
[DEBUG] http-outgoing-0 << "              </configuration>[\n]"
[DEBUG] http-outgoing-0 << "            </execution>[\n]"
[DEBUG] http-outgoing-0 << "          </executions>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      </plugins>[\n]"
[DEBUG] http-outgoing-0 << "    </pluginManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>plexus-component-metadata</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>1.7.1</version>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>descriptors</id>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>generate-metadata</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "    </plugins>[\n]"
[DEBUG] http-outgoing-0 << "  </build>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <profiles>[\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\n]"
[DEBUG] http-outgoing-0 << "      <id>run-its</id>[\n]"
[DEBUG] http-outgoing-0 << "      <build>[\n]"
[DEBUG] http-outgoing-0 << "        <pluginManagement>[\n]"
[DEBUG] http-outgoing-0 << "          <plugins>[\n]"
[DEBUG] http-outgoing-0 << "            <plugin>[\n]"
[DEBUG] http-outgoing-0 << "              <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "              <artifactId>maven-i"
Progress (1): maven-compiler-plugin-3.8.0.pom (8.3/12 kB)
[DEBUG] http-outgoing-0 << "nvoker-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "              <executions>[\n]"
[DEBUG] http-outgoing-0 << "                <execution>[\n]"
[DEBUG] http-outgoing-0 << "                  <id>integration-test</id>[\n]"
[DEBUG] http-outgoing-0 << "                  <configuration>[\n]"
[DEBUG] http-outgoing-0 << "                    <environmentVariables>[\n]"
[DEBUG] http-outgoing-0 << "                      <JENKINS_MAVEN_AGENT_DISABLED>true</JENKINS_MAVEN_AGENT_DISABLED>[\n]"
[DEBUG] http-outgoing-0 << "                    </environmentVariables>[\n]"
[DEBUG] http-outgoing-0 << "                    <debug>true</debug>[\n]"
[DEBUG] http-outgoing-0 << "                    <projectsDirectory>src/it</projectsDirectory>[\n]"
[DEBUG] http-outgoing-0 << "                    <cloneProjectsTo>${project.build.directory}/it</cloneProjectsTo>[\n]"
[DEBUG] http-outgoing-0 << "                    <pomIncludes>[\n]"
[DEBUG] http-outgoing-0 << "                      <pomInclude>*/pom.xml</pomInclude>[\n]"
[DEBUG] http-outgoing-0 << "                      <pomInclude>extras/*/pom.xml</pomInclude>[\n]"
[DEBUG] http-outgoing-0 << "                      <pomInclude>multirelease-patterns/*/pom.xml</pomInclude>[\n]"
[DEBUG] http-outgoing-0 << "                    </pomIncludes>[\n]"
[DEBUG] http-outgoing-0 << "                    <!--[\n]"
[DEBUG] http-outgoing-0 << "                      ! Unfortunately we can't define an execution order.[\n]"
[DEBUG] http-outgoing-0 << "                      ! https://issues.apache.org/jira/browse/MINVOKER-174[\n]"
[DEBUG] http-outgoing-0 << "                      -->[\n]"
[DEBUG] http-outgoing-0 << "                    <setupIncludes>[\n]"
[DEBUG] http-outgoing-0 << "                      <setupInclude>setup_jar_module/pom.xml</setupInclude>[\n]"
[DEBUG] http-outgoing-0 << "                      <setupInclude>setup_jar_automodule/pom.xml</setupInclude>[\n]"
[DEBUG] http-outgoing-0 << "                      <setupInclude>setup_x/pom.xml</setupInclude>[\n]"
[DEBUG] http-outgoing-0 << "                    </setupIncludes>[\n]"
[DEBUG] http-outgoing-0 << "                    <setupExcludes>[\n]"
[DEBUG] http-outgoing-0 << "                      <setupExclude>setup_x/**</setu"
[DEBUG] http-outgoing-0 << "pExclude>[\n]"
[DEBUG] http-outgoing-0 << "                    </setupExcludes>[\n]"
[DEBUG] http-outgoing-0 << "                    <postBuildHookScript>verify</postBuildHookScript>[\n]"
[DEBUG] http-outgoing-0 << "                    <localRepositoryPath>${project.build.directory}/local-repo</localRepositoryPath>[\n]"
[DEBUG] http-outgoing-0 << "                    <settingsFile>src/it/settings.xml</settingsFile>[\n]"
[DEBUG] http-outgoing-0 << "                    <ignoreFailures>${maven.it.failure.ignore}</ignoreFailures>[\n]"
[DEBUG] http-outgoing-0 << "                    <properties>[\n]"
[DEBUG] http-outgoing-0 << "                      <!-- e.g. ensure that Java7 picks up TLSv1.2 when connecting with Central -->[\n]"
[DEBUG] http-outgoing-0 << "                      <https.protocols>${https.protocols}</https.protocols>[\n]"
[DEBUG] http-outgoing-0 << "                    </properties>[\n]"
[DEBUG] http-outgoing-0 << "                    <goals>[\n]"
[DEBUG] http-outgoing-0 << "                      <goal>clean</goal>[\n]"
[DEBUG] http-outgoing-0 << "                      <goal>test-compile</goal>[\n]"
[DEBUG] http-outgoing-0 << "                    </goals>[\n]"
[DEBUG] http-outgoing-0 << "                  </configuration>[\n]"
[DEBUG] http-outgoing-0 << "                </execution>[\n]"
[DEBUG] http-outgoing-0 << "              </executions>[\n]"
[DEBUG] http-outgoing-0 << "            </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          </plugins>[\n]"
[DEBUG] http-outgoing-0 << "        </pluginManagement>[\n]"
[DEBUG] http-outgoing-0 << "      </build>[\n]"
[DEBUG] http-outgoing-0 << "      <!--[\n]"
[DEBUG] http-outgoing-0 << "      add those dependency just to have faster it test (for folks who doesn't have those locally[\n]"
[DEBUG] http-outgoing-0 << "      they will be downloaded from local repo rather than central[\n]"
[DEBUG] http-outgoing-0 << "      -->[\n]"
[DEBUG] http-outgoing-0 << "      <dependencies>[\n]"
[DEBUG] http-outgoing-0 << "        <dependency>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.groovy</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>groovy-eclipse-compiler</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${groovyEclipseCompilerVersion}</version>[\n]"
[DEBUG] http-outgoing-0 << "          <scope>test</scope>[\n]"
[DEBUG] http-outgoing-0 << "        </dependency"
Progress (1): maven-compiler-plugin-3.8.0.pom (11/12 kB) 
[DEBUG] http-outgoing-0 << ">[\n]"
[DEBUG] http-outgoing-0 << "        <dependency>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.groovy</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>groovy-eclipse-batch</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${groovy-eclipse-batch}</version>[\n]"
[DEBUG] http-outgoing-0 << "          <scope>test</scope>[\n]"
[DEBUG] http-outgoing-0 << "        </dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <dependency>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.groovy</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>groovy-all</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${groovyVersion}</version>[\n]"
[DEBUG] http-outgoing-0 << "          <scope>test</scope>[\n]"
[DEBUG] http-outgoing-0 << "        </dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <dependency>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.openjpa</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>openjpa</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${openJpaVersion}</version>[\n]"
[DEBUG] http-outgoing-0 << "          <!-- TODO: scope test? -->[\n]"
[DEBUG] http-outgoing-0 << "        </dependency>[\n]"
[DEBUG] http-outgoing-0 << "      </dependencies>[\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\n]"
[DEBUG] http-outgoing-0 << "      <id>Jenkins</id>[\n]"
[DEBUG] http-outgoing-0 << "      <activation>[\n]"
[DEBUG] http-outgoing-0 << "        <property>[\n]"
[DEBUG] http-outgoing-0 << "          <name>env.JENKINS_URL</name>[\n]"
[DEBUG] http-outgoing-0 << "        </property>[\n]"
[DEBUG] http-outgoing-0 << "      </activation>[\n]"
[DEBUG] http-outgoing-0 << "      <build>[\n]"
[DEBUG] http-outgoing-0 << "        <pluginManagement>[\n]"
[DEBUG] http-outgoing-0 << "          <plugins>[\n]"
[DEBUG] http-outgoing-0 << "            <plugin>[\n]"
[DEBUG] http-outgoing-0 << "              <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "              <artifactId>maven-invoker-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "              <configuration>[\n]"
[DEBUG] http-outgoing-0 << "                <pomExcludes>[\n]"
[DEBUG] http-outgoing-0 << "                  <pomExclude>multirelease-patterns/singleproject-toolchains/pom.xml</pomExclude>[\n]"
[DEBUG] http-outgoing-0 << "                </pomExcludes>[\n]"
[DEBUG] http-outgoing-0 << "              </configuration>[\n]"
[DEBUG] http-outgoing-0 << "            </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          </plugins>[\n]"
[DEBUG] http-outgoing-0 << "        </pluginManagement>[\n]"
[DEBUG] http-outgoing-0 << "      </build>[\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\n]"
[DEBUG] http-outgoing-0 << "  "
[DEBUG] http-outgoing-0 << "</profiles>[\n]"
[DEBUG] http-outgoing-0 << "</project>[\n]"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
Progress (1): maven-compiler-plugin-3.8.0.pom (12 kB)   
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/maven-compiler-plugin-3.8.0.pom.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/maven-compiler-plugin-3.8.0.pom.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/maven-compiler-plugin-3.8.0.pom.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "178d4408c0be2dcca579429ec7a5afa0"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Thu, 26 Jul 2018 17:14:38 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 178d4408c0be2dcca579429ec7a5afa0[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 7d1359019034b8c3cdfc72b0f678fd6d0d7d8723[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:51 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 730543[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 37[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251812.874246,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "178d4408c0be2dcca579429ec7a5afa0"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Thu, 26 Jul 2018 17:14:38 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 178d4408c0be2dcca579429ec7a5afa0
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 7d1359019034b8c3cdfc72b0f678fd6d0d7d8723
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:51 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 730543
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 37
[DEBUG] http-outgoing-0 << X-Timer: S1698251812.874246,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "b8847e359d345411fb6b8741e8efb74df550d1ca"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
                                                     
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/maven-compiler-plugin-3.8.0.pom (12 kB at 19 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/maven-compiler-plugin-3.8.0.pom.lastUpdated
[DEBUG] Resolving artifact org.apache.maven.plugins:maven-plugins:pom:32 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-plugins/32/maven-plugins-32.pom
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/apache/maven/plugins/maven-plugins/32/maven-plugins-32.pom HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/apache/maven/plugins/maven-plugins/32/maven-plugins-32.pom HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/apache/maven/plugins/maven-plugins/32/maven-plugins-32.pom HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 10687[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "f459c39fbe8819098e535efffbd6924c"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/xml[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Sun, 15 Jul 2018 18:19:55 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: f459c39fbe8819098e535efffbd6924c[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 2a3baf1ef657c0bdf4ac47035c27540a67569279[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:51 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 1848778[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 43[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251812.961659,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 10687
[DEBUG] http-outgoing-0 << ETag: "f459c39fbe8819098e535efffbd6924c"
[DEBUG] http-outgoing-0 << Content-Type: text/xml
[DEBUG] http-outgoing-0 << Last-Modified: Sun, 15 Jul 2018 18:19:55 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: f459c39fbe8819098e535efffbd6924c
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 2a3baf1ef657c0bdf4ac47035c27540a67569279
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:51 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 1848778
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 43
[DEBUG] http-outgoing-0 << X-Timer: S1698251812.961659,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "<?xml version='1.0' encoding='UTF-8'?>[\n]"
[DEBUG] http-outgoing-0 << "<!--[\n]"
[DEBUG] http-outgoing-0 << "Licensed to the Apache Software Foundation (ASF) under one[\n]"
[DEBUG] http-outgoing-0 << "or more contributor license agreements.  See the NOTICE file[\n]"
[DEBUG] http-outgoing-0 << "distributed with this work for additional information[\n]"
[DEBUG] http-outgoing-0 << "regarding copyright ownership.  The ASF licenses this file[\n]"
[DEBUG] http-outgoing-0 << "to you under the Apache License, Version 2.0 (the[\n]"
[DEBUG] http-outgoing-0 << ""License"); you may not use this file except in compliance[\n]"
[DEBUG] http-outgoing-0 << "with the License.  You may obtain a copy of the License at[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  http://www.apache.org/licenses/LICENSE-2.0[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "Unless required by applicable law or agreed to in writing,[\n]"
[DEBUG] http-outgoing-0 << "software distributed under the License is distributed on an[\n]"
[DEBUG] http-outgoing-0 << ""AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY[\n]"
[DEBUG] http-outgoing-0 << "KIND, either express or implied.  See the License for the[\n]"
[DEBUG] http-outgoing-0 << "specific language governing permissions and limitations[\n]"
[DEBUG] http-outgoing-0 << "under the License.[\n]"
[DEBUG] http-outgoing-0 << "-->[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">[\n]"
[DEBUG] http-outgoing-0 << "  <modelVersion>4.0.0</modelVersion>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <parent>[\n]"
[DEBUG] http-outgoing-0 << "    <groupId>org.apache.maven</groupId>[\n]"
[DEBUG] http-outgoing-0 << "    <artifactId>maven-parent</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "    <version>32</version>[\n]"
[DEBUG] http-outgoing-0 << "    <relativePath>../pom.xml</relativePath>[\n]"
[DEBUG] http-outgoing-0 << "  </parent>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "  <artifactId>maven-plugins</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "  <packaging>pom</packaging>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <name>Apache Maven Plugins</name>[\n]"
[DEBUG] http-outgoing-0 << "  <"
[DEBUG] http-outgoing-0 << "description>Maven Plugins</description>[\n]"
[DEBUG] http-outgoing-0 << "  <url>https://maven.apache.org/plugins/</url>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <ciManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <system>Jenkins</system>[\n]"
[DEBUG] http-outgoing-0 << "    <url>https://builds.apache.org/job/maven-plugins/</url>[\n]"
[DEBUG] http-outgoing-0 << "  </ciManagement>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <distributionManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <site>[\n]"
[DEBUG] http-outgoing-0 << "      <id>apache.website</id>[\n]"
[DEBUG] http-outgoing-0 << "      <url>scm:svn:https://svn.apache.org/repos/asf/maven/website/components/plugins-archives/</url>[\n]"
[DEBUG] http-outgoing-0 << "    </site>[\n]"
[DEBUG] http-outgoing-0 << "  </distributionManagement>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <properties>[\n]"
[DEBUG] http-outgoing-0 << "    <maven.site.path>plugins-archives/${project.artifactId}-LATEST</maven.site.path>[\n]"
[DEBUG] http-outgoing-0 << "  </properties>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <dependencies>[\n]"
[DEBUG] http-outgoing-0 << "    <!-- dependencies to annotations -->[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.apache.maven.plugin-tools</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>maven-plugin-annotations</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "  </dependencies>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <build>[\n]"
[DEBUG] http-outgoing-0 << "    <pluginManagement>[\n]"
[DEBUG] http-outgoing-0 << "      <plugins>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-changes-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <issueManagementSystems>[\n]"
[DEBUG] http-outgoing-0 << "              <issueManagementSystem>JIRA</issueManagementSystem>[\n]"
[DEBUG] http-outgoing-0 << "            </issueManagementSystems>[\n]"
[DEBUG] http-outgoing-0 << "            <maxEntries>1000</maxEntries>[\n]"
[DEBUG] http-outgoing-0 << "            <runOnlyAtExecutionRoot>true</runOnlyAtExecutionRoot>[\n]"
[DEBUG] http-outgoing-0 << "            <!-- Used by announcement-generate goal -->[\n]"
[DEBUG] http-outgoing-0 << "            <templateDirectory>org/apache/maven/plugins</templateDirectory>[\n]"
[DEBUG] http-outgoing-0 << "            <!-- Used by announce"
Progress (1): maven-plugins-32.pom (2.8/11 kB)
[DEBUG] http-outgoing-0 << "ment-mail goal -->[\n]"
[DEBUG] http-outgoing-0 << "            <subject>[ANN] ${project.name} ${project.version} Released</subject>[\n]"
[DEBUG] http-outgoing-0 << "            <toAddresses>[\n]"
[DEBUG] http-outgoing-0 << "              <toAddress implementation="java.lang.String">announce@maven.apache.org</toAddress>[\n]"
[DEBUG] http-outgoing-0 << "              <toAddress implementation="java.lang.String">users@maven.apache.org</toAddress>[\n]"
[DEBUG] http-outgoing-0 << "            </toAddresses>[\n]"
[DEBUG] http-outgoing-0 << "            <ccAddresses>[\n]"
[DEBUG] http-outgoing-0 << "              <ccAddress implementation="java.lang.String">dev@maven.apache.org</ccAddress>[\n]"
[DEBUG] http-outgoing-0 << "            </ccAddresses>[\n]"
[DEBUG] http-outgoing-0 << "            <!-- These values need to be specified as properties in the profile apache-release in your settings.xml -->[\n]"
[DEBUG] http-outgoing-0 << "            <fromDeveloperId>${apache.availid}</fromDeveloperId>[\n]"
[DEBUG] http-outgoing-0 << "            <smtpHost>${smtp.host}</smtpHost>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <dependencies>[\n]"
[DEBUG] http-outgoing-0 << "            <!-- Used by announcement-generate goal -->[\n]"
[DEBUG] http-outgoing-0 << "            <dependency>[\n]"
[DEBUG] http-outgoing-0 << "              <groupId>org.apache.maven.shared</groupId>[\n]"
[DEBUG] http-outgoing-0 << "              <artifactId>maven-shared-resources</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "              <version>2</version>[\n]"
[DEBUG] http-outgoing-0 << "            </dependency>[\n]"
[DEBUG] http-outgoing-0 << "          </dependencies>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-javadoc-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <tagletArtifacts>[\n]"
[DEBUG] http-outgoing-0 << "              <tagletArtifact combine.id="org.apache.maven.plugin-tools:maven-plugin-tools-javadoc">[\n]"
[DEBUG] http-outgoing-0 << "                <g"
[DEBUG] http-outgoing-0 << "roupId>org.apache.maven.plugin-tools</groupId>[\n]"
[DEBUG] http-outgoing-0 << "                <artifactId>maven-plugin-tools-javadoc</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                <version>${mavenPluginToolsVersion}</version>[\n]"
[DEBUG] http-outgoing-0 << "              </tagletArtifact>[\n]"
[DEBUG] http-outgoing-0 << "            </tagletArtifacts>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-release-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <tagBase>https://svn.apache.org/repos/asf/maven/plugins/tags</tagBase>[\n]"
[DEBUG] http-outgoing-0 << "            <releaseProfiles>apache-release,run-its</releaseProfiles>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-plugin-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${mavenPluginToolsVersion}</version>[\n]"
[DEBUG] http-outgoing-0 << "          <executions>[\n]"
[DEBUG] http-outgoing-0 << "            <execution>[\n]"
[DEBUG] http-outgoing-0 << "              <id>default-descriptor</id>[\n]"
[DEBUG] http-outgoing-0 << "              <phase>process-classes</phase>[\n]"
[DEBUG] http-outgoing-0 << "            </execution>[\n]"
[DEBUG] http-outgoing-0 << "            <execution>[\n]"
[DEBUG] http-outgoing-0 << "              <id>generate-helpmojo</id>[\n]"
[DEBUG] http-outgoing-0 << "              <goals>[\n]"
[DEBUG] http-outgoing-0 << "                <goal>helpmojo</goal>[\n]"
[DEBUG] http-outgoing-0 << "              </goals>[\n]"
[DEBUG] http-outgoing-0 << "            </execution>[\n]"
[DEBUG] http-outgoing-0 << "          </executions>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <!-- publish mono-module site with "mvn site-deploy" -->[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-site-plugin</artifactId"
Progress (1): maven-plugins-32.pom (5.5/11 kB)
[DEBUG] http-outgoing-0 << ">[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <skipDeploy>true</skipDeploy><!-- don't deploy site with maven-site-plugin -->[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-scm-publish-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <content>${project.reporting.outputDirectory}</content><!-- no need for site:stage, use target/site -->[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <executions>[\n]"
[DEBUG] http-outgoing-0 << "            <execution>[\n]"
[DEBUG] http-outgoing-0 << "              <id>scm-publish</id>[\n]"
[DEBUG] http-outgoing-0 << "              <phase>site-deploy</phase><!-- deploy site with maven-scm-publish-plugin -->[\n]"
[DEBUG] http-outgoing-0 << "              <goals>[\n]"
[DEBUG] http-outgoing-0 << "                <goal>publish-scm</goal>[\n]"
[DEBUG] http-outgoing-0 << "              </goals>[\n]"
[DEBUG] http-outgoing-0 << "            </execution>[\n]"
[DEBUG] http-outgoing-0 << "          </executions>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      </plugins>[\n]"
[DEBUG] http-outgoing-0 << "    </pluginManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-plugin-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-enforcer-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>enforce</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "            <id>ensure-no-container-api</id>[\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\n]"
[DEBUG] http-outgoing-0 << "              <rules>[\n]"
[DEBUG] http-outgoing-0 << "                <bannedDependencies>[\n]"
[DEBUG] http-outgoing-0 << "                  <exclud"
[DEBUG] http-outgoing-0 << "es>[\n]"
[DEBUG] http-outgoing-0 << "                    <exclude>org.codehaus.plexus:plexus-component-api</exclude>[\n]"
[DEBUG] http-outgoing-0 << "                  </excludes>[\n]"
[DEBUG] http-outgoing-0 << "                  <message>The new containers are not supported. You probably added a dependency that is missing the exclusions.</message>[\n]"
[DEBUG] http-outgoing-0 << "                </bannedDependencies>[\n]"
[DEBUG] http-outgoing-0 << "              </rules>[\n]"
[DEBUG] http-outgoing-0 << "              <fail>true</fail>[\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "    </plugins>[\n]"
[DEBUG] http-outgoing-0 << "  </build>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <reporting>[\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-plugin-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>${mavenPluginToolsVersion}</version>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "    </plugins>[\n]"
[DEBUG] http-outgoing-0 << "  </reporting>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <profiles>[\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\n]"
[DEBUG] http-outgoing-0 << "      <id>quality-checks</id>[\n]"
[DEBUG] http-outgoing-0 << "      <activation>[\n]"
[DEBUG] http-outgoing-0 << "        <property>[\n]"
[DEBUG] http-outgoing-0 << "          <name>quality-checks</name>[\n]"
[DEBUG] http-outgoing-0 << "          <value>true</value>[\n]"
[DEBUG] http-outgoing-0 << "        </property>[\n]"
[DEBUG] http-outgoing-0 << "      </activation>[\n]"
[DEBUG] http-outgoing-0 << "      <build>[\n]"
[DEBUG] http-outgoing-0 << "        <plugins>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-docck-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\n]"
[DEBUG] http-outgoing-0 << "                <id>docck-check</id>[\n]"
[DEBUG] http-outgoing-0 << "                <phase>verify</phase>[\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\n]"
[DEBUG] http-outgoing-0 << "                  <goal>check</goal>[\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin"
Progress (1): maven-plugins-32.pom (8.3/11 kB)
[DEBUG] http-outgoing-0 << "s>[\n]"
[DEBUG] http-outgoing-0 << "      </build>[\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\n]"
[DEBUG] http-outgoing-0 << "      <id>run-its</id>[\n]"
[DEBUG] http-outgoing-0 << "      <properties>[\n]"
[DEBUG] http-outgoing-0 << "        <!-- when testing with JDK9, change these values to 1.6 from cmdline -->[\n]"
[DEBUG] http-outgoing-0 << "        <invoker.maven.compiler.source>${maven.compiler.source}</invoker.maven.compiler.source>[\n]"
[DEBUG] http-outgoing-0 << "        <invoker.maven.compiler.target>${maven.compiler.target}</invoker.maven.compiler.target>[\n]"
[DEBUG] http-outgoing-0 << "        <maven.it.failure.ignore>false</maven.it.failure.ignore>[\n]"
[DEBUG] http-outgoing-0 << "      </properties>[\n]"
[DEBUG] http-outgoing-0 << "      <build>[\n]"
[DEBUG] http-outgoing-0 << "        <plugins>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-invoker-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\n]"
[DEBUG] http-outgoing-0 << "              <debug>true</debug>[\n]"
[DEBUG] http-outgoing-0 << "              <projectsDirectory>src/it</projectsDirectory>[\n]"
[DEBUG] http-outgoing-0 << "              <cloneProjectsTo>${project.build.directory}/it</cloneProjectsTo>[\n]"
[DEBUG] http-outgoing-0 << "              <preBuildHookScript>setup</preBuildHookScript>[\n]"
[DEBUG] http-outgoing-0 << "              <postBuildHookScript>verify</postBuildHookScript>[\n]"
[DEBUG] http-outgoing-0 << "              <localRepositoryPath>${project.build.directory}/local-repo</localRepositoryPath>[\n]"
[DEBUG] http-outgoing-0 << "              <settingsFile>src/it/settings.xml</settingsFile>[\n]"
[DEBUG] http-outgoing-0 << "              <pomIncludes>[\n]"
[DEBUG] http-outgoing-0 << "                <pomInclude>*/pom.xml</pomInclude>[\n]"
[DEBUG] http-outgoing-0 << "              </pomIncludes>[\n]"
[DEBUG] http-outgoing-0 << "              <properties>[\n]"
[DEBUG] http-outgoing-0 << "                <maven.compiler.source>${invoker.maven.compiler.source}</maven.compiler.source>[\n]"
[DEBUG] http-outgoing-0 << "                <maven.compiler.target>${invoker.mav"
[DEBUG] http-outgoing-0 << "en.compiler.target}</maven.compiler.target>[\n]"
[DEBUG] http-outgoing-0 << "                <!-- e.g. ensure that Java7 picks up TLSv1.2 when connecting with Central -->[\n]"
[DEBUG] http-outgoing-0 << "                <https.protocols>${https.protocols}</https.protocols>[\n]"
[DEBUG] http-outgoing-0 << "              </properties>[\n]"
[DEBUG] http-outgoing-0 << "              <ignoreFailures>${maven.it.failure.ignore}</ignoreFailures>[\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\n]"
[DEBUG] http-outgoing-0 << "                <id>integration-test</id>[\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\n]"
[DEBUG] http-outgoing-0 << "                  <goal>install</goal>[\n]"
[DEBUG] http-outgoing-0 << "                  <goal>integration-test</goal>[\n]"
[DEBUG] http-outgoing-0 << "                  <goal>verify</goal>[\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        </plugins>[\n]"
[DEBUG] http-outgoing-0 << "      </build>[\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\n]"
[DEBUG] http-outgoing-0 << "      <id>reporting</id>[\n]"
[DEBUG] http-outgoing-0 << "      <reporting>[\n]"
[DEBUG] http-outgoing-0 << "        <plugins>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-invoker-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        </plugins>[\n]"
[DEBUG] http-outgoing-0 << "      </reporting>[\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\n]"
[DEBUG] http-outgoing-0 << "  </profiles>[\n]"
[DEBUG] http-outgoing-0 << "</project>[\n]"
Progress (1): maven-plugins-32.pom (11 kB)    
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/apache/maven/plugins/maven-plugins/32/maven-plugins-32.pom.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/apache/maven/plugins/maven-plugins/32/maven-plugins-32.pom.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/apache/maven/plugins/maven-plugins/32/maven-plugins-32.pom.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "adbe13e7c626b624df6039feabf98695"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Sun, 15 Jul 2018 18:19:56 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: adbe13e7c626b624df6039feabf98695[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 934a03649a3b7122dc53f98c6f492866ac39a0a1[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:52 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 776181[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 38[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251812.017261,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "adbe13e7c626b624df6039feabf98695"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Sun, 15 Jul 2018 18:19:56 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: adbe13e7c626b624df6039feabf98695
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 934a03649a3b7122dc53f98c6f492866ac39a0a1
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:52 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 776181
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 38
[DEBUG] http-outgoing-0 << X-Timer: S1698251812.017261,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "2a3baf1ef657c0bdf4ac47035c27540a67569279"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
                                          
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-plugins/32/maven-plugins-32.pom (11 kB at 120 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/apache/maven/plugins/maven-plugins/32/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/apache/maven/plugins/maven-plugins/32/maven-plugins-32.pom.lastUpdated
[DEBUG] Resolving artifact org.apache.maven:maven-parent:pom:32 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/maven-parent/32/maven-parent-32.pom
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/apache/maven/maven-parent/32/maven-parent-32.pom HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/apache/maven/maven-parent/32/maven-parent-32.pom HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/apache/maven/maven-parent/32/maven-parent-32.pom HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 43335[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "74008b0cbd1837a1cd8cb2dbb4b93027"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/xml[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Sun, 15 Jul 2018 18:19:48 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 74008b0cbd1837a1cd8cb2dbb4b93027[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: c0c90b7620fadf06be2c7f6ede7d7a54ab2f1606[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:52 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 1924607[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 44[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251812.056861,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 43335
[DEBUG] http-outgoing-0 << ETag: "74008b0cbd1837a1cd8cb2dbb4b93027"
[DEBUG] http-outgoing-0 << Content-Type: text/xml
[DEBUG] http-outgoing-0 << Last-Modified: Sun, 15 Jul 2018 18:19:48 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 74008b0cbd1837a1cd8cb2dbb4b93027
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: c0c90b7620fadf06be2c7f6ede7d7a54ab2f1606
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:52 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 1924607
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 44
[DEBUG] http-outgoing-0 << X-Timer: S1698251812.056861,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "<?xml version="1.0" encoding="UTF-8"?>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "<!--[\n]"
[DEBUG] http-outgoing-0 << "Licensed to the Apache Software Foundation (ASF) under one[\n]"
[DEBUG] http-outgoing-0 << "or more contributor license agreements.  See the NOTICE file[\n]"
[DEBUG] http-outgoing-0 << "distributed with this work for additional information[\n]"
[DEBUG] http-outgoing-0 << "regarding copyright ownership.  The ASF licenses this file[\n]"
[DEBUG] http-outgoing-0 << "to you under the Apache License, Version 2.0 (the[\n]"
[DEBUG] http-outgoing-0 << ""License"); you may not use this file except in compliance[\n]"
[DEBUG] http-outgoing-0 << "with the License.  You may obtain a copy of the License at[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  http://www.apache.org/licenses/LICENSE-2.0[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "Unless required by applicable law or agreed to in writing,[\n]"
[DEBUG] http-outgoing-0 << "software distributed under the License is distributed on an[\n]"
[DEBUG] http-outgoing-0 << ""AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY[\n]"
[DEBUG] http-outgoing-0 << "KIND, either express or implied.  See the License for the[\n]"
[DEBUG] http-outgoing-0 << "specific language governing permissions and limitations[\n]"
[DEBUG] http-outgoing-0 << "under the License.[\n]"
[DEBUG] http-outgoing-0 << "-->[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">[\n]"
[DEBUG] http-outgoing-0 << "  <modelVersion>4.0.0</modelVersion>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <!-- for more information, see the documentation of this POM: https://maven.apache.org/pom/maven/ -->[\n]"
[DEBUG] http-outgoing-0 << "  <parent>[\n]"
[DEBUG] http-outgoing-0 << "    <groupId>org.apache</groupId>[\n]"
[DEBUG] http-outgoing-0 << "    <artifactId>apache</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "    <version>20</version>[\n]"
[DEBUG] http-outgoing-0 << "    <relativePath>../asf/pom.xml</relativePath>[\n]"
[DEBUG] http-outgoing-0 << "  </parent>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <groupId>org.apache.maven</groupId>[\n]"
[DEBUG] http-outgoing-0 << "  <artifactId>maven-parent</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "  <version>32</version>[\n]"
[DEBUG] http-outgoing-0 << "  <packaging>pom</packaging>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <name>Apache Maven</name>[\n]"
[DEBUG] http-outgoing-0 << "  <description>Maven is a software project management and comprehension tool. Based on the concept of a project object model (POM), Maven can manage a project's build, reporting and documentation from a central piece of information.</description>[\n]"
[DEBUG] http-outgoing-0 << "  <url>https://maven.apache.org/</url>[\n]"
[DEBUG] http-outgoing-0 << "  <inceptionYear>2002</inceptionYear>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <!-- Developers listed by PMC Chair, PMC, Committers, Contributers, all alphabetical-->[\n]"
[DEBUG] http-outgoing-0 << "  <developers>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>rfscholte</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Robert Scholte</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>rfscholte@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Chair</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>Europe/Amsterdam</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>aheritier</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Arnaud H[0xc3][0xa9]ritier</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>aheritier@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>andham</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Anders Hammar</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>andham@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>baerrach</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Barrie Treloar</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>baerrach@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>Australia/Adelaide</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>bimargulies</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Benson Margulies</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>bimargulies@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>America/New_York</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>brianf</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Brian Fox</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>brianf@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>Sonatype</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>-5</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>cstamas</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Tamas Cservenak</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>cstamas@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>dennisl</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Dennis Lundberg</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>dennisl@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>ASF</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>dkulp</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Daniel Kulp</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>dkulp@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>ASF</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>-5</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id"
Progress (1): maven-parent-32.pom (4.1/43 kB)
[DEBUG] http-outgoing-0 << ">evenisse</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Emmanuel Venisse</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>evenisse@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>ASF</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>gboue</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Guillaume Bou[0xc3][0xa9]</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>gboue@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>Europe/Paris</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>hboutemy</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Herv[0xc3][0xa9] Boutemy</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>hboutemy@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>ASF</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>Europe/Paris</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>ifedorenko</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Igor Fedorenko</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>igor@ifedorenko.com</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>Sonatype</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>-5</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>jvanzyl</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Jason van Zyl</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>jason@maven.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>-5</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>khmarbaise</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Karl Heinz Marbaise</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>khmarbaise@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>krosenvold</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Kristian Rosenvold</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>krosenvold@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>mkleint</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Milos Kleint</name>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>olamy</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Olivier Lamy</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>olamy@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>Australia/Melbourne</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>michaelo</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Michael Osipov</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>michaelo@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>Europe/Berlin</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>rgoers</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Ralph Goers</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>rgoers@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>Intuit</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>-8</timezone>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>snicoll</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Stephane Nicoll</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>snicoll@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>ASF</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>stephenc</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Stephen Connolly</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>stephenc@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>0</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>tibordigana</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Tibor Diga[0xc5][0x88]a</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>tibordigana@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>Europe/Bratislava</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>vsiveton</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Vincent Siveton</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>vsiveton@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>ASF</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>-5</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>wfay</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Wayne Fay</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>wfay@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>ASF</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>PMC Member</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>-6</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <!--Committers-->[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>adangel</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Andreas Dangel</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>adangel@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>Europe/Berlin</timezone>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>bdemers</id>[\n]"
[DEBUG] http-outgoing-0 << "  "
Progress (1): maven-parent-32.pom (8.2/43 kB)
[DEBUG] http-outgoing-0 << "    <name>Brian Demers</name>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>Sonatype</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <email>bdemers@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>-5</timezone>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>bellingard</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Fabrice Bellingard</name>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>bentmann</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Benjamin Bentmann</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>bentmann@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>Sonatype</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>chrisgwarp</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Chris Graham</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>chrisgwarp@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>Australia/Melbourne</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>dantran</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Dan Tran</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>dantran@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>-8</timezone>      [\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>dbradicich</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Damian Bradicich</name>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>Sonatype</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <email>dbradicich@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>-5</timezone>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>brett</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Brett Porter</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>brett@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>ASF</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+10</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>dfabulich</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Daniel Fabulich</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>dfabulich@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>-8</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>fgiust</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Fabrizio Giustina</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>fgiust@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>openmind</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>godin</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Evgeny Mandrikov</name>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>SonarSource</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <email>godin@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+3</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>handyande</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Andrew Williams</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>handyande@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>0</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>imod</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Dominik Bartholdi</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>imod@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>Europe/Zurich</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>     [\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>jjensen</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Jeff Jensen</name>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>ltheussl</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Lukas Theussl</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>ltheussl@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>markh</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Mark Hobson</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>markh@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>0</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>mauro</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Mauro Talevi</name>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>mfriedenhagen</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Mirko Friedenhagen</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>mfriedenhagen@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>mmoser</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Manfred Moser</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>mmoser@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>-8</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>nicolas</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Nicolas de Loof</nam"
Progress (1): maven-parent-32.pom (12/43 kB) 
[DEBUG] http-outgoing-0 << "e>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>oching</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Maria Odea B. Ching</name>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>pgier</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Paul Gier</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>pgier@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>Red Hat</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>-6</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>ptahchiev</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Petar Tahchiev</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>ptahchiev@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+2</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>rafale</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Rapha[0xc3][0xab]l Pi[0xc3][0xa9]roni</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>rafale@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>Dexem</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>schulte</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Christian Schulte</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>schulte@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>Europe/Berlin</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>simonetripodi</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Simone Tripodi</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>simonetripodi@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>sor</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Christian Stein</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>sor@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>Europe/Berlin</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>struberg</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Mark Struberg</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>struberg@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>tchemit</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Tony Chemit</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>tchemit@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>CodeLutin</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>Europe/Paris</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>vmassol</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Vincent Massol</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>vmassol@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>ASF</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Committer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <!--End Committers-->[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>agudian</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Andreas Gudian</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>agudian@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>Europe/Berlin</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>aramirez</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Allan Q. Ramirez</name>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>bayard</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Henri Yandell</name>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>carlos</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Carlos Sanchez</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>carlos@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>ASF</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>chrisjs</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Chris Stevenson</name>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>dblevins</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>David Blevins</name>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>dlr</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Daniel Rall</name>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>epunzalan</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Edwin Punzalan</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>epunzalan@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>-8</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>felipeal</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Felipe Leme</name>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>jdcasey</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>John Casey</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>jdcas"
Progress (1): maven-parent-32.pom (16/43 kB)
[DEBUG] http-outgoing-0 << "ey@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>ASF</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>-6</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>jmcconnell</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Jesse McConnell</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>jmcconnell@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>ASF</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>-6</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>joakime</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Joakim Erdfelt</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>joakime@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>ASF</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>-5</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>jruiz</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Johnny Ruiz III</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>jruiz@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>jstrachan</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>James Strachan</name>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>jtolentino</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Ernesto Tolentino Jr.</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>jtolentino@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>ASF</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+8</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>kenney</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Kenney Westerhof</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>kenney@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>Neonics</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>mperham</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Mike Perham</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>mperham@gmail.com</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>IBM</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>-6</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>ogusakov</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Oleg Gusakov</name>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>pschneider</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Patrick Schneider</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>pschneider@gmail.com</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>-6</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>rinku</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Rahul Thakur</name>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>shinobu</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Shinobu Kuwai</name>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>smorgrav</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Torbjorn Eikli Smorgrav</name>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>trygvis</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Trygve Laugstol</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>trygvis@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>ASF</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>wsmoak</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Wendy Smoak</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>wsmoak@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Emeritus</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>-7</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "  </developers>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <mailingLists>[\n]"
[DEBUG] http-outgoing-0 << "    <mailingList>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Maven User List</name>[\n]"
[DEBUG] http-outgoing-0 << "      <subscribe>users-subscribe@maven.apache.org</subscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <unsubscribe>users-unsubscribe@maven.apache.org</unsubscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <post>users@maven.apache.org</post>[\n]"
[DEBUG] http-outgoing-0 << "      <archive>https://lists.apache.org/list.html?users@maven.apache.org</archive>[\n]"
[DEBUG] http-outgoing-0 << "      <otherArchives>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://mail-archives.apache.org/mod_mbox/maven-users</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://www.mail-archive.com/users@maven.apache.org/</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://maven.40175.n5.nabble.com/Maven-Users-f40176.html</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://maven-users.markmail.org/</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "      </otherArchives>[\n]"
[DEBUG] http-outgoing-0 << "    </mailingList>[\n]"
[DEBUG] http-outgoing-0 << "    <mailingList>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Maven Developer List</name>[\n]"
[DEBUG] http-outgoing-0 << "      <subscribe>dev-subscribe@maven.apache.org</subscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <unsubscribe>dev-unsubscribe@maven.apache.org</unsubscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <post>dev@maven.apache.org</post>[\n]"
[DEBUG] http-outgoing-0 << "     "
Progress (1): maven-parent-32.pom (20/43 kB)
[DEBUG] http-outgoing-0 << " <archive>https://lists.apache.org/list.html?dev@maven.apache.org</archive>[\n]"
[DEBUG] http-outgoing-0 << "      <otherArchives>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://mail-archives.apache.org/mod_mbox/maven-dev</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://www.mail-archive.com/dev@maven.apache.org/</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://maven.40175.n5.nabble.com/Maven-Developers-f142166.html</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://maven-dev.markmail.org/</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "      </otherArchives>[\n]"
[DEBUG] http-outgoing-0 << "    </mailingList>[\n]"
[DEBUG] http-outgoing-0 << "    <mailingList>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Maven Issues List</name>[\n]"
[DEBUG] http-outgoing-0 << "      <subscribe>issues-subscribe@maven.apache.org</subscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <unsubscribe>issues-unsubscribe@maven.apache.org</unsubscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <archive>https://lists.apache.org/list.html?issues@maven.apache.org</archive>[\n]"
[DEBUG] http-outgoing-0 << "      <otherArchives>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://mail-archives.apache.org/mod_mbox/maven-issues/</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://www.mail-archive.com/issues@maven.apache.org</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://maven.40175.n5.nabble.com/Maven-Issues-f219593.html</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://maven-issues.markmail.org/</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "      </otherArchives>[\n]"
[DEBUG] http-outgoing-0 << "    </mailingList>[\n]"
[DEBUG] http-outgoing-0 << "    <mailingList>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Maven Commits List</name>[\n]"
[DEBUG] http-outgoing-0 << "      <subscribe>commits-subscribe@maven.apache.org</subscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <unsubscribe>commits-unsubscribe@maven.apache.org</unsubscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <archive>https://lists.apache.org/list.html?commits@maven.apache.org</archive>[\n]"
[DEBUG] http-outgoing-0 << "      <otherArchives>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://mail-archives.apache.org/mod_mbox/maven-commits/</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://www.mail-archive.com/commits@maven.apache.org</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://maven.40175.n5.nabble.com/Maven-Commits-f277168.html</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://maven-commits.markmail.org/</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "      </otherArchives>[\n]"
[DEBUG] http-outgoing-0 << "    </mailingList>[\n]"
[DEBUG] http-outgoing-0 << "    <mailingList>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Maven Announcements List</name>[\n]"
[DEBUG] http-outgoing-0 << "      <post>announce@maven.apache.org</post>[\n]"
[DEBUG] http-outgoing-0 << "      <subscribe>announce-subscribe@maven.apache.org</subscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <unsubscribe>announce-unsubscribe@maven.apache.org</unsubscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <archive>https://lists.apache.org/list.html?announce@maven.apache.org</archive>[\n]"
[DEBUG] http-outgoing-0 << "      <otherArchives>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://mail-archives.apache.org/mod_mbox/maven-announce/</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://www.mail-archive.com/announce@maven.apache.org</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://maven.40175.n5.nabble.com/Maven-Announcements-f326045.html</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://maven-announce.markmail.org/</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "      </otherArchives>[\n]"
[DEBUG] http-outgoing-0 << "    </mailingList>[\n]"
[DEBUG] http-outgoing-0 << "    <mailingList>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Maven Notifications List</name>[\n]"
[DEBUG] http-outgoing-0 << "      <subscribe>notifications-subscribe@maven.apache.org</subscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <unsubscribe>notifications-unsubscribe@maven.apache.org</unsubscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <archive>https://lists.apache.org/list.html?notifications@maven.apache.org</archive>[\n]"
[DEBUG] http-outgoing-0 << "      <otherArchives>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://mail-archives.apache.org/mod_mbox/maven-notifications/</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://www.mail-archive.com/notifications@maven.apache.org</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://maven.40175.n5.nabble.com/Maven-Notifications-f301718.html</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>https://maven-notifications.markmail.org/</otherArchive>[\n]"
[DEBUG] http-outgoing-0 << "      </otherArchives>[\n]"
[DEBUG] http-outgoing-0 << "    </mailingList>[\n]"
[DEBUG] http-outgoing-0 << "  </mailingLists>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <modules>[\n]"
[DEBUG] http-outgoing-0 << "    <module>maven-plugins</module>[\n]"
[DEBUG] http-outgoing-0 << "    <module>maven-shared-components</module>[\n]"
[DEBUG] http-outgoing-0 << "    <module>maven-skins</module>[\n]"
[DEBUG] http-outgoing-0 << "    <module>doxia-tools</module>[\n]"
[DEBUG] http-outgoing-0 << "    <module>apache-resource-bundles</module>[\n]"
[DEBUG] http-outgoing-0 << "  </modules>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <scm>[\n]"
[DEBUG] http-outgoing-0 << "    <connection>scm:git:https://gitbox.apache.org/repos/asf/maven-parent.git</connection>[\n]"
[DEBUG] http-outgoing-0 << "    <developerConnection>scm:git:https://gitbox.apache.org/repos/asf/maven-parent.git</developerConnection>[\n]"
[DEBUG] http-outgoing-0 << "    <url>https://github.com/apache/maven-parent/tree/${project.scm.tag}</url>[\n]"
[DEBUG] http-outgoing-0 << "    <tag>maven-parent-32</tag>[\n]"
[DEBUG] http-outgoing-0 << "  </scm>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <ciManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <system>Jenkins</system>[\n]"
[DEBUG] http-outgoing-0 << "    <url>https://builds.apache.org/view/M-R/view/Maven/job/maven-box/</url>[\n]"
[DEBUG] http-outgoing-0 << "    <notifiers>[\n]"
[DEBUG] http-outgoing-0 << "      <notifier>[\n]"
[DEBUG] http-outgoing-0 << "        <t"
Progress (1): maven-parent-32.pom (25/43 kB)
[DEBUG] http-outgoing-0 << "ype>mail</type>[\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <address>notifications@maven.apache.org</address>[\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "      </notifier>[\n]"
[DEBUG] http-outgoing-0 << "    </notifiers>[\n]"
[DEBUG] http-outgoing-0 << "  </ciManagement>[\n]"
[DEBUG] http-outgoing-0 << "  <distributionManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <site>[\n]"
[DEBUG] http-outgoing-0 << "      <id>apache.website</id>[\n]"
[DEBUG] http-outgoing-0 << "      <url>scm:svn:https://svn.apache.org/repos/asf/maven/website/components/${maven.site.path}</url>[\n]"
[DEBUG] http-outgoing-0 << "    </site>[\n]"
[DEBUG] http-outgoing-0 << "  </distributionManagement>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <properties>[\n]"
[DEBUG] http-outgoing-0 << "    <javaVersion>6</javaVersion>[\n]"
[DEBUG] http-outgoing-0 << "    <maven.compiler.source>1.${javaVersion}</maven.compiler.source>[\n]"
[DEBUG] http-outgoing-0 << "    <maven.compiler.target>1.${javaVersion}</maven.compiler.target>    [\n]"
[DEBUG] http-outgoing-0 << "    <sonar.host.url>https://builds.apache.org/analysis/</sonar.host.url>[\n]"
[DEBUG] http-outgoing-0 << "    <maven.site.cache>${user.home}/maven-sites</maven.site.cache>[\n]"
[DEBUG] http-outgoing-0 << "    <maven.site.path>../..</maven.site.path><!-- to be overridden -->[\n]"
[DEBUG] http-outgoing-0 << "    <mavenPluginToolsVersion>3.5.2</mavenPluginToolsVersion>[\n]"
[DEBUG] http-outgoing-0 << "    <!-- don't fail check for some rules that are too hard to enforce (could even be told broken for some) -->[\n]"
[DEBUG] http-outgoing-0 << "    <checkstyle.violation.ignore>RedundantThrows,NewlineAtEndOfFile,ParameterNumber,MethodLength,FileLength</checkstyle.violation.ignore>[\n]"
[DEBUG] http-outgoing-0 << "  </properties>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <dependencyManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <dependencies>[\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>plexus-component-annotations</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>1.7.1</version>[\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugin-tools</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-plugin-annotations</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>${mavenPluginToolsVersion}</version>[\n]"
[DEBUG] http-outgoing-0 << "        <scope>provided</scope>[\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    </dependencies>[\n]"
[DEBUG] http-outgoing-0 << "  </dependencyManagement>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <repositories>[\n]"
[DEBUG] http-outgoing-0 << "    <repository><!-- useful to resolve parent pom when it is a SNAPSHOT -->[\n]"
[DEBUG] http-outgoing-0 << "      <id>apache.snapshots</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Apache Snapshot Repository</name>[\n]"
[DEBUG] http-outgoing-0 << "      <url>https://repository.apache.org/snapshots</url>[\n]"
[DEBUG] http-outgoing-0 << "      <releases>[\n]"
[DEBUG] http-outgoing-0 << "        <enabled>false</enabled>[\n]"
[DEBUG] http-outgoing-0 << "      </releases>[\n]"
[DEBUG] http-outgoing-0 << "    </repository>[\n]"
[DEBUG] http-outgoing-0 << "  </repositories>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <build>[\n]"
[DEBUG] http-outgoing-0 << "    <pluginManagement>[\n]"
[DEBUG] http-outgoing-0 << "      <plugins>[\n]"
[DEBUG] http-outgoing-0 << "        <!-- The Maven universe targets Java 5 minimum, with generics -->[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-plugin-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${mavenPluginToolsVersion}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.modello</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>modello-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>1.9.1</version>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <useJava5>true</useJava5>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <!-- site publishing configuration -->[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-site-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <!-- for multi-modules site staging -->[\n]"
[DEBUG] http-outgoing-0 << "            <topSiteURL>scm:svn:https://svn.apache.org/repos/asf/maven/website/components/${maven.site.path}</topSiteURL>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-scm-publish-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <checkoutDirectory>${maven.site.cache}/${maven.site.path}</checkoutDirectory>[\n]"
[DEBUG] http-outgoing-0 << "            <serverId>apache.releases.https</serverId>[\n]"
[DEBUG] http-outgoing-0 << "            <tryUpdate>true</tryUpdate>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <!-- Plexus description generator is either plexus-maven-plugin or plexus-component-metadata -->[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>plexus-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>1.3.8</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>plexus-component-metadata</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>1.7.1</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-checkstyle-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.0.0</version>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <configLocation>config/maven_checks.xml</configLocation>[\n]"
[DEBUG] http-outgoing-0 << "            <headerLocatio"
Progress (1): maven-parent-32.pom (29/43 kB)
[DEBUG] http-outgoing-0 << "n>config/maven-header.txt</headerLocation>[\n]"
[DEBUG] http-outgoing-0 << "            <!-- workaround to avoid analysing generated content (Modello, plugin help mojo, ...) -->[\n]"
[DEBUG] http-outgoing-0 << "            <sourceDirectories>[\n]"
[DEBUG] http-outgoing-0 << "              <sourceDirectory>src/main/java</sourceDirectory>[\n]"
[DEBUG] http-outgoing-0 << "            </sourceDirectories>[\n]"
[DEBUG] http-outgoing-0 << "            <testSourceDirectories>[\n]"
[DEBUG] http-outgoing-0 << "              <testSourceDirectory>src/test/java</testSourceDirectory>[\n]"
[DEBUG] http-outgoing-0 << "            </testSourceDirectories>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <dependencies>[\n]"
[DEBUG] http-outgoing-0 << "            <!-- MCHECKSTYLE-327: the maven_checks.xml was moved to a shared project -->[\n]"
[DEBUG] http-outgoing-0 << "            <dependency>[\n]"
[DEBUG] http-outgoing-0 << "              <groupId>org.apache.maven.shared</groupId>[\n]"
[DEBUG] http-outgoing-0 << "              <artifactId>maven-shared-resources</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "              <version>2</version>[\n]"
[DEBUG] http-outgoing-0 << "            </dependency>[\n]"
[DEBUG] http-outgoing-0 << "          </dependencies>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-jxr-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>2.5</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-pmd-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.8</version>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <targetJdk>${maven.compiler.target}</targetJdk>[\n]"
[DEBUG] http-outgoing-0 << "            <rulesets>[\n]"
[DEBUG] http-outgoing-0 << "              <ruleset>rulesets/maven.xml</ruleset>[\n]"
[DEBUG] http-outgoing-0 << "            </rulesets>[\n]"
[DEBUG] http-outgoing-0 << "            <excludeRoots>[\n]"
[DEBUG] http-outgoing-0 << "              <excludeRoot>${project.build.directory}/generated-sources/modello</excludeRoot>[\n]"
[DEBUG] http-outgoing-0 << "              <excludeRoot>${project.build.directory}/generated-sources/plugin</excludeRoot>[\n]"
[DEBUG] http-outgoing-0 << "            </excludeRoots>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-release-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <releaseProfiles>apache-release</releaseProfiles>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>deploy</goals>[\n]"
[DEBUG] http-outgoing-0 << "            <arguments>${arguments}</arguments>[\n]"
[DEBUG] http-outgoing-0 << "            <autoVersionSubmodules>true</autoVersionSubmodules>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-toolchains-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>1.1</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.mojo</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>findbugs-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.0.5</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.mojo</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>taglist-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>2.4</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-changes-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>2.12.1</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-javadoc-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <notimestamp>true</notimestamp>[\n]"
[DEBUG] http-outgoing-0 << "            <quiet>true</quiet>[\n]"
[DEBUG] http-outgoing-0 << "            <detectLinks>true</detectLinks>[\n]"
[DEBUG] http-outgoing-0 << "            <locale>en</locale>[\n]"
[DEBUG] http-outgoing-0 << "            <tagletArtifacts>[\n]"
[DEBUG] http-outgoing-0 << "              <tagletArtifact combine.id="org.codehaus.plexus:plexus-javadoc">[\n]"
[DEBUG] http-outgoing-0 << "                <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "                <artifactId>plexus-javadoc</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                <version>1.0</version>[\n]"
[DEBUG] http-outgoing-0 << "              </tagletArtifact>[\n]"
[DEBUG] http-outgoing-0 << "            </tagletArtifacts>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <!-- [\n]"
[DEBUG] http-outgoing-0 << "          The Maven TLP's sub-projects are likely to fork Maven for tests[\n]"
[DEBUG] http-outgoing-0 << "          ensure such forked tests do not get picked up by CI[\n]"
[DEBUG] http-outgoing-0 << "        -->[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-surefire-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <environmentVariables>[\n]"
[DEBUG] http-outgoing-0 << "              <JENKINS_MAVEN_AGENT_DISABLED>true</JENKINS_MAVEN_AGENT_DISABLED>[\n]"
[DEBUG] http-outgoing-0 << "            </environmentVariables>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-failsafe-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <environmentVariables>[\n]"
[DEBUG] http-outgoing-0 << " "
Progress (1): maven-parent-32.pom (33/43 kB)
[DEBUG] http-outgoing-0 << "             <JENKINS_MAVEN_AGENT_DISABLED>true</JENKINS_MAVEN_AGENT_DISABLED>[\n]"
[DEBUG] http-outgoing-0 << "            </environmentVariables>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-invoker-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <environmentVariables>[\n]"
[DEBUG] http-outgoing-0 << "              <JENKINS_MAVEN_AGENT_DISABLED>true</JENKINS_MAVEN_AGENT_DISABLED>[\n]"
[DEBUG] http-outgoing-0 << "            </environmentVariables>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      </plugins>[\n]"
[DEBUG] http-outgoing-0 << "    </pluginManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-checkstyle-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>checkstyle-check</id>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>check</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-enforcer-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>enforce-bytecode-version</id>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>enforce</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\n]"
[DEBUG] http-outgoing-0 << "              <rules>[\n]"
[DEBUG] http-outgoing-0 << "                <enforceBytecodeVersion>[\n]"
[DEBUG] http-outgoing-0 << "                  <maxJdkVersion>${maven.compiler.target}</maxJdkVersion>[\n]"
[DEBUG] http-outgoing-0 << "                </enforceBytecodeVersion>[\n]"
[DEBUG] http-outgoing-0 << "              </rules>[\n]"
[DEBUG] http-outgoing-0 << "              <fail>true</fail>[\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>ban-known-bad-maven-versions</id>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>enforce</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\n]"
[DEBUG] http-outgoing-0 << "              <rules>[\n]"
[DEBUG] http-outgoing-0 << "                <requireMavenVersion>[\n]"
[DEBUG] http-outgoing-0 << "                  <version>[3.0.4,)</version>[\n]"
[DEBUG] http-outgoing-0 << "                  <message>Maven 3.0 through 3.0.3 inclusive do not pass correct settings.xml to Maven Release Plugin.</message>[\n]"
[DEBUG] http-outgoing-0 << "                </requireMavenVersion>[\n]"
[DEBUG] http-outgoing-0 << "              </rules>[\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "        <dependencies>[\n]"
[DEBUG] http-outgoing-0 << "          <dependency>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.codehaus.mojo</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>extra-enforcer-rules</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <version>1.0-beta-9</version>[\n]"
[DEBUG] http-outgoing-0 << "          </dependency>[\n]"
[DEBUG] http-outgoing-0 << "        </dependencies>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.rat</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>apache-rat-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <excludes combine.children="append">[\n]"
[DEBUG] http-outgoing-0 << "            <exclude>.repository/**</exclude><!-- Jenkins job with local Maven repository -->[\n]"
[DEBUG] http-outgoing-0 << "            <exclude>.maven/spy.log</exclude><!-- Hudson Maven3 integration log -->[\n]"
[DEBUG] http-outgoing-0 << "            <exclude>dependency-reduced-pom.xml</exclude><!-- Maven shade plugin -->[\n]"
[DEBUG] http-outgoing-0 << "            <exclude>.java-version</exclude>[\n]"
[DEBUG] http-outgoing-0 << "          </excludes>[\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>rat-check</id>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>check</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "    </plugins>[\n]"
[DEBUG] http-outgoing-0 << "  </build>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <reporting>[\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-project-info-reports-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <dependencyLocationsEnabled>false</dependencyLocationsEnabled><!-- waiting for MPIR-267 -->[\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        <reportSets>[\n]"
[DEBUG] http-outgoing-0 << "          <reportSet>[\n]"
[DEBUG] http-outgoing-0 << "            <reports>[\n]"
[DEBUG] http-outgoing-0 << "              <report>index</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>summary</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>dependency-info</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>modules</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>team</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>scm</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>issue-management</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>mailing-lists</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>dependency-management</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>dependencies</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>dependency-convergence</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>ci-management</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>plugin-management</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>plugins</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>distr"
Progress (1): maven-parent-32.pom (37/43 kB)
[DEBUG] http-outgoing-0 << "ibution-management</report>[\n]"
[DEBUG] http-outgoing-0 << "            </reports>[\n]"
[DEBUG] http-outgoing-0 << "          </reportSet>[\n]"
[DEBUG] http-outgoing-0 << "        </reportSets>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "    </plugins>[\n]"
[DEBUG] http-outgoing-0 << "  </reporting>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <profiles>[\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\n]"
[DEBUG] http-outgoing-0 << "      <id>jdk-toolchain</id>[\n]"
[DEBUG] http-outgoing-0 << "      <build>[\n]"
[DEBUG] http-outgoing-0 << "        <plugins>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-toolchains-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\n]"
[DEBUG] http-outgoing-0 << "              <toolchains>[\n]"
[DEBUG] http-outgoing-0 << "                <jdk>[\n]"
[DEBUG] http-outgoing-0 << "                  <version>${maven.compiler.target}</version>[\n]"
[DEBUG] http-outgoing-0 << "                </jdk>[\n]"
[DEBUG] http-outgoing-0 << "              </toolchains>[\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\n]"
[DEBUG] http-outgoing-0 << "                  <goal>toolchain</goal>[\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        </plugins>[\n]"
[DEBUG] http-outgoing-0 << "      </build>[\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\n]"
[DEBUG] http-outgoing-0 << "      <id>quality-checks</id>[\n]"
[DEBUG] http-outgoing-0 << "      <activation>[\n]"
[DEBUG] http-outgoing-0 << "        <property>[\n]"
[DEBUG] http-outgoing-0 << "          <name>quality-checks</name>[\n]"
[DEBUG] http-outgoing-0 << "          <value>true</value>[\n]"
[DEBUG] http-outgoing-0 << "        </property>[\n]"
[DEBUG] http-outgoing-0 << "      </activation>[\n]"
[DEBUG] http-outgoing-0 << "      <build>[\n]"
[DEBUG] http-outgoing-0 << "        <plugins>[\n]"
[DEBUG] http-outgoing-0 << "          <!--<plugin>[\n]"
[DEBUG] http-outgoing-0 << "          Clirr needs to be more flexble before we can force this everywhere. New releases that don't have previous artifacts to compare cause Clirr to blow up. And Clirr needs to be smart enough to only look at the previous release artifact so we can make this work during snapshot builds, otherwise it just blows up when you try to do a release.[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.codehaus.mojo</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>clirr-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\n]"
[DEBUG] http-outgoing-0 << "                <id>clirr-check</id>[\n]"
[DEBUG] http-outgoing-0 << "                <phase>verify</phase>[\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\n]"
[DEBUG] http-outgoing-0 << "                  <goal>check</goal>[\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>-->[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-pmd-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\n]"
[DEBUG] http-outgoing-0 << "                <id>cpd-check</id>[\n]"
[DEBUG] http-outgoing-0 << "                <phase>verify</phase>[\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\n]"
[DEBUG] http-outgoing-0 << "                  <goal>cpd-check</goal>[\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        </plugins>[\n]"
[DEBUG] http-outgoing-0 << "      </build>[\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\n]"
[DEBUG] http-outgoing-0 << "      <id>reporting</id>[\n]"
[DEBUG] http-outgoing-0 << "      <reporting>[\n]"
[DEBUG] http-outgoing-0 << "        <plugins>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-project-info-reports-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-surefire-report-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-checkstyle-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <reportSets>[\n]"
[DEBUG] http-outgoing-0 << "              <reportSet>[\n]"
[DEBUG] http-outgoing-0 << "                <id>default</id>[\n]"
[DEBUG] http-outgoing-0 << "                <reports>[\n]"
[DEBUG] http-outgoing-0 << "                  <report>checkstyle</report>[\n]"
[DEBUG] http-outgoing-0 << "                </reports>[\n]"
[DEBUG] http-outgoing-0 << "              </reportSet>[\n]"
[DEBUG] http-outgoing-0 << "            </reportSets>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-pmd-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-jxr-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <reportSets>[\n]"
[DEBUG] http-outgoing-0 << "              <reportSet>[\n]"
[DEBUG] http-outgoing-0 << "                <id>default</id>[\n]"
[DEBUG] http-outgoing-0 << "                <reports>[\n]"
[DEBUG] http-outgoing-0 << "                  <report>jxr</report>[\n]"
[DEBUG] http-outgoing-0 << "                  <report>test-jxr</report>[\n]"
[DEBUG] http-outgoing-0 << "                </reports>[\n]"
[DEBUG] http-outgoing-0 << "              </reportSet>[\n]"
[DEBUG] http-outgoing-0 << "            </reportSets>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <!-- Taglist Plugin must be executed after JXR Plugin -->[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.codehaus.mojo</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>taglist-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\n]"
[DEBUG] http-outgoing-0 << "              <tagListOptions>[\n]"
[DEBUG] http-outgoing-0 << "                <tagClasses>[\n]"
[DEBUG] http-outgoing-0 << "                  <tagClass>[\n]"
[DEBUG] http-outgoing-0 << "                    <displayName>FIXME Work</displayName>[\n]"
[DEBUG] http-outgoing-0 << "                    <tags>[\n]"
[DEBUG] http-outgoing-0 << "                      <tag>[\n]"
[DEBUG] http-outgoing-0 << "       "
Progress (1): maven-parent-32.pom (41/43 kB)
[DEBUG] http-outgoing-0 << "                 <matchString>fixme</matchString>[\n]"
[DEBUG] http-outgoing-0 << "                        <matchType>ignoreCase</matchType>[\n]"
[DEBUG] http-outgoing-0 << "                      </tag>[\n]"
[DEBUG] http-outgoing-0 << "                      <tag>[\n]"
[DEBUG] http-outgoing-0 << "                        <matchString>@fixme</matchString>[\n]"
[DEBUG] http-outgoing-0 << "                        <matchType>ignoreCase</matchType>[\n]"
[DEBUG] http-outgoing-0 << "                      </tag>[\n]"
[DEBUG] http-outgoing-0 << "                    </tags>[\n]"
[DEBUG] http-outgoing-0 << "                  </tagClass>[\n]"
[DEBUG] http-outgoing-0 << "                  <tagClass>[\n]"
[DEBUG] http-outgoing-0 << "                    <displayName>Todo Work</displayName>[\n]"
[DEBUG] http-outgoing-0 << "                    <tags>[\n]"
[DEBUG] http-outgoing-0 << "                      <tag>[\n]"
[DEBUG] http-outgoing-0 << "                        <matchString>todo</matchString>[\n]"
[DEBUG] http-outgoing-0 << "                        <matchType>ignoreCase</matchType>[\n]"
[DEBUG] http-outgoing-0 << "                      </tag>[\n]"
[DEBUG] http-outgoing-0 << "                      <tag>[\n]"
[DEBUG] http-outgoing-0 << "                        <matchString>@todo</matchString>[\n]"
[DEBUG] http-outgoing-0 << "                        <matchType>ignoreCase</matchType>[\n]"
[DEBUG] http-outgoing-0 << "                      </tag>[\n]"
[DEBUG] http-outgoing-0 << "                    </tags>[\n]"
[DEBUG] http-outgoing-0 << "                  </tagClass>[\n]"
[DEBUG] http-outgoing-0 << "                  <tagClass>[\n]"
[DEBUG] http-outgoing-0 << "                    <displayName>Deprecated Work</displayName>[\n]"
[DEBUG] http-outgoing-0 << "                    <tags>[\n]"
[DEBUG] http-outgoing-0 << "                      <tag>[\n]"
[DEBUG] http-outgoing-0 << "                        <matchString>@deprecated</matchString>[\n]"
[DEBUG] http-outgoing-0 << "                        <matchType>ignoreCase</matchType>[\n]"
[DEBUG] http-outgoing-0 << "                      </tag>[\n]"
[DEBUG] http-outgoing-0 << "                    </tags>[\n]"
[DEBUG] http-outgoing-0 << "                  </tagClass>[\n]"
[DEBUG] http-outgoing-0 << "                </tagClasses>[\n]"
[DEBUG] http-outgoing-0 << "              </tagListOptions>[\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-javadoc-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <reportSets>[\n]"
[DEBUG] http-outgoing-0 << "              <reportSet>[\n]"
[DEBUG] http-outgoing-0 << "                <id>default</id>[\n]"
[DEBUG] http-outgoing-0 << "                <reports>[\n]"
[DEBUG] http-outgoing-0 << "                  <report>javadoc</report>[\n]"
[DEBUG] http-outgoing-0 << "                  <report>test-javadoc</report>[\n]"
[DEBUG] http-outgoing-0 << "                </reports>[\n]"
[DEBUG] http-outgoing-0 << "              </reportSet>[\n]"
[DEBUG] http-outgoing-0 << "            </reportSets>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <!--plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.codehaus.mojo</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>clirr-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <version>2.2.2</version>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin-->[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.codehaus.mojo</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>findbugs-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.codehaus.sonar-plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-report</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <version>0.1</version>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        </plugins>[\n]"
[DEBUG] http-outgoing-0 << "      </reporting>[\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\n]"
[DEBUG] http-outgoing-0 << "  </profiles>[\n]"
[DEBUG] http-outgoing-0 << "</project>[\n]"
Progress (1): maven-parent-32.pom (43 kB)   
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/apache/maven/maven-parent/32/maven-parent-32.pom.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/apache/maven/maven-parent/32/maven-parent-32.pom.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/apache/maven/maven-parent/32/maven-parent-32.pom.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "36c73382db81cff81738c40453075d34"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Sun, 15 Jul 2018 18:19:49 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 36c73382db81cff81738c40453075d34[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 51f8daddc2a2bbce81b5134646cbb836402bfb66[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:52 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 1162155[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 37[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251812.146432,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "36c73382db81cff81738c40453075d34"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Sun, 15 Jul 2018 18:19:49 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 36c73382db81cff81738c40453075d34
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 51f8daddc2a2bbce81b5134646cbb836402bfb66
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:52 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 1162155
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 37
[DEBUG] http-outgoing-0 << X-Timer: S1698251812.146432,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "c0c90b7620fadf06be2c7f6ede7d7a54ab2f1606"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
                                         
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/maven-parent/32/maven-parent-32.pom (43 kB at 461 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/apache/maven/maven-parent/32/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/apache/maven/maven-parent/32/maven-parent-32.pom.lastUpdated
[DEBUG] Resolving artifact org.apache:apache:pom:20 from [central (https://repo.maven.apache.org/maven2, default, releases), apache.snapshots (https://repository.apache.org/snapshots, default, snapshots)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/apache/20/apache-20.pom
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/apache/apache/20/apache-20.pom HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/apache/apache/20/apache-20.pom HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/apache/apache/20/apache-20.pom HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 15852[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "8d696f82fa4491021016f5c76f095e6e"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/xml[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Wed, 04 Jul 2018 17:25:32 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 8d696f82fa4491021016f5c76f095e6e[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 4fb8f25e507d8f53ed9f4d79510fc42a2ce92dad[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:52 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 1298394[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 52[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251812.179504,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 15852
[DEBUG] http-outgoing-0 << ETag: "8d696f82fa4491021016f5c76f095e6e"
[DEBUG] http-outgoing-0 << Content-Type: text/xml
[DEBUG] http-outgoing-0 << Last-Modified: Wed, 04 Jul 2018 17:25:32 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 8d696f82fa4491021016f5c76f095e6e
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 4fb8f25e507d8f53ed9f4d79510fc42a2ce92dad
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:52 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 1298394
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 52
[DEBUG] http-outgoing-0 << X-Timer: S1698251812.179504,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "<?xml version="1.0" encoding="UTF-8"?>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "<!--[\n]"
[DEBUG] http-outgoing-0 << "Licensed to the Apache Software Foundation (ASF) under one[\n]"
[DEBUG] http-outgoing-0 << "or more contributor license agreements.  See the NOTICE file[\n]"
[DEBUG] http-outgoing-0 << "distributed with this work for additional information[\n]"
[DEBUG] http-outgoing-0 << "regarding copyright ownership.  The ASF licenses this file[\n]"
[DEBUG] http-outgoing-0 << "to you under the Apache License, Version 2.0 (the[\n]"
[DEBUG] http-outgoing-0 << ""License"); you may not use this file except in compliance[\n]"
[DEBUG] http-outgoing-0 << "with the License.  You may obtain a copy of the License at[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  http://www.apache.org/licenses/LICENSE-2.0[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "Unless required by applicable law or agreed to in writing,[\n]"
[DEBUG] http-outgoing-0 << "software distributed under the License is distributed on an[\n]"
[DEBUG] http-outgoing-0 << ""AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY[\n]"
[DEBUG] http-outgoing-0 << "KIND, either express or implied.  See the License for the[\n]"
[DEBUG] http-outgoing-0 << "specific language governing permissions and limitations[\n]"
[DEBUG] http-outgoing-0 << "under the License.[\n]"
[DEBUG] http-outgoing-0 << "-->[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">[\n]"
[DEBUG] http-outgoing-0 << "  <modelVersion>4.0.0</modelVersion>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <!-- for more information, see the documentation of this POM: http://maven.apache.org/pom/asf/ -->[\n]"
[DEBUG] http-outgoing-0 << "  <groupId>org.apache</groupId>[\n]"
[DEBUG] http-outgoing-0 << "  <artifactId>apache</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "  <version>20</version>[\n]"
[DEBUG] http-outgoing-0 << "  <packaging>pom</packaging>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <name>The Apache Software Foundation</name>[\n]"
[DEBUG] http-outgoing-0 << "  <description>[\n]"
[DEBUG] http-outgoing-0 << "    The Apache Software Foundation provides support for the Apache community of open-source software projects.[\n]"
[DEBUG] http-outgoing-0 << "    The Apache projects are characterized by a collaborative, consensus based development process, an open and[\n]"
[DEBUG] http-outgoing-0 << "    pragmatic software license, and a desire to create high quality software that leads the way in its field.[\n]"
[DEBUG] http-outgoing-0 << "    We consider ourselves not simply a group of projects sharing a server, but rather a community of developers[\n]"
[DEBUG] http-outgoing-0 << "    and users.[\n]"
[DEBUG] http-outgoing-0 << "  </description>[\n]"
[DEBUG] http-outgoing-0 << "  <url>https://www.apache.org/</url>[\n]"
[DEBUG] http-outgoing-0 << "  <organization>[\n]"
[DEBUG] http-outgoing-0 << "    <name>The Apache Software Foundation</name>[\n]"
[DEBUG] http-outgoing-0 << "    <url>https://www.apache.org/</url>[\n]"
[DEBUG] http-outgoing-0 << "  </organization>[\n]"
[DEBUG] http-outgoing-0 << "  <licenses>[\n]"
[DEBUG] http-outgoing-0 << "    <license>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Apache License, Version 2.0</name>[\n]"
[DEBUG] http-outgoing-0 << "      <url>https://www.apache.org/licenses/LICENSE-2.0.txt</url>[\n]"
[DEBUG] http-outgoing-0 << "      <distribution>repo</distribution>[\n]"
[DEBUG] http-outgoing-0 << "    </license>[\n]"
[DEBUG] http-outgoing-0 << "  </licenses>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <mailingLists>[\n]"
[DEBUG] http-outgoing-0 << "    <mailingList>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Apache Announce List</name>[\n]"
[DEBUG] http-outgoing-0 << "      <subscribe>announce-subscribe@apache.org</subscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <unsubscribe>announce-unsubscribe@apache.org</unsubscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <post>announce@apache.org</post>[\n]"
[DEBUG] http-outgoing-0 << "      <archive>https://mail-archives.apache.org/mod_mbox/www-announce/</archive>[\n]"
[DEBUG] http-outgoing-0 << "    </mailingList>[\n]"
[DEBUG] http-outgoing-0 << "  </mailingLists>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <scm>[\n]"
[DEBUG] http-outgoing-0 << "    <connection>scm:git:https://gitbox.apache.org/repos/asf/maven-apache-parent.git</connection>[\n]"
[DEBUG] http-outgoing-0 << "    <developerConnection>scm:git:https://gitbox.apache.org/repos/asf/maven-apache-parent.git</developerConnection>[\n]"
[DEBUG] http-outgoing-0 << "    <url>https://github.com/apache/maven-apache-parent/tree/${project.scm.tag}</url>[\n]"
[DEBUG] http-outgoing-0 << "    <tag>apache-20</tag>[\n]"
[DEBUG] http-outgoing-0 << "  </scm>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <distributionManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <repository>[\n]"
[DEBUG] http-outgoing-0 << "      <id>apache.releases.https</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Apache Release Distribution Repository</name>[\n]"
[DEBUG] http-outgoing-0 << "      <url>https://repository.apache.org/service/local/staging/deploy/maven2</url>[\n]"
[DEBUG] http-outgoing-0 << "    </repository>[\n]"
[DEBUG] http-outgoing-0 << "    <snapshotRepository>[\n]"
[DEBUG] http-outgoing-0 << "      <id>apache.snapshots.https</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>${distMgmtSnapshotsName}</name>[\n]"
[DEBUG] http-outgoing-0 << "      <url>${distMgmtSnapshotsUrl}</url>[\n]"
[DEBUG] http-outgoing-0 << "    </snapshotRepository>[\n]"
[DEBUG] http-outgoing-0 << "  </distributionManagement>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <properties>[\n]"
[DEBUG] http-outgoing-0 << "    <distMgmtSnapshotsName>Apache Development Snapshot Repository</distMgmtSnapshotsName>[\n]"
[DEBUG] http-outgoing-0 << "    <distMgmtSnapshotsUrl>https://repository.apache.org/content/repositories/snapshots</distMgmtSnapshotsUrl>[\n]"
[DEBUG] http-outgoing-0 << "    <organization.logo>https://www.apache.org/images/asf_logo_wide.gif</organization.logo>[\n]"
[DEBUG] http-outgoing-0 << "    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>[\n]"
[DEBUG] http-outgoing-0 << "    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>[\n]"
[DEBUG] http-outgoing-0 << "    <sourceReleaseAssemblyDescriptor>source-release</sourceReleaseAssemblyDescriptor>[\n]"
[DEBUG] http-outgoing-0 << "    <gpg.useagent>true</gpg.useagent>[\n]"
[DEBUG] http-outgoing-0 << "    <arguments />[\n]"
[DEBUG] http-outgoing-0 << "    <maven.compiler.source>1.7</maven.compiler.source>[\n]"
[DEBUG] http-outgoing-0 << "    <maven.compiler.target>1.7</maven.compiler.target>[\n]"
[DEBUG] http-outgoing-0 << "    <surefire.version>2.22.0</surefire.version>[\n]"
[DEBUG] http-outgoing-0 << "    <assembly.tarLongFile"
Progress (1): apache-20.pom (4.1/16 kB)
[DEBUG] http-outgoing-0 << "Mode>posix</assembly.tarLongFileMode>[\n]"
[DEBUG] http-outgoing-0 << "  </properties>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <repositories>[\n]"
[DEBUG] http-outgoing-0 << "    <repository>[\n]"
[DEBUG] http-outgoing-0 << "      <id>apache.snapshots</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Apache Snapshot Repository</name>[\n]"
[DEBUG] http-outgoing-0 << "      <url>https://repository.apache.org/snapshots</url>[\n]"
[DEBUG] http-outgoing-0 << "      <releases>[\n]"
[DEBUG] http-outgoing-0 << "        <enabled>false</enabled>[\n]"
[DEBUG] http-outgoing-0 << "      </releases>[\n]"
[DEBUG] http-outgoing-0 << "    </repository>[\n]"
[DEBUG] http-outgoing-0 << "  </repositories>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <build>[\n]"
[DEBUG] http-outgoing-0 << "    <pluginManagement>[\n]"
[DEBUG] http-outgoing-0 << "      <plugins>[\n]"
[DEBUG] http-outgoing-0 << "        <!-- set versions of common plugins for reproducibility, ordered alphabetically -->[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-antrun-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>1.8</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-assembly-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.0.0</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-clean-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.1.0</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-compiler-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.7.0</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-dependency-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.1.1</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-deploy-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>2.8.2</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-docck-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>1.1</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-ear-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.0.1</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-enforcer-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>1.4.1</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-failsafe-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${surefire.version}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-gpg-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>1.6</version>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <gpgArguments>[\n]"
[DEBUG] http-outgoing-0 << "              <arg>--digest-algo=SHA512</arg>[\n]"
[DEBUG] http-outgoing-0 << "            </gpgArguments>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-help-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.1.0</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-install-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>2.5.2</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-invoker-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.1.0</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-jar-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.1.0</version>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <archive>[\n]"
[DEBUG] http-outgoing-0 << "              <manifest>[\n]"
[DEBUG] http-outgoing-0 << "                <addDefaultSpecificationEntries>true</addDefaultSpecificationEntries>[\n]"
[DEBUG] http-outgoing-0 << "                <addDefaultImplementationEntries>true</addDefaultImplementationEntries>[\n]"
[DEBUG] http-outgoing-0 << "              </manifest>[\n]"
[DEBUG] http-outgoing-0 << "            </archive>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-javadoc-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.0.1</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-plugin-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.5.2</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>mav"
Progress (1): apache-20.pom (8.2/16 kB)
[DEBUG] http-outgoing-0 << "en-project-info-reports-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.0.0</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <!-- START SNIPPET: release-plugin-configuration -->[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-release-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>2.5.3</version>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <useReleaseProfile>false</useReleaseProfile>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>deploy</goals>[\n]"
[DEBUG] http-outgoing-0 << "            <arguments>-Papache-release ${arguments}</arguments>[\n]"
[DEBUG] http-outgoing-0 << "            <waitBeforeTagging>10</waitBeforeTagging>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <!-- END SNIPPET: release-plugin-configuration -->[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-remote-resources-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>1.5</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-resources-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.1.0</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-scm-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>1.9.5</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-scm-publish-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.0.0</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-site-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.7.1</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-source-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.0.1</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-surefire-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${surefire.version}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-surefire-report-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${surefire.version}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-war-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.2.2</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-shade-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.1.1</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.rat</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>apache-rat-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>0.12</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.mojo</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>clirr-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>2.8</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      </plugins>[\n]"
[DEBUG] http-outgoing-0 << "    </pluginManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\n]"
[DEBUG] http-outgoing-0 << "      <!-- We want to package up license resources in the JARs produced -->[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-remote-resources-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>process-resource-bundles</id>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>process</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\n]"
[DEBUG] http-outgoing-0 << "              <resourceBundles>[\n]"
[DEBUG] http-outgoing-0 << "                <resourceBundle>org.apache:apache-jar-resource-bundle:1.4</resourceBundle>[\n]"
[DEBUG] http-outgoing-0 << "              </resourceBundles>[\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-enforcer-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>enforce-maven-version</id>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>enforce</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\n]"
[DEBUG] http-outgoing-0 << "              <rules>[\n]"
[DEBUG] http-outgoing-0 << "                <requireMavenVersion>[\n]"
[DEBUG] http-outgoing-0 << "                  <version>3.0.5</version>[\n]"
[DEBUG] http-outgoing-0 << "                </requireMavenVersion>[\n]"
[DEBUG] http-outgoing-0 << "              </rules>[\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin"
Progress (1): apache-20.pom (12/16 kB) 
[DEBUG] http-outgoing-0 << ">[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-site-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>attach-descriptor</id>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>attach-descriptor</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "    </plugins>[\n]"
[DEBUG] http-outgoing-0 << "  </build>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <profiles>[\n]"
[DEBUG] http-outgoing-0 << "    <!-- START SNIPPET: release-profile -->[\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\n]"
[DEBUG] http-outgoing-0 << "      <id>apache-release</id>[\n]"
[DEBUG] http-outgoing-0 << "      <build>[\n]"
[DEBUG] http-outgoing-0 << "        <plugins>[\n]"
[DEBUG] http-outgoing-0 << "          <!-- Create a source-release artifact that contains the fully buildable[\n]"
[DEBUG] http-outgoing-0 << "               project directory source structure. This is the artifact which is[\n]"
[DEBUG] http-outgoing-0 << "               the official subject of any release vote. -->[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-assembly-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <dependencies>[\n]"
[DEBUG] http-outgoing-0 << "              <dependency>[\n]"
[DEBUG] http-outgoing-0 << "                <groupId>org.apache.apache.resources</groupId>[\n]"
[DEBUG] http-outgoing-0 << "                <artifactId>apache-source-release-assembly-descriptor</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                <version>1.0.6</version>[\n]"
[DEBUG] http-outgoing-0 << "              </dependency>[\n]"
[DEBUG] http-outgoing-0 << "            </dependencies>[\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\n]"
[DEBUG] http-outgoing-0 << "                <id>source-release-assembly</id>[\n]"
[DEBUG] http-outgoing-0 << "                <phase>package</phase>[\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\n]"
[DEBUG] http-outgoing-0 << "                  <goal>single</goal>[\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\n]"
[DEBUG] http-outgoing-0 << "                <configuration>[\n]"
[DEBUG] http-outgoing-0 << "                  <runOnlyAtExecutionRoot>true</runOnlyAtExecutionRoot>[\n]"
[DEBUG] http-outgoing-0 << "                  <descriptorRefs>[\n]"
[DEBUG] http-outgoing-0 << "                    <descriptorRef>${sourceReleaseAssemblyDescriptor}</descriptorRef>[\n]"
[DEBUG] http-outgoing-0 << "                  </descriptorRefs>[\n]"
[DEBUG] http-outgoing-0 << "                  <tarLongFileMode>posix</tarLongFileMode>[\n]"
[DEBUG] http-outgoing-0 << "                </configuration>[\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <!-- We want to deploy the artifact to a staging location for perusal -->[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <inherited>true</inherited>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-deploy-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\n]"
[DEBUG] http-outgoing-0 << "              <updateReleaseInfo>true</updateReleaseInfo>[\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-source-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\n]"
[DEBUG] http-outgoing-0 << "                <id>attach-sources</id>[\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\n]"
[DEBUG] http-outgoing-0 << "                  <goal>jar-no-fork</goal>[\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-javadoc-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\n]"
[DEBUG] http-outgoing-0 << "                <id>attach-javadocs</id>[\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\n]"
[DEBUG] http-outgoing-0 << "                  <goal>jar</goal>[\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <!-- We want to sign the artifact, the POM, and all attached artifacts -->[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-gpg-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\n]"
[DEBUG] http-outgoing-0 << "                <id>sign-release-artifacts</id>[\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\n]"
[DEBUG] http-outgoing-0 << "                  <goal>sign</goal>[\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        </plugins>[\n]"
[DEBUG] http-outgoing-0 << "      </build>[\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\n]"
[DEBUG] http-outgoing-0 << "    <!-- END SNIPPET: release-profile -->[\n]"
[DEBUG] http-outgoing-0 << "  </profiles>[\n]"
[DEBUG] http-outgoing-0 << "</project>[\n]"
Progress (1): apache-20.pom (16 kB)   
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/apache/apache/20/apache-20.pom.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/apache/apache/20/apache-20.pom.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/apache/apache/20/apache-20.pom.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "92bd2414cb36e9088eb249d3518dd78f"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Wed, 04 Jul 2018 17:25:32 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 92bd2414cb36e9088eb249d3518dd78f[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 94b7c468380f120d3c71e8b818959e19cf684c77[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:52 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 1302564[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 39[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251812.203957,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "92bd2414cb36e9088eb249d3518dd78f"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Wed, 04 Jul 2018 17:25:32 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 92bd2414cb36e9088eb249d3518dd78f
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 94b7c468380f120d3c71e8b818959e19cf684c77
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:52 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 1302564
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 39
[DEBUG] http-outgoing-0 << X-Timer: S1698251812.203957,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "4fb8f25e507d8f53ed9f4d79510fc42a2ce92dad"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
                                   
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/apache/20/apache-20.pom (16 kB at 453 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/apache/apache/20/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/apache/apache/20/apache-20.pom.lastUpdated
[DEBUG] Resolving artifact org.apache.maven.plugins:maven-compiler-plugin:jar:3.8.0 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/maven-compiler-plugin-3.8.0.jar
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/maven-compiler-plugin-3.8.0.jar HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/maven-compiler-plugin-3.8.0.jar HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/maven-compiler-plugin-3.8.0.jar HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 61935[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "5a0d698e5eb10fe2ac2c57fad3cc5fdf"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: application/java-archive[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Thu, 26 Jul 2018 17:14:36 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 5a0d698e5eb10fe2ac2c57fad3cc5fdf[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: a24b0fda131f69df4cbedbff79004a29b275c6e8[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:52 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 604678[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 42[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251812.257443,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 61935
[DEBUG] http-outgoing-0 << ETag: "5a0d698e5eb10fe2ac2c57fad3cc5fdf"
[DEBUG] http-outgoing-0 << Content-Type: application/java-archive
[DEBUG] http-outgoing-0 << Last-Modified: Thu, 26 Jul 2018 17:14:36 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 5a0d698e5eb10fe2ac2c57fad3cc5fdf
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: a24b0fda131f69df4cbedbff79004a29b275c6e8
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:52 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 604678
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 42
[DEBUG] http-outgoing-0 << X-Timer: S1698251812.257443,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "PK[0x3][0x4][\n]"
[DEBUG] http-outgoing-0 << "[0x0][0x8][0x8][0x8][0x0][0x87][0x99][0xfa]L[0x0]"[N[0xe9][0x0][0x0][0x0][0xf2][0x1][0x0][0x0][0x14][0x0][0x0][0x0]META-INF/MANIFEST.MF[0x9d][0x90][0xdd]J[0x3]1[0x10][0x85][0xef][0x3]y[0x87]y[0x81]d][0xda][0xa2][0xe4][0xce][0x16][0x84]J[0xb][0xc5]Vo%lf[0xdb][0xd0][0xfc][0x91][0xcd][0xb6][0xf8][0xf6]nvWE[0x17][0x11][0xbc][0x9d][0x99]s[0xe6];g+[0x9d][0xae][0xb1]I[0xec][0x5]c[0xa3][0xbd][0x13]P[0xf2][0x1b]J[0xd6]6[0x18][0xb4][0xe8][0x92]L[0xdd][0x90][0x1d]t2([0xe0]>[0xc8][0xea][0x84][0xb0][0x95][0x17]t[0xb0][0xf2]6h[0x83][0x11]v[0xa6]=j7[0xd1]|[0x1a][0xce][0xf8]][0xb6]\[0xb6][0xda]$[0xb6]|[0x13]p[0x96]VR[0xb2][0xf]X[0xe9]ZW[0x1f][0xd7]N[0xf9]([0xe0][0xd0][0xf9][0x8f]o[0xf6][0xbe]NW[0x19][0x11][0x1e]|[0xeb]T[0xf6]S[0xf5]?[0xae][0xfc][0x89][0xad][0x95][0x0][0x1f][0x8f]\[0xf6]Zn[0xb3][0x96][0x87]^[0xd2]P[0xb2][0x8a]([0x13][0xaa][0x1e][0xf7][0x9b][0xfb][0x8c]/[0xf8]|[0x8][0xa3][0xd8][0xa3]:[0xe7][0xbe][0xba]x[0xaf][0xe5]m9[0x8d][0xf4]U[0xc0][0x84][0xe2][0xf9]i#[0xe0][0x94]RhDQ[0xc][0xcf]G[0x92][0xe][0xaa][0x18]9[0x86][0x5][0xab][0xc6]D,[0x18]J o[0x8a]_R[0xfd][0xdd][0x1f]%[0xef]PK[0x7][0x8][0x0]"[N[0xe9][0x0][0x0][0x0][0xf2][0x1][0x0][0x0]PK[0x3][0x4][\n]"
[DEBUG] http-outgoing-0 << "[0x0][0x0][0x8][0x0][0x0][0x87][0x99][0xfa]L[0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x9][0x0][0x0][0x0]META-INF/PK[0x3][0x4][\n]"
[DEBUG] http-outgoing-0 << "[0x0][0x0][0x8][0x0][0x0][0x85][0x99][0xfa]L[0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0xf][0x0][0x0][0x0]META-INF/maven/PK[0x3][0x4][\n]"
[DEBUG] http-outgoing-0 << "[0x0][0x0][0x8][0x0][0x0][0x85][0x99][0xfa]L[0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0]([0x0][0x0][0x0]META-INF/maven/org.apache.maven.plugins/PK[0x3][0x4][\n]"
[DEBUG] http-outgoing-0 << "[0x0][0x0][0x8][0x0][0x0][0x85][0x99][0xfa]L[0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0]>[0x0][0x0][0x0]META-INF/maven/org.apache.maven.plugins/maven-compiler-plugin/PK[0x3][0x4][\n]"
[DEBUG] http-outgoing-0 << "[0x0][0x0][0x8][0x0][0x0][0x84][0x99][0xfa]L[0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x4][0x0][0x0][0x0]org/PK[0x3][0x4][\n]"
[DEBUG] http-outgoing-0 << "[0x0][0x0][0x8][0x0][0x0][0x84][0x99][0xfa]L[0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0xb][0x0][0x0][0x0]org/apache/PK[0x3][0x4][\n]"
[DEBUG] http-outgoing-0 << "[0x0][0x0][0x8][0x0][0x0][0x84][0x99][0xfa]L[0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x11][0x0][0x0][0x0]org/apache/maven/PK[0x3][0x4][\n]"
[DEBUG] http-outgoing-0 << "[0x0][0x0][0x8][0x0][0x0][0x84][0x99][0xfa]L[0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x18][0x0][0x0][0x0]org/apache/maven/plugin/PK[0x3][0x4][\n]"
[DEBUG] http-outgoing-0 << "[0x0][0x0][0x8][0x0][0x0][0x85][0x99][0xfa]L[0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0]![0x0][0x0][0x0]org/apache/maven/plugin/compiler/PK[0x3][0x4][\n]"
[DEBUG] http-outgoing-0 << "[0x0][0x8][0x8][0x8][0x0][0x84][0x99][0xfa]LA[0xa5][0xbc]>([0x6][0x0][0x0]z%[0x0][0x0][0x15][0x0][0x0][0x0]META-INF/DEPENDENCIES[0xcd]YYo[0xdb]8[0x10]~[0xcf][0xaf][0xe0][0xc3][0x2]m[0x80]J[0x8a][0x93][0xa6][0xd9][0xea][0xcd]M[0xf]8[0xa8][0x1b]o[0xdd]=[0xde][\n]"
[DEBUG] http-outgoing-0 << "Fbl[0xa6][0x12][0xa9]%[0xa9]8[0xd9]_[0xbf][0xc3]K[0x87]O[0x5][0x8d][0xad][0x6][0x88]-q[0xbe][0xa1][0xe6][0x1b][\r][0x87]3t[0x14][0xa1][0xe0][0xa7][0xff][0x8e][0xa2][0x8]}[0x13][0x98]I[0xaa][0xe8]=A))[0x8]K[0x9]K([0x91][0x88][0xdf]"5[0xa7][0x12][0x15][0x82][0xdf][0x91]D[0x81]P[0x11][0x91]SFRt+x[0xe]B[0xa2][0xd5]s|O[0x18]*`[0x80][0x8b][0x19]f[0xf4]?[0x90][0xdf]<[0xfa][0x1b][0xac](g[0xa1][0xc6]=[0x83][0xb1]G[0xc3][0x2]'s[0x82][0xc6][0xe6][0x91][0x97]</hF[0x4][0x9a]d[0xe5][0x8c][0xb2][0xa3][0xa3][0xa3][0x8f]`V[0x8c]^`[0x86]J[0xf6][0x83][0xf1][0x5]kY[0xf1][0xe2][0x8][0xa1][0x0][0xfd][0xf1][0x9e]?[0xa0][0x97]s[0xa5][\n]"
[DEBUG] http-outgoing-0 << "[0x19]G[0xd1][0x8c][0xaa]yy[0x13]&<[0x8f][\n]"
[DEBUG] http-outgoing-0 << "\f[0xc1][0x1c][0xe7]9f*[0xfa]7[0xe5][0xf][0xc7][0x8][0x86]C5[0xe7][0xe5]l[0xae][0x16]\[0xfc][0x90][0xa1][0x1e][0x8e][0xcd][0xc7][0x1d][0x16][0xf1]ix[0x12][0x8c][0xdf][0xc2][0xac][0x8]}[0xa6][0x9]a[0x92][0xc4][0xe8][0x1b][0x98][0xe7][0xac][0x9c][0xf2][[0xb5][0xc0][0x82]x[0xe1]+[0xf4][0x17][0x11][0x12][0xc]A[0xa0][0x88][0xac][\r]`[0xc2]b[0xb1][0x8][0xb1][0xd1][0x8][0xc1][0xda]([0xb3]`[0x19]}[0x1e]]~[0xf8]2[0xfd][0x10][0x0]8T[0xf][0xea][0xd8]X?%[0xa2][0xa8]4%[0xdc][0x84][0x92][0x97]"![0xb7][0xa0]JBF[0xd4]1[0x82][0x8f][0xd6][0xa0]F[0xc5][0xe6]C[0xdb]<[0x8][0x7][0xe7][0xe1][0xa0]m[0xf3][0xbb][0xe9]{0[0xc7]=[0xaf]~[0xd6]$#[0xf][0xa5]D[0x97][0xb4][0x98][0x13][0x11]#[0x88][\n]"
[DEBUG] http-outgoing-0 << "[0xf1]XhGF)[0xf1][0x97][0xe6]%pF[0x98][0xaa][0xad]*`^[0xb0][0x80]a[0xf5]XXN[0x85][0x99])H[0xcc]L[0xc7][0xfa][0xa5][0xd4]r+[0x8b][[0x10]g[0xe9][0xeb][0xb6][0x99][0xce][0xad][0x93][0xf2][0x6]|[0xe4][0x87][0x9f][0xec][0xcb][0x16][0xb7])IJA[0xd5]#zOe[0x81][0x15][0xe8][0x89]'1[0x92]$[0x9][0xd2]Ju+[0xb3]6[0xd4]1<[0xdb][0x1f][0xc3])[0x95]%|}*[0x1]Qi[0xbb][0x88][0xf0][0x16][0xea][0xb0][0x97][0x80][0xb]f[0x1a][0x15]-[0xd9][0xaf]%q-[0x8e]a[0x89]C[0xc8][0xf][0xc2][0x8b][0x83]E[0xbc]_[0xd0][0xcb][0xd3][0xe4]%K[0xed][0x9a][0xde]0[0xa9][0xf5][0xc1][0xd5]x[0x8a] [0xe0]+[0xcc][0x8c][0x8][0xce]h[0xce][0x9b]O[0x97][0x5]Id%[0x9][0xee]r[0xf9][0x1d]T[0xbe][0xeb]a[0xeb][0xf][0x87][0xad]t[0x8d]B[0xbc]V[0xc1][0xaf][0xb0][0xe5][0x5][0xb6][0xe7][0xa4]p[0xf5]m[0xf8]d[0x9a][\n]"
[DEBUG] http-outgoing-0 << "?[0x91]fC[0xa1]'[0x9a][0xd7][0xb0]7]M[0x86]h8[0x9b][0x9]2[0xc3][0x8a][0xa0]+,[0xaa][0xa9]8H[0xef][\n]"
[DEBUG] http-outgoing-0 << "[0xdc][0x9c][0xce][\r][0xb5][0xf8][0xb9][0xb1][0xd8][0xdf]@$e[0x4][0xe2][0xfa]ux[0xba]v1.3[0xf9][0xc9]P[0xbe][0xe4])[0x99][0xe3]R[0xb6][0x3]7q[0xa3]f[0x92]v[0xf6][0xcd][0xb0][0x94][0xb0][0xf9]d[0xa9][0xac]4lFi+[0xf9][0xfc]Y[0xc3][0xdd]r[0xae]PK[0x89][0xb6][0x6][0xba][0xbd][0xec]t9[0x1b][0xed][0xf9]u:[0x82]#[0x6]UE[0xc1]3[0xb3][0x9c][0xd1]p2[0xea]D[0xd3][0xe7]h[0xe9]Ghs[0x9a][0xad][0xcc][H[0x1f][0xca][0xaf][0xfb][0xa0][0xe];M[0xe][0xf3][0xfc][0xa9]h[0x6][0xe5][0x17][0xe9][0xf4][0x82]K[0x0][0xcb][0xad][0xfc][0xc][0xc2][0xd7]'[0xcb]{[0xe8][0x1][0x92][0xb5][0x8f]p[0xc7][0xb2][0xe]too[0xe0][0xe8][0xb9][0xda][0x8b][0xf2]h[0xd9]+[0xb6][0xac][0x1b][0x16][0xb4][0x83][0xee][0xc6][0x88]H[0xdc]<[0xcb][0xf7][0x1].[0xe8][0x8e][0xb5][0xd1]@:G[0xfe][0xbe][0xa1][0x18]y[0xfe][0xa0][0xb8][0xc2][0xf7]8YS[0x84]<[0xa3][0x7]V[0x0][0xc1][0x9d]~hG[0xa7][0x18][0xec][0xc1][0xdd]RE[0xc5][0x18]3[0xc][0xbb][0xd2]^"#[0xb7]swt[0x84]C[0x1f][0xdc][0x15]q[0xdc][0x8][0x8f]!c\[0x99]T&[0x9f][0xe2][0x12][0xa6]04r[0xed]H0[0x13][0x6][0xb8][0x9e]p[0xb7][0x1f]VT\>[0xbd]X.[\r][0xf6][0xe6][0x8c][0xcf][0x98][0xcd]Jx[\r][0xc6]-z[0xed]t[0xf7]B[0xe6]U[0xfd][0x80][0x8e][0xec][0xed][0x9c]5[0xc2]P<[0x9][0xdf][0x86][0x83][0x93]}r[0xf4][0xe9][0xf4][0xfa][0xef][0xd3]v[0xad][0xc0][0x17][0xa7][0x8d]2[0x1][0xcb][0xbc][0x92][0xc2]u-54[0xf4][0x1d][0x8c][0xc6][0xfa]_[0x9b][0xfd]f[0xb9][0xc8][0xb1][0xad][0xdf][0x1a]}gX8WyV[0x1b]3u[0xbd][0xc1]+[0xd8][0xb6][0x93][0xb0]mW[0xb3][0xab][0xb0][0xb6][\r][0x89][0xd2][\r][0x15][0xbc][0x98][0xe6][0xb6][0x8e][0xcd]h[0xbb][0x9d][0xb2]c[0x8d][0xcc]\I[0xad]$[0xae][0x1]>[0xc2][0xe][0xba]a[0xd7]LF9[0x84]B[0xe]Qo[0xeb][0x95][0xe][0xa4](hlg[0xa5][0x11]=[0xd3][0x9a]v{Ar[0xd7][0xb][0x92][0xbd][0xbf][0xa0][0xd5]jj[0xb][0x1f]]+m'[0xa4][0x11][0xbd]0r[0xfd][0xfb][0x88][0x99][0xb3][0xb7][0x97]W[0xd3][0xaf]gg'[0xe8][0x86]`[0x86]dY[0x14]\[0xa8][0xe3][0xfa][0x80][0x2][0xb0]m~[0xa6]m[0xa7]F72[0xdd]{[0xa0]5[0x9b][0xc3]v`c[0xcb][0xdf][0x0][0xf9][0x1e]i[0xb0][0xda]#[0x1d][0xd6][0x7].[0xe5][0xff][0xa4][0xf][0xec][0xa5]M[0xe8]-[0x87][0xb8][0xa1]].q;B/N[0xf1]yx[0xdd]#[0xba][0x1e][0x8a]8[0xbd]O[0xae][0xb3]G[0x10]Y[0xc1][0xd9][0xe0][0x2][0xe9][0xd6]Z[5[0x85][0xe][0xbf][0x95][0xb0][0xd7][0x1d]'[0x98]C[0xdf][0x95]C[0x85][0x2][0x87][0x19][0xcc][0x19]A[0x4]MU[0xb7][0x83][0x85][0x86]R[0xed][0xd4][0xc3][0x9e],[0xb8][0xa9][0xff]y[0xa7]#KoW[0xd3]1:Gr[0x8e]S[0x92][0xa2][0x97][0x82][0x80][0xf8][0x7][0x14][\n]"
[DEBUG] http-outgoing-0 << "[0xe9]q[0x7][0x9f]<[0x98] ;[0xb][0x7][0x17][0xf6]2[0x80]][0xf5]<[0xb0]s[0xb5][0x1c]b[0xa4][0xf1][\n]"
[DEBUG] http-outgoing-0 << "[0xc6][0xfb]@[0xcf][0xd0]v[0xc2]o~Of8'[0x1d]v[0xed].[0xca][0x7][0x8a][0xbc][0xa8][0xe5]g[0xdb][0xf7][0xea][0x9a]>[0xcb]`E-[0xd5][0xae]F[0xd6][0xd4]MjXd[0xce][0xe4]5 h[0x8c][0xc6][0xeb][0xc6]t[0xce]>[0xb]O7[0x9c][0xea]<[0xf0]xR[0xa3][0xeb]m\[\n]"
[DEBUG] http-outgoing-0 << "[0xc1][0xb]h0[0xbc][0xc1][0xba][0xf7]E[0xf5]M[0xdc][0xb8][0xb4]][0xc5][0xf9][0xbe][0xad][0xf7]f[0xeb]:z[0x9b][0xe1][0xba]Xn[0xd8][0xaa]o[0xe3][0xd6][0x8d][0xb5][0xf7][0xcd]A[0xd7][0xad]7~[0xc2]y[0xb6][0xd5][0xeb] o[0x18][0xaf]o[0xe3][0xd6][0x8d][0xdd][0xe0][0xf]k[0xbc][0xfd][0x11][0xcb][0x15].[0x13][0xc1][0xef]i[0xda]hkM2Y[0xc9].[0x81][0xab]J[\n]"
[DEBUG] http-outgoing-0 << "[0x7][0x8f]Z[0x19][0xc5]@[0xe2][0xb5]@[0xb7][0x1c]N[0xfa]`([0x14][0xbd][0xc5][0x89][0xda]I[0xcd][0xe1][0xb6]pr[0x88][0x1e][0xc9]\rAv[0x11]I[0x0][0xb3][0x99][0x84][0x96][0xf6]H`[0xc][0x8d]m[0xb6][0x8b]A[0xae]A[0x9b])[0x18]q[0xef][0x1c][0xde][0x95]4[0xeb][0xb0]b[0x8c][0xb1][0xc1][0x8d][0x5][0xef][0xe0][0xe4]a=r[0xb3][0xbf]e[0xb7][0xea][0xaf][\r][0xc4][\n]"
[DEBUG] http-outgoing-0 << "[0x83]l[0xf4][0xcc]kX[0xd5][0x98][0x1e])}%[0x5][0x97]Tq[0xf1][0x88][0xc6]Da[0xa8][0x14]p[0xb7]0[0x14][0x95]b[0x90];[0xc5][0xcd]T[0xd7][0x80]{[0xe4]<%JQ6[0x93][0xbb](J[0x87][0xdb][0xcc][0xcb]#~[0x5]2[0x1d][0x97][0x9c]7y[0xf7][0xaa][F[0xf6]Hr[0xc4][0x12]a[0xf]v\j[0xf1]-&*WN[0x14]VxC[0xe9].H[0xea][0xe9][0x9b][0x1b]h[0x17][0xab][0x9][0xd7]8 [0xb4][0xa8]x[0x93][0x8a][0xff]a[0xea][0xd7]l-[0xe5][0x8e][\n]"
[DEBUG] http-outgoing-0 << "[0xdf][0xc5][0x8d]aeNdd[0xad][0xd7][0xc9]y[0xe6]G[0xac][0xae]n[0xab][0xf1][0xd2][0x5][0xff][0x93]O[0x9e][0xd7][0x90][0xd9][0xe2]&[0xf8][0xfb][0x1f]PK[0x7][0x8]A[0xa5][0xbc]>([0x6][0x0][0x0]z%[0x0][0x0]PK[0x3][0x4][\n]"
[DEBUG] http-outgoing-0 << "[0x0][0x8][0x8][0x8][0x0][0x84][0x99][0xfa]L[0xb5][0x1][0xcc][0xad]yD[0x0][0x0]*[0xa1][0x0][0x0];[0x0][0x0][0x0]org/apache/maven/plugin/compiler/AbstractCompilerMojo.class[0xd5]}[0x7]|TU[0xf6][0xf0]y[0xb7][0xbc][0xf7]f[0xe6][0xa5][0x17][0x18][0x9a]C/i[0x82][0x80][0x12][0x8a][0x6][0x8][0x10]L[0x2][0x12][0x8a][0xa0][0x82]C2[0x90][0x81]$[0x13]3[0x9][0xc5][0xb6][0xb6][0xb5][0xad]uuU[0xd0][0xd5][0xb5]b[0xc1].[0x81][0x88][0x88][0xac][0x8a]b[0xdb][0xb5][0xac]e[0xed]e-[0xab][0xae]}UD[0xf3][0x9d]s[0xdf][0x9b][0x96]L [0xc0][0xff][0xff][0xfd][0xbe][0x8f]5[0xef][0xdd]w[0xef][0xb9][0xe7][0x9e]{[0xce][0xb9][0xe7][0x9c][0xdb]f[0x9f][0xf9][0xed][0xe1]G[0x1][0xe0]0[0xdd][0xeb][0xd6][0x9e][0xe4]G[0xba][0xb4][0x87][0xf9]Q[0xf4](1[0xf9]dzO[0xa1][0xc7]Tz[0x94][0xba][0xf1]1[0xcd][0xe0][0xd3][0xdd][0xe0][0xe2]G[0x9a]|[0x6][0xbd][0xcb][0xa8]d[0xa6][0xc9][0x8f][0xa6][0x8f]r[0xf][0xaf][0xe0][0x95][0x94]3[0xcb][0xc3]g[0xf3]c[0xc]>[0xc7][0xe0]U&[0x9f][0xeb][0x86],>[0xcf][\r][0xd9]|>[0xe1]X`[0xf2]c[0x9]|![0x81]/"[0xf0][0xe3]L~<}[0x9c]@[0x1f][0x8b]=[0xda]R[0xbe][0x84][0x0]O[0xa4][0x87][0xdf][0xa3][0x9d][0xc8][0x97][0x9a][0xbc][0x9a][0xca]j[0xdc]<[0xc0][0x97][0x99]|[0xb9][0x87][0xd7][0xf2][0xa0][0xc9]W[0xd0]{%[0x81][0xd5][0xb9][0xb5]2^n[0xf0]z7[0x14][0xf0]#)[0xa7][0xc1][\r][0x9b]x[0x88]>[0x1b][0xa9][0xea]I[0x94]j[0xa2]G[0x98]>[0x9b][0xdd][0xbc][0x85][0xaf]2[0xf9]jz[0xaf]1[0xf9]Z7[0xec][0xe0]'[0x9b][0xfc][0x14][0x93][0x9f]jj[0xff]6[0xf8]i&?[0xdd][0xe4][0xbf]s[0xc3]$[0xa4][0x1a]k[0x9d]A[0xb5][0xce][0xa4][0xd4]Y[0x94]:[0x9b]R[0xe7]P[0xea][0xf7][0x94]:[0x97]R[0xe7]Q[0xea]|J]@[0xa9][0xb][0x89][0x8a]?P[0xea]"J]L[0xa9]K(u)[0xa5].#[0xb8][0xcb])[0xf5]G[0xca][0xbb][0x82][0xa8][0xbd][0x92]>[0xff]D[0x1d][0xbe][0x8a][0xf2][0xae]6[0xf9]5&_[0xe7][0xe6][0xeb][0xf9][0xb5][0x94]y[0x9d][0xc9][0xff]L[0xe4]^O97[0x98][0xfc]/&[0xbf][0x91][0x92]'[0xd2][0xe3]&[0x93][0xdf]L[0xd5]o![0xbc][0xb7]R[0xea]6[0xc2][0xb1][0x81]R[0xb7]S[0xde][0x1d][0x94][0xba][0x93][0x1e]w[0xd1][0xe7]F[0xc2]t7[0x81][0xdc]Cy[0xf7][0x9a][0xfc]>[0x93][0xdf]O%[0xf][0xd0][0xf7][0x83]&[0x88][0xde][0x9b]([0xa7][0x95]R[0x9b])[0xb5][0x85]Rm[0x94]z[0x98]R[)[0xf5][0x8][0xa5][0xb6][0xb9][0xf8][0xa3]|[0xbb][0x1b][0x1f][0x8f][0x11]'wP[0xde]_)[0xef]qj[0xe2][0x9][0x82]{[0xd2][0xe4];M[0xfe][0x94][0x9b]?[0xcd]w[0x99][0xfc][0x19]*|[0xd6][0xe4][0xcf][0x11][0xe4][0xf3]T[0xfe][0x2][0xa5][0xfe]f[0xf0][0xbf][0xd3][0xc7][0x8b][0xf4]x[0xc9][\r][0x97][0xf1][0x97]M[0xfe][0x8a][0x87][0xff][0x83][0xbf]J[0x88]^[0xa3][0xc7][0xeb][0x6][0xc3][\r][0xd7][0xa0][0x98][0xf1][0xf1]O*{[0xd3][0xc5][0xdf][0xe2]o[0xbb][0xf1][0xb1][0x83][0x0][0xde][0xa1][0xc7][0xbb]T[0xf9]=[0x93][0xbf]Oh?0[0xf9][0x87][0x6][0xff][0xc8][0xe4][0xff]"5[0xfc][0xd8][0xe4][0x9f][0x10][0xfe]O[0x9][0xee]3z[0xfc][0xdb][0xe4][0x9f][0x13][0xa2]/[0xe8][0xe3]K[0xf][0xff][0xf][0xa9][0xdc]W[0xf4][0xfe][0x9a]T[0xf5][0x1b]J][0xe7][0xe1][0xdf][0xf2][0xef][0xe8][0xf1][0xbd][0xc1] y[0xfd][0x97][0xa0]$[0xfc]?Q[0xea]gJ[0xed][0xa6][0xd4]/[0xf4][0xd8]CM[0xfc][0xea][0xd1][0x16][0xf3][0xdf][0xc][0xde][0xee][0x81][0xed][0x2][0xc][0xa1][0xd1][0x9b][0x99][0x82]#[0x97][0x84]0[0x85]4[0x85][0x8e][0xd5][0x84][0x81][0xfa]/LS[0xb8]L[0xe1]6[0x85][0xc7][0x14][0x16][0xe5][0xa6][0x98]"[0x15][0xc9][0x12]i[0xa6]H'[0x80][0xc]Cd[0xba][0xe1]%[0x1c]~"[0xcb][0x14][0xd9][0xa6][0xc8]1E[0xae])z[0xb8]EO[0xe1][0xa5][\n]"
[DEBUG] http-outgoing-0 << "[0xbd]<[0xa2]7[0x12][0xaa][0x9d]([0xfa][0x98][0xa2]/[0xb5][0xd1][0x8f][0xe8]G[\r]v[0x89]C[0xc][0xe1]s[0xc3][0xfb][0xfc]HC[0xf4]'u[0xc2][0x9e]}([0x6][0xb8][0xc5]@1[0x88][0xba]w[0x15][0x92]+[0x6]c[0xb7][0xc4][0x10][0x8f][0x18][0xca]w[0x98]b[0x18][0xbe][0xc5]p,[0x13]}[0xb0]?b[0x4]vE[0xe4][0x19]"[0xdf][0x10][0x5]nQ([0x8a]Lq[0xa8][0x1b][0xbe][0x16]#[0x9][0xf9](S[0x1c]f[0x8a][0xd1]Tc[0x8c]![0xc6]R[0xf9][0xe1]nq[0x84][0x18]g[0x88]b[0xfa][0x18]o[0x88][0x9]n[0xf8]ML$*'[0x99][0xe2]H[0x97]8J[0x94][0x98]b2[0xca]PLAQ[0x89][0xa9][0xa6]([0xa5][0xbe]N3[0xc4]tz[0xcf][0xa0]6[0xcb][0xc]1[0xd3][0x14]G[0x93][0xa0]i<[0x8b]rST[0x10]p[0xa5])f[0xb9][0xb5][0xe1]b[0xb6]![0x8e]qkYb[0x8e]KT[0x89][0xb9][0xf4][0x98]G[0x8f][0xf9]T[0xb6][0x80]:t[0xac]![0x16][0xba][0xb5]^d[0x12][0xb2]8[0xd6]^d[0x8a][0xe3]Lq[0xbc])N0[0xc5]bb[0xe9][0x12]S[0x9c]h[\n]"
[DEBUG] http-outgoing-0 << "[0xbf]![0x96][0xba][0xb5][0xc1][0x2][0xad][0xcc]NQ[0xe3]F[0x14][0x1]C,C,[0xa4]d/[0x89][0x1][0x86]X[0xee][0xd6][0xf2]E-=[0xe6][0xd0]#H[0x8f][0x15][0xf4]XI[0x8f]:zL$J[0xea])[0xd5]@[0xed][0x87]L[0xd1]H}8[0xc9]#[0x9a]D[0x18][0x99]M[0xb8]6[0x91],6[0x89]fR[0x15]5[0xe6]wPi[0xb]1y[0x95]!V[0x9b]b[0x8d])[0xd6][0x9a][0xe2]d4g[0xe2][0x14]S[0x9c]j[0x88][0xd3][0xdc][0xda]tq[0xba]!~[0xe7][0x16]g[0x88]3Mq[0x96][[0x9c]-[0xce][0xa1][0xc7][0xef]Mq[0xae][[0xf3][0x88][0x89][0xa6]8[0xcf][0x10][0xe7][0x9b][0xe2][0x2][0xb7]v[0x8c]2~[0xe2]BS[0xfc][0x81][0xa4]?[0x8a][0x1e][0x17][0xd1][0xe3]bz\B,[0xb9][0x94][0x1e][0x97][0x99][0xe2]rC[0xfc][0xd1][0x10]W[0x98][0xe2]JC[0x90][0xd1][0x11]W[0x19][0xe2]jC\c[0x88]u[0x86]Xo[0x88]k=[0xe2]:[0xf1]g[0xaa]v[0xbd][[0xdc] [0xfe][0x82]cL[0xdc]h[0x88][0x9b]H87[0xbb][0xb5][0x95][0xe2][0x16]C[0xdc]J[0x1f]hh[0x1a][0xc4]-[0xa6][0xd8]`[\n]"
[DEBUG] http-outgoing-0 << "[0xb2]4[0xe2][0xe]S[0xdc]ID[0xdc]EHWQj[0xa3])[0xee][0xa6]n[0xdf]C[0x8f]{[0x91]Jq[0x1f][0xa5][0xee]w[0x8b][0x7][0xc4][0x83][0x94]z[0x88][0xea]m2D+[0xbd]7[0xbb][0xb5]3[0xc4][0x16][0x8f]h[0x13][0xf][0x1b]b+}<B[0x8f]m[0xf4]x[0xd4]-[0xb6][0x8b][0xc7][0xc][0x81][0xea][0xf9]W[0xfa]F[0xf3]r[0x8e]x[0xc2][0x10]O[0xba][0xb5][0x8b][0xa9][0xeb][0x17][0x8b][0x9d][0xf4]x[0x8a][0x1e]O[0xd3]c[0x97][;A<C[0xa9]g[0xd1]L[0x8a][0xe7](E[0xe6]F[0xbc][0xe0][0x11][0x13]dk[0xc4][0x8b][0x1e][0xf1][0x92]x[0xd9]-^[0x11][0xff][0xf0]h[0x17][0x88]WM[0xf1][0x1a][0xb1][0xf5]uS[0xbc]a[0x8a][0x9a][0xe2]M[0xea][0xc2][[0xa6]x[0xdb][0x14]dY[0xc4][0xbb][0xa6]x[0xcf][0x14][0xef]S[0xd5][0xf]L[0xf1][0xa1])>r[0x89][0x89][0x8f]M[0xf1][0x89][0x1b][0xdf][0x9f]R[0xfe]g[0xa6][0xf8][0xb7])>'[0xe6]}a[0x8a]/)[0xeb]?[0xa6][0xf8][0xca][0x14]_[0x9b][0xe2][0x1b]S|k[0x88][0xef][0xdc][0xda][0x16][0x1a][0x8a][0xdf][0x9b][0xe2][0x7][0x92]0[0xda][0x92]-[0xe2]G[0xb7][0xf8]I[0xfc]l[0x8]4"[0xdb][0xc5]/&/$[0xa0]]T[0xbc][0xc7][0x14][0xbf][0x1a][0xe2]7C[0xb4]k`[0x95]54[0x4][0x9a][0xa6][0xd4][0xf9][0xc3][0xe1]@X[0x3]6[0xbb]J[0x83][0xcc][0xf2][0x15][0xfe]U[0xfe][0xa2]:[0xc3][0xf2][0xa2][0xaa][0xe6][0xa6]`[0xc3][0xf2][0xf1][0x1a][0xa4]N-[0x9d]V2[0xaf]|[0xee][0x92][0xaa]Y[0xf3][0xe6]L)[0xd5] eJ[0xa8]![0xdc][0xec]oh[0x9e][0xef][0xaf]k[0x9][0x98][0xda][0x17]q0sK[0xe6]L/[0x9d][0x8b][0x98]*fM[0x9d]W^[0xba][0xa4][0xac]r[0xda],'[0xd3][0x94][0xa0][0x81]g[0x99]?X7[0xab][0xa1][0xb4][0xa9])[0xd4][0xa4][0x81][0xb6]H[0x83][0xde]sZ[0x1a][0x9a][0x83][0xf5]"
Progress (1): maven-compiler-plugin-3.8.0.jar (4.1/62 kB)
[DEBUG] http-outgoing-0 << "[0x81][0xb2][0x86]U[0xc1]ppi][0xa0][0xa4][0xa1]![0xd4][0xec]o[0xe]b#[0x1a][0x1c]Z[0x1e]jZ^[0xe4]o[0xf4]W[0xd7][0x6][0x8a][0xea][0xfd][0xab][0x2][\r]E[0x8d]u-[0xcb][0x83][\r][0xe1]"[0xc][0xae]h[0xb6][0xbf][0xc9]_[0x1f]h[0xe]4![0xbd]fcS[0xa8]1[0xd0][0xd4][0xbc]V[0x83]^[0xaa]Fau[0xa8][0xbe]1X[0x17]h*Lh[0xdc][0xaa][0x9],[0xf3][0xb7][0xd4][0xd9][0xbd][0xd0]@47[0xd1]+[0xc5][0x86]Y[0xe0]oj[0xc0][0xee]k[0xd0]')[0x8a]h[0xb1]\[0xe6][0xaf][0xb]c5Y[0x13]X[0xda][0x82][0xdf][0xd9][0x1d][0xc0][0x9d]lwc[0x84]@[0xec][0x93][0xb7][0x3]L|[0x99][0xb1]*[0xd0][0xb4]4D[0x18]s;@E[0xb][0xd2][0xc2][0xb5][0xa1][0xd5]S[0x3][0x8d]M[0x81]j[0xd5]{[\r][0xfa]u[0x0][0xed][0x4]`[0x86][0x1a][0x91][0xc5][0xc1][0x93][0xb1][0xb6];R[0x10][0xa8]AJ[0x1c][0xde][0xcf]O[0xc2][0xf9][0xdc]8][0x88][0xd5]A[0xfe][0xf6][0xe8][0xd0]Z[0xc][0xb7]E[\r];[0xac]A[0xc][0xbd][0x93][0x90][0x15]+[0xd5][0xc3][0xa1][0x96][0xa6]j[0xac][0x95][0xd3][0x11][0xcc][0xc9][0xe7]#[0xb][0xc7]"X[0xb3][0xbf]iy[0xa0][0xb9]3X$[0xdf]h[\n]"
[DEBUG] http-outgoing-0 << "[0xd4][0x5][0xfc][0xc9]8[0x16]-0[0x3][\r][0xd5][0xa1][0x1a]%[0xaf]C[0x6][0x9d][0x82][0xea][0xb1]"P[0xdd]\[0xb8][0xb4]%XW[0xe3][0xb4]V[0xea][0x0][0x9c][0x86][0x1a][0x8a][0xaa]][0x17][0xa8][0x8][0xd6][0xd5][0x5][0x91]J[0xad][0xc]e[0x8a]c[0xa4][0xb9]"T3[0xbd][0xc9][0xdf][0xd0]R[0xe7]o[\n]"
[DEBUG] http-outgoing-0 << "6[0xaf][0xad][0xa0][0xa2]C[0x91][0x9b][0x91][0xc6][0xca]j:[0xcb]5[0xbe]L[0x12]3[0xab]Qx[0x91][0xcc][0xf9]([0xee][0xa4][0xc2][0xeb][0x4] [0x96][0x85][0x9a]Vj[0x90][0xd5]Q[0x11]U[0xae][0xbb]>P[0x1f]l[0x8]6[0x7][0xfd]u[0x9d][0x9][0x88]/[0xd3][0xeb][0xfd]k[0xf0][0xbb]3[0x1f]#[0xf9][0xee][0xc0][0x9a]@uK[0xb3][0x1f][0xf5][0xa0]3[0xa6][0xf8]2[0x81][0xc][0xc4][0x9e]d[0xc7][0xc6][0xdf]l[0xcc][0x8][0x84][0xc3]!R[0xe0][0xac][0xe3][0x92][0xd9][0x90][0x9e]I[0x80]g[0xfb][0x9b]k[0xb1]B[0xba][\r][0xdf][0xd2][0x1c][0xac]+*[0xf][0x86][0x9b][0x11][0xda]U[0x15]\[0xde][0xe0]oni[0xc2][0xd6][0xca]:[0x14]O[0xe8][0xca] [0x14]E[0x88]%m[\r]4[0xd4][0xa0][0xcc][0xd7]N[0x9][0x85][0x9a]P[0xac][0xa8][0xb8][0xe3]'![0xde][0x8c][0x8]HI[0xd3][0xf2][0x96][0xfa]@C36[0x9f][0x16][0x87][0xbf][0xc2][0xdf][0x88]Pc[0x12]s&t[0xee]O[0xe7][0x1c][0xc2]n[0xc5]aG[0xc4][0x83]:[0x11][0x9e][0xb4]VzG[0x9a][0xd0][0x98][0x86]Z[0x9a][0x1b][[0x9a][0xa7]an%[0x1a][0x6][0x14][0x8d][0xb2]"u[0x81]U[0x81]$B[0x8e]/Ko[0xe][0x85][0xea][0xaa]k[0xfd][0xc1][0x86][\n]"
[DEBUG] http-outgoing-0 << "[0x83]y[0x0][0xad]\Ag~E[0x81][0x8a][0xe6]v[0x0][0x1f][0xdf]m[0x83];[0x5][0x9b][0xf]5 [0xb9][0xd4][0xf1][0x15]5+[0xa3][0x98]p@.[0xc5]AW[0x13][0xc4][0xb6]S[0xed].[0x7]CE[0xd4][0x17][0x84]t[0xf]:[0xc5])[0xc4][0x81]f6[0x5]Nj[0x9]6[0x5]j8N[0x1d]5[0xf5][0xed][0xaf][0x9]5[0xd4][0xa1][0xdd]NUcs*[0x16]V7[0x87][0x9a][0xc8][0x90]w[0x1c][0xb6]5[0x91][0xb2][0xd3][0xe2][0xc6]T[0xb4][0xd3]cU'pH[0x7]j[0xfd]-a[0xec]@`[\r][0xbe][0xa2][\n]"
[DEBUG] http-outgoing-0 << "Ro[0xc3][0xa9]N[0xc4][0xd5]C[0x2][\r][0xf4][0x85][0xf6][0xa8][0x1b][0xde][0x99][0xf][0xf6] [0xc0]RT[0xb][0xfc][0xae][0xb2]A[0xed]n9[0xf5][0x90][0x1a][0xc3]!T[0x83][0xa1]IXi[0x97][0xd9][0x8]f[0xdb][0x1f]6[0x2][0xa7][0x4][0x11][0xe4]D[0xe8][0x9c][0x13]h[0x9][0x7]PUP}[0x97]#[0xf][0xd2][0x6][0x9d][0xd2]D9S[0x90]Oh[0x89][0x11]rP[0x17][0xa6][0xa3]C[0xc5][0xdc][0xf0][0xca]`c[0x5]z[0xba][0xe0][0xdc]Z[0xe2]q[0xd4]y[\r][0xee]hy[0xbb][0x80][0xcb]A[S[0x1d][0x98]I[0x16],[0xc2][0xb2]ydW;[0xb6][0xdf][0x5]XJ}hE[0xa8]4[0xc2][0xbc][0xe4]|[0xb1][0x87]pE< [\r][0x8e]A[0xa7]$[0xd4][0xc5]>[0xa7].C[0xbc][0xa5]k[0x9a][0x3][\r]a[0xdb]Oy[0xb1][0xaf]e[\r][0xd5]M[0x1][0x1a]<[0xfe]:[0xbb]e[0xc7][0xef][\r][0xeb]@[0xe0]^@[0xd3][0x9b][0x2][0x8d][0xa1]p[0x90]T[0xaa]jm[0xb8][0x99],bag:c@Es:[0xc0]#[0xb9][0xb9][0xfe][0xa6][0xe6][0xe0]2u[0xf3][0xc]CM[0xbc]>N[0xee][0x8c])[0x2]ZTk[0xc3]F[0x95][0xb2]$)[0xe][0xc2][0xde][0x14][0x8][0x87][0xea][0x14][0x1f]T[0x4][0xe3][0x0]h0~/[0xd8]U[0x1d][0xc][0x1d][0x90][0xdc]d[0x95][0x11]m[0xa6][0xed]Hg[0xa1][0xd6](wYEN5k[0x10][0x85][0x85]Mj[0xac]O[\r][0x86][0xc9][0xf0][0xa3][0x13][0xd3]'[0x90];[0x99][0x84][0xae]y[0xd8][0xf0][0xf9][0xe8][0x8][0xa6][0xe0][0x0]#[0xb][0x1a]l[0x8]T[0xb6][0xd4]/[\r]4[0xcd][0xb5]=Dfy[0xa8][0xda]_7[0x1f]=%};[0x99][0xa2][0xb9][0x96][0x1c][0xea][0xe1][0xfb][0xb6][0xdf]%K[0xc3][0xa8][0xbb][0xd5][0xcd][0xd1][0xe1][0x89]*[0x80][0x84]z[0x91][0xca]*[0xe5][0xad]Q[0x86]u-$[0xfe][0xaa]j?[0x5][0xb1][0xc8][0xe0]ae[0xc3][0xf7]>[0xe6][0x95][0x1d][0xe]#|Qr[0x1c][0xd8]@[0xd5][0xb0][0xce]&[0xfa][0xe0][0xb1]f#[0xd9]*[0xca]nDWZ[0x17]p[0xfc]N[0xe6][0xb0][0xe1][0x9d][0x1d][0xdf][0x90]N[0x99]]x[0x8d][0x1c]D[0x8a][0x11]IK] [0x11]k[0x1a][0xe6][0xcf]N[0xc8][0xc9]H@i{[0xb8][0xea][0x8e]y[0xc9]|\[0xb2]~[0x13]@[0xb][0xaa]c[0xb8]H[0xc1][0xaf]h[0xac][0xf][0x17][0xd1]x[0xb7]I[0x99][0x1a][0x8]W7[0x5][0x1b]qHDitDh3gN(D[0x14]Y[0x18]Kb[0xc8][0x1b]p[0xfc][0xe6][0xb0]8ZP[0xf9][0xc6][0x93]j[\r][0xe8][0x90]9![0xd1][0x93]LR@[0x99][0xa4][0xb6][0xca]U[0xc6]y[0x8a][0xf4]H[0xdf]b^[0xc7][0x15]U[0x1c][0x94]F[0xa4]81>![0x90][0xb9]N@[0xe9][0xc6][0xe7][0x9c]H[0xe8][0x98][0x15][0xeb]D[0x9c][0x87][0xce]N[0x92]K[0xba][0xdd][0x1d][0xbe]&[0x95]g[0x1f]D8=[0x80][\n]"
[DEBUG] http-outgoing-0 << "C[0x96][0xdd][0xa6]5[0x1c][0xd7])"iv[0xc4][0xad][0xc][0x1f][0x96][0xc4]7u[0xe1]X[0xc][0xdb]gQ[0xc][0x1c][0xd1][0xd9][0x88][0xfd][0xed]R[0xa7]#[0x1d][0xc3][0xea][0x1a]V[0x9c][0xd4]=_Z[0x19][0xaa]j[0xa9][0xae][0x8d][0xd4]-]S[0x1d]ht[0x8c]8[0xab]_[0x1a][0xf1][0xc8][0x9]$[0x87]kQ[0x9]j[0x14][0xaf]P[0xb3]B[0xcb][0xd1][0x6],/[0xaa]@?[0x8a][0xf8]&[0x93][0x93]W$[0xe8][0xfe][0xaa][0xc6][0xba] v[[0x86][0xed]7[0xf2]>[0x91]O[0xa4]GX[0xbc][0xca][0x9e][0xc6][0xa5][0xe1][0xc0][0x9b][0xd7]X[0x83][0xe5][0x11]y[0x1a]v[0x88][0xdf]![0xd6]#M[0x8b]D;[0xfb]dE|w[0xd2][0x82][\r]NpV[0xda]`[0xcf]),5c[0xa8][0x8a][0xb4]"[0xc8]C[0xa1][0x8d]\[0x19]XK[0x9d][0xa7][0xc9]aiC3[0x89]1'Q;[0x6][0xa9]lD[0xc9][0x91]Rd6[0xd5]m[0xc2]1[0x82][0xc9] [0xd6][0xc]#[0xdb][0xbc][0x9d][0xd4]%[0xc6][0x19]V]G[0xf][0x8a][0xf][0xc3][0xd1][0xf1][0xa5][0x82][0xc5][0xc8]0[0xec]Jx[0xdd][0x1e][0xc6][0xa8]6[0xd4][0x19][0x9b][0xc3][0xd9]6-[\r]8[0xb0]([0xb3][0x88]r[0x89][0xf8][0xfa][0x10][0xb9][0x6][0xc5]~[0xec][0x81][0x7][\r]Ru[0xad][0x8d][0x89][0xc6]z[0xec][0x8b][\n]"
[DEBUG] http-outgoing-0 << "[0xc3][0xf1][0x86]@[0x12][0x1a]z[0x7][0x12][0xd8][0x83][0xf8][0xcb]f[0xc5]s[0xdc]$[0x2][0xed][0xa0]:5[0x16][0xe2][0x84]1P[0x89][0x84][0xb7][0xfb][0x14][0xa0][\r]N[0xb8][0xea]m[0x5]#^W7[0x4][0xf7][0xad][0xdf][0x11][0xc][0x95][0xa1][0xe6][0xb2][0xfa]F[0xdb][0xac][0x6]j[0xe2][0xc9][0xcb][0x89][0x93]Q|[0xbe][0xe1]4[0x15][0x9]$[0xf6][0xd9][0x84][0xa3][0xfb]Tuut}!`/V[0xb0]f[0x9c]r[\r][0xe9]V,O>[0xbd]:[0x89][0xdd][0x8d][0xc6][0x95]SB[\r][0xcb][0x82][0xcb][[0x9a][0x9c][0xc8]gt[0xf7][0xa8]K[0xa8]e[0xbb][0xe3].[0xed]Un0[0x16]e)[0x85][0x9d][0x11][0xa8]k$[0xd3][0x93]d[0xf6][0xe6][0x98][0x81][0xb8][0x1a]EeIkc[0x9b]}[0x93][0xe3][0x9d][0x83][0x13][0x89]@[0x18][0xb5]a[0xca]A[0xa0]w[0x90]`+[0xbd][0x2][0xcb][0x96]aO[0x82][0xab][0x2]Il[0xbc][0xb7][0xcb]B[0xf4]"[0xa4][0xa8][0x14][\r][0x91][0xaa]7[0xd9]K+[0xb6][0xde][0xba][0x94][0x8a][0x97]5[0x13][0xf][0xb2][0xe3][0xac][0x0][0xe5][0xf8][0x9d][0x91][0xb6]:[0xb6]4[0xa2][0x84]N[0x89]Ps[0xad]Z[0x16][0xca]M[0x8c][0xa8][0xd6]6F[0xa2][0xaa]C[0xf6][0xe1]'):Lbt[0xba][0xed][0x98][0x6]&[0xc5][0xdf][0x11]jpR[0xa8]DKAp[0x93][0x92]N[0xe6][0xf7]m~[0x89][0x1d][\n]"
[DEBUG] http-outgoing-0 << "[0xc1]1Ix7[0xe1] :H8[0x8f]:0[0xa2]"[0xa3][0x95]P[0xa4]T5[0xfb][0xab]Wb[0xe3]J*[0xb4]c)5[0xb5][0xc][0xfe]'C|o[0xf0]z[0x83][0xe9][0x6][0xff][0xc1][0x10][0x9a][0xc1][0xff]nHf[0xf0]7[0xc][0xfe][0x91]![0xb9]![0xae]0x[0xbb][0xc1][0xa7][0x1b]R[0xa8]-[0x91][0x9][0x86][0x98]n[0x88]e[0x86][0x98]i[0x88]c[0xc])[0xd1][0x9c]GM[\n]"
[DEBUG] http-outgoing-0 << "[0xaa]AJ0<[0x17]u[0xd4]!A[0xc5][0xe1][0x8b]4[0xe8]Q[0x1d]j[0xc0][0x10][0xbf]ynhJ[0x7][0xeb]x[0xe4][0xb0][0xe]}[0xdb]W<[0xdb][0xc9]^[0xa6]T[0xc7][0xcb]@[0x83][0xfc][0xfd][0x91]X[0x9c][0xbd].u4[0xda][0x8][0xb7]T[0xd3][0xf2][0x8f][0x6][0xf5][0x1d]i[0xdb]Oe[0xd8][0xdf][0x9e]dt[0xc]G[0x91][0x86][0xba]a[0xdd][0xc][0x85][0xe][0xc4]D[0xe][0xef][0x14]m[0x1c][0xd9][0xdd][0xc9]D[0xc2]4"[0xc1][0x15]R|0M[0xc9][0xde][0x8]G&?[0xdd]F[0xdb][0xe5][0x1c][0xc5][0xa8][0xf7]76*[0xb3][0xf]o[0x18][0xc3][0xe5][0xd4]ppV[0xd8]_q[0x12]wxlH[0xdd][0x90][0x86]!Vkp[0xea][0xff]Ef'[0xb1][0x83]i[0xc1][0xb0]M[0xd3][0x14][0x9c]q/[0xa7][0xe9][0xec][0xe2]n[0x12][0x94][0xd8]R7;A[0x3]S[f[0xcf][0x82][0x1c][0xd7]b[0xaf][0xaa]L[0x9][0xb5][0x90][0xaf][0xc0][0xa1][[0x86][0xb3][0x95]XiE[0xa0][0xb9][0x96]"[0xa9]^qv[0xaa])[0xb0][0xac]NE[0xf5][0xaa]l|d[0xf1]6[0xba][0x1e][0x12]u|[0xf1][0x1b]7[0xb3][0x96]:[0xc1]?[0xcd]R[0xe2][0xda][0x8c]4[0xe0]i[0x8e][0xa7][0x83][0x86][0x85][0xf2][0x82]h[0xc2][0xd0][0x84][0x4])^M[0x9c][0x9b]N[0xa5][0xe5]S{[0xb6][0x15][0x85][0x89][0xe0][0xb2][0x17][0xea][0xe2][0xaa][0xa6]w[0xac]h[0x88][0xf3][0x11][0x8c][0xa6][0xab][0x81]z[0x8c][\r][0x9c][0xcd][0x96]!I[0xa7][0xda]I&fF8[0xd0][0xdc]lG[0xd9][0xd4][0x9b][0xd8]*[0xe3][0xb0]d[0xb3][0xa0][0xe4][0xd1]PV|[0xcd]p[0x84]t[0xde]\[0x8d][0x16][0xe0][0x88]}//'C:i|l[0x8b][0x93][0x9f]f[0x88]kqh[0x6]1[0x2]Z[0x1e]T[0x91]V[0xb2][0xde]-[0xa2]-[0xf][0x95]F[0x8a]H_pZV[0x95]0q[0xb8][0xeb]S[0x1f][0xf][0xd2]Ft6fi[0xa1][0x8e]So[0x8f][0x9d]S[0xd5][0xbc][0x96][0xec][0xd3][0xa8][0xee][0x11]6+Vg[0xbc]!M[\r][0xb6][0xfe][0xff][0xc3][0x85]$V&=[0xba][0xd6]P[0x11]1[0xa9][0xd7][0xfe][0xaf][0x9a][0x99][0x83][0xb5][0xd7]}1[0x14][0xe][0xad][\n]"
[DEBUG] http-outgoing-0 << "[0x94][0xd6]76[0xaf]M[0xb6]Z3 I[0x8][0xd1]i[0xdd]J[0xf]7UO[0xa5][0x15][0xff][0xdc][0xce][0x13][0xf][0x2][0xc1][0xb0][0xb9]![0xb0][0xba]3z[0xbb]lf[0xe7]H I[0xa8][0xd6][0xcd][0x95][0xb1],[0x1c][0x86][0xb1][0xfd][0x9f][0x88][0xa9][0xb7]"[0xb][0xa2][0xf6]T[0xd6][0x13][0xb7]jfOR[0xe3]V[0xcc][0xdc][0xb5][0xfe]pe`[0xb5][0xed]e[0xfb][0xf]K[0x94]qG[0xf3]F[0xc3]ZV[0xd7][0xa2][0x11]$[0xd7]G[0xcb]|j[0xf9][0xcd][0x9e][0xd0][0x9a][0xaa][0xa0])[0x80][0xe6]*[0xed][0xb8]D<[0x6][0xf7]j[0xd0][0xdb]Y[0x9d]M[0xd8][0x12][0xa3][0xba]A2[0x7]Fmd[0x81][0xf7][0xb0]n,[0x1f]wX6[0xa6]X![0x2]C{Q]#[0x88]T[0x1c]O[0x1d][0xa1][0xfd][0xb2]n[0xad][0xd1]&[0xdd]cS{8[0xaa]G[0x88]$#[0xb2][0xbd][0x13]i [0xac][0xf6]j[0x1d][0x87]5[0xa9];k[0xd6][0x91][0x9a][0xb1][0xb5][0xeb][0xd8]$-[0xbd]).[0xd3][0x8e]v'[0x1e]([0xce][0xc8][0xda]@ [0xaa][0x3]c:[0xe][0xf4]n[0xf0][0x8f]|[0xc2][0x93][0x86]t[0xa1]z[0xad]n[\n]"
[DEBUG] http-outgoing-0 << "6[0x7]f+[0x96]i0n[0xd8][0x81][0xad]}[0xd1][0x12]'[0x1d][0x7][0xb0][0xf7][0xa3]l[0x9c][0xb6][0x8d]@[0xe7][0xd5]T[0x87]T[0xf6]%[0xb7]J[0xf8]"[0xe6][0xc0]n2[0xba]s[0xcc]h[0xc1]=nU[0xa5]AYZt[0xfe][0xb4]Y[0x91][0xd2][0x18][0xaa][0x9f]m[0x9f][0x94]P[0xea][0x96][0x1b][0xd7][0xe5]X>[0xf6][0xe9];C[0xba][\r][0xb1][0x1b]'[0xb1][0xe1][0xd8]"iR[0x17]7[0x9f][0x96][0xa2][0xe2][0x16]O[0xcd][0x9][0xd5]u[0xce][0x8e][0x81][0xdb][0x1e][0xf5][0xf6][0xa0][0xf2]&[[0xe1]/$|[0x16][0x9b][0xc8]&Y[0xda][0xdf][0xb5][0x17]-[0xed]M[0xed]E[0x1c][0x9b][0xf1][^[0x16][0x1b][0xa6][0xed][0xb2][0xd8]aX[0xca][0x6][0xb3]![0x96][0xf4]H[0xb][0xa7]?],[0x91][0xa1][0x8e][0xcc][0xb][0xe3][0xa7]/[0xa2][0xb4][0xbe][0xa1][0x96]L[0x91][0xa9][0x96][0xf6][0xa3][0xb6][0xb][0x99]3[0xb4][0xd0][0x92]il[0x1e][0xce][0xb2][0xb0]-[0x99]n1[0x1f][0xeb]o[0xc8][0xc]Kf[0xca],[\r]&[0x1c][0xcc][0xaa][0xa7][0x6][0x85][0xdd][0xda][0xe2][0x8a][0xab][0x90]Y[0x19][0xf2][0xe1][0xdc][0xa8]6[0x81][0xda]l6[0xf]9[0xc2][0x8b]-[0xf6]=[0xfb][0x1][0x87]q4[0x0][0xf1][0xe1][\n]"
[DEBUG] http-outgoing-0 << "mA[0x4][0xbc][0xc0][0xc6]_[0xec][0xa3]>[0xe6]X2[0x97]z[0xc4][0xa8][0xa3][0xe3]b[0xb1][0x8f][0xf][0xb5][0xce][0x17]\[0xde][0x10]B[0xe5][0xcb][0xf7][\r][0x8d][0xed][0xea][0xf][0xf5]E[0xf][0xa1][0xf8][0x82]a[0x1f][\n]"
[DEBUG] http-outgoing-0 << "[0xd1][0xd7][0x1c]BT=[0x10][0x8b][0xb6][0x7][0xf9]-{[0xb2]o-V[0xc9]fX[0xdc][0xcd]=[0x96][0xf4][0xb2]gQ[0x9][0x88]f;z!p[0x87][0x14][0x8b][0x8d][0xd7]^4d/K[0xf6][0x96]}4(@[0xa0][0xc0][0x9a][0xc6][0xba]`u[0xb0][0xd9][0xa7]V[0x9][0x15][0xfa]e[0xa1]&[0x9f][0xbd][0xf3][0xe4][0xc3][0x94]sT[0xa3][0xbf][0xcf][0x90]}-[0xd9]O[0x1e][0xa2][0xc1][0xf4][0xb9]X[0xaf]![0xdc][0x82][0x14]7[0xd7]b[0x1d][0xa4][0xe][0xc1]h[0xa8][0xfa][0x2][0xd8]q[0x9f][0x19][0x11][0xdb][0xd2][0xb8][0xbc][0xc9]O[0xcb][0xb0]>[0xda]^[0xf2][0xd9]L[0xc8][0xc7]7[0xa1][0xf3][0xf9]kj[0xb0][0x13]>[0xd9][0xc7][0xe2][0xf9][0xbc][0x0]c[0x92][0xfd]w[0xc4][0x16][0x9b][0xc3][0xaa],[0xd9][0x1f]%![0x7][0xa0]$[0xb4][0xf][0x89][0x1b][0x3][0xe5] K[0xe][0xc6][0x7][0xcb][0xd4]v[0x19][0x12]Uq(k[0xc7][0x80]?[0xdf]b[0xcb][0xe4]0[\r]DC[0xa8]!`[0xc9][0xe1][0x94])[0xeb][0xec][0x15]![0xb1][0xca]O3[0xe0][0x1]1u-[0xab][0xab][0xb],[0xf7][0xd7]E[0x16][0x90][0xe2][0xb4][0xa1][0xcf]\[0xea]rc[0xa0]:[0xb8],[0x18][0xa8][0xf1][0xa9]=~[0x9f][0xda][0xe4]/[0xf6][\r][0xd5][0xa0]l(I[0xa9][0x5][0xb9][0xd3][0xd8][0x18]j[0xc2][0x91]Q[0xe8]+'L6m1[0xf]%[0x12][0x86][0xa2][0x94]U[0xf3][0x94][0xa0][0xf6][0xf1][0x8d][0xe][0xc2]7[0xd4][0x16][0x1b][\r][0x81][0x11][0xd4][0xa9][0xb7][0xa9]Sy[0xd8][0x1f][0xed]]J[0xe5]S[0xea]SJ[0x15]P[0xea]uJ[0x15]R[0xea]}J[0x15]Q[0xbf][0xe7][0x12]C[0xe]%[0xd5][0x9c]O[0xa9][0x91][0x94]Z@[0xa9]Q[0x94][0x12]8N[0xe5]a[0x94]:[0x8e][0xb8]7[0x9a][0x1e]c[0xe4]XK[0x1e].[0x8f][0xb0][0xd8]3[0xa4]<FI[0x8d][0x92][0x1b][0xfa]XR[0x9e]f4[0xeb][0x11]ev[0x94][0xca]G[0x93][0xdd]p[0xb1][0xdb][0x87]Z1[0xce][0x92][0xc5][0xa8]|r[0xbc][0x9c][0x80][0xd6][0xf][0xb3],9QN[0xb2][0xe4][0x91][0xbc][0x18][0x11][0xa0][0xbb][0xde];[0x2][0xe4]h[0x9c][0x86]v*[0xb6][0xe4]Q[0x84][0xa7][0xf][0xe1][0xe9][0x1a][0xa6]DN[0xb6][0x98]d[0xba]%[0xa7][0xc8][0xa9][0x16][0xef]M[0xf4][0x94]b[0x9e][0xf6][\r][0xf5]v[0x1a][0xf6]VN'&[0xfd]F[0x9f]^[0xd6]n[0xb1]op[0xd0][0xc8][0x19][0x84][0xba]_Y[0x3][0xca]&X[0xe3][0x8c][0x0][0xd2][0xfe][0xd8][0xe9][0x1c][0x12]i[0xcf][0xa1][0x85][0xbe]2[0x1a][0x99]Q][0xe])]([0xb4][0x18][0x10][0xbe]2[0xc2][0xd2]+[0x9][0x16]u|[0x87][0x8c][0xc5]LbwO[0xe6][0xb5][0xe4][0xd1][0xc8]im7[0xd5]*[0xa7][0xbc]~[0x94]W[0x81]y,[0x83][0xf2]*y[0xb1]!gY[0xf2][0x18]9[0xc7][0x92]U$[0xb2][0xb9]([0x14]9O[0xce][0xb7][0xe4][0x2][0xcc]c[0x1f][0xb2][0x8f],[0x96]G[0xd6][0xf7][0x8e][0x85][0xa1][0x16][0xdb]f[0xe0]x[0xf3][0xd5][0xd3][0xd1][0x80][0x2]{[0xf6][0xe8]S[0x13]?[0xa5]KIO[0x1d][0xc4]Y[0x11]e[0xc1][0xab]p[0xf8][0x16][0xfa][0xe6]R[0xbf]0[0x1a][0xc5]?[0xcc]C[0x98]0i+"[0xf][0x87]pt[0x7][0x1a]V[0x5][0x9b]B[\r][0xca][0x5][0xfb][0x86][0x85][0xc2]E+jV[0xe][0xef][0xef][0xa3][0xd3][0x86][0xc1][0x1a][0x1a][0xeb][0xca][0x9a][0xc7];[0x4]_[0xd8]i[0xae][0x10][0xa3]/[0xd2][0xf3][0xc2]0m-[0xd2][0xaa][0xa0]![0x8f][0xb5][0xe4]B[0xf6][0xad][0x6]7[0x96]-[0xf3][0xad]Ea[0x12]~"k[0x19][0xc2][0xf9]V[0x7][0x9b]kc[0x94][0xe5][0x13][0x80]"[0x8c][0x8e]@[0xd8][0xec]w[0x96]cmH[0xb2]@[0xd5][0xf1]f[0x1][0xb1][0x4][0xb0][0xff][0xc9]OL[0x90]E+[0x98][0xda][0xad][0xc3][0x15][0x13][0xe9][0xd4][0xa1]%[0x17][0xc9]9[0xe8][0xc8][0xa6]$[0xe3]$Y[0xf4][0xe3][0xe4][0xf1][0x16]+[0xa0][0xb1]w[0x82][0x1c][0xa4]A[0xf1][0x81][0xaf]r[[0xac][0x88][0x1d]j[0xb1][0x1]l :[0x16][0xb9][0x18][0xc7]N[0x97]g"[0x90]_j[0xdf][0xdf][0x90][0x9a][0xc5][0x82]r[0x89][0xc5]^f[0xaf]Xl[0x16][0x9b][0xad]A[0xc9]A[0xaf][0xb3][[0xf2]D[0xe9][0xb7][0xe4]RY[0x8d][0xb3]>K[0xd6]pi[0xf1]t[0xf6][0xac][0xc5][0xde]c[0xef][2 [0x97]i0[0xc2][0xe][0xdf][0xc3]h[0xfe][0x9a]q[0x8e][0x89][0xe2].@[0x91][0xd9][0xc][0xb5]GI[0xc0]W[0xaf][0xb6][0x96][0xfa][r9[0x91][0x85][0xfe][0x18]eGE[0xb1][0xb1]^[0xe0][0xf3][0xd7][0xd5][0xf9][0xaa][0xed]s[0xae]J[0x99][[0x1a][0xa9][0x9c][0xb6][0x8][0x1d]?[0xdd][0xfd][0xfd]?[0xf4][0xc1]j[0xfd][0xd1][0xb7][0xba][0x96]p[0xdb][0xd3]xjPm[0x1]F[0x9c]a![0xf1][0x16][0xa7][0x19][0x3][0xba][0xe6]m[0x8d]s[0xa8][0xc2][0xd2][0xbe][0xd3][0xbe][0xb7][0xd8]TVjq[0x8d]3[0x8c][0xf3][0xe6][0x94]VM)[0xa9][0xac],[0xab][0x9c][0x8e][0xbd][\n]"
[DEBUG] http-outgoing-0 << "[0xca][0x15]T8[0xcd][0x90]X[0xe4][0x1e][0xe8][0xe0]'[0x95]X[0x89][0x83][0x14]=}[0x9d]![0xb9]%[0xeb][0x91]q[0xb2]A[0x86][0xe8],V[0xdc],[0xc3][0x92][0x8d][0x14]@L'Cu[0x12][0x19][0xaf][0x99][0x94]j[0xa2][0xd4]BVn[0xb1]c[0xa9]4[0x95]![0xfb][0xc3][0x92][0xe]}$YG[0xb7]d[0xb][0xa1]M[0xef][0x18]cYr[0x15][0xe5]k[0x5][0x96]\MN[0x8e][0x15][0x94][0xe0][0xd7]DK[0xae][0x91]X[0xe7]d2[0xf0][0xee][0xe8][0xa9][0x7][0xb4]\[0x1a][0x9a]`O[0xec][0xc4]B1[0xad][0x99][0xc7][0x1b]XK[0x9e][0xc2][0xe6]![0x8]r[0xda][0x8e]H[0xa8][0x8f][0xa7]J:[0xe][0x85]l[0xab]';C[0xe3][0xdb]1[0x8a][0x8][0x8f]![0xdb][0xe9][0x9]$[0x97]45[0xf9][0xd7][0xda][0x13][0xca][0x9c][0x82][0x2];.[0x8][0x14][0xd8][0xfa]Q[0xd0][0xa8][0xa6]})[0x5][0x5][0x18][0x11][0x14]`4[0x82]N[0x93][0xf6]h[0xec]o[0x1a][0x8e][0xe1]hi}d[0xb3]2[0xad][0xa0][0xa0].X[0x1f]l[0x8e][0xe4][0x18][0xf2]w[0x96]<C[0x9e]i[0xc9][0xb3][0xe4][0xd9][0x86]<[0xc7][0x92][0xbf][0x97]G[0xa0][0xdd])(P[0x9b][0x9c][0xe][0x18][0x5][0x0][0xe7][0x92]d[0xce][0x8b]D[0xa6][0xce][0x8c][0xb5]ae[0xa0]f[0x86]?\[0xab][0xce][0xd7]d[0xc4]J[0x9c]<R[0x99][0xf3][\r]y[0x81]%/[0x94][0xb0][0xb4][0xa7][0xc9]R_$/6[0xa4]@[0xe6]-A.[0xcb]K,v4J[\r][0xcb]/[0xed]"<[0xed][0xee][0xbe].z[0x9b][0xc4][\r]"
Progress (1): maven-compiler-plugin-3.8.0.jar (8.2/62 kB)
[DEBUG] http-outgoing-0 << "[0x1a][0xa2]n[0x96][0xe2][0xac]!/[0xb3][0xe4][0xe5][0xf2][0x8f]t[0xca][0xdb][0xdf]0[0xb4][0xd9]W[0x17][0xa2]s[0xbf]([0x8c]+[0xc8]S\[0xc9]>BY[0xe7][0xfb][0xe8][0x80]w[0xe9][0xdc][0x92][0x82][0xb2][0xca]i[0xaa][0x99]B[0x8c][0xe4][0xc2][0x96][0xfc][0x93][0xbc]X[0x83][0xc1][0x89][0xb8][0xfd][0xcd][0xa8]0K[[0x9a][0x3]J%K"_[0x96][0xbc]J^m[0xc8]k,[0xb9]N[0xae][0xef]D[0xd0],[0x9c]][0xda][0x4]Y[0xf2]Zy]D[0xd0][0x89][[0xc3][0x96][0xfc]3[0xd1]t=)[0xce][0x11][0x84][0xdc][0x17]9[0xe7][0xeb][0xc3])[0xbc][0xaf]![0xd4][0xec][[0x1a][0xc0]H[0x11][0xfd]P[0xbe][0xe3]<[0x1a]q[0xf8][0xa1][0xd7][0xac][0x8f]A[0x1a][0xf2][0x6]K[0xfe][0x85][0xe2][0xe4][0x81][0xf9][0xbe]`!:)[0xdb][0xb3][0xa9]`[0xd2][0x1][0xae]q&[0xbb][0xcd][0xfd]-[0xce][0xe5][0x8d][0x96][0xbc]I[0xde][0x8c][0x81]m[0xf2][0xcd][0xc8][0x81]K[0x3]X)0'[0xa0][0xf0]DC}K[0xde]"o[0xdd][0xd7][0xb4]b[0x1f][0x9b][0xcd][0x96][0xbc]Mn[0xb0][0xd8]s[0xec][0xf9][0x8]C[0x12]7[0x9d]Q[0xf3][0xa7][0xf9][0x91][0x1a][0x9f][0xda]?[0xf4]E-[0xa5]%o[0xa7][0x1][0x99][0xdf][0x5][0xc1]*[0x92][0xee]L[0xef][0x1d][0xf2]N[\r][0x16]([0x87][0xb8]?[0x15]}[0xc8]z[0xb2][0xcc][0xf6]b[0xa0]/zP[0xd3]W[0x13][\n]"
[DEBUG] http-outgoing-0 << "[0x84]I[0xa3][0x2]k[0x82]d[0xfe][0xef]b3[0x90][0xa4][0xfd][0xd9]j[0xb3][0xe4]Fy[0x8f]![0xef][0xb5][0xe4]}[0xf2]~K>@[0x8f][0x7][0xe9][0xf1][0x10][0xf9][0x8d][0x9][0x14][0xb0][0xf4][0x8c]q[0xa5]$r[0x80][0xcd][0xd9][0xb9][0x9a]Xp0[0xff]p~<eV[0xc5][0xec][0xb2][0xf2][0x92][0xb9]e[0xb3]*}[0xb]J[0xe6][0x90][0x85][0xf6][0x15][0xe3]HpGB[0x85]0~[0x98][0xbe][0xe8][0xd6]}v|[0x85][0xd2]9sf[0xcd]Ap[0x8b]]![0xd3][0x9][0xcc][0xde][0xe2][0xc5][0x1a][0xba][0xcf][0xd9][0xe2][0x9f][0xb0][0xcf]E[0x97]8[0x7]2[0xcd][0x1f][0xac][0xc3][0xa9]QL5[0xd0]|L[0xb6][0xe4]&[0xd9]j[0xc9][0xcd]4H[0xf3][0xf6]cw[0xc]]m[0xf7][0xb7][0xde],[0xb9][0x85]F][0x1b][0xf9][0x9f][0x87][0xe9][0xb1][0x95][0x1e][0x8f]P[0xcc]8Qn[0xb3][0xe4][0xa3]$[0x8c][0x89]r;[0xda][0x89][0xc2][0x11][0x16]7[0xb9][0xcb][0x90][0x18]2?&w[0xa0]a!?[0xf5]W[0xf9][0xb8]%[0x9f][0x90]O[0xa2]H[0xe]j;L[0x83][0xbe][0xb6]/V[0x1b]a[0xca][0x9][0xc7]<[0x8a][0x9a]&[0x1d]2T[0x85][0xc8][0xb6]oV[0x87]M[0xec]x[0xd4]i[0x2][0x83][0xc6][0x1e]Uq~;[0x1a]g[0x90][0xef][0xd9])[0x9f][0x8a][0x1c][0x5]s[0x96][0xac][0xd2]b[0xba][0xa5]<[0x9b]%[0x9f][0x96][0xbb][0x12]\[0xa3][0xbd][0xf3]b[0xc8]g,[0xf9][0xac]|[0xce][0xde][0xf6][0x88][0xdb][0x80]1[0xe4][0xf3][0x96]|A[0xfe][0xcd][0x92]'[0x9][0x8d]lQ[0xf1][0x15][0x91]D[0xb3][0xe0][0xb8]][0x19]{rL!gKS[0x13][0x8e]:[0xdb]$[0x91][0xae]Y[0xf1][0x9b]0[0xd8]@[0xe2]j[0xa4][0x6][0x87]&[0xe2][0xc][0x13][0xa8][0x8f][0x8e]!t[0x85][0xd2]b[0xbf][0xa9][0x89]k[0xbd]%_[0x94]/Y[0xf2]e[0x92][0xed]+4w[0xe9][0xc1],[0x8b]e[0xb3][0x1c][0xf4][0x4][0x9][0xfb]'[0x1a][0xc][0xed][0xe6][0xc9]h[0xac][0x99][0x10]Q[0xe0][0xc4][\r]#[0xfa][0x4][0xaa][0xcb][0x95])[0xb8]P[0xfe][0x3][0x3][0xab][0xee]l[0xbc][0xa0]Lc[0xec][0xb6][0x17]_[0xec][0xfd][0x9c]8[0xad][0xe8][0x1d][0x17][0xab] Yt]#[0xae][0xd4][0xd7]y[0xfa][0xad]6[0xa4][0xe3]@[0xf2]:o[0xc0][0xe1][0xbc]+d_[0xa6][0xb1][0x17][0xc0][0xa2][0xd0][0x86]|[0xd5][0x92][0xaf][0xc9][0xd7]-[0xf9][0x86][0xfc][0xa7]![0xdf][0xb4][0xe4][[0xf2]mK[0xbe][0xc3][0xe5][0xbe]l}[0x92][0xa5][0xf9][0x96]e[0xcb][0x82]k[0x9c][0xa5]yK[0xbe]K[0xb1][0xdf]D[0xb9]V[0x83][0x19][0xfb][0x8b][0x8][0x1f]u[0xce][0xf9][0xb7][0x84][0xf5]~K[0xbe]'W [0xf][0xe6]5[0xac]l[0x8][0xad]n[0x88][0xad]59[0xc6]:L[0xdb]0j[0xe8][0xcc][0x98][0xa2]N[0xfb][0xab]![0xd1]TO[0x91][0x97][0xed][0x16]m[0x8d][0xa2][0xd8]9_M~[0x8]k[0xbc]{p[0xc0][0xec][0x91]dO_G1[0x13][0xad]\[0xa1][0x8a][0xc0]-[0xf6]9[0xfb][0xc2][0xe2][0xd9][0x1c][0xd5][0xca]K[0xb3][0xed][0x9a][0xe8]:r[0xc2][0xe8]{[0x9f][0xc][0xc5][0x7][0xf2]CK~D[0x8f][0xd1]2[0xdd][0xc7][0x96][0xfc][0x84][0xe6][0xd3][0x9f][0xca][0xcf],f[0x10][0xd2]1[0x7][0xb4]Lm[0xb1][0xc3][0x19][0xce]w[0xff]M8?[0xb7][0xe4][0x17][0xf2]K[\r][0x86]w[0xbd][0xbe];[0xd5][0xbe]%[0x16]Y[0xe6][0xb5][0xe4]h[0x88]|E[0x8f][0xaf][0x9][0xc5]7[0x96][0xfc]V~[0x87][0xc1][0xfe][0xbe][0x97][0x88]i[0x1][0xdc]>[0x15]d[0xc9][0xef][0xd5][0xd2][0x9e][0xfc]![0x99][0xdd][0xdf][0x8f][0x95]pK[0xfe]W[0xfe]h[0xc9][0x9f][0xe4][0xcf][0x96][0xdc]M[0x8f]_[0xe4][0x1e]K[0xfe]*[0xb3]d[0xbb][0xe][0x96][0xae]a[0xc4][0xaf]3[0x9d][l[0xc][0x1b]k[0xe8][0xc2][0xd2][0xa5][0xae][l[0x1c]+6t[0xc3][0xd2]M[0x1d][0xad][0xb3][0xcb][0xd2][0xdd][0xb2][0x99][0xec]z[0x95][0x6][0xe3]cM[0xf8]B[0xcb]|][0xdc][0xdb][0x89][0xc9]-[0x88]F[0x95][0xee][0xc4])[0xb9][0xe9][0x1e][0x8a][0xc5],[0x1f][0xfe][0x9b]`[0x8b]b[0x12][0xda]9[0x9f][0xfa]7ayS[0xa8][0xa5][0xb1][0xac]f[0x12]v[0xb7][0xd0][0xee]n[0xa1]=Ev[0xee][0x97]L([0x8a]@h0[0xda][0xa9][0x13][0xe1][0x4]f&][0xee][0x9c]P[0x14][0x7]a[0xf1][0x91][0xd4]|[0x9a]S[0xd7]Y:[0xa7][0xc5][0xe7][0x9]E[0x91][0xf][0x8b][0x17][0xd1]B`[0x8a][0x82]([0x8a][0xd2][0x98][0xe3][0xd4]I[0x98][0xe3]O[0xb2][0xb4][0xaf](0[0xcc][0xf0]9[0xff]&8[0xcb][0x94]6[0xca][0xc8][0x87][0xa1][[0x96][0x9e][0xa2][0xa7][0xa2][0x15][0x1d]g[0xe9]ih;[0xb5]/[0xa9][0x1a]/<[0x1e]=[0xc][0xca] ][0xcf]@7[0x11]Eb;[0x1b]D[0xfe]o[0x82]rM(r2[0xe2]a[0xec][0x95][0xd1]I[0xaa]8[0x9a][0xce]u[\n]"
[DEBUG] http-outgoing-0 << "[0x8b][0x12][0xa9][0xc4]H#[0xd9][0xca]}w[0x86]G[0xb2]ux[\r]*[0x8a][0xa2][0xb1][0xbd]][0xad]+[0x89][0x15]%[0x95]IQc[0xa8][0xbe][0xb0]1J[0x87][0xa5]g[0xea]Y[0x96][0x9e][0xad][0xe7][0x18][0xd2]m[0xe9][0xb9]lRB[0xf0][0x8a][0xfe]1[0xb4][0x9a]|[0x96][0xa5][0xf7][0x90][0xa7][zO[0x8a]TSi[0xbe][0x16][[0xcb]I[0xe6][0x1c][0x9c][0xb6]"=[0xb0])?[0xfc][0x80]z<h[0xa4][0xba]l8[0xe][0xa7].[0xdd][\n]"
[DEBUG] http-outgoing-0 << "[0x83][0x12][0xfc][0x9a][0x9a][0xce]ev>[0x95][0x16][0xc9]L8[0x4][0x87][0x93][0x8c][0xae]([0x8c]l[0x2][0x95][0x87][0x96]w?0[0xea][0xea]:[0xc4]~{[0x9e][0xc4][0x1d][0xeb]C[0xf7]w[0xe7][0x1e][0xcd][0xc6]Al[0xbd][0xc5][0xcf][0xee]b[T[0xe8]3P[0xed][0x15];[0x86]&=[0xf1][0xde][0x89]o[0xea][0xb4]x#[0xd9]%[0x9c][0x9c]u[0xeb][0x80]I[0xec]0[0xb5][0xd9][0x1c][0xaa]r[0xe]g[0xf4][0x8e][0xaf]:[0xa5][0xd6][0xdf]TE[0xa6][0xb6][0xa1]:[0xa0]6[0xb8]F[0x1f][0xc8][0xcd]2[\r]<q7[0x6][0x90][[0xfb][0xd3]$[0xba][0xe3]o[0xc7]c[0x91][0xcf][0xb2][0x8e]}uN[0xff][0xec][0xad][0xaf]"[0xd8][0xb0][0xc][0x7][0x8b][0xa0][0x99][0x8a]:[0xe1][0xdd]PC[0x1][0x16]z[0xa7]`X[0x9d][0x3]p6J[0xba][0xbd]c9[0x8f]2[0x91][0xf9]K1[0x80][0xa1][0xee][0x1d][0x91]L^[0xdd];[0xee]?[0xfa]@[0xea][0xa1][0xe5][0xf5]k0[0xbb]k[0xb9][0x1d](5FC`5-;[0xd9][0x17]k[0xd0]v[0x90][0xde]:[0xc7][0xe1]3[0xc2][0x91][0xdb]'[0xe5][0xa1][0xc8]=k[0xf][0xe5]E[0xaf]C[0x8b]a[0x8b]HcL[0xcc][0x9c]j_[0x3][0xef][0x1c]f$[0xd0]a[0xb][0xca][0xe1][0xa6];[0x18][0xae][0xc]5;[0xf2][0x18][0x97]Da[0x92][0xa8]P[0xd2][0xbb][0xb7][0xe9][0xc8][0x6]]Xm.[0x4][0xa6][0xa8][0x8d][0xd9][0x94][0x8]I[0xe5][0xf6][0xbd]Q[0xfa][0x9e][0x1d]w[0xb][0x9d]vq[0xe7][0xc7][0xee][0x9b]c[0x14][0x99]pu;[0x1d]s[0xa6]%[0xde][0x83][0xcf]t[0x80][0x12][0xae][0x9d][0x13][0x9c]mZ[0xa2][[0xd2][0xe9][0xd1]=[0xe4]hVFl[0xcb]8[0x9a]G[0xc7][0xbe]f[0xab]k[0xc6][0xf1][0x8c][0xb7]w[0x8f][0xfb][0x84][0xf7]zW&[0xa3][0xc3][0x89][\r][0x12][0x1][0xf7][0xd7][0xd4]t[0xd8][0xb3][0x8e][0x8c][0x93]E[0x8e][0xd5][0xdf][0xe7]m[0x1a][0xfb][0x2]Z[0xdc][0xf1][0xdb][0x84]c2f0j[0xf7]s[0x13]N[0xcf][0xc5][0x9d][0xf0][0x16]+B4[0x83])[0x1e][0x96][0xac][0xb8][0x9b][0xe7][0xe1]zbO[0x92][0xd2]@[0x17][0xb2]kj[0x92]dgF[0x85][0x10][0xd1]S[0xba][0xa5][0xdd][0xf9]T[0xf][0xb2][0xa9][0x7][0x82][0x96]$[0xbd][0xd5][0x9d];,[0x89]jE[0xaa]$?[0xbe][0x92][0x11]m7r[0xb9][0xde][0x16][0xeb]4uo[0x9d]4[0xae]"[0xee]z:[0x9d]-[0xa8]pn[0xa2]SYi[0xdc][0x85][0xf3],[0xfc]^[0x80][0xb5][0x10]E[0x9c][0x9c][0xa9]_[0x11]+[0x98][0xa0]K[0x93];[0xdc][0x1d][0x8e][\r][0xd4][0xd8][0x8d][0xea][0xbd][0xcf][0xa8][0x92]n[0x4][0xf]J[0xba];c[0xaf]O'[0xcb]w[0x95][0xd4][0xad][0xf6][0xaf][0xa5][0x93]D[0x1a][0xcc]<[0x80][0x13]`[0xc9][0xdb][0x1b]o[0xbb][0x92]X3[0xba]=[0xba]Q3[0xe2][0xf8][0xd1][0x81][0x94][0x8a][0x3]9[0x81][0xd6]E[0xfb]$t[0xd7][0x9c][0xc8][0xc6]Y[0xe2][0xce][0x80]so[0xd6]c[0xdf]Vs~[0x83][0xc3][0x9a][0x13][0xb7]a[0x87]d.[0xef][0x92][0xcc][0xf2]a[0xdd][0xf4][0x81][0xdd][0xe3]SO[[0xd9][0x92][0xdd]H[0xae]Jr$'[0xe9][0x15][0xe4]n_[0x2]'[0xb6][0x1c]~@[0x7][0xfd][0xc8][0x2][0xb9][0xa3]w[0xca]P[0x90]s;][0x88][0xfc][0x1f][0xb9][0xd5][0x92][0x1b][0xc7][0xf8][0x84][0xd0]mtw[0xd9][0x1e]T[0x13]#[0xd9]Y[0x95][0xa5]Kf[0xcd][0x9b];{[0xde][0xdc]%[0xd3][0xca][0xca]K[0x97]L[0x9b]5gIIy[0xf9][0x92][0xb2][0xca]H[0x16]N'[0xb3][0xa3]=[0x9b][0xdb][0x14][0x88][0x1d][0xbd][0x9e][0xd6][0xf5][0x99][0xa8][0xee][0xf7][0x89]8[0x97][0x1a][0xb5]2[0xe][0xf7]F[0xec][0xdd]S[0xc6][0xe][0xd3]Gn[0x90][0x96]u[0xbc][0xce]7[0xe3][0xc0][0xc4][0x98][0xcc]Z[0x8b][0xb0][\n]"
[DEBUG] http-outgoing-0 << "[0x4][0xfa][0xd0][0xed][0xe4][0xae][0xe3]1C[0x1d]8\[0xd3]L[0xc7]F[0xd4]+[0xf1][0x92]j[0xf4]<[0xb7][0xe1]\[0xee][0xb5]-b[0xec]Nq[0xc4][0xe2]f[0x87][0x13].[0x5]G[0xb2]Mu[0xf7][0xc8][0xde]v[0x1a][0xd6][0xf9][0x1c]/E[0xd9]G[0xd3]UE[0x93][0x9c][0xb2]}<[0xdb][0xad][0x96]y[0xc2][0xb][0x82][0xd4][0x98]7[0xe6]W[0x9a][0xa6][0xb4][0x84][0x9b]C[0xf5][0xb1][0x1b]OC[0xbb][0x17][0x97] [0xab]S[0xe9]\&F[0x1c][0xa5][\r][0xce]e[0xf2][0x94][0xe5][0x89]V>[0xa3]Z[0x99][0x8]g[0xb7][0xaf]\E]e[0x7]&[0x8a][0xe4]?A[0x12]Ojt[0xd6][0xa9]hK[0xef][0xb0][0x8b]H[0xb1][0xac][0xdf]9[0x9c]:8[0xc1][0xf3]%F[0xd7][0xf1]G`[0xfb][0xd3][0xf0]R[0xdc][0x89][0x10][0x15][0xbd]4[0x16][0x95]D[0xcf][0x4][0xf6]O[0x9][0xd5][0xd5][0xd9][0x8b]d[0xe3][0x13][0xa6][0xf0][0xb1]|uk[0x17][0x95][0xdf]^uM6gH[0xda][0xd3]>[0xc3][0xa6][0xec]5[0xfc]Gm[0x9c][0x1f][0x99]s%LO[0xb1][0x15][0xae]N[0xf7]%[0x9b][0x9f]$[0xb][0x0][0x92][0xdf][0x8][0xd5][0x9b]C[0xb6][0x9a]F#[0xa1][0x8e][0x10]=[0x86]%[0xcb][0xa7][0xf1]<d[0x1f]s[0x99][0xe8]`[0xe8]@[0xbe]3[0xfa]=[0xc1][0xf8]PpT[0xd2]f[0x8e][0xeb][0x90][0x19][0xdb][0xfc]T[0x14][0x88][0x6][0xe5][0xd7][0xf4][0xba]@[0xc3]r[0xea][0x84]K[0x8d][0xba]&[0xa5][0xee][0x8e][0x8e]F[0x9a]PB[0x9d][0xd3][0xad]F[0xba][0xd8][0x5][0xed][0x8a][0x85][0xb1][0xed][0xd0]j[0xb4][0x8a]8[0xae][0xd5][0x84]&L[0xb2]Iu~[0xb6]*[0x9a][0xd1]'[0x9e][0xcb][0x1d][0xc0][0xe9]P[0xaf]:8[0xaa]A8)[0x9d]q[0xac]U[0xa1][0xe8][0xd2][0xe8][0x19][0xe7]d[0xd8]:[0xf6]*[0xb6]M[0xdb]UG[0xdc][0xea][0xe6][0x87]s[0xf]6cy[0xe7][0x90]0o[0xef]S[0xa2]9[0x1]?j[0xed]4[0xbf]#[0xd1][0x14][0xe5]iJ+[0xa7][0xcc][0x9a]ZV9=[0xf6][0x93][\r][0xd1]`[0xff][0xc6]U&[0xdf][0xc2][0xd5][0xe0][0xd8][0xff][0x11][0xb7][0x95]t[0xd0][0xa6]"[0x4]m6;[0xd6][0x83]~[0xa4][0xe1][0xc0][0xdc][0xd0][0xfe][0xdd][0x8a]3[0xaa]#[0xed]M;[0x18][0xb7][0x97]p[0x19]@m[0xf0][0xd2][0xc9][0xe3][0xa4][0xfb][0xc3][0xff]S[0xce]~[0xe2][0x8f]3TD[0xef]u[0x93][0x9b]<:H[0x8b]@[0xc2]~[0x8d][0xe9]n[0x80][0xe3][0xe0][0x18]D[0xd5][0xb0]##[0xf7][0xbb][0x12]][0xf5][0xa7][0x8d][0xde][0xc8][0x91][0xfd][0xfd]m[0xd1]p[0xf6][0x95]q[0xa8]T[0x94]TN-[0x99];k[0xce][0xc2]%[0xd1]<W0\[0x15][0xb9]=9aP[0xd5][0xea] ][0xad][0xf7]7[0xd2][0xda][0xe8][0xa0]HK[0x83][0xec][0x96][0x6]EZ[0x1a][0x94][0x9c]Lv\[0x19][0xb6]f[0xef][0x88][0xd4][0xd9],[0xb3]'[0xd7][0xd1][\r]F[0xdb][0x1][0xa7]F>[0xd1])[0xb5][0xd4]7[0xd8][0xe3][0xb9]4[0xe2][0x9e]-[0xfb]#R[0xb6]8Y0[0xb0][0xff]l([0xc3][0xc9]c[0x8]Z[0x9f][0xb2][0xf7][0xca][0xb3][0x87]-J6[0x81]M[0xc7]P[0xa5][0xc3]Zf[0xc9]>[0xd4]z_7dbabu]KMt[0xc1]![0xb6][0xea][0x96]x3$f[0x88]:[0xc5]YH[I[0x1d][0xfd][0x80][0xd7][0xb0][0xe4]A[0x0]9[0x1f]3[0xf2]c14E[0x8f][0xf7]{*[0xd3][0xf9][0xa1][0x92][0xc8][\r][0xb6][0xa3][0xf6][0xe1][0xa5][0xed]:[0xf1]X:[0xdd]'[0xec][0xd9]U[0x19][0xd2][0x1b]lX[0x15]Z[0x19][0xe8][0xb0][0xf6][0xe4][0xf8][0xdf][0xae]c[0xa2]D/[0x9d][0x11][0xe7]^[0x1a]p^Fku[0x86]:[0xbc]:kY[0x17]!My[0xa7]*[0xb4]<[0x1b]l[0x88][0xc4][0xa6][0xae]p[0xcb][0xd2][0xc8]=[0xba][0xdc]aeI"k[0x9a]#7[0x87][0xca]C[0xab][0xd1]XE~[0xff][0xaf][0xc6][0x9]fs[0x12]C[0xf1]([0xa1][0x9d][0xdd]P[0xd7][0xbf][0x92]f[0xff]\K4wZS[0xa8]^[0x19]([0xb4][0x8c][0xcd]*~[0xaf][0xee]j tk&[0xd9][0xdd]k[0x8d]:y[0xe7][0x92]f;[0xa0][0x9b][0x92][0xb0][0xa9]B[0xfe][0x1a]][0xa6][0xe2][0xb6]s-[0x91][0xc0][0xa6][0x90][0x8a][0xf5][0xeb]8o[0x9b]]:'n[0xce]f[[0xd5][0xd8]zIdV[0x94][0x92][0x90]K[0xea][0x11]v,[0x86][0xf3][0xeb][0x88][0xea][0xa4][0xba][0xba][0xd7]:[0xd3]6([0xf6][0x11][0x5]J[0xd9]k.#[0xf6][0xee][0xe9][0x9d][0x91][0xe3][0xac]~*[0xd3][0x12][0xf9][0x95]/[0x14]z[0x1d][0xe]q'[0xc6][0xc3]IL[0x87][0xcb]TN{k[0x1b][0xb1][0x95][0xa3][0xe][0xf6][0x97][0xb6][0xec][0xe1][0xde][0xa1]L[0x83][0xe9]].[0xd9][0xef][0xef][0xbd],2[0xa1][0xd3][0xed][0x9d]N[0x9b][0xad]%[0xd1]-L[0xbb]0[0xba][0x9a]5[0xb2]k[0xf4][0xce]~&[0xd9]+[0x7]|[0xe]M[0xad][0xe9]W>T[0x88]JJ[0xe9][0x14]T5[0x6][0xaa][0xf7][0x9f][0xfe].[0x1a][0x88]l?[0x90]i![0x89]#g[0x9e][0xe8][0xd6][0x1c][0xf0][0x0][0x9a][0xea][0x16][0xda][0xfd]i[0xa8]+[0x99][0xa8]ezZ[0xbb][0x8f]m[0xd4][0xd7]$[0x89]Q:_[0xf9][0xda][0x1b][0x3][0xbb]u[0x8b]-U[0xad]}+@{[0xb5][0xb6]d[0xd8][0xa2][0x83]F[0x9a][0x1b]C:[0xb7][0xc9][0x8f]#[0x88]~[0xad][0x84]~V[0xb1]G\[0x17][0xa7][0xc6]m[0xe0]k0[0xab][0x1b]kN[0xfb]GD[0xa6][0xda][0xbc][0xab][0xf6][0xd7][0xc5]~'[0x8f][0xe6][0x95]{[0xc7][0x1b][0xfd]q[0xbd][0x18][0xe6]H[0xd6]x{[0xc5][0xb7][0x13][0xca][0xb5]{[0x13][0xd4][0xde]1[0x1e]t[0x1f][0xfb][0xaa][0xe3]a[0xf5][0xa1][0xe6]@'[0xec][0x8a][0xab]9[0xe1]Hyb[0xfe][0xec][0xce][0xab][0xef][0x7]KJ~[0xa7][0xfa]{[0xf9][0xa9]Bu[0x11]S![0xd5][0xe0][0xb4][0xbd][0xf3]o[0x9f]M[0x1f]0[0xe5][0xce][0x4]d\7[0xaa]'[0xff][0xed]B[0xfb][0x97][0xc]B[0xab]#?-r[0xfa]A[0xf6][0xe4][0xa0]:B[0x16][0xc4][0x8a][0xb3][0xe5]a[0xdb][0x8f]*u[\r][0x9e][0x1c][0xa8][0x89][0xce][0x81][0xc7]v[0xe3][0xe0][0xb6]c[0x10][0xd5][0xd9][0xed][0xa8]3[0xb0]f[0x96][0xcc]/Y2[0xbf]tNU[0xd9][0xac]J[0xfa][0xe1][0x82][0x3]C4^[0xcd]#J[0x9a][0xcb][0x3]~Z[0xde]r[0xa3][0x9a][0xd4][0xf9][0xab][0x3]*2=[0xa2]{[0xab]y[0xc9][0x2][0xae][0xac][0xe5][0xb6][0xcd][0xa1][0xe0][0xb8]$[0x1c][0xd9][0xc1][0x1f][0xde]u[0x80][0xd7][0xe9]>[0xaa][0xa8][0xb][0xf9]kb[0xcb]t[0x1d][0xca][0x89][0xbf][0xb2][0xba]NmH[0xa6]P[0x94][0xdf][0xd2][0xd8][0x88]R[0x9]S[0xc8][0x91][[0x13][0x8][0xab][0x8b][0xc6][0x91][0x13][0xbf]8cin[0x9][0x8b][0xfe][0xda][0xc3][0xda][0x93][0x0]0[0x12][0xfa][0xd0]yzL[0xf5][0xd4][0x9e][0xc1][0xb7][0xa6]=[0x8b]i[0xa6]=[0x87][0xdf][0xcf]w[0xf8]~![0xee][0xfb]o[0xf8][0xf7]w[0xed]E[0xfc]~[0x9][0xbf][0xd3][0xf1][0xfd]2[0xe6][0xbc][0x12][0xd6][0xfe][0xa1][0xbd][0x1a][0xd6]^[0xc3][0xf4][0xeb][0xc9]J[0xdf][0xa0][0xd2]b[0xfa][0xcd]d[0xa5]oE[0xea][0xbe][0x9d][0xac][0xf4][0x9d]H[0xdd]w[0x93][0x95][0xbe][0x17])}?Y[0xe9][0x7][0x91][0xd2][0xf][0xb1][0x94]k[0x1f][0x1][0xfd][0xac][0xee][0xbf][0xf0][0xa1]#[0xcc][0xc7][0x98][0xee][0x0][0xff]I[0x4][0xfe][0xd3]d[0xd8]>[0xb3]K[0x5][0x9d]R[0xea]\[0xfa]9[0x95]~[0x81][0xa5]_&+[0xfd]O[0xa4][0xf4][0xab]h[0xa9]G[0x95]jT[0xfa]5[0x2]}[0x93][0xac][0xda]7T[0xed][L[0xa7]}[0xdf][0xb9][0xf4][0x7]*[0xfd]/[0xa6]LV[0xf7]'*[0xfd][0x19][0xd3][0xbb][0x93]5[0xf9][0xb][0x16][0xec]I[0xd6][0xcb]_#<[0xf8]-Y[0xb5]v`t[0xf9][0xae]S[0x1][0xc3][0x9]4[0xdd][0xad][0xed]\[0xc0][0xb1]@D[0xb]tU[0x80][0xaa]D[0xb7][0x6];[0xe5][0x19][0xcc][0xc4][0xa7]K[0xa5][0xdd][0x9]e[0x82]y[0x98][0x85][0xec]S"t R:[0x89]2[0x6][0x9d][0x1a][0x87])[0xad]CYz[0x12]j2[0x92][0xe4]e&[0xeb]N[0x16][0x16]d[0xb3][0x9c][0x18]0[0xcb]U[0xc0]=[0x90]<[0x16]%,[0x11]QO[0xe6]u[0xe0]sT[0x1e]G[0x16][0xb3]^[0xac][0xf7]"[0xd6][0x87][0xf5][0xc5][0x7][0x82][0xf4]K[0x6]rH[0x2][0x88][0x8f][0xf5][0xef][0xd4][0xea][0x0]6[0xb0]s[0xb5]A[0xaa][0x86][0xaa][0x8b] [0x83][0xd9][0x90][0xce] C[0x13]@[0x86]uR[0x1f][0x4][0x19]N[0xbd][0x1d][0x81][0xa5]y[0x9d][0xb4][0x84][0x14][0x84]J[0xf3][0xb1][0xb4][0xa0][0xcb][0xd2]B,-b[0x87]vn|dB[0xe3][0xa3][0xba][0x94][0x14][0xa3]k[0xed][0x9d][0x91][0xbf]F[0xc8]Gc[0xe9][0x18]6[0xb6][0x13]C[0xe]gGt[0xca][0x1b][0xc7][0x8a];[0xe5][0x8d]G[0xcc][0x90][0xde][0x93][0xae]H[0xe0]7r[0x88]n[0xd9][0xe3][0xf3]H[0xfc][0x9a][0xb][0xa8][0xe2][0xf8][0xee]5b3h#[0xc4]&`[0xf4][0xe0]#2[0xf1]))[0xa9][0xdf][0xaf]p[0x1c][0x85][0xcf]\[0x90][0xf8]<[0x11][0x4][0xb4][0x80][0xb][0xce][0x86]Tm[0x7]dk[0x1f][0xb3][0x12][0xcc][0xb5]l,l2[0x9b][0x82]j'[0xe8]F[0x9b][0xf3][0x9e][0xa6][0xde][0xd3][0xd9][0xc]l[0xd1][0xee]y[0x19]~[0xcf][0xec][0xf0]}4+[0x8f]~W[0xe0]we[0x87][0xf2]Ylv[0xf4][0xfb][0x18][0xfc][0x9e][0xc3][0xaa][0x14][0xde][0xb9]l[0x9e]z[0xcf]w[0xde][0xb][0x9c][0xf7][0xb1][0xce]{a[0x1c][0xde]E[0xf8]}[0x1c][0xd5][0x83]lv<;[0xc1][0xe1]@[0x11]RN[0xb4][0xcb][0x11][0xf][0x81]q_[0xb4][0xb3]:e2o\[0xe7][0xa4][0xdd]9[0xac][0xb6][0x18][0xd9][0xc7][0xa8]r[0xbf]I[0xc8][0x92]>[0x0]i[0x1b]G[0xb4][0x82][0xb9][0x15]\[0xb]7[0x83];[0xd3][0xd3][\n]"
[DEBUG] http-outgoing-0 << "[0x16]"K[0xc1]Wf*>Z![0xad][\r][0xd2][0x19]`^[0x6][0xe5][0xb7]A&[0x83][0xf2][\r]0[0xb4]b+d/[0x8c][0xd4][0xca]A[0xc8][0xfc]V[0xc8][0x8d][0xab][0xb5][0x19]zlG[0xc4]=+[0xf2]w[0xc2]q[0xf1]-xm[0xd8]^[\n]"
[DEBUG] http-outgoing-0 << "so[0x85][0xb9][0xcf]N[0x18][0x1c][0xf][0xd2][0xd7]&[0xa2]O[0xa4][0xfd]~[0xc]6@.I[0xf4][0x90][0x11][0xf9]6[0x15]>[0x6][0x9b][0xa0][0xf]V[0xea][0xbf][0x5][0x6]T[0x16][0xb4][0xc1]@[\r][0xd6]A[0x1a]a[0xc9][0x1c][0xa4][0xf0][0xde][0x8f][0x80][0x83][0xd7][0xc3][0xe8]-0$sh[0x1b][0xc]c[0x90]9\[0xbd][0xda]`[0x84]f?[0x8a][0xc5][0x8][0xaf][0xd8][0xc]yT"
Progress (1): maven-compiler-plugin-3.8.0.jar (12/62 kB) 
[DEBUG] http-outgoing-0 << "[0xcb]+Z!_5[0xb5][0x15][\n]"
[DEBUG] http-outgoing-0 << "[0x90][0x8a][0xc2]b[0xe1][0x15]XP[0xd4][\n]"
[DEBUG] http-outgoing-0 << "[0x87][0xb6][0xc2]HL?[0x4][0xa3]Z[0xe1]0[0x95][0xe0][0xad]0[0x9a]^[0xeb][0xe0]B|[0x8d][0xd9][0x2]c[0xd7][0xc1][0x19][0x94][0xca]<|[0xb][0x1c]Q,[0xbd][0xb2]X[0xf7][0xea][0x8f][0x8e]5[0xf8]X3[0xc7][0xcc]1n[0x82][0x85]^=[0xc7][0x1c]U[0xec][0xf2][0xba]2[0xc7][0xb5]B[0xf1]z[0x98][0x8c][0xa9][0xf1]*u[0x4][0xa6]&[0xa8]T![0xa6]&[0xaa][0xd4][0xa0][0xad]0)[0xca][0xdd]#[0x91][0xf]^[0x17][0xf1][0xf6][0xa8][0x8]oK[0xb6][0x9f]cj[0x1b][0xda]oS[0xd4][0x8c]iEl[0x94][0x98][0xd2][\n]"
[DEBUG] // more debugging
Progress (1): maven-compiler-plugin-3.8.0.jar (33/62 kB)
[DEBUG] http-outgoing-0 << "INF/maven/plugin.xml[0xed]=ks[0xdb]8[0x92][0xdf][0xf5]+x[0xb9][0xa9][0xd4]L[0x9d]E[0xc5][0xce]$[0x9b]I[0x1c][0xef]fc[0xcf][0xae]g[0xc7]I*[0xf6][0xee]^[0xd5][0xd5][0xd5][0x14],B[0x12]c[0x8a][0xd0][0x10][0xa4][0x1f][0x97][0xf2][0xbf][0xee][0xc6][0x83] E=LQ[0xf]OX[0x95][0x8a]E[0x12]h4[0x1a][0x8d]Fw[0xa3][0x81]>[0xfc][0xf3][0xed]8[0xf2][0xae]y"C[0x11][0xbf]}[0xb2][0xef]?{[0xe2][0xf1][0xb8]/[0x82]0[0x1e][0xbe]}[0xf2][0xcf][0x8b][0x9f][0xbb][0xaf][0x9e][0xfc][0xf9][0xa8][0xd3]9[0xfc][0x8f]n[0xd7][0xfb][0x1b][0x8f]y[0xc2]R[0x1e]x[0x97]w[0xde][0x98]][0xf3][0xb8];[0x89][0xb2]a[0x18]wS!"[0xe9]=[0xf7]_x[0xdd].[0x96]V[0xaf][0x8f]:[0x9e]w[0x18][0xb3]1?z7a[0xfd][0x11][0xf7][0xce][0xb0][0x8e][0xf7]^[0x8c]'a[0xc4][0x13][0xef][0x13][0x95]:[0xec]Q[0x11],[0x1b]p[0xd9]O[0xc2]I[\n]"
[DEBUG] // more debugging
Progress (1): maven-compiler-plugin-3.8.0.jar (61/62 kB)
[DEBUG] http-outgoing-0 << "[0xfa]LC[0x13]}[0x8a]z[0x1][0x0][0x0][0xe7][0x2][0x0][0x0]9[0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0xa4][0x81]][0xcb][0x0][0x0]org/apache/maven/plugin/CompilationFailureException.classPK[0x1][0x2][0x14][0x3][0x14][0x0][0x8][0x8][0x8][0x0][0x84][0x99][0xfa]L[0xce][0xb0]?8[\r][0x1][0x0][0x0][0xa6][0x1][0x0][0x0]*[0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0xa4][0x81]>[0xcd][0x0][0x0]org/apache/maven/plugin/CompilerMojo.classPK[0x1][0x2][0x14][0x3][0x14][0x0][0x8][0x8][0x8][0x0][0x84][0x99][0xfa]L[0xd4][0xd6][0x9f][0xb0][0x10][0x1][0x0][0x0][0xb6][0x1][0x0][0x0].[0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0xa4][0x81][0xa3][0xce][0x0][0x0]org/apache/maven/plugin/TestCompilerMojo.classPK[0x1][0x2][0x14][0x3][0x14][0x0][0x8][0x8][0x8][0x0][0x84][0x99][0xfa]L[0x11][0xec]<B[0x2][0x0][0x0]![0x4][0x0][0x0]=[0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0xa4][0x81][0xf][0xd0][0x0][0x0]org/apache/maven/plugin/compiler/AbstractCompilerMojo$1.classPK[0x1][0x2][0x14][0x3][0x14][0x0][0x8][0x8][0x8][0x0][0x84][0x99][0xfa]LaD[0xb][0x85]M[0x15][0x0][0x0]93[0x0][0x0]3[0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0xa4][0x81][0xbc][0xd2][0x0][0x0]org/apache/maven/plugin/compiler/CompilerMojo.classPK[0x5][0x6][0x0][0x0][0x0][0x0][0x1c][0x0][0x1c][0x0]o[0x9][0x0][0x0]j[0xe8][0x0][0x0][0x0][0x0]"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
Progress (1): maven-compiler-plugin-3.8.0.jar (62 kB)   
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/maven-compiler-plugin-3.8.0.jar.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/maven-compiler-plugin-3.8.0.jar.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/maven-compiler-plugin-3.8.0.jar.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "bb0f43f6d1550c896cd41c7df162e50f"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Thu, 26 Jul 2018 17:14:36 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: bb0f43f6d1550c896cd41c7df162e50f[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 730c476a99a9268b1ff60ec56c5d2eee313d6f97[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:52 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 1837523[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 37[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251812.324002,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "bb0f43f6d1550c896cd41c7df162e50f"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Thu, 26 Jul 2018 17:14:36 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: bb0f43f6d1550c896cd41c7df162e50f
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 730c476a99a9268b1ff60ec56c5d2eee313d6f97
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:52 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 1837523
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 37
[DEBUG] http-outgoing-0 << X-Timer: S1698251812.324002,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "a24b0fda131f69df4cbedbff79004a29b275c6e8"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
                                                     
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/maven-compiler-plugin-3.8.0.jar (62 kB at 804 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/apache/maven/plugins/maven-compiler-plugin/3.8.0/maven-compiler-plugin-3.8.0.jar.lastUpdated
[DEBUG] Resolving artifact org.sonarsource.scanner.maven:sonar-maven-plugin:pom:3.9.0.2155 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/org/sonarsource/scanner/maven/sonar-maven-plugin/3.9.0.2155/sonar-maven-plugin-3.9.0.2155.pom
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/sonarsource/scanner/maven/sonar-maven-plugin/3.9.0.2155/sonar-maven-plugin-3.9.0.2155.pom HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/sonarsource/scanner/maven/sonar-maven-plugin/3.9.0.2155/sonar-maven-plugin-3.9.0.2155.pom HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/sonarsource/scanner/maven/sonar-maven-plugin/3.9.0.2155/sonar-maven-plugin-3.9.0.2155.pom HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 11580[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "c09c4db00e31a22ac79cf7ca03de0a11"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/xml[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Thu, 29 Apr 2021 14:02:07 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: c09c4db00e31a22ac79cf7ca03de0a11[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 17a92f0e0c3c66676f70b7ccf08defb099db7401[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:52 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 114298[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 1[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251812.385157,VS0,VE1[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 11580
[DEBUG] http-outgoing-0 << ETag: "c09c4db00e31a22ac79cf7ca03de0a11"
[DEBUG] http-outgoing-0 << Content-Type: text/xml
[DEBUG] http-outgoing-0 << Last-Modified: Thu, 29 Apr 2021 14:02:07 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: c09c4db00e31a22ac79cf7ca03de0a11
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 17a92f0e0c3c66676f70b7ccf08defb099db7401
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:52 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 114298
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 1
[DEBUG] http-outgoing-0 << X-Timer: S1698251812.385157,VS0,VE1
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"[\n]"
[DEBUG] http-outgoing-0 << "  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">[\n]"
[DEBUG] http-outgoing-0 << "  <modelVersion>4.0.0</modelVersion>[\n]"
[DEBUG] http-outgoing-0 << "  <parent>[\n]"
[DEBUG] http-outgoing-0 << "    <groupId>org.sonarsource.parent</groupId>[\n]"
[DEBUG] http-outgoing-0 << "    <artifactId>parent</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "    <version>59.0.29</version>[\n]"
[DEBUG] http-outgoing-0 << "    <relativePath />[\n]"
[DEBUG] http-outgoing-0 << "  </parent>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <groupId>org.sonarsource.scanner.maven</groupId>[\n]"
[DEBUG] http-outgoing-0 << "  <artifactId>sonar-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "  <version>3.9.0.2155</version>[\n]"
[DEBUG] http-outgoing-0 << "  <packaging>maven-plugin</packaging>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <name>SonarQube Scanner for Maven</name>[\n]"
[DEBUG] http-outgoing-0 << "  <description>Trigger SonarQube analysis on Maven projects</description>[\n]"
[DEBUG] http-outgoing-0 << "  <url>http://sonarsource.github.io/sonar-scanner-maven/</url>[\n]"
[DEBUG] http-outgoing-0 << "  <inceptionYear>2009</inceptionYear>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <licenses>[\n]"
[DEBUG] http-outgoing-0 << "    <license>[\n]"
[DEBUG] http-outgoing-0 << "      <name>GNU LGPL 3</name>[\n]"
[DEBUG] http-outgoing-0 << "      <url>http://www.gnu.org/licenses/lgpl.txt</url>[\n]"
[DEBUG] http-outgoing-0 << "      <distribution>repo</distribution>[\n]"
[DEBUG] http-outgoing-0 << "    </license>[\n]"
[DEBUG] http-outgoing-0 << "  </licenses>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <developers>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>olamy</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Olivier Lamy</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>olamy@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>godin</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Evgeny Mandrikov</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>mandrikov@gmail.com</email>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+3</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>simon.brandhof</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Simon Brandhof</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>simon.brandhof@gmail.com</email>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>henryju</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Julien Henry</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>henryju@yahoo.fr</email>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "  </developers>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <prerequisites>[\n]"
[DEBUG] http-outgoing-0 << "    <maven>3.0</maven>[\n]"
[DEBUG] http-outgoing-0 << "  </prerequisites>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <scm>[\n]"
[DEBUG] http-outgoing-0 << "    <connection>scm:git:https://github.com/SonarSource/sonar-scanner-maven.git</connection>[\n]"
[DEBUG] http-outgoing-0 << "    <developerConnection>scm:git:ssh://git@github.com/SonarSource/sonar-scanner-maven.git</developerConnection>[\n]"
[DEBUG] http-outgoing-0 << "    <url>https://github.com/SonarSource/sonar-scanner-maven</url>[\n]"
[DEBUG] http-outgoing-0 << "    <tag>HEAD</tag>[\n]"
[DEBUG] http-outgoing-0 << "  </scm>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <issueManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <system>JIRA</system>[\n]"
[DEBUG] http-outgoing-0 << "    <url>http://jira.sonarsource.com/browse/MSONAR</url>[\n]"
[DEBUG] http-outgoing-0 << "  </issueManagement>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <distributionManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <site>[\n]"
[DEBUG] http-outgoing-0 << "      <id>github</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>GitHub</name>[\n]"
[DEBUG] http-outgoing-0 << "      <url>scm:git:git@github.com:SonarSource/sonar-scanner-maven.git</url>[\n]"
[DEBUG] http-outgoing-0 << "    </site>[\n]"
[DEBUG] http-outgoing-0 << "  </distributionManagement>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <properties>[\n]"
[DEBUG] http-outgoing-0 << "    <mojo.java.target>1.7</mojo.java.target>[\n]"
[DEBUG] http-outgoing-0 << "    <mavenVersion>3.5.2</mavenVersion>[\n]"
[DEBUG] http-outgoing-0 << "    <sonar.exclusions>src/main/java/org/apache/maven/shared/dependency/tree/DependencyTreeResolutionListener.java,target/generated-sources/**/*</sonar.exclusions>[\n]"
[DEBUG] http-outgoing-0 << "    <gitRepositoryName>sonar-scanner-maven</gitRepositoryName>[\n]"
[DEBUG] http-outgoing-0 << "  </properties>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <dependencies>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.apache.maven</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>maven-plugin-api</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>${mavenVersion}</version>[\n]"
[DEBUG] http-outgoing-0 << "      <scope>provided</scope>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.apache.maven</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>maven-model</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>${mavenVersion}</version>[\n]"
[DEBUG] http-outgoing-0 << "      <scope>provided</scope>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.apache.maven</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>maven-compat</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>${mavenVersion}</version>[\n]"
[DEBUG] http-outgoing-0 << "      <scope>provided</scope>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.apache.maven</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>maven-artifact</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>${mavenVersion}</version>[\n]"
[DEBUG] http-outgoing-0 << "      <scope>provided</scope>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.apache.maven</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>maven-core</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>${mavenVersion}</version>[\n]"
[DEBUG] http-outgoing-0 << "      <scope>provided</scope>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.sonatype.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>plexus-sec-dispatcher</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>1.4</version>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <!-- MSONAR-141 and MSONAR-171 -->[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>plexus-utils</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>3.2.1</version>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.apache.maven.plugin-tools</groupId>[\n]"
[DEBUG] http-outgoing-0 << "  "
Progress (1): sonar-maven-plugin-3.9.0.2155.pom (4.1/12 kB)
[DEBUG] http-outgoing-0 << "    <artifactId>maven-plugin-annotations</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>3.5</version>[\n]"
[DEBUG] http-outgoing-0 << "      <scope>provided</scope>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.sonarsource.scanner.api</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>sonar-scanner-api</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>2.16.1.361</version>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>commons-lang</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>commons-lang</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>2.6</version>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>com.google.code.findbugs</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>jsr305</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>2.0.3</version>[\n]"
[DEBUG] http-outgoing-0 << "      <scope>provided</scope>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>junit</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>junit</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>4.13.1</version>[\n]"
[DEBUG] http-outgoing-0 << "      <scope>test</scope>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.mockito</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>mockito-all</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>1.9.5</version>[\n]"
[DEBUG] http-outgoing-0 << "      <scope>test</scope>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.assertj</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>assertj-core</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>3.18.0</version>[\n]"
[DEBUG] http-outgoing-0 << "      <scope>test</scope>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.skyscreamer</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>jsonassert</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>1.2.3</version>[\n]"
[DEBUG] http-outgoing-0 << "      <scope>test</scope>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.apache.maven.plugin-testing</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>maven-plugin-testing-harness</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>3.3.0</version>[\n]"
[DEBUG] http-outgoing-0 << "      <scope>test</scope>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.mortbay.jetty</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>jetty</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>6.1.25</version>[\n]"
[DEBUG] http-outgoing-0 << "      <scope>test</scope>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "  </dependencies>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <build>[\n]"
[DEBUG] http-outgoing-0 << "    <pluginManagement>[\n]"
[DEBUG] http-outgoing-0 << "      <plugins>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-javadoc-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <source>8</source>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      </plugins>[\n]"
[DEBUG] http-outgoing-0 << "    </pluginManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-plugin-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <!-- see http://jira.codehaus.org/browse/MNG-5346 -->[\n]"
[DEBUG] http-outgoing-0 << "          <skipErrorNoDescriptorsFound>true</skipErrorNoDescriptorsFound>[\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>mojo-descriptor</id>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>descriptor</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "          <!-- if you want to generate help goal -->[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>help-goal</id>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>helpmojo</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>plexus-component-metadata</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>1.7</version>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>generate-metadata</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-release-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>2.5.3</version>[\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <goals>deploy site site:stage</goals>[\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-site-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>3.4</version>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>stage-for-scm-publish</id>[\n]"
[DEBUG] http-outgoing-0 << "            <phase>post-site</phase>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>stage</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <skipDeploy>true</skipDeploy>[\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-scm-publish-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>1.1</version>[\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <scmBranch>gh-"
Progress (1): sonar-maven-plugin-3.9.0.2155.pom (8.2/12 kB)
[DEBUG] http-outgoing-0 << "pages</scmBranch>[\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>scm-publish</id>[\n]"
[DEBUG] http-outgoing-0 << "            <phase>site-deploy</phase>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>publish-scm</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.jacoco</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>jacoco-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>0.8.4</version>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>pre-unit-test</id>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>prepare-agent</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>post-unit-test</id>[\n]"
[DEBUG] http-outgoing-0 << "            <phase>test</phase>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>report</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>pre-integration-test</id>[\n]"
[DEBUG] http-outgoing-0 << "            <phase>pre-integration-test</phase>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>prepare-agent</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\n]"
[DEBUG] http-outgoing-0 << "              <destFile>${project.build.directory}/jacoco-it.exec</destFile>[\n]"
[DEBUG] http-outgoing-0 << "              <propertyName>invoker.mavenOpts</propertyName>[\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>post-integration-test</id>[\n]"
[DEBUG] http-outgoing-0 << "            <phase>post-integration-test</phase>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>report</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\n]"
[DEBUG] http-outgoing-0 << "              <dataFile>${project.build.directory}/jacoco-it.exec</dataFile>[\n]"
[DEBUG] http-outgoing-0 << "              <outputDirectory>${project.reporting.outputDirectory}/jacoco-it</outputDirectory>[\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-invoker-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>3.2.1</version>[\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <cloneProjectsTo>${project.build.directory}/it</cloneProjectsTo>[\n]"
[DEBUG] http-outgoing-0 << "          <settingsFile>src/it/settings.xml</settingsFile>[\n]"
[DEBUG] http-outgoing-0 << "          <localRepositoryPath>target/local-repo</localRepositoryPath>[\n]"
[DEBUG] http-outgoing-0 << "          <postBuildHookScript>verify</postBuildHookScript>[\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>integration-test</id>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>install</goal>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>run</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "    </plugins>[\n]"
[DEBUG] http-outgoing-0 << "  </build>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <reporting>[\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-plugin-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>3.4</version>[\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <requirements>[\n]"
[DEBUG] http-outgoing-0 << "            <jdk>${mojo.java.target}</jdk>[\n]"
[DEBUG] http-outgoing-0 << "          </requirements>[\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-project-info-reports-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>2.8.1</version>[\n]"
[DEBUG] http-outgoing-0 << "        <reportSets>[\n]"
[DEBUG] http-outgoing-0 << "          <reportSet>[\n]"
[DEBUG] http-outgoing-0 << "            <reports>[\n]"
[DEBUG] http-outgoing-0 << "              <report>dependency-info</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>index</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>issue-tracking</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>project-team</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>scm</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>summary</report>[\n]"
[DEBUG] http-outgoing-0 << "            </reports>[\n]"
[DEBUG] http-outgoing-0 << "          </reportSet>[\n]"
[DEBUG] http-outgoing-0 << "        </reportSets>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "    </plugins>[\n]"
[DEBUG] http-outgoing-0 << "  </reporting>[\n]"
[DEBUG] http-outgoing-0 << "</project>[\n]"
Progress (1): sonar-maven-plugin-3.9.0.2155.pom (12 kB)    
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/sonarsource/scanner/maven/sonar-maven-plugin/3.9.0.2155/sonar-maven-plugin-3.9.0.2155.pom.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/sonarsource/scanner/maven/sonar-maven-plugin/3.9.0.2155/sonar-maven-plugin-3.9.0.2155.pom.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/sonarsource/scanner/maven/sonar-maven-plugin/3.9.0.2155/sonar-maven-plugin-3.9.0.2155.pom.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "0804fc6e284136d2fd6cac355b032c7f"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Thu, 29 Apr 2021 14:02:07 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 0804fc6e284136d2fd6cac355b032c7f[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 2c794b8594536d143dd4dbcab84836823bf70c98[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:52 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 1928819[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 1[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251812.415676,VS0,VE1[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "0804fc6e284136d2fd6cac355b032c7f"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Thu, 29 Apr 2021 14:02:07 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 0804fc6e284136d2fd6cac355b032c7f
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 2c794b8594536d143dd4dbcab84836823bf70c98
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:52 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 1928819
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 1
[DEBUG] http-outgoing-0 << X-Timer: S1698251812.415676,VS0,VE1
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "17a92f0e0c3c66676f70b7ccf08defb099db7401"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
                                                       
Downloaded from central: https://repo.maven.apache.org/maven2/org/sonarsource/scanner/maven/sonar-maven-plugin/3.9.0.2155/sonar-maven-plugin-3.9.0.2155.pom (12 kB at 313 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonarsource/scanner/maven/sonar-maven-plugin/3.9.0.2155/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonarsource/scanner/maven/sonar-maven-plugin/3.9.0.2155/sonar-maven-plugin-3.9.0.2155.pom.lastUpdated
[DEBUG] Resolving artifact org.sonarsource.parent:parent:pom:59.0.29 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/org/sonarsource/parent/parent/59.0.29/parent-59.0.29.pom
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/sonarsource/parent/parent/59.0.29/parent-59.0.29.pom HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/sonarsource/parent/parent/59.0.29/parent-59.0.29.pom HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/sonarsource/parent/parent/59.0.29/parent-59.0.29.pom HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 31694[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "ff00487e5675caa7629dcab47ba0964c"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/xml[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Wed, 28 Apr 2021 04:55:28 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: ff00487e5675caa7629dcab47ba0964c[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 16417adadfb28cbb1094184257cf952975c79e11[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:52 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 615476[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 36[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251812.435686,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 31694
[DEBUG] http-outgoing-0 << ETag: "ff00487e5675caa7629dcab47ba0964c"
[DEBUG] http-outgoing-0 << Content-Type: text/xml
[DEBUG] http-outgoing-0 << Last-Modified: Wed, 28 Apr 2021 04:55:28 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: ff00487e5675caa7629dcab47ba0964c
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 16417adadfb28cbb1094184257cf952975c79e11
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:52 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 615476
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 36
[DEBUG] http-outgoing-0 << X-Timer: S1698251812.435686,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "<?xml version="1.0" encoding="UTF-8"?>[\n]"
[DEBUG] http-outgoing-0 << "<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">[\n]"
[DEBUG] http-outgoing-0 << "  <modelVersion>4.0.0</modelVersion>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <groupId>org.sonarsource.parent</groupId>[\n]"
[DEBUG] http-outgoing-0 << "  <artifactId>parent</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "  <version>59.0.29</version>[\n]"
[DEBUG] http-outgoing-0 << "  <packaging>pom</packaging>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <name>SonarSource OSS parent</name>[\n]"
[DEBUG] http-outgoing-0 << "  <description>Parent pom of SonarSource public projects</description>[\n]"
[DEBUG] http-outgoing-0 << "  <url>http://docs.sonarqube.org/display/PLUG/Plugin+Library</url>[\n]"
[DEBUG] http-outgoing-0 << "  <inceptionYear>2009</inceptionYear>[\n]"
[DEBUG] http-outgoing-0 << "  <!-- License defined here, because most of projects uses this license. Can be overridden in children POMs. -->[\n]"
[DEBUG] http-outgoing-0 << "  <licenses>[\n]"
[DEBUG] http-outgoing-0 << "    <license>[\n]"
[DEBUG] http-outgoing-0 << "      <name>GNU LGPL 3</name>[\n]"
[DEBUG] http-outgoing-0 << "      <url>http://www.gnu.org/licenses/lgpl.txt</url>[\n]"
[DEBUG] http-outgoing-0 << "      <distribution>repo</distribution>[\n]"
[DEBUG] http-outgoing-0 << "    </license>[\n]"
[DEBUG] http-outgoing-0 << "  </licenses>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <developers>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>ehartmann</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Eric Hartmann</name>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>SonarSource</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>henryju</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Julien Henry</name>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>SonarSource</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>Godin</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Evgeny Mandrikov</name>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>SonarSource</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>olivier.gaudin</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Olivier Gaudin</name>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>SonarSource</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>simonbrandhof</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Simon Brandhof</name>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>SonarSource</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <timezone>+1</timezone>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "  </developers>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <mailingLists>[\n]"
[DEBUG] http-outgoing-0 << "    <mailingList>[\n]"
[DEBUG] http-outgoing-0 << "      <name>SonarSource Community</name>[\n]"
[DEBUG] http-outgoing-0 << "      <archive>https://community.sonarsource.com</archive>[\n]"
[DEBUG] http-outgoing-0 << "    </mailingList>[\n]"
[DEBUG] http-outgoing-0 << "  </mailingLists>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <scm>[\n]"
[DEBUG] http-outgoing-0 << "    <connection>scm:git:https://github.com/SonarSource/parent-oss.git</connection>[\n]"
[DEBUG] http-outgoing-0 << "    <developerConnection>scm:git:git@github.com:SonarSource/parent-oss.git</developerConnection>[\n]"
[DEBUG] http-outgoing-0 << "    <url>https://github.com/SonarSource/parent-oss</url>[\n]"
[DEBUG] http-outgoing-0 << "    <tag>HEAD</tag>[\n]"
[DEBUG] http-outgoing-0 << "  </scm>[\n]"
[DEBUG] http-outgoing-0 << "  <issueManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <system>jira</system>[\n]"
[DEBUG] http-outgoing-0 << "    <url>http://jira.sonarsource.com</url>[\n]"
[DEBUG] http-outgoing-0 << "  </issueManagement>[\n]"
[DEBUG] http-outgoing-0 << "  <ciManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <system>travis-ci</system>[\n]"
[DEBUG] http-outgoing-0 << "    <url>https://travis-ci.org/SonarSource/</url>[\n]"
[DEBUG] http-outgoing-0 << "  </ciManagement>[\n]"
[DEBUG] http-outgoing-0 << "  <distributionManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <repository>[\n]"
[DEBUG] http-outgoing-0 << "      <id>bintray</id>[\n]"
[DEBUG] http-outgoing-0 << "      <url>https://api.bintray.com/maven/sonarsource/SonarQube/${project.groupId}/;publish=1</url>[\n]"
[DEBUG] http-outgoing-0 << "    </repository>[\n]"
[DEBUG] http-outgoing-0 << "    <snapshotRepository>[\n]"
[DEBUG] http-outgoing-0 << "      <id>${sonar.snapshotRepository.id}</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Sonar plugins snapshot repository</name>[\n]"
[DEBUG] http-outgoing-0 << "      <uniqueVersion>false</uniqueVersion>[\n]"
[DEBUG] http-outgoing-0 << "      <url>${sonar.snapshotRepository.url}</url>[\n]"
[DEBUG] http-outgoing-0 << "    </snapshotRepository>[\n]"
[DEBUG] http-outgoing-0 << "  </distributionManagement>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <properties>[\n]"
[DEBUG] http-outgoing-0 << "    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>[\n]"
[DEBUG] http-outgoing-0 << "    <maven.min.version>3.3.9</maven.min.version>[\n]"
[DEBUG] http-outgoing-0 << "    <jdk.min.version>1.8</jdk.min.version>[\n]"
[DEBUG] http-outgoing-0 << "    <timestamp>${maven.build.timestamp}</timestamp>[\n]"
[DEBUG] http-outgoing-0 << "    <maven.build.timestamp.format>yyyy-MM-dd'T'HH:mm:ssZ</maven.build.timestamp.format>[\n]"
[DEBUG] http-outgoing-0 << "    <sonar.snapshotRepository.id>snapshot</sonar.snapshotRepository.id>[\n]"
[DEBUG] http-outgoing-0 << "    <sonar.snapshotRepository.url />[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <!-- ================ -->[\n]"
[DEBUG] http-outgoing-0 << "    <!-- Plugins versions -->[\n]"
[DEBUG] http-outgoing-0 << "    <!-- ================ -->[\n]"
[DEBUG] http-outgoing-0 << "    <version.assembly.plugin>3.1.1</version.assembly.plugin>[\n]"
[DEBUG] http-outgoing-0 << "    <version.clean.plugin>3.1.0</version.clean.plugin>[\n]"
[DEBUG] http-outgoing-0 << "    <version.compiler.plugin>3.8.1</version.compiler.plugin>[\n]"
[DEBUG] http-outgoing-0 << "    <version.dependency.plugin>3.1.1</version.dependency.plugin>[\n]"
[DEBUG] http-outgoing-0 << "    <version.deploy.plugin>3.0.0-M1</version.deploy.plugin>[\n]"
[DEBUG] http-outgoing-0 << "    <version.enforcer.plugin>3.0.0-M1</version.enforcer.plugin>[\n]"
[DEBUG] http-outgoing-0 << "    <version.surefire.plugin>2.22.2</version.surefire.plugin>[\n]"
[DEBUG] http-outgoing-0 << "    <version.failsafe.plugin>${version.surefire.plugin}</version.failsafe.plugin>[\n]"
[DEBUG] http-outgoing-0 << "    <version.install.plugin>3.0.0-M1</version.install.plugin>[\n]"
[DEBUG] http-outgoing-0 << "    <version.beanshell.plugin>1.4</version.beanshell.plugin>[\n]"
[DEBUG] http-outgoing-0 << "    <versio"
Progress (1): parent-59.0.29.pom (4.1/32 kB)
[DEBUG] http-outgoing-0 << "n.jar.plugin>3.1.2</version.jar.plugin>[\n]"
[DEBUG] http-outgoing-0 << "    <version.jarjar.plugin>1.9</version.jarjar.plugin>[\n]"
[DEBUG] http-outgoing-0 << "    <version.javadoc.plugin>3.1.0</version.javadoc.plugin>[\n]"
[DEBUG] http-outgoing-0 << "    <version.plugin.plugin>3.6.0</version.plugin.plugin>[\n]"
[DEBUG] http-outgoing-0 << "    <version.resources.plugin>3.1.0</version.resources.plugin>[\n]"
[DEBUG] http-outgoing-0 << "    <version.shade.plugin>3.2.1</version.shade.plugin>[\n]"
[DEBUG] http-outgoing-0 << "    <version.source.plugin>3.1.0</version.source.plugin>[\n]"
[DEBUG] http-outgoing-0 << "    <version.site.plugin>3.7.1</version.site.plugin>[\n]"
[DEBUG] http-outgoing-0 << "    <version.artifactory.plugin>2.6.1</version.artifactory.plugin>[\n]"
[DEBUG] http-outgoing-0 << "    <version.jacoco.plugin>0.8.6</version.jacoco.plugin>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <version.buildnumber.plugin>1.4</version.buildnumber.plugin>[\n]"
[DEBUG] http-outgoing-0 << "    <version.native2ascii.plugin>1.0-beta-1</version.native2ascii.plugin>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <version.sonar-packaging.plugin>1.18.0.372</version.sonar-packaging.plugin>[\n]"
[DEBUG] http-outgoing-0 << "    <version.sonar-dev.plugin>1.8</version.sonar-dev.plugin>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <version.codehaus.license.plugin>2.0.0</version.codehaus.license.plugin>[\n]"
[DEBUG] http-outgoing-0 << "    <version.mycila.license.plugin>3.0</version.mycila.license.plugin>[\n]"
[DEBUG] http-outgoing-0 << "    <version.gpg.plugin>0.3.1</version.gpg.plugin>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <!-- To configure maven-license-plugin to check license headers -->[\n]"
[DEBUG] http-outgoing-0 << "    <license.name>GNU LGPL v3</license.name>[\n]"
[DEBUG] http-outgoing-0 << "    <license.owner>SonarSource SA</license.owner>[\n]"
[DEBUG] http-outgoing-0 << "    <license.title>${project.name}</license.title>[\n]"
[DEBUG] http-outgoing-0 << "    <license.years>${project.inceptionYear}-2021</license.years>[\n]"
[DEBUG] http-outgoing-0 << "    <license.mailto>mailto:info AT sonarsource DOT com</license.mailto>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <gitRepositoryName>parent-oss</gitRepositoryName>[\n]"
[DEBUG] http-outgoing-0 << "  </properties>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <build>[\n]"
[DEBUG] http-outgoing-0 << "    <extensions>[\n]"
[DEBUG] http-outgoing-0 << "      <!-- See SONARPLUGINS-839 and SONARPLUGINS-840 -->[\n]"
[DEBUG] http-outgoing-0 << "      <extension>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.wagon</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>wagon-webdav</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>1.0-beta-2</version>[\n]"
[DEBUG] http-outgoing-0 << "      </extension>[\n]"
[DEBUG] http-outgoing-0 << "    </extensions>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <pluginManagement>[\n]"
[DEBUG] http-outgoing-0 << "      <plugins>[\n]"
[DEBUG] http-outgoing-0 << "        <!-- Plugins ordered by shortname (archetype, assembly ...) -->[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-assembly-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.assembly.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <archiverConfig>[\n]"
[DEBUG] http-outgoing-0 << "              <!-- Workaround for http://jira.codehaus.org/browse/MASSEMBLY-422 -->[\n]"
[DEBUG] http-outgoing-0 << "              <!-- 420(dec) = 644(oct) -->[\n]"
[DEBUG] http-outgoing-0 << "              <fileMode>420</fileMode>[\n]"
[DEBUG] http-outgoing-0 << "              <!-- 493(dec) = 755(oct) -->[\n]"
[DEBUG] http-outgoing-0 << "              <directoryMode>493</directoryMode>[\n]"
[DEBUG] http-outgoing-0 << "              <defaultDirectoryMode>493</defaultDirectoryMode>[\n]"
[DEBUG] http-outgoing-0 << "            </archiverConfig>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.mojo</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>buildnumber-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.buildnumber.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-clean-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.clean.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-compiler-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.compiler.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-dependency-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.dependency.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-deploy-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.deploy.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-enforcer-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.enforcer.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-failsafe-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.failsafe.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-install-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.install.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plug"
Progress (1): parent-59.0.29.pom (8.2/32 kB)
[DEBUG] http-outgoing-0 << "in>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <!--[\n]"
[DEBUG] http-outgoing-0 << "          This plugin is used to list the licenses of Maven dependencies.[\n]"
[DEBUG] http-outgoing-0 << "          Command-line is: mvn license:aggregate-add-third-party[\n]"
[DEBUG] http-outgoing-0 << "          -->[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.mojo</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>license-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.codehaus.license.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <failOnBlacklist>true</failOnBlacklist>[\n]"
[DEBUG] http-outgoing-0 << "            <failOnMissing>true</failOnMissing>[\n]"
[DEBUG] http-outgoing-0 << "            <includedScopes>compile</includedScopes>[\n]"
[DEBUG] http-outgoing-0 << "            <includedLicenses>apache_v2|bouncy_castle|bsd|cddl_gpl|cddl_v1|epl_v1|epl_v2|h2|jaxen|lgpl_v2_1|lgpl_v3|mit|public_domain|tmate</includedLicenses>[\n]"
[DEBUG] http-outgoing-0 << "            <licenseMerges>[\n]"
[DEBUG] http-outgoing-0 << "              <!-- keep values on a single line -->[\n]"
[DEBUG] http-outgoing-0 << "              <licenseMerge>agpl_v3|AGPL 3|GNU Affero General Public License (AGPL) version 3.0|AGPL 3.0</licenseMerge>[\n]"
[DEBUG] http-outgoing-0 << "              <licenseMerge>apache_v2|Apache 2|Apache 2.0|Apache Public License 2.0|The Apache Software License, Version 2.0|Apache License, Version 2.0|Apache License, version 2.0|Apache Software Licenses|Apache License Version 2.0|ASF 2.0|ASL, version 2|The Apache License, Version 2.0</licenseMerge>[\n]"
[DEBUG] http-outgoing-0 << "              <licenseMerge>bouncy_castle|Bouncy Castle Licence</licenseMerge>[\n]"
[DEBUG] http-outgoing-0 << "              <licenseMerge>bsd|BSD|BSD License|The BSD License|3-Clause BSD License|The BSD 3-Clause License|New BSD License|New BSD license|BSD 3-Clause|BSD-style license|Trilead Library License (BSD-Like)|Revised BSD|BSD style</licenseMerge>[\n]"
[DEBUG] http-outgoing-0 << "              <licenseMerge>cddl_gpl|Dual license: CDDL 1.0 and GPL v2|Dual license consisting of the CDDL v1.0 and GPL v2|CDDL+GPLv2|CDDL+GPL|CDDL+GPL License|Dual license: CDDL 1.1 and GPL v2|Dual license consisting of the CDDL v1.1 and GPL v2|CDDL1_1+GPLv2|Dual License: CDDL 1.0 and GPL V2 with Classpath Exception|CDDL + GPLv2 with classpath exception|CDDL/GPLv2+CE</licenseMerge>[\n]"
[DEBUG] http-outgoing-0 << "              <licenseMerge>cddl_v1|CDDL|CDDL 1.0|CDDL 1.1|COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Version 1.0|Common Development and Distribution License (CDDL) v1.0</licenseMerge>[\n]"
[DEBUG] http-outgoing-0 << "              <licenseMerge>epl_v1|EPL 1.0|Eclipse Public License 1.0|Eclipse Public License - v 1.0|Eclipse Public License, Version 1.0|Eclipse Public License v1.0|EPL</licenseMerge>[\n]"
[DEBUG] http-outgoing-0 << "              <licenseMerge>epl_v2|Eclipse Public License - v 2.0</licenseMerge>[\n]"
[DEBUG] http-outgoing-0 << "              <licenseMerge>gpl_v2|GPL 2|GNU General Public License (GPL) version 2.0|GPL 2.0|GNU General Public License (GPL)|GNU General Public Library</licenseMerge>[\n]"
[DEBUG] http-outgoing-0 << "              <licenseMerge>gpl_v2_cpe|GPL2 w/ CPE</licenseMerge>[\n]"
[DEBUG] http-outgoing-0 << "              <licenseMerge>gpl_v3|GPL 3|GNU General Public License (GPL) version 3.0|GNU General Public License, Version 3|GPL 3.0</licenseMerge>[\n]"
[DEBUG] http-outgoing-0 << "              <licenseMerge>h2|The H2 License, Version 1.0</licenseMerge>[\n]"
[DEBUG] http-outgoing-0 << "              <licenseMerge>jaxen|http://jaxen.codehaus.org/license.html|Jaxen</licenseMerge>[\n]"
[DEBUG] http-outgoing-0 << "              <licenseMerge>lgpl_v2|LGPL 2.0|GNU LGPL 2.0|GNU Lesser General Public License (LGPL), Version 2.0|GNU Lesser General Public License, version 2.0</licenseMerge>[\n]"
[DEBUG] http-outgoing-0 << "              <licenseMerge>lgpl_v2_1|LGPL 2.1|GNU LGPL 2.1|GNU Lesser General Public License (LGPL), Version 2.1|GNU Lesser General Public License, version 2.1|LGPL, version 2.1</licenseMerge>[\n]"
[DEBUG] http-outgoing-0 << "              <licenseMerge>lgpl_v3|LGPL 3|GNU LGPL 3|GNU LGPL v3|LGPL v.3|GNU LESSER GENERAL PUBLIC LICENSE, Version 3|GNU Lesser General Public Licence|GNU Lesser General Public License|GNU LESSER GENERAL PUBLIC LICENSE</licenseMerge>[\n]"
[DEBUG] http-outgoing-0 << "              <licenseMerge>mit|MIT|MIT License|MIT license|The MIT License|The MIT license</licenseMerge>[\n]"
[DEBUG] http-outgoing-0 << "              <licenseMerge>public_domain|Public Domain|Public Domain, per Creative Commons CC0|Common Public License Version 1.0</licenseMerge>[\n]"
[DEBUG] http-outgoing-0 << "              <licenseMerge>sonarsource|SonarSource|Commercial</licenseMerge>[\n]"
[DEBUG] http-outgoing-0 << "              <licenseMerge>tmate|TMate Open Source License</licenseMerge>[\n]"
[DEBUG] http-outgoing-0 << "            </licenseMerges>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.sonatype.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>jarjar-m"
Progress (1): parent-59.0.29.pom (12/32 kB) 
[DEBUG] http-outgoing-0 << "aven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.jarjar.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-jar-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.jar.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <archive>[\n]"
[DEBUG] http-outgoing-0 << "              <manifestEntries>[\n]"
[DEBUG] http-outgoing-0 << "                <Version>${project.version}</Version>[\n]"
[DEBUG] http-outgoing-0 << "                <!-- ${buildNumber} is the svn revision generated by the buildnumber-maven-plugin -->[\n]"
[DEBUG] http-outgoing-0 << "                <Implementation-Build>${buildNumber}</Implementation-Build>[\n]"
[DEBUG] http-outgoing-0 << "                <Build-Time>${timestamp}</Build-Time>[\n]"
[DEBUG] http-outgoing-0 << "              </manifestEntries>[\n]"
[DEBUG] http-outgoing-0 << "            </archive>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-javadoc-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.javadoc.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <quiet>true</quiet>[\n]"
[DEBUG] http-outgoing-0 << "            <doclint>none</doclint>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-plugin-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.plugin.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-resources-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.resources.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-shade-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.shade.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-source-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.source.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-surefire-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.surefire.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-site-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.site.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.mojo</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>native2ascii-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.native2ascii.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.sonarsource.sonar-packaging-maven-plugin</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>sonar-packaging-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.sonar-packaging.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.sonar</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>sonar-dev-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.sonar-dev.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>com.github.genthaler</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>beanshell-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.beanshell.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.simplify4u.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>sign-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${version.gpg.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      </plugins>[\n]"
[DEBUG] http-outgoing-0 << "    </pluginManagement>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.codehaus.mojo</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>buildnumber-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <phase>validate</phase>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>create</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <doCheck>false</doCheck>[\n]"
[DEBUG] http-outgoing-0 << "          <doUpdate>false</doUpdate>[\n]"
[DEBUG] http-outgoing-0 << "          <getRevisionOnlyOnce>true</getRevisionOnlyOnce>[\n]"
[DEBUG] http-outgoing-0 << "          <revisionOnScmFailure>0</revisionOnScmFailure>[\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-compiler-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <source>${jdk.min.version}</source>"
Progress (1): parent-59.0.29.pom (16/32 kB)
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "          <target>${jdk.min.version}</target>[\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-surefire-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <runOrder>random</runOrder>[\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-enforcer-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>enforce</id>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>enforce</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\n]"
[DEBUG] http-outgoing-0 << "              <rules>[\n]"
[DEBUG] http-outgoing-0 << "                <requireProperty>[\n]"
[DEBUG] http-outgoing-0 << "                  <property>project.description</property>[\n]"
[DEBUG] http-outgoing-0 << "                  <message>Project description must be specified (requirement for OSSRH).</message>[\n]"
[DEBUG] http-outgoing-0 << "                </requireProperty>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "                <requireProperty>[\n]"
[DEBUG] http-outgoing-0 << "                  <property>project.name</property>[\n]"
[DEBUG] http-outgoing-0 << "                  <message>Project name must be specified (requirement for OSSRH).</message>[\n]"
[DEBUG] http-outgoing-0 << "                </requireProperty>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "                <requireProperty>[\n]"
[DEBUG] http-outgoing-0 << "                  <property>project.url</property>[\n]"
[DEBUG] http-outgoing-0 << "                  <message>Project url must be specified (requirement for OSSRH).</message>[\n]"
[DEBUG] http-outgoing-0 << "                </requireProperty>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "                <requireProperty>[\n]"
[DEBUG] http-outgoing-0 << "                  <property>project.licenses</property>[\n]"
[DEBUG] http-outgoing-0 << "                  <message>At least on license must be specified (requirement for OSSRH).</message>[\n]"
[DEBUG] http-outgoing-0 << "                </requireProperty>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "                <requireProperty>[\n]"
[DEBUG] http-outgoing-0 << "                  <property>project.developers</property>[\n]"
[DEBUG] http-outgoing-0 << "                  <message>At least one developer must be specified (requirement for OSSRH).</message>[\n]"
[DEBUG] http-outgoing-0 << "                </requireProperty>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "                <requireProperty>[\n]"
[DEBUG] http-outgoing-0 << "                  <property>project.scm.connection</property>[\n]"
[DEBUG] http-outgoing-0 << "                  <message>scm section should be defined in project (requirement for OSSRH).</message>[\n]"
[DEBUG] http-outgoing-0 << "                </requireProperty>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "                <requireMavenVersion>[\n]"
[DEBUG] http-outgoing-0 << "                  <message>To build this project Maven ${maven.min.version} (or upper) is required. Please install it.[\n]"
[DEBUG] http-outgoing-0 << "                  </message>[\n]"
[DEBUG] http-outgoing-0 << "                  <version>${maven.min.version}</version>[\n]"
[DEBUG] http-outgoing-0 << "                </requireMavenVersion>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "                <requireJavaVersion>[\n]"
[DEBUG] http-outgoing-0 << "                  <message>To build this project JDK ${jdk.min.version} (or upper) is required. Please install it.[\n]"
[DEBUG] http-outgoing-0 << "                  </message>[\n]"
[DEBUG] http-outgoing-0 << "                  <version>${jdk.min.version}</version>[\n]"
[DEBUG] http-outgoing-0 << "                </requireJavaVersion>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "                <requirePluginVersions>[\n]"
[DEBUG] http-outgoing-0 << "                  <!--[\n]"
[DEBUG] http-outgoing-0 << "                  This rule produces warning under Maven 3.0 :[\n]"
[DEBUG] http-outgoing-0 << "                  "This rule is not compatible with the current version of Maven."[\n]"
[DEBUG] http-outgoing-0 << "                  -->[\n]"
[DEBUG] http-outgoing-0 << "                  <message>Build reproducibility : always define plugin versions!</message>[\n]"
[DEBUG] http-outgoing-0 << "                  <banLatest>true</banLatest>[\n]"
[DEBUG] http-outgoing-0 << "                  <banRelease>true</banRelease>[\n]"
[DEBUG] http-outgoing-0 << "                  <phases>clean,deploy</phases>[\n]"
[DEBUG] http-outgoing-0 << "                </requirePluginVersions>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "                <!-- TODO SONARPLUGINS-797[\n]"
[DEBUG] http-outgoing-0 << "                <DependencyConvergence />[\n]"
[DEBUG] http-outgoing-0 << "                -->[\n]"
[DEBUG] http-outgoing-0 << "              </rules>[\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-source-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>attach-sources</id>[\n]"
[DEBUG] http-outgoing-0 << "            <phase>verify</phase>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>jar-no-fork</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.sonarsource.sonar-packaging-maven-plugin</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>sonar-packaging-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <extensions>true</extensions>[\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <archive>[\n]"
[DEBUG] http-outgoing-0 << "            <manifestEntries>[\n]"
[DEBUG] http-outgoing-0 << "              <!-- ${buildNumber} is the svn revision generated by the buildnumber-maven-plugin -->[\n]"
[DEBUG] http-outgoing-0 << "              <Implementation-Build>${buildNumber}</Implementation-Build>[\n]"
[DEBUG] http-outgoing-0 << "              <Bui"
Progress (1): parent-59.0.29.pom (20/32 kB)
[DEBUG] http-outgoing-0 << "ld-Time>${timestamp}</Build-Time>[\n]"
[DEBUG] http-outgoing-0 << "            </manifestEntries>[\n]"
[DEBUG] http-outgoing-0 << "          </archive>[\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>com.mycila</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>license-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>${version.mycila.license.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "        <dependencies>[\n]"
[DEBUG] http-outgoing-0 << "          <dependency>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.sonarsource.license-headers</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>license-headers</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <version>1.3</version>[\n]"
[DEBUG] http-outgoing-0 << "          </dependency>[\n]"
[DEBUG] http-outgoing-0 << "        </dependencies>[\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <header>sonarsource/licenseheaders/${license.name}.txt</header>[\n]"
[DEBUG] http-outgoing-0 << "          <failIfMissing>true</failIfMissing>[\n]"
[DEBUG] http-outgoing-0 << "          <strictCheck>true</strictCheck>[\n]"
[DEBUG] http-outgoing-0 << "          <encoding>${project.build.sourceEncoding}</encoding>[\n]"
[DEBUG] http-outgoing-0 << "          <properties>[\n]"
[DEBUG] http-outgoing-0 << "            <license.owner>${license.owner}</license.owner>[\n]"
[DEBUG] http-outgoing-0 << "            <license.title>${license.title}</license.title>[\n]"
[DEBUG] http-outgoing-0 << "            <license.years>${license.years}</license.years>[\n]"
[DEBUG] http-outgoing-0 << "            <license.mailto>${license.mailto}</license.mailto>[\n]"
[DEBUG] http-outgoing-0 << "          </properties>[\n]"
[DEBUG] http-outgoing-0 << "          <mapping>[\n]"
[DEBUG] http-outgoing-0 << "            <java>SLASHSTAR_STYLE</java>[\n]"
[DEBUG] http-outgoing-0 << "            <js>SLASHSTAR_STYLE</js>[\n]"
[DEBUG] http-outgoing-0 << "            <ts>SLASHSTAR_STYLE</ts>[\n]"
[DEBUG] http-outgoing-0 << "            <tsx>SLASHSTAR_STYLE</tsx>[\n]"
[DEBUG] http-outgoing-0 << "            <css>SLASHSTAR_STYLE</css>[\n]"
[DEBUG] http-outgoing-0 << "            <less>SLASHSTAR_STYLE</less>[\n]"
[DEBUG] http-outgoing-0 << "          </mapping>[\n]"
[DEBUG] http-outgoing-0 << "          <includes>[\n]"
[DEBUG] http-outgoing-0 << "            <include>src/*/java/**/*.java</include>[\n]"
[DEBUG] http-outgoing-0 << "            <include>src/**/*.js</include>[\n]"
[DEBUG] http-outgoing-0 << "            <include>src/**/*.ts</include>[\n]"
[DEBUG] http-outgoing-0 << "            <include>src/**/*.tsx</include>[\n]"
[DEBUG] http-outgoing-0 << "            <include>src/**/*.css</include>[\n]"
[DEBUG] http-outgoing-0 << "            <include>src/**/*.less</include>[\n]"
[DEBUG] http-outgoing-0 << "            <include>tests/**/*.test.ts</include>[\n]"
[DEBUG] http-outgoing-0 << "          </includes>[\n]"
[DEBUG] http-outgoing-0 << "          <excludes>[\n]"
[DEBUG] http-outgoing-0 << "            <exclude>src/test/resources/**</exclude>[\n]"
[DEBUG] http-outgoing-0 << "          </excludes>[\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>enforce-license-headers</id>[\n]"
[DEBUG] http-outgoing-0 << "            <phase>validate</phase>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>check</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>com.github.genthaler</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>beanshell-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>compute-artifact-display-version</id>[\n]"
[DEBUG] http-outgoing-0 << "            <phase>generate-resources</phase>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>run</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\n]"
[DEBUG] http-outgoing-0 << "              <script><![CDATA[[\n]"
[DEBUG] http-outgoing-0 << "                if (! "${project.version}".endsWith("-SNAPSHOT")) {[\n]"
[DEBUG] http-outgoing-0 << "                  // example: "6.3.0.1234".[\n]"
[DEBUG] http-outgoing-0 << "                  fields = "${project.version}".split("\\.");[\n]"
[DEBUG] http-outgoing-0 << "                  if (fields.length == 4) {[\n]"
[DEBUG] http-outgoing-0 << "                    digitsCountToDisplay = 0;[\n]"
[DEBUG] http-outgoing-0 << "                    if ("0".equals(fields[2])) {[\n]"
[DEBUG] http-outgoing-0 << "                      digitsCountToDisplay = 2;[\n]"
[DEBUG] http-outgoing-0 << "                    } else {[\n]"
[DEBUG] http-outgoing-0 << "                      digitsCountToDisplay = 3;[\n]"
[DEBUG] http-outgoing-0 << "                    }[\n]"
[DEBUG] http-outgoing-0 << "                    sj = new StringJoiner(".");[\n]"
[DEBUG] http-outgoing-0 << "                    for (int i = 0; i < digitsCountToDisplay; ++i) { sj.add(fields[i]); }[\n]"
[DEBUG] http-outgoing-0 << "                    project.getProperties().setProperty("sonar.pluginDisplayVersion", sj.toString() + " (build " + fields[3] + ")");[\n]"
[DEBUG] http-outgoing-0 << "                  }[\n]"
[DEBUG] http-outgoing-0 << "                }[\n]"
[DEBUG] http-outgoing-0 << "                ]]>[\n]"
[DEBUG] http-outgoing-0 << "              </script>[\n]"
[DEBUG] http-outgoing-0 << "              <quiet>true</quiet>[\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "    </plugins>[\n]"
[DEBUG] http-outgoing-0 << "  </build>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <profiles>[\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\n]"
[DEBUG] http-outgoing-0 << "      <id>skipSanityChecks</id>[\n]"
[DEBUG] http-outgoing-0 << "      <activation>[\n]"
[DEBUG] http-outgoing-0 << "        <property>[\n]"
[DEBUG] http-outgoing-0 << "          <name>skipSanityChecks</name>[\n]"
[DEBUG] http-outgoing-0 << "          <value>true</value>[\n]"
[DEBUG] http-outgoing-0 << "        </property>[\n]"
[DEBUG] http-outgoing-0 << "      </activation>[\n]"
[DEBUG] http-outgoing-0 << "      <properties>[\n]"
[DEBUG] http-outgoing-0 << "        <license.skip>true</license.skip>[\n]"
[DEBUG] http-outgoing-0 << "        <enforcer.skip>true</enforcer.skip>[\n]"
[DEBUG] http-outgoing-0 << "      </properties>[\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\n]"
[DEBUG] http-outgoing-0 << "      <id>sign</id>      [\n]"
[DEBUG] http-outgoing-0 << "      <build>[\n]"
[DEBUG] http-outgoing-0 << "        <plugins>          [\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.simplify4u.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>sign-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <version>${version.gpg.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\n]"
[DEBUG] http-outgoing-0 << "      "
Progress (1): parent-59.0.29.pom (25/32 kB)
[DEBUG] http-outgoing-0 << "        <execution>[\n]"
[DEBUG] http-outgoing-0 << "                <id>sign-artifacts</id>[\n]"
[DEBUG] http-outgoing-0 << "                <phase>verify</phase>[\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\n]"
[DEBUG] http-outgoing-0 << "                  <goal>sign</goal>[\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\n]"
[DEBUG] http-outgoing-0 << "                <configuration>[\n]"
[DEBUG] http-outgoing-0 << "                  <keyPass>${env.PGP_PASSPHRASE}</keyPass>[\n]"
[DEBUG] http-outgoing-0 << "                </configuration>[\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        </plugins>[\n]"
[DEBUG] http-outgoing-0 << "      </build>[\n]"
[DEBUG] http-outgoing-0 << "    </profile>  [\n]"
[DEBUG] http-outgoing-0 << "    <!-- This profile is activated when a project is released. -->[\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\n]"
[DEBUG] http-outgoing-0 << "      <id>release</id>[\n]"
[DEBUG] http-outgoing-0 << "      <build>[\n]"
[DEBUG] http-outgoing-0 << "        <plugins>[\n]"
[DEBUG] http-outgoing-0 << "          <!-- Generates Javadoc -->[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-javadoc-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\n]"
[DEBUG] http-outgoing-0 << "                <id>attach-javadocs</id>[\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\n]"
[DEBUG] http-outgoing-0 << "                  <goal>jar</goal>[\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        </plugins>[\n]"
[DEBUG] http-outgoing-0 << "      </build>[\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\n]"
[DEBUG] http-outgoing-0 << "      <id>integration-tests</id>[\n]"
[DEBUG] http-outgoing-0 << "      <build>[\n]"
[DEBUG] http-outgoing-0 << "        <plugins>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-failsafe-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\n]"
[DEBUG] http-outgoing-0 << "                <id>integration-test</id>[\n]"
[DEBUG] http-outgoing-0 << "                <phase>integration-test</phase>[\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\n]"
[DEBUG] http-outgoing-0 << "                  <goal>integration-test</goal>[\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\n]"
[DEBUG] http-outgoing-0 << "                <id>verify</id>[\n]"
[DEBUG] http-outgoing-0 << "                <phase>verify</phase>[\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\n]"
[DEBUG] http-outgoing-0 << "                  <goal>verify</goal>[\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        </plugins>[\n]"
[DEBUG] http-outgoing-0 << "      </build>[\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\n]"
[DEBUG] http-outgoing-0 << "      <id>deploy-sonarsource</id>[\n]"
[DEBUG] http-outgoing-0 << "      <build>[\n]"
[DEBUG] http-outgoing-0 << "        <plugins>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-enforcer-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\n]"
[DEBUG] http-outgoing-0 << "                <id>enforce-deploy-settings</id>[\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\n]"
[DEBUG] http-outgoing-0 << "                  <goal>enforce</goal>[\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\n]"
[DEBUG] http-outgoing-0 << "                <configuration>[\n]"
[DEBUG] http-outgoing-0 << "                  <rules>[\n]"
[DEBUG] http-outgoing-0 << "                    <requireProperty>[\n]"
[DEBUG] http-outgoing-0 << "                      <property>gitRepositoryName</property>[\n]"
[DEBUG] http-outgoing-0 << "                      <message>You must set name of Git repository in your pom</message>[\n]"
[DEBUG] http-outgoing-0 << "                    </requireProperty>[\n]"
[DEBUG] http-outgoing-0 << "                    <requireEnvironmentVariable>[\n]"
[DEBUG] http-outgoing-0 << "                      <variableName>ARTIFACTORY_URL</variableName>[\n]"
[DEBUG] http-outgoing-0 << "                    </requireEnvironmentVariable>[\n]"
[DEBUG] http-outgoing-0 << "                    <requireEnvironmentVariable>[\n]"
[DEBUG] http-outgoing-0 << "                      <variableName>ARTIFACTORY_DEPLOY_REPO</variableName>[\n]"
[DEBUG] http-outgoing-0 << "                    </requireEnvironmentVariable>[\n]"
[DEBUG] http-outgoing-0 << "                    <requireEnvironmentVariable>[\n]"
[DEBUG] http-outgoing-0 << "                      <variableName>ARTIFACTORY_DEPLOY_USERNAME</variableName>[\n]"
[DEBUG] http-outgoing-0 << "                    </requireEnvironmentVariable>[\n]"
[DEBUG] http-outgoing-0 << "                    <requireEnvironmentVariable>[\n]"
[DEBUG] http-outgoing-0 << "                      <variableName>ARTIFACTORY_DEPLOY_PASSWORD</variableName>[\n]"
[DEBUG] http-outgoing-0 << "                    </requireEnvironmentVariable>[\n]"
[DEBUG] http-outgoing-0 << "                  </rules>[\n]"
[DEBUG] http-outgoing-0 << "                  <fail>true</fail>[\n]"
[DEBUG] http-outgoing-0 << "                </configuration>[\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <!-- this maven plugin is not deployed in Maven Central. It should be downloaded[\n]"
[DEBUG] http-outgoing-0 << "            from JFrog JCenter or from SonarSource repositories -->[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.jfrog.buildinfo</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>artifactory-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <version>${version.artifactory.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\n]"
[DEBUG] http-outgoing-0 << "                <id>build-info</id>[\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\n]"
[DEBUG] http-outgoing-0 << "                  <goal>publish</goal>[\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\n]"
[DEBUG] http-outgoing-0 << "                <configuration>[\n]"
[DEBUG] http-outgoing-0 << "                  <artifactory>[\n]"
[DEBUG] http-outgoing-0 << "                    <envVarsExcludePatterns>[\n]"
[DEBUG] http-outgoing-0 << "                      *password*,*PASSWORD*,*secret*,*MAVEN_CMD_LINE_ARGS*,sun.java.comm"
Progress (1): parent-59.0.29.pom (29/32 kB)
[DEBUG] http-outgoing-0 << "and,*token*,*TOKEN*,*LOGIN*,*login*,*SIGN_KEY*,*ARTIFACTORY_API_KEY*,*API_KEY*,*PASSPHRASE*[\n]"
[DEBUG] http-outgoing-0 << "                    </envVarsExcludePatterns>[\n]"
[DEBUG] http-outgoing-0 << "                    <includeEnvVars>true</includeEnvVars>[\n]"
[DEBUG] http-outgoing-0 << "                    <timeoutSec>60</timeoutSec>[\n]"
[DEBUG] http-outgoing-0 << "                  </artifactory>[\n]"
[DEBUG] http-outgoing-0 << "                  <deployProperties>[\n]"
[DEBUG] http-outgoing-0 << "                    <vcs.revision>{{GIT_COMMIT|TRAVIS_COMMIT|APPVEYOR_REPO_COMMIT|BUILD_SOURCEVERSION}}</vcs.revision>[\n]"
[DEBUG] http-outgoing-0 << "                    <vcs.branch>{{GIT_BRANCH|TRAVIS_BRANCH|APPVEYOR_REPO_BRANCH|SYSTEM_PULLREQUEST_TARGETBRANCH|BUILD_SOURCEBRANCHNAME}}</vcs.branch>[\n]"
[DEBUG] http-outgoing-0 << "                    <build.name>${gitRepositoryName}</build.name>[\n]"
[DEBUG] http-outgoing-0 << "                    <build.number>{{BUILD_ID|TRAVIS_BUILD_NUMBER|APPVEYOR_BUILD_NUMBER|BUILD_BUILDID}}</build.number>[\n]"
[DEBUG] http-outgoing-0 << "                  </deployProperties>[\n]"
[DEBUG] http-outgoing-0 << "                  <licenses>[\n]"
[DEBUG] http-outgoing-0 << "                    <autoDiscover>true</autoDiscover>[\n]"
[DEBUG] http-outgoing-0 << "                    <includePublishedArtifacts>false</includePublishedArtifacts>[\n]"
[DEBUG] http-outgoing-0 << "                    <runChecks>true</runChecks>[\n]"
[DEBUG] http-outgoing-0 << "                    <scopes>project,provided</scopes>[\n]"
[DEBUG] http-outgoing-0 << "                    <violationRecipients>licences-control@sonarsource.com</violationRecipients>[\n]"
[DEBUG] http-outgoing-0 << "                  </licenses>[\n]"
[DEBUG] http-outgoing-0 << "                  <publisher>[\n]"
[DEBUG] http-outgoing-0 << "                    <contextUrl>{{ARTIFACTORY_URL}}</contextUrl>[\n]"
[DEBUG] http-outgoing-0 << "                    <repoKey>{{ARTIFACTORY_DEPLOY_REPO}}</repoKey>[\n]"
[DEBUG] http-outgoing-0 << "                    <username>{{ARTIFACTORY_DEPLOY_USERNAME}}</username>[\n]"
[DEBUG] http-outgoing-0 << "                    <password>{{ARTIFACTORY_DEPLOY_PASSWORD}}</password>[\n]"
[DEBUG] http-outgoing-0 << "                    <publishBuildInfo>true</publishBuildInfo>[\n]"
[DEBUG] http-outgoing-0 << "                    <publishArtifacts>true</publishArtifacts>[\n]"
[DEBUG] http-outgoing-0 << "                  </publisher>[\n]"
[DEBUG] http-outgoing-0 << "                  <buildInfo>[\n]"
[DEBUG] http-outgoing-0 << "                    <buildName>${gitRepositoryName}</buildName>[\n]"
[DEBUG] http-outgoing-0 << "                    <buildNumber>{{BUILD_ID|TRAVIS_BUILD_NUMBER|APPVEYOR_BUILD_NUMBER|BUILD_BUILDID}}</buildNumber>[\n]"
[DEBUG] http-outgoing-0 << "                    <buildUrl>{{CI_BUILD_URL|BUILD_URL}}</buildUrl>[\n]"
[DEBUG] http-outgoing-0 << "                    <vcsRevision>{{GIT_COMMIT|TRAVIS_COMMIT|APPVEYOR_REPO_COMMIT|BUILD_SOURCEVERSION}}</vcsRevision>[\n]"
[DEBUG] http-outgoing-0 << "                  </buildInfo>[\n]"
[DEBUG] http-outgoing-0 << "                </configuration>[\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        </plugins>[\n]"
[DEBUG] http-outgoing-0 << "      </build>[\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\n]"
[DEBUG] http-outgoing-0 << "      <id>coverage</id>[\n]"
[DEBUG] http-outgoing-0 << "      <build>[\n]"
[DEBUG] http-outgoing-0 << "        <plugins>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "              <groupId>org.jacoco</groupId>[\n]"
[DEBUG] http-outgoing-0 << "              <artifactId>jacoco-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "              <version>${version.jacoco.plugin}</version>[\n]"
[DEBUG] http-outgoing-0 << "              <executions>[\n]"
[DEBUG] http-outgoing-0 << "                <execution>[\n]"
[DEBUG] http-outgoing-0 << "                  <id>prepare-agent</id>[\n]"
[DEBUG] http-outgoing-0 << "                  <goals>[\n]"
[DEBUG] http-outgoing-0 << "                    <goal>prepare-agent</goal>[\n]"
[DEBUG] http-outgoing-0 << "                  </goals>[\n]"
[DEBUG] http-outgoing-0 << "                </execution>[\n]"
[DEBUG] http-outgoing-0 << "                <execution>[\n]"
[DEBUG] http-outgoing-0 << "                  <id>report</id>[\n]"
[DEBUG] http-outgoing-0 << "                  <goals>[\n]"
[DEBUG] http-outgoing-0 << "                    <goal>report</goal>[\n]"
[DEBUG] http-outgoing-0 << "                  </goals>[\n]"
[DEBUG] http-outgoing-0 << "                </execution>[\n]"
[DEBUG] http-outgoing-0 << "              </executions>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        </plugins>[\n]"
[DEBUG] http-outgoing-0 << "      </build>[\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\n]"
[DEBUG] http-outgoing-0 << "  </profiles>[\n]"
[DEBUG] http-outgoing-0 << "</project>[\n]"
Progress (1): parent-59.0.29.pom (32 kB)   
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/sonarsource/parent/parent/59.0.29/parent-59.0.29.pom.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/sonarsource/parent/parent/59.0.29/parent-59.0.29.pom.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/sonarsource/parent/parent/59.0.29/parent-59.0.29.pom.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "45008de7e3dc574e20619e429a0d2be2"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Wed, 28 Apr 2021 04:55:28 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 45008de7e3dc574e20619e429a0d2be2[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: de1531ef89c637e01db9f6c3349295f0a317386f[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:52 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 32439[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 1[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251812.480931,VS0,VE1[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "45008de7e3dc574e20619e429a0d2be2"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Wed, 28 Apr 2021 04:55:28 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 45008de7e3dc574e20619e429a0d2be2
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: de1531ef89c637e01db9f6c3349295f0a317386f
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:52 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 32439
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 1
[DEBUG] http-outgoing-0 << X-Timer: S1698251812.480931,VS0,VE1
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "16417adadfb28cbb1094184257cf952975c79e11"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
                                        
Downloaded from central: https://repo.maven.apache.org/maven2/org/sonarsource/parent/parent/59.0.29/parent-59.0.29.pom (32 kB at 621 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonarsource/parent/parent/59.0.29/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonarsource/parent/parent/59.0.29/parent-59.0.29.pom.lastUpdated
[DEBUG] Resolving artifact org.sonarsource.scanner.maven:sonar-maven-plugin:jar:3.9.0.2155 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/org/sonarsource/scanner/maven/sonar-maven-plugin/3.9.0.2155/sonar-maven-plugin-3.9.0.2155.jar
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/sonarsource/scanner/maven/sonar-maven-plugin/3.9.0.2155/sonar-maven-plugin-3.9.0.2155.jar HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/sonarsource/scanner/maven/sonar-maven-plugin/3.9.0.2155/sonar-maven-plugin-3.9.0.2155.jar HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/sonarsource/scanner/maven/sonar-maven-plugin/3.9.0.2155/sonar-maven-plugin-3.9.0.2155.jar HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 49676[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "b3a8844fae58bc6284f378b14946fec1"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: application/java-archive[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Thu, 29 Apr 2021 14:02:07 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: b3a8844fae58bc6284f378b14946fec1[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: eaf3110985438a6516100c6af01809fd91b74879[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:52 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 1214436[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 1[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.516747,VS0,VE2[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 49676
[DEBUG] http-outgoing-0 << ETag: "b3a8844fae58bc6284f378b14946fec1"
[DEBUG] http-outgoing-0 << Content-Type: application/java-archive
[DEBUG] http-outgoing-0 << Last-Modified: Thu, 29 Apr 2021 14:02:07 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: b3a8844fae58bc6284f378b14946fec1
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: eaf3110985438a6516100c6af01809fd91b74879
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:52 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 1214436
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 1
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.516747,VS0,VE2
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "PK[0x3][0x4][0x14][0x0][0x0][0x8][0x8][0x0],l[0x9d]R[0x86][0x1c][0x97]&[0xa4][0x0][0x0][0x0][0xca][0x0][0x0][0x0][0x14][0x0][0x0][0x0]META-INF/MANIFEST.MF=[0x8e][0xcb][\n]"
[DEBUG] http-outgoing-0 << "[0xc2]0[0x14]D[0xf7][0x81][0xfc]C[0xf6][0x92][0x90][0x9b][0x87][0xb4][0xd9]YW[\n]"
[DEBUG] // more debugging 
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
Progress (1): sonar-maven-plugin-3.9.0.2155.jar (50 kB)   
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/sonarsource/scanner/maven/sonar-maven-plugin/3.9.0.2155/sonar-maven-plugin-3.9.0.2155.jar.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/sonarsource/scanner/maven/sonar-maven-plugin/3.9.0.2155/sonar-maven-plugin-3.9.0.2155.jar.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/sonarsource/scanner/maven/sonar-maven-plugin/3.9.0.2155/sonar-maven-plugin-3.9.0.2155.jar.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "7d7f1a009f00b6325ef2f7d9559778ed"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Thu, 29 Apr 2021 14:02:07 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 7d7f1a009f00b6325ef2f7d9559778ed[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: b75f3ae57eaae76df01499a4e91838f6931eb7a0[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:52 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 617816[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 1[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.624857,VS0,VE1[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "7d7f1a009f00b6325ef2f7d9559778ed"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Thu, 29 Apr 2021 14:02:07 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 7d7f1a009f00b6325ef2f7d9559778ed
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: b75f3ae57eaae76df01499a4e91838f6931eb7a0
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:52 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 617816
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 1
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.624857,VS0,VE1
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "eaf3110985438a6516100c6af01809fd91b74879"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
                                                       
Downloaded from central: https://repo.maven.apache.org/maven2/org/sonarsource/scanner/maven/sonar-maven-plugin/3.9.0.2155/sonar-maven-plugin-3.9.0.2155.jar (50 kB at 436 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonarsource/scanner/maven/sonar-maven-plugin/3.9.0.2155/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonarsource/scanner/maven/sonar-maven-plugin/3.9.0.2155/sonar-maven-plugin-3.9.0.2155.jar.lastUpdated
[DEBUG] Resolved plugin prefix sonar to org.sonarsource.scanner.maven:sonar-maven-plugin from POM com.example:your-project-name:jar:1.0-SNAPSHOT
[DEBUG] === REACTOR BUILD PLAN ================================================
[DEBUG] Project: com.example:your-project-name:jar:1.0-SNAPSHOT
[DEBUG] Tasks:   [sonar:sonar]
[DEBUG] Style:   Aggregating
[DEBUG] =======================================================================
[INFO] 
[INFO] -------------------< com.example:your-project-name >--------------------
[INFO] Building your-project-name 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[DEBUG] Resolving plugin prefix sonar from [org.apache.maven.plugins, org.codehaus.mojo]
[DEBUG] Resolved plugin prefix sonar to org.sonarsource.scanner.maven:sonar-maven-plugin from POM com.example:your-project-name:jar:1.0-SNAPSHOT
[DEBUG] Lifecycle default -> [validate, initialize, generate-sources, process-sources, generate-resources, process-resources, compile, process-classes, generate-test-sources, process-test-sources, generate-test-resources, process-test-resources, test-compile, process-test-classes, test, prepare-package, package, pre-integration-test, integration-test, post-integration-test, verify, install, deploy]
[DEBUG] Lifecycle clean -> [pre-clean, clean, post-clean]
[DEBUG] Lifecycle site -> [pre-site, site, post-site, site-deploy]
[DEBUG] === PROJECT BUILD PLAN ================================================
[DEBUG] Project:       com.example:your-project-name:1.0-SNAPSHOT
[DEBUG] Dependencies (collect): []
[DEBUG] Dependencies (resolve): [test]
[DEBUG] Repositories (dependencies): [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Repositories (plugins)     : [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] -----------------------------------------------------------------------
[DEBUG] Goal:          org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar (default-cli)
[DEBUG] Style:         Aggregating
[DEBUG] Configuration: <?xml version="1.0" encoding="UTF-8"?>
<configuration>
  <mojoExecution default-value="${mojoExecution}"/>
  <session default-value="${session}"/>
  <skip default-value="false">${sonar.skip}</skip>
</configuration>
[DEBUG] =======================================================================
[DEBUG] Resolving artifact junit:junit:pom:4.12 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/junit/junit/4.12/junit-4.12.pom
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/junit/junit/4.12/junit-4.12.pom HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/junit/junit/4.12/junit-4.12.pom HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/junit/junit/4.12/junit-4.12.pom HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 23678[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "af7ca61fba26556cfe5b40cf15aadc14"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/xml[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Thu, 04 Dec 2014 16:17:43 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: af7ca61fba26556cfe5b40cf15aadc14[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 35fb238baee3f3af739074d723279ebea2028398[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:52 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 2460879[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 2448[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.696993,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 23678
[DEBUG] http-outgoing-0 << ETag: "af7ca61fba26556cfe5b40cf15aadc14"
[DEBUG] http-outgoing-0 << Content-Type: text/xml
[DEBUG] http-outgoing-0 << Last-Modified: Thu, 04 Dec 2014 16:17:43 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: af7ca61fba26556cfe5b40cf15aadc14
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 35fb238baee3f3af739074d723279ebea2028398
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:52 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 2460879
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 2448
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.696993,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "<?xml version="1.0" encoding="UTF-8"?>[\n]"
[DEBUG] http-outgoing-0 << "<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">[\n]"
[DEBUG] http-outgoing-0 << "    <modelVersion>4.0.0</modelVersion>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <groupId>junit</groupId>[\n]"
[DEBUG] http-outgoing-0 << "    <artifactId>junit</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "    <version>4.12</version>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <name>JUnit</name>[\n]"
[DEBUG] http-outgoing-0 << "    <description>JUnit is a unit testing framework for Java, created by Erich Gamma and Kent Beck.</description>[\n]"
[DEBUG] http-outgoing-0 << "    <url>http://junit.org</url>[\n]"
[DEBUG] http-outgoing-0 << "    <inceptionYear>2002</inceptionYear>[\n]"
[DEBUG] http-outgoing-0 << "    <organization>[\n]"
[DEBUG] http-outgoing-0 << "        <name>JUnit</name>[\n]"
[DEBUG] http-outgoing-0 << "        <url>http://www.junit.org</url>[\n]"
[DEBUG] http-outgoing-0 << "    </organization>[\n]"
[DEBUG] http-outgoing-0 << "    <licenses>[\n]"
[DEBUG] http-outgoing-0 << "        <license>[\n]"
[DEBUG] http-outgoing-0 << "            <name>Eclipse Public License 1.0</name>[\n]"
[DEBUG] http-outgoing-0 << "            <url>http://www.eclipse.org/legal/epl-v10.html</url>[\n]"
[DEBUG] http-outgoing-0 << "            <distribution>repo</distribution>[\n]"
[DEBUG] http-outgoing-0 << "        </license>[\n]"
[DEBUG] http-outgoing-0 << "    </licenses>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <developers>[\n]"
[DEBUG] http-outgoing-0 << "        <developer>[\n]"
[DEBUG] http-outgoing-0 << "            <id>dsaff</id>[\n]"
[DEBUG] http-outgoing-0 << "            <name>David Saff</name>[\n]"
[DEBUG] http-outgoing-0 << "            <email>david@saff.net</email>[\n]"
[DEBUG] http-outgoing-0 << "        </developer>[\n]"
[DEBUG] http-outgoing-0 << "        <developer>[\n]"
[DEBUG] http-outgoing-0 << "            <id>kcooney</id>[\n]"
[DEBUG] http-outgoing-0 << "            <name>Kevin Cooney</name>[\n]"
[DEBUG] http-outgoing-0 << "            <email>kcooney@google.com</email>[\n]"
[DEBUG] http-outgoing-0 << "        </developer>[\n]"
[DEBUG] http-outgoing-0 << "        <developer>[\n]"
[DEBUG] http-outgoing-0 << "            <id>stefanbirkner</id>[\n]"
[DEBUG] http-outgoing-0 << "            <name>Stefan Birkner</name>[\n]"
[DEBUG] http-outgoing-0 << "            <email>mail@stefan-birkner.de</email>[\n]"
[DEBUG] http-outgoing-0 << "        </developer>[\n]"
[DEBUG] http-outgoing-0 << "        <developer>[\n]"
[DEBUG] http-outgoing-0 << "            <id>marcphilipp</id>[\n]"
[DEBUG] http-outgoing-0 << "            <name>Marc Philipp</name>[\n]"
[DEBUG] http-outgoing-0 << "            <email>mail@marcphilipp.de</email>[\n]"
[DEBUG] http-outgoing-0 << "        </developer>[\n]"
[DEBUG] http-outgoing-0 << "    </developers>[\n]"
[DEBUG] http-outgoing-0 << "    <contributors>[\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\n]"
[DEBUG] http-outgoing-0 << "            <name>JUnit contributors</name>[\n]"
[DEBUG] http-outgoing-0 << "            <organization>JUnit</organization>[\n]"
[DEBUG] http-outgoing-0 << "            <email>junit@yahoogroups.com</email>[\n]"
[DEBUG] http-outgoing-0 << "            <url>https://github.com/junit-team/junit/graphs/contributors</url>[\n]"
[DEBUG] http-outgoing-0 << "            <roles>[\n]"
[DEBUG] http-outgoing-0 << "                <role>developers</role>[\n]"
[DEBUG] http-outgoing-0 << "            </roles>[\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\n]"
[DEBUG] http-outgoing-0 << "    </contributors>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <mailingLists>[\n]"
[DEBUG] http-outgoing-0 << "        <mailingList>[\n]"
[DEBUG] http-outgoing-0 << "            <name>JUnit Mailing List</name>[\n]"
[DEBUG] http-outgoing-0 << "            <post>junit@yahoogroups.com</post>[\n]"
[DEBUG] http-outgoing-0 << "            <archive>https://groups.yahoo.com/neo/groups/junit/info</archive>[\n]"
[DEBUG] http-outgoing-0 << "        </mailingList>[\n]"
[DEBUG] http-outgoing-0 << "    </mailingLists>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <prerequisites>[\n]"
[DEBUG] http-outgoing-0 << "        <maven>3.0.4</maven>[\n]"
[DEBUG] http-outgoing-0 << "    </prerequisites>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <scm>[\n]"
[DEBUG] http-outgoing-0 << "        <connection>scm:git:git://github.com/junit-team/junit.git</connection>[\n]"
[DEBUG] http-outgoing-0 << "        <developerConnection>scm:git:git@github.com:junit-team/junit.git</developerConnection>[\n]"
[DEBUG] http-outgoing-0 << "        <url>http://github.com/junit-team/junit/tree/master</url>[\n]"
[DEBUG] http-outgoing-0 << "      <tag>r4.12</tag>[\n]"
[DEBUG] http-outgoing-0 << "  </scm>[\n]"
[DEBUG] http-outgoing-0 << "    <issueManagement>[\n]"
[DEBUG] http-outgoing-0 << "        <system>github</system>[\n]"
[DEBUG] http-outgoing-0 << "        <url>https://github.com/junit-team/junit/issues</url>[\n]"
[DEBUG] http-outgoing-0 << "    </issueManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <ciManagement>[\n]"
[DEBUG] http-outgoing-0 << "        <system>jenkins</system>[\n]"
[DEBUG] http-outgoing-0 << "        <url>https://junit.ci.cloudbees.com/</url>[\n]"
[DEBUG] http-outgoing-0 << "    </ciManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <distributionManagement>[\n]"
[DEBUG] http-outgoing-0 << "        <downloadUrl>https://github.com/junit-team/junit/wiki/Download-and-Install</downloadUrl>[\n]"
[DEBUG] http-outgoing-0 << "        <snapshotRepository>[\n]"
[DEBUG] http-outgoing-0 << "            <id>junit-snapshot-repo</id>[\n]"
[DEBUG] http-outgoing-0 << "            <name>Nexus Snapshot Repository</name>[\n]"
[DEBUG] http-outgoing-0 << "            <url>https://oss.sonatype.org/content/repositories/snapshots/</url>[\n]"
[DEBUG] http-outgoing-0 << "        </snapshotRepository>[\n]"
[DEBUG] http-outgoing-0 << "        <repository>[\n]"
[DEBUG] http-outgoing-0 << "            <id>junit-releases-repo</id>[\n]"
[DEBUG] http-outgoing-0 << "            <name>Nexus Release Repository</name>[\n]"
[DEBUG] http-outgoing-0 << "            <url>https://oss.sonatype.org/service/local/staging/deploy/maven2/</url>[\n]"
[DEBUG] http-outgoing-0 << "        </repository>[\n]"
[DEBUG] http-outgoing-0 << "        <site>[\n]"
[DEBUG] http-outgoing-0 << "            <id>junit.github.io</id>[\n]"
[DEBUG] http-outgoing-0 << "            <url>gitsite:git@github.com/junit-team/junit.git</url>[\n]"
[DEBUG] http-outgoing-0 << "        </site>[\n]"
[DEBUG] http-outgoing-0 << "    </distributionManagement>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <properties>[\n]"
[DEBUG] http-outgoing-0 << "        <jdkVersion>1.5</jdkVersion>[\n]"
[DEBUG] http-outgoing-0 << "        <project.build.sourceEncoding>ISO-8859-1</project.build.sourceEncoding>[\n]"
[DEBUG] http-outgoing-0 << "        <arguments />[\n]"
[DEBUG] http-outgoing-0 << "        <gpg.keyname>67893CC4</gpg.keyname>[\n]"
[DEBUG] http-outgoing-0 << "    </properties>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <dependencies>[\n]"
[DEBUG] http-outgoing-0 << "        <dependency>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.hamcrest</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>hamcrest-core</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <version>1.3</version>[\n]"
[DEBUG] http-outgoing-0 << "        </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    </dependencies>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <build>[\n]"
[DEBUG] http-outgoing-0 << "        <resources>[\n]"
[DEBUG] http-outgoing-0 << "            <resource>[\n]"
[DEBUG] http-outgoing-0 << "                <directory>${p"
Progress (1): junit-4.12.pom (4.1/24 kB)
[DEBUG] http-outgoing-0 << "roject.basedir}/src/main/resources</directory>[\n]"
[DEBUG] http-outgoing-0 << "            </resource>[\n]"
[DEBUG] http-outgoing-0 << "            <resource>[\n]"
[DEBUG] http-outgoing-0 << "                <directory>${project.basedir}</directory>[\n]"
[DEBUG] http-outgoing-0 << "                <includes>[\n]"
[DEBUG] http-outgoing-0 << "                    <include>LICENSE-junit.txt</include>[\n]"
[DEBUG] http-outgoing-0 << "                </includes>[\n]"
[DEBUG] http-outgoing-0 << "            </resource>[\n]"
[DEBUG] http-outgoing-0 << "        </resources>[\n]"
[DEBUG] http-outgoing-0 << "        <plugins>[\n]"
[DEBUG] http-outgoing-0 << "            <!--[\n]"
[DEBUG] http-outgoing-0 << "            Both "org.apache" and "org.codehaus" are default providers of MOJO plugins[\n]"
[DEBUG] http-outgoing-0 << "            which are especially dedicated to Maven projects.[\n]"
[DEBUG] http-outgoing-0 << "            The MOJO stands for "Maven plain Old Java Object".[\n]"
[DEBUG] http-outgoing-0 << "            Each mojo is an executable goal in Maven, and a plugin is a distribution of[\n]"
[DEBUG] http-outgoing-0 << "            one or more related mojos.[\n]"
[DEBUG] http-outgoing-0 << "            For more information see http://maven.apache.org/plugin-developers/index.html[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "            The following plugins are ordered according the Maven build lifecycle.[\n]"
[DEBUG] http-outgoing-0 << "            http://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html[\n]"
[DEBUG] http-outgoing-0 << "            -->[\n]"
[DEBUG] http-outgoing-0 << "            <plugin>[\n]"
[DEBUG] http-outgoing-0 << "                <!--[\n]"
[DEBUG] http-outgoing-0 << "                Checks that the version of user's maven installation is 3.0.4,[\n]"
[DEBUG] http-outgoing-0 << "                the JDK is 1.5+, no non-standard repositories are specified in[\n]"
[DEBUG] http-outgoing-0 << "                the project, requires only release versions of dependencies of other artifacts.[\n]"
[DEBUG] http-outgoing-0 << "                -->[\n]"
[DEBUG] http-outgoing-0 << "                <artifactId>maven-enforcer-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                <version>1.3.1</version>[\n]"
[DEBUG] http-outgoing-0 << "                <executions>[\n]"
[DEBUG] http-outgoing-0 << "                    <execution>[\n]"
[DEBUG] http-outgoing-0 << "                        <id>enforce-versions</id>[\n]"
[DEBUG] http-outgoing-0 << "                        <phase>initialize</phase>[\n]"
[DEBUG] http-outgoing-0 << "                        <goals>[\n]"
[DEBUG] http-outgoing-0 << "                            <goal>enforce</goal>[\n]"
[DEBUG] http-outgoing-0 << "                        </goals>[\n]"
[DEBUG] http-outgoing-0 << "                        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "                            <fail>true</fail>[\n]"
[DEBUG] http-outgoing-0 << "                            <rules>[\n]"
[DEBUG] http-outgoing-0 << "                                <requireMavenVersion>[\n]"
[DEBUG] http-outgoing-0 << "                                    <!-- Some plugin features require a recent Maven runtime to work properly -->[\n]"
[DEBUG] http-outgoing-0 << "                                    <message>Current version of Maven ${maven.version} required to build the project[\n]"
[DEBUG] http-outgoing-0 << "                                        should be ${project.prerequisites.maven}, or higher![\n]"
[DEBUG] http-outgoing-0 << "                                    </message>[\n]"
[DEBUG] http-outgoing-0 << "                                    <version>[${project.prerequisites.maven},)</version>[\n]"
[DEBUG] http-outgoing-0 << "                                </requireMavenVersion>[\n]"
[DEBUG] http-outgoing-0 << "                                <requireJavaVersion>[\n]"
[DEBUG] http-outgoing-0 << "                                    <message>Current JDK version ${java.version} should be ${jdkVersion}, or higher![\n]"
[DEBUG] http-outgoing-0 << "                                    </message>[\n]"
[DEBUG] http-outgoing-0 << "                                    <version>${jdkVersion}</version>[\n]"
[DEBUG] http-outgoing-0 << "                                </requireJavaVersion>[\n]"
[DEBUG] http-outgoing-0 << "                                <requireNoRepositories>[\n]"
[DEBUG] http-outgoing-0 << "                                    <message>Best Practice is to never define repositories in pom.xml (use a repository[\n]"
[DEBUG] http-outgoing-0 << "                                        manager instead).[\n]"
[DEBUG] http-outgoing-0 << "                                    </message>[\n]"
[DEBUG] http-outgoing-0 << "                                </requireNoRepositories>[\n]"
[DEBUG] http-outgoing-0 << "                                <requireReleaseDeps>[\n]"
[DEBUG] http-outgoing-0 << "                                    <message>No Snapshots Dependencies Allowed!</message>[\n]"
[DEBUG] http-outgoing-0 << "                                </requireReleaseDeps>[\n]"
[DEBUG] http-outgoing-0 << "                            </rules>[\n]"
[DEBUG] http-outgoing-0 << "                        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "                    </execution>[\n]"
[DEBUG] http-outgoing-0 << "                </executions>[\n]"
[DEBUG] http-outgoing-0 << "            </plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <plugin>[\n]"
[DEBUG] http-outgoing-0 << "                <!--[\n]"
[DEBUG] http-outgoing-0 << "                Updates Version#id().[\n]"
[DEBUG] http-outgoing-0 << "                -->[\n]"
[DEBUG] http-outgoing-0 << "                <groupId>com.google.code.maven-replacer-plugin</groupId>[\n]"
[DEBUG] http-outgoing-0 << "                <artifactId>replacer</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                <version>1.5.3</version>[\n]"
[DEBUG] http-outgoing-0 << "                <executions>[\n]"
[DEBUG] http-outgoing-0 << "                    <execution>[\n]"
[DEBUG] http-outgoing-0 << "                        <phase>process-sources</phase>[\n]"
[DEBUG] http-outgoing-0 << "                        <goals>[\n]"
[DEBUG] http-outgoing-0 << "                            <goal>replace</goal>[\n]"
[DEBUG] http-outgoing-0 << "                        </goals>[\n]"
[DEBUG] http-outgoing-0 << "                    </execution>[\n]"
[DEBUG] http-outgoing-0 << "                </executions>[\n]"
[DEBUG] http-outgoing-0 << "                <configuration>[\n]"
[DEBUG] http-outgoing-0 << "                    <ignoreMissingFile>false</ignoreMissingFile>[\n]"
[DEBUG] http-outgoing-0 << "                    <file>src/main/java/junit/runner/V"
Progress (1): junit-4.12.pom (8.2/24 kB)
[DEBUG] http-outgoing-0 << "ersion.java.template</file>[\n]"
[DEBUG] http-outgoing-0 << "                    <outputFile>src/main/java/junit/runner/Version.java</outputFile>[\n]"
[DEBUG] http-outgoing-0 << "                    <regex>false</regex>[\n]"
[DEBUG] http-outgoing-0 << "                    <token>@version@</token>[\n]"
[DEBUG] http-outgoing-0 << "                    <value>${project.version}</value>[\n]"
[DEBUG] http-outgoing-0 << "                </configuration>[\n]"
[DEBUG] http-outgoing-0 << "            </plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <plugin><!-- Using jdk 1.5.0_22, package-info.java files are compiled correctly. -->[\n]"
[DEBUG] http-outgoing-0 << "                <!--[\n]"
[DEBUG] http-outgoing-0 << "                java compiler plugin forked in extra process[\n]"
[DEBUG] http-outgoing-0 << "                -->[\n]"
[DEBUG] http-outgoing-0 << "                <artifactId>maven-compiler-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                <version>3.1</version>[\n]"
[DEBUG] http-outgoing-0 << "                <configuration>[\n]"
[DEBUG] http-outgoing-0 << "                    <encoding>${project.build.sourceEncoding}</encoding>[\n]"
[DEBUG] http-outgoing-0 << "                    <source>${jdkVersion}</source>[\n]"
[DEBUG] http-outgoing-0 << "                    <target>${jdkVersion}</target>[\n]"
[DEBUG] http-outgoing-0 << "                    <testSource>${jdkVersion}</testSource>[\n]"
[DEBUG] http-outgoing-0 << "                    <testTarget>${jdkVersion}</testTarget>[\n]"
[DEBUG] http-outgoing-0 << "                    <compilerVersion>1.5</compilerVersion>[\n]"
[DEBUG] http-outgoing-0 << "                    <showDeprecation>true</showDeprecation>[\n]"
[DEBUG] http-outgoing-0 << "                    <showWarnings>true</showWarnings>[\n]"
[DEBUG] http-outgoing-0 << "                    <debug>true</debug>[\n]"
[DEBUG] http-outgoing-0 << "                    <fork>true</fork>[\n]"
[DEBUG] http-outgoing-0 << "                    <compilerArgs>[\n]"
[DEBUG] http-outgoing-0 << "                        <arg>-Xlint:unchecked</arg>[\n]"
[DEBUG] http-outgoing-0 << "                    </compilerArgs>[\n]"
[DEBUG] http-outgoing-0 << "                    <maxmem>128m</maxmem>[\n]"
[DEBUG] http-outgoing-0 << "                </configuration>[\n]"
[DEBUG] http-outgoing-0 << "            </plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <plugin>[\n]"
[DEBUG] http-outgoing-0 << "                <groupId>org.codehaus.mojo</groupId>[\n]"
[DEBUG] http-outgoing-0 << "                <artifactId>animal-sniffer-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                <version>1.11</version>[\n]"
[DEBUG] http-outgoing-0 << "                <executions>[\n]"
[DEBUG] http-outgoing-0 << "                    <execution>[\n]"
[DEBUG] http-outgoing-0 << "                        <id>signature-check</id>[\n]"
[DEBUG] http-outgoing-0 << "                        <phase>test</phase>[\n]"
[DEBUG] http-outgoing-0 << "                        <goals>[\n]"
[DEBUG] http-outgoing-0 << "                            <goal>check</goal>[\n]"
[DEBUG] http-outgoing-0 << "                        </goals>[\n]"
[DEBUG] http-outgoing-0 << "                        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "                            <signature>[\n]"
[DEBUG] http-outgoing-0 << "                                <groupId>org.codehaus.mojo.signature</groupId>[\n]"
[DEBUG] http-outgoing-0 << "                                <artifactId>java15</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                                <version>1.0</version>[\n]"
[DEBUG] http-outgoing-0 << "                            </signature>[\n]"
[DEBUG] http-outgoing-0 << "                        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "                    </execution>[\n]"
[DEBUG] http-outgoing-0 << "                </executions>[\n]"
[DEBUG] http-outgoing-0 << "            </plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <plugin>[\n]"
[DEBUG] http-outgoing-0 << "                <!--[\n]"
[DEBUG] http-outgoing-0 << "                A plugin which uses the JUnit framework in order to start[\n]"
[DEBUG] http-outgoing-0 << "                our junit suite "AllTests" after the sources are compiled.[\n]"
[DEBUG] http-outgoing-0 << "                -->[\n]"
[DEBUG] http-outgoing-0 << "                <artifactId>maven-surefire-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                <version>2.17</version>[\n]"
[DEBUG] http-outgoing-0 << "                <configuration>[\n]"
[DEBUG] http-outgoing-0 << "                    <test>org/junit/tests/AllTests.java</test>[\n]"
[DEBUG] http-outgoing-0 << "                    <useSystemClassLoader>true</useSystemClassLoader>[\n]"
[DEBUG] http-outgoing-0 << "                    <enableAssertions>false</enableAssertions>[\n]"
[DEBUG] http-outgoing-0 << "                </configuration>[\n]"
[DEBUG] http-outgoing-0 << "            </plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <plugin>[\n]"
[DEBUG] http-outgoing-0 << "                <!--[\n]"
[DEBUG] http-outgoing-0 << "                This plugin can package the main artifact's sources (src/main/java)[\n]"
[DEBUG] http-outgoing-0 << "                in to jar archive. See target/junit-*-sources.jar.[\n]"
[DEBUG] http-outgoing-0 << "                -->[\n]"
[DEBUG] http-outgoing-0 << "                <artifactId>maven-source-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                <version>2.2.1</version>[\n]"
[DEBUG] http-outgoing-0 << "            </plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <plugin>[\n]"
[DEBUG] http-outgoing-0 << "                <!--[\n]"
[DEBUG] http-outgoing-0 << "                This plugin can generate Javadoc by a forked[\n]"
[DEBUG] http-outgoing-0 << "                process and then package the Javadoc[\n]"
[DEBUG] http-outgoing-0 << "                in jar archive target/junit-*-javadoc.jar.[\n]"
[DEBUG] http-outgoing-0 << "                -->[\n]"
[DEBUG] http-outgoing-0 << "                <artifactId>maven-javadoc-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                <version>2.9.1</version>[\n]"
[DEBUG] http-outgoing-0 << "                <configuration>[\n]"
[DEBUG] http-outgoing-0 << "                    <stylesheetfile>${basedir}/src/main/javadoc/stylesheet.css</stylesheetfile>[\n]"
[DEBUG] http-outgoing-0 << "                    <show>protected</show>[\n]"
[DEBUG] http-outgoing-0 << "                    <author>false</author>[\n]"
[DEBUG] http-outgoing-0 << "                    <version>false</version>[\n]"
[DEBUG] http-outgoing-0 << "                    <detectLinks>false</detectLinks>[\n]"
[DEBUG] http-outgoing-0 << "                    <linksource>true</linksource>[\n]"
[DEBUG] http-outgoing-0 << "                    <keywords>true</keywords>[\n]"
[DEBUG] http-outgoing-0 << "                    <use>false</use>[\n]"
[DEBUG] http-outgoing-0 << "                    <windowtitle>JUnit API</windowtitle>[\n]"
[DEBUG] http-outgoing-0 << "                    <en"
Progress (1): junit-4.12.pom (12/24 kB) 
[DEBUG] http-outgoing-0 << "coding>UTF-8</encoding>[\n]"
[DEBUG] http-outgoing-0 << "                    <locale>en</locale>[\n]"
[DEBUG] http-outgoing-0 << "                    <javadocVersion>${jdkVersion}</javadocVersion>[\n]"
[DEBUG] http-outgoing-0 << "                    <javaApiLinks>[\n]"
[DEBUG] http-outgoing-0 << "                        <property>[\n]"
[DEBUG] http-outgoing-0 << "                            <name>api_${jdkVersion}</name>[\n]"
[DEBUG] http-outgoing-0 << "                            <value>http://docs.oracle.com/javase/${jdkVersion}.0/docs/api/</value>[\n]"
[DEBUG] http-outgoing-0 << "                        </property>[\n]"
[DEBUG] http-outgoing-0 << "                    </javaApiLinks>[\n]"
[DEBUG] http-outgoing-0 << "                    <excludePackageNames>junit.*,*.internal.*</excludePackageNames>[\n]"
[DEBUG] http-outgoing-0 << "                    <verbose>true</verbose>[\n]"
[DEBUG] http-outgoing-0 << "                    <minmemory>32m</minmemory>[\n]"
[DEBUG] http-outgoing-0 << "                    <maxmemory>128m</maxmemory>[\n]"
[DEBUG] http-outgoing-0 << "                    <failOnError>true</failOnError>[\n]"
[DEBUG] http-outgoing-0 << "                    <includeDependencySources>true</includeDependencySources>[\n]"
[DEBUG] http-outgoing-0 << "                    <dependencySourceIncludes>[\n]"
[DEBUG] http-outgoing-0 << "                        <dependencySourceInclude>org.hamcrest:hamcrest-core:*</dependencySourceInclude>[\n]"
[DEBUG] http-outgoing-0 << "                    </dependencySourceIncludes>[\n]"
[DEBUG] http-outgoing-0 << "                </configuration>[\n]"
[DEBUG] http-outgoing-0 << "            </plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <plugin>[\n]"
[DEBUG] http-outgoing-0 << "                <artifactId>maven-release-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                <version>2.5</version>[\n]"
[DEBUG] http-outgoing-0 << "                <configuration>[\n]"
[DEBUG] http-outgoing-0 << "                    <mavenExecutorId>forked-path</mavenExecutorId>[\n]"
[DEBUG] http-outgoing-0 << "                    <useReleaseProfile>false</useReleaseProfile>[\n]"
[DEBUG] http-outgoing-0 << "                    <arguments>-Pgenerate-docs,junit-release ${arguments}</arguments>[\n]"
[DEBUG] http-outgoing-0 << "                    <tagNameFormat>r@{project.version}</tagNameFormat>[\n]"
[DEBUG] http-outgoing-0 << "                </configuration>[\n]"
[DEBUG] http-outgoing-0 << "            </plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <plugin>[\n]"
[DEBUG] http-outgoing-0 << "                <artifactId>maven-site-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                <version>3.3</version>[\n]"
[DEBUG] http-outgoing-0 << "                <dependencies>[\n]"
[DEBUG] http-outgoing-0 << "                    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "                        <groupId>com.github.stephenc.wagon</groupId>[\n]"
[DEBUG] http-outgoing-0 << "                        <artifactId>wagon-gitsite</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                        <version>0.4.1</version>[\n]"
[DEBUG] http-outgoing-0 << "                    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "                    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "                        <groupId>org.apache.maven.doxia</groupId>[\n]"
[DEBUG] http-outgoing-0 << "                        <artifactId>doxia-module-markdown</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                        <version>1.5</version>[\n]"
[DEBUG] http-outgoing-0 << "                    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "                </dependencies>[\n]"
[DEBUG] http-outgoing-0 << "            </plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <plugin>[\n]"
[DEBUG] http-outgoing-0 << "                <artifactId>maven-jar-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                <version>2.4</version>[\n]"
[DEBUG] http-outgoing-0 << "                <configuration>[\n]"
[DEBUG] http-outgoing-0 << "                    <archive>[\n]"
[DEBUG] http-outgoing-0 << "                        <addMavenDescriptor>false</addMavenDescriptor>[\n]"
[DEBUG] http-outgoing-0 << "                        <manifest>[\n]"
[DEBUG] http-outgoing-0 << "                            <addDefaultImplementationEntries>true</addDefaultImplementationEntries>[\n]"
[DEBUG] http-outgoing-0 << "                        </manifest>[\n]"
[DEBUG] http-outgoing-0 << "                    </archive>[\n]"
[DEBUG] http-outgoing-0 << "                </configuration>[\n]"
[DEBUG] http-outgoing-0 << "            </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        </plugins>[\n]"
[DEBUG] http-outgoing-0 << "    </build>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <reporting>[\n]"
[DEBUG] http-outgoing-0 << "        <plugins>[\n]"
[DEBUG] http-outgoing-0 << "            <plugin>[\n]"
[DEBUG] http-outgoing-0 << "                <artifactId>maven-project-info-reports-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                <version>2.7</version>[\n]"
[DEBUG] http-outgoing-0 << "                <configuration>[\n]"
[DEBUG] http-outgoing-0 << "                    <dependencyLocationsEnabled>false</dependencyLocationsEnabled>[\n]"
[DEBUG] http-outgoing-0 << "                    <!-- waiting for MPIR-267 -->[\n]"
[DEBUG] http-outgoing-0 << "                </configuration>[\n]"
[DEBUG] http-outgoing-0 << "                <reportSets>[\n]"
[DEBUG] http-outgoing-0 << "                    <reportSet>[\n]"
[DEBUG] http-outgoing-0 << "                        <reports>[\n]"
[DEBUG] http-outgoing-0 << "                            <report>index</report>[\n]"
[DEBUG] http-outgoing-0 << "                            <report>dependency-info</report>[\n]"
[DEBUG] http-outgoing-0 << "                            <report>modules</report>[\n]"
[DEBUG] http-outgoing-0 << "                            <report>license</report>[\n]"
[DEBUG] http-outgoing-0 << "                            <report>project-team</report>[\n]"
[DEBUG] http-outgoing-0 << "                            <report>scm</report>[\n]"
[DEBUG] http-outgoing-0 << "                            <report>issue-tracking</report>[\n]"
[DEBUG] http-outgoing-0 << "                            <report>mailing-list</report>[\n]"
[DEBUG] http-outgoing-0 << "                            <report>dependency-management</report>[\n]"
[DEBUG] http-outgoing-0 << "                            <report>dependencies</report>[\n]"
[DEBUG] http-outgoing-0 << "                            <report>dependency-convergence</report>[\n]"
[DEBUG] http-outgoing-0 << "                            <report>cim</report>[\n]"
[DEBUG] http-outgoing-0 << "                            <report>distribution-management</report>[\n]"
[DEBUG] http-outgoing-0 << "                        </reports>[\n]"
[DEBUG] http-outgoing-0 << "                    </reportSet>[\n]"
[DEBUG] http-outgoing-0 << "                </reportSets>[\n]"
[DEBUG] http-outgoing-0 << "  "
Progress (1): junit-4.12.pom (16/24 kB)
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <plugin>[\n]"
[DEBUG] http-outgoing-0 << "                <artifactId>maven-javadoc-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                <version>2.9.1</version>[\n]"
[DEBUG] http-outgoing-0 << "                <configuration>[\n]"
[DEBUG] http-outgoing-0 << "                    <destDir>javadoc/latest</destDir>[\n]"
[DEBUG] http-outgoing-0 << "                    <stylesheetfile>${basedir}/src/main/javadoc/stylesheet.css</stylesheetfile>[\n]"
[DEBUG] http-outgoing-0 << "                    <show>protected</show>[\n]"
[DEBUG] http-outgoing-0 << "                    <author>false</author>[\n]"
[DEBUG] http-outgoing-0 << "                    <version>false</version>[\n]"
[DEBUG] http-outgoing-0 << "                    <detectLinks>false</detectLinks>[\n]"
[DEBUG] http-outgoing-0 << "                    <linksource>true</linksource>[\n]"
[DEBUG] http-outgoing-0 << "                    <keywords>true</keywords>[\n]"
[DEBUG] http-outgoing-0 << "                    <use>false</use>[\n]"
[DEBUG] http-outgoing-0 << "                    <windowtitle>JUnit API</windowtitle>[\n]"
[DEBUG] http-outgoing-0 << "                    <encoding>UTF-8</encoding>[\n]"
[DEBUG] http-outgoing-0 << "                    <locale>en</locale>[\n]"
[DEBUG] http-outgoing-0 << "                    <javadocVersion>${jdkVersion}</javadocVersion>[\n]"
[DEBUG] http-outgoing-0 << "                    <javaApiLinks>[\n]"
[DEBUG] http-outgoing-0 << "                        <property>[\n]"
[DEBUG] http-outgoing-0 << "                            <name>api_${jdkVersion}</name>[\n]"
[DEBUG] http-outgoing-0 << "                            <value>http://docs.oracle.com/javase/${jdkVersion}.0/docs/api/</value>[\n]"
[DEBUG] http-outgoing-0 << "                        </property>[\n]"
[DEBUG] http-outgoing-0 << "                    </javaApiLinks>[\n]"
[DEBUG] http-outgoing-0 << "                    <excludePackageNames>junit.*,*.internal.*</excludePackageNames>[\n]"
[DEBUG] http-outgoing-0 << "                    <verbose>true</verbose>[\n]"
[DEBUG] http-outgoing-0 << "                    <minmemory>32m</minmemory>[\n]"
[DEBUG] http-outgoing-0 << "                    <maxmemory>128m</maxmemory>[\n]"
[DEBUG] http-outgoing-0 << "                    <failOnError>true</failOnError>[\n]"
[DEBUG] http-outgoing-0 << "                    <includeDependencySources>true</includeDependencySources>[\n]"
[DEBUG] http-outgoing-0 << "                    <dependencySourceIncludes>[\n]"
[DEBUG] http-outgoing-0 << "                        <dependencySourceInclude>org.hamcrest:hamcrest-core:*</dependencySourceInclude>[\n]"
[DEBUG] http-outgoing-0 << "                    </dependencySourceIncludes>[\n]"
[DEBUG] http-outgoing-0 << "                </configuration>[\n]"
[DEBUG] http-outgoing-0 << "                <reportSets>[\n]"
[DEBUG] http-outgoing-0 << "                    <reportSet>[\n]"
[DEBUG] http-outgoing-0 << "                        <reports>[\n]"
[DEBUG] http-outgoing-0 << "                            <report>javadoc</report>[\n]"
[DEBUG] http-outgoing-0 << "                        </reports>[\n]"
[DEBUG] http-outgoing-0 << "                    </reportSet>[\n]"
[DEBUG] http-outgoing-0 << "                </reportSets>[\n]"
[DEBUG] http-outgoing-0 << "            </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        </plugins>[\n]"
[DEBUG] http-outgoing-0 << "    </reporting>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <profiles>[\n]"
[DEBUG] http-outgoing-0 << "        <profile>[\n]"
[DEBUG] http-outgoing-0 << "            <id>junit-release</id>[\n]"
[DEBUG] http-outgoing-0 << "            <!--[\n]"
[DEBUG] http-outgoing-0 << "            Signs all artifacts before deploying to Maven Central.[\n]"
[DEBUG] http-outgoing-0 << "            -->[\n]"
[DEBUG] http-outgoing-0 << "            <build>[\n]"
[DEBUG] http-outgoing-0 << "                <plugins>[\n]"
[DEBUG] http-outgoing-0 << "                    <plugin>[\n]"
[DEBUG] http-outgoing-0 << "                        <!--[\n]"
[DEBUG] http-outgoing-0 << "                        The goal is to sign all artifacts so that the user may verify them before downloading.[\n]"
[DEBUG] http-outgoing-0 << "                        The automatic build system may reuire your key ID, and passphrase specified using system properties:[\n]"
[DEBUG] http-outgoing-0 << "                        -Dgpg.passphrase="<passphrase>" -Dgpg.keyname="<your key ID>"[\n]"
[DEBUG] http-outgoing-0 << "                        In order to create the key pair, use the command "gpg &ndash;&ndash;gen-key".[\n]"
[DEBUG] http-outgoing-0 << "                        (&ndash;&ndash; stands for double dash)[\n]"
[DEBUG] http-outgoing-0 << "                        -->[\n]"
[DEBUG] http-outgoing-0 << "                        <artifactId>maven-gpg-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                        <version>1.5</version>[\n]"
[DEBUG] http-outgoing-0 << "                        <executions>[\n]"
[DEBUG] http-outgoing-0 << "                            <execution>[\n]"
[DEBUG] http-outgoing-0 << "                                <id>gpg-sign</id>[\n]"
[DEBUG] http-outgoing-0 << "                                <phase>verify</phase>[\n]"
[DEBUG] http-outgoing-0 << "                                <goals>[\n]"
[DEBUG] http-outgoing-0 << "                                    <goal>sign</goal>[\n]"
[DEBUG] http-outgoing-0 << "                                </goals>[\n]"
[DEBUG] http-outgoing-0 << "                            </execution>[\n]"
[DEBUG] http-outgoing-0 << "                        </executions>[\n]"
[DEBUG] http-outgoing-0 << "                    </plugin>[\n]"
[DEBUG] http-outgoing-0 << "                </plugins>[\n]"
[DEBUG] http-outgoing-0 << "            </build>[\n]"
[DEBUG] http-outgoing-0 << "        </profile>[\n]"
[DEBUG] http-outgoing-0 << "        <profile>[\n]"
[DEBUG] http-outgoing-0 << "            <id>generate-docs</id>[\n]"
[DEBUG] http-outgoing-0 << "            <!--[\n]"
[DEBUG] http-outgoing-0 << "            Generate the documentation artifacts. [\n]"
[DEBUG] http-outgoing-0 << "            Note: this profile is also required to be active for release[\n]"
[DEBUG] http-outgoing-0 << "            builds due to the packaging requirements of the Central repo[\n]"
[DEBUG] http-outgoing-0 << "            -->[\n]"
[DEBUG] http-outgoing-0 << "            <build>[\n]"
[DEBUG] http-outgoing-0 << "                <plugins>[\n]"
[DEBUG] http-outgoing-0 << "                    <plugin>[\n]"
[DEBUG] http-outgoing-0 << "                        <artifactId>maven-source-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                        <executions>[\n]"
[DEBUG] http-outgoing-0 << "                            <execution>[\n]"
[DEBUG] http-outgoing-0 << "                                <id>attach-sources</id>[\n]"
[DEBUG] http-outgoing-0 << "                                <phase>prepare-package</phase>[\n]"
[DEBUG] http-outgoing-0 << "                                <goa"
Progress (1): junit-4.12.pom (20/24 kB)
[DEBUG] http-outgoing-0 << "ls>[\n]"
[DEBUG] http-outgoing-0 << "                                    <goal>jar-no-fork</goal>[\n]"
[DEBUG] http-outgoing-0 << "                                </goals>[\n]"
[DEBUG] http-outgoing-0 << "                            </execution>[\n]"
[DEBUG] http-outgoing-0 << "                        </executions>[\n]"
[DEBUG] http-outgoing-0 << "                    </plugin>[\n]"
[DEBUG] http-outgoing-0 << "                    <plugin>[\n]"
[DEBUG] http-outgoing-0 << "                        <artifactId>maven-javadoc-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                        <executions>[\n]"
[DEBUG] http-outgoing-0 << "                            <execution>[\n]"
[DEBUG] http-outgoing-0 << "                                <id>attach-javadoc</id>[\n]"
[DEBUG] http-outgoing-0 << "                                <phase>package</phase>[\n]"
[DEBUG] http-outgoing-0 << "                                <goals>[\n]"
[DEBUG] http-outgoing-0 << "                                    <goal>jar</goal>[\n]"
[DEBUG] http-outgoing-0 << "                                </goals>[\n]"
[DEBUG] http-outgoing-0 << "                            </execution>[\n]"
[DEBUG] http-outgoing-0 << "                        </executions>[\n]"
[DEBUG] http-outgoing-0 << "                    </plugin>[\n]"
[DEBUG] http-outgoing-0 << "                </plugins>[\n]"
[DEBUG] http-outgoing-0 << "            </build>[\n]"
[DEBUG] http-outgoing-0 << "        </profile>[\n]"
[DEBUG] http-outgoing-0 << "        <profile>[\n]"
[DEBUG] http-outgoing-0 << "            <id>restrict-doclint</id>[\n]"
[DEBUG] http-outgoing-0 << "            <!-- doclint is only supported by JDK 8 -->[\n]"
[DEBUG] http-outgoing-0 << "            <activation>[\n]"
[DEBUG] http-outgoing-0 << "                <jdk>[1.8,)</jdk>[\n]"
[DEBUG] http-outgoing-0 << "            </activation>[\n]"
[DEBUG] http-outgoing-0 << "            <build>[\n]"
[DEBUG] http-outgoing-0 << "                <plugins>[\n]"
[DEBUG] http-outgoing-0 << "                    <plugin>[\n]"
[DEBUG] http-outgoing-0 << "                        <artifactId>maven-compiler-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "                            <compilerArgs>[\n]"
[DEBUG] http-outgoing-0 << "                                <arg>-Xlint:unchecked</arg>[\n]"
[DEBUG] http-outgoing-0 << "                                <arg>-Xdoclint:accessibility,reference,syntax</arg>[\n]"
[DEBUG] http-outgoing-0 << "                            </compilerArgs>[\n]"
[DEBUG] http-outgoing-0 << "                        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "                    </plugin>[\n]"
[DEBUG] http-outgoing-0 << "                    <plugin>[\n]"
[DEBUG] http-outgoing-0 << "                        <artifactId>maven-javadoc-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "                            <additionalparam>-Xdoclint:accessibility -Xdoclint:reference</additionalparam>[\n]"
[DEBUG] http-outgoing-0 << "                        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "                    </plugin>[\n]"
[DEBUG] http-outgoing-0 << "                </plugins>[\n]"
[DEBUG] http-outgoing-0 << "            </build>[\n]"
[DEBUG] http-outgoing-0 << "            <reporting>[\n]"
[DEBUG] http-outgoing-0 << "                <plugins>[\n]"
[DEBUG] http-outgoing-0 << "                    <plugin>[\n]"
[DEBUG] http-outgoing-0 << "                        <artifactId>maven-javadoc-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "                            <additionalparam>-Xdoclint:accessibility -Xdoclint:reference</additionalparam>[\n]"
[DEBUG] http-outgoing-0 << "                        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "                    </plugin>[\n]"
[DEBUG] http-outgoing-0 << "                </plugins>[\n]"
[DEBUG] http-outgoing-0 << "            </reporting>[\n]"
[DEBUG] http-outgoing-0 << "        </profile>[\n]"
[DEBUG] http-outgoing-0 << "        <profile>[\n]"
[DEBUG] http-outgoing-0 << "            <id>fast-tests</id>[\n]"
[DEBUG] http-outgoing-0 << "            <build>[\n]"
[DEBUG] http-outgoing-0 << "                <plugins>[\n]"
[DEBUG] http-outgoing-0 << "                    <plugin>[\n]"
[DEBUG] http-outgoing-0 << "                        <artifactId>maven-surefire-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "                            <parallel>classes</parallel>[\n]"
[DEBUG] http-outgoing-0 << "                            <threadCountClasses>2</threadCountClasses>[\n]"
[DEBUG] http-outgoing-0 << "                        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "                        <dependencies>[\n]"
[DEBUG] http-outgoing-0 << "                            <dependency>[\n]"
[DEBUG] http-outgoing-0 << "                                <groupId>org.apache.maven.surefire</groupId>[\n]"
[DEBUG] http-outgoing-0 << "                                <artifactId>surefire-junit47</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "                                <version>2.17</version>[\n]"
[DEBUG] http-outgoing-0 << "                            </dependency>[\n]"
[DEBUG] http-outgoing-0 << "                        </dependencies>[\n]"
[DEBUG] http-outgoing-0 << "                    </plugin>[\n]"
[DEBUG] http-outgoing-0 << "                </plugins>[\n]"
[DEBUG] http-outgoing-0 << "            </build>[\n]"
[DEBUG] http-outgoing-0 << "        </profile>[\n]"
[DEBUG] http-outgoing-0 << "    </profiles>[\n]"
[DEBUG] http-outgoing-0 << "</project>[\n]"
Progress (1): junit-4.12.pom (24 kB)   
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/junit/junit/4.12/junit-4.12.pom.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/junit/junit/4.12/junit-4.12.pom.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/junit/junit/4.12/junit-4.12.pom.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "44dac38d1205316760f1d48a79969889"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Thu, 04 Dec 2014 16:17:44 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 44dac38d1205316760f1d48a79969889[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: b6954ce7780fad28128f38d2b5a4dd15756dbd76[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:52 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 1915248[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 120[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.711434,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "44dac38d1205316760f1d48a79969889"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Thu, 04 Dec 2014 16:17:44 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 44dac38d1205316760f1d48a79969889
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: b6954ce7780fad28128f38d2b5a4dd15756dbd76
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:52 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 1915248
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 120
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.711434,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "35fb238baee3f3af739074d723279ebea2028398"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
                                    
Downloaded from central: https://repo.maven.apache.org/maven2/junit/junit/4.12/junit-4.12.pom (24 kB at 789 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/junit/junit/4.12/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/junit/junit/4.12/junit-4.12.pom.lastUpdated
[DEBUG] Resolving artifact org.hamcrest:hamcrest-core:pom:1.3 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.pom
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.pom HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.pom HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.pom HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 766[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "7cd680c223f3807e1ef353466b9a4b25"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/xml[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Mon, 09 Jul 2012 21:08:02 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 7cd680c223f3807e1ef353466b9a4b25[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 872e413497b906e7c9fa85ccc96046c5d1ef7ece[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:52 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 695888[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 5038[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.734739,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 766
[DEBUG] http-outgoing-0 << ETag: "7cd680c223f3807e1ef353466b9a4b25"
[DEBUG] http-outgoing-0 << Content-Type: text/xml
[DEBUG] http-outgoing-0 << Last-Modified: Mon, 09 Jul 2012 21:08:02 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 7cd680c223f3807e1ef353466b9a4b25
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 872e413497b906e7c9fa85ccc96046c5d1ef7ece
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:52 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 695888
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 5038
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.734739,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "<?xml version="1.0" encoding="ISO-8859-1"?>[\r][\n]"
[DEBUG] http-outgoing-0 << "<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"[\r][\n]"
[DEBUG] http-outgoing-0 << "  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">[\r][\n]"
[DEBUG] http-outgoing-0 << "  <modelVersion>4.0.0</modelVersion>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <parent>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <groupId>org.hamcrest</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <artifactId>hamcrest-parent</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <version>1.3</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </parent>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <artifactId>hamcrest-core</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <packaging>jar</packaging>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <name>Hamcrest Core</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <description>[\r][\n]"
[DEBUG] http-outgoing-0 << "    This is the core API of hamcrest matcher framework to be used by third-party framework providers. This includes the a foundation set of matcher implementations for common operations.[\r][\n]"
[DEBUG] http-outgoing-0 << "  </description>[\r][\n]"
[DEBUG] http-outgoing-0 << "</project>[\r][\n]"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
Progress (1): hamcrest-core-1.3.pom (766 B)
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.pom.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.pom.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.pom.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "56083e12423c24449f6c490ca1508fb0"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Mon, 09 Jul 2012 21:08:02 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 56083e12423c24449f6c490ca1508fb0[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: a3413756dba1f6e78f7f5407dee17844a0fe71f7[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:52 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 639536[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 185[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.744837,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "56083e12423c24449f6c490ca1508fb0"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Mon, 09 Jul 2012 21:08:02 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 56083e12423c24449f6c490ca1508fb0
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: a3413756dba1f6e78f7f5407dee17844a0fe71f7
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:52 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 639536
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 185
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.744837,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "872e413497b906e7c9fa85ccc96046c5d1ef7ece"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
                                           
Downloaded from central: https://repo.maven.apache.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.pom (766 B at 38 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/hamcrest/hamcrest-core/1.3/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.pom.lastUpdated
[DEBUG] Resolving artifact org.hamcrest:hamcrest-parent:pom:1.3 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/org/hamcrest/hamcrest-parent/1.3/hamcrest-parent-1.3.pom
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/hamcrest/hamcrest-parent/1.3/hamcrest-parent-1.3.pom HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/hamcrest/hamcrest-parent/1.3/hamcrest-parent-1.3.pom HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/hamcrest/hamcrest-parent/1.3/hamcrest-parent-1.3.pom HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 1972[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "7591ddb3581de8684c17e51741a7461d"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/xml[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Mon, 09 Jul 2012 21:06:57 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 7591ddb3581de8684c17e51741a7461d[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 80391bd32bfa4837a15215d5e9f07c60555c379a[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:52 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 1241571[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 5112[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.763277,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 1972
[DEBUG] http-outgoing-0 << ETag: "7591ddb3581de8684c17e51741a7461d"
[DEBUG] http-outgoing-0 << Content-Type: text/xml
[DEBUG] http-outgoing-0 << Last-Modified: Mon, 09 Jul 2012 21:06:57 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 7591ddb3581de8684c17e51741a7461d
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 80391bd32bfa4837a15215d5e9f07c60555c379a
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:52 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 1241571
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 5112
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.763277,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "<?xml version="1.0" encoding="ISO-8859-1"?>[\r][\n]"
[DEBUG] http-outgoing-0 << "<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"[\r][\n]"
[DEBUG] http-outgoing-0 << "  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">[\r][\n]"
[DEBUG] http-outgoing-0 << "  <modelVersion>4.0.0</modelVersion>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <groupId>org.hamcrest</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <artifactId>hamcrest-parent</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <version>1.3</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <packaging>pom</packaging>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <name>Hamcrest Maven Parent</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <url>https://github.com/hamcrest/JavaHamcrest</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <description>General parent POM for all hamcrest libraries.</description>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <licenses>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <license>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <name>New BSD License</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <url>http://www.opensource.org/licenses/bsd-license.php</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <distribution>repo</distribution>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </license>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </licenses>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <scm>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <url>https://github.com/hamcrest/JavaHamcrest</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <connection>scm:git:git@github.com:hamcrest/JavaHamcrest.git</connection>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </scm>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <developers>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <id>joe.walnes</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <name>Joe Walnes</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <id>nat.pryce</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <name>Nat Pryce</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <id>smgfreeman</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <name>Steve Freeman</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <id>neildunn</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <name>Neil Dunn</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <id>scarytom</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <name>Tom Denley</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </developers>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <modules>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <module>hamcrest-core</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <module>hamcrest-generator</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <module>hamcrest-library</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <module>hamcrest-integration</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </modules>[\r][\n]"
[DEBUG] http-outgoing-0 << "</project>[\r][\n]"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
Progress (1): hamcrest-parent-1.3.pom (2.0 kB)
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/hamcrest/hamcrest-parent/1.3/hamcrest-parent-1.3.pom.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/hamcrest/hamcrest-parent/1.3/hamcrest-parent-1.3.pom.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/hamcrest/hamcrest-parent/1.3/hamcrest-parent-1.3.pom.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "bd7ffed407742b97a90788796bd6f85c"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Mon, 09 Jul 2012 21:06:57 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: bd7ffed407742b97a90788796bd6f85c[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 156c9f6d708f31e6de7428d0fcca7a39df5458d1[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:52 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 112028[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 182[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.774567,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "bd7ffed407742b97a90788796bd6f85c"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Mon, 09 Jul 2012 21:06:57 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: bd7ffed407742b97a90788796bd6f85c
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 156c9f6d708f31e6de7428d0fcca7a39df5458d1
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:52 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 112028
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 182
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.774567,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "80391bd32bfa4837a15215d5e9f07c60555c379a"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
                                              
Downloaded from central: https://repo.maven.apache.org/maven2/org/hamcrest/hamcrest-parent/1.3/hamcrest-parent-1.3.pom (2.0 kB at 116 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/hamcrest/hamcrest-parent/1.3/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/hamcrest/hamcrest-parent/1.3/hamcrest-parent-1.3.pom.lastUpdated
[DEBUG] Dependency collection stats {ConflictMarker.analyzeTime=540512, ConflictMarker.markTime=293010, ConflictMarker.nodeCount=3, ConflictIdSorter.graphTime=306458, ConflictIdSorter.topsortTime=282507, ConflictIdSorter.conflictIdCount=2, ConflictIdSorter.conflictIdCycleCount=0, ConflictResolver.totalTime=3617253, ConflictResolver.conflictItemCount=2, DefaultDependencyCollector.collectTime=103151677, DefaultDependencyCollector.transformTime=6839952}
[DEBUG] com.example:your-project-name:jar:1.0-SNAPSHOT
[DEBUG]    junit:junit:jar:4.12:test
[DEBUG]       org.hamcrest:hamcrest-core:jar:1.3:test
[DEBUG] Resolving artifact junit:junit:jar:4.12 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Resolving artifact org.hamcrest:hamcrest-core:jar:1.3 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/junit/junit/4.12/junit-4.12.jar
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 0; route allocated: 1 of 20; total allocated: 1 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/junit/junit/4.12/junit-4.12.jar HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/junit/junit/4.12/junit-4.12.jar HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/junit/junit/4.12/junit-4.12.jar HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
Downloading from central: https://repo.maven.apache.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 314932[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "5b38c40c97fbd0adee29f91e60405584"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: application/java-archive[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Thu, 04 Dec 2014 16:17:43 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 5b38c40c97fbd0adee29f91e60405584[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 2973d150c0dc1fefe998f834810d68f278ea58ec[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:52 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 1938488[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 6[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.805718,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 314932
[DEBUG] http-outgoing-0 << ETag: "5b38c40c97fbd0adee29f91e60405584"
[DEBUG] http-outgoing-0 << Content-Type: application/java-archive
[DEBUG] http-outgoing-0 << Last-Modified: Thu, 04 Dec 2014 16:17:43 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 5b38c40c97fbd0adee29f91e60405584
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 2973d150c0dc1fefe998f834810d68f278ea58ec
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:52 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 1938488
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 6
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.805718,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "PK[0x3][0x4][\n]"
[DEBUG] // more debugging
Progress (1): junit-4.12.jar (315 kB)    
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/junit/junit/4.12/junit-4.12.jar.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/junit/junit/4.12/junit-4.12.jar.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/junit/junit/4.12/junit-4.12.jar.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "464307386731991a4a9965487f541d42"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Thu, 04 Dec 2014 16:17:43 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 464307386731991a4a9965487f541d42[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 1f4cf01fe39b8d77b29aa92575b4174d28bf8560[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 1952545[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 1162[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.102119,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "464307386731991a4a9965487f541d42"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Thu, 04 Dec 2014 16:17:43 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 464307386731991a4a9965487f541d42
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 1f4cf01fe39b8d77b29aa92575b4174d28bf8560
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 1952545
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 1162
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.102119,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "2973d150c0dc1fefe998f834810d68f278ea58ec"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
                                     
Downloaded from central: https://repo.maven.apache.org/maven2/junit/junit/4.12/junit-4.12.jar (315 kB at 1.0 MB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/junit/junit/4.12/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/junit/junit/4.12/junit-4.12.jar.lastUpdated
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/hamcrest/hamcrest-core/1.3/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar.lastUpdated
[INFO] 
[INFO] --- sonar-maven-plugin:3.9.0.2155:sonar (default-cli) @ your-project-name ---
[DEBUG] Resolving artifact org.sonatype.plexus:plexus-sec-dispatcher:pom:1.4 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/org/sonatype/plexus/plexus-sec-dispatcher/1.4/plexus-sec-dispatcher-1.4.pom
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/sonatype/plexus/plexus-sec-dispatcher/1.4/plexus-sec-dispatcher-1.4.pom HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/sonatype/plexus/plexus-sec-dispatcher/1.4/plexus-sec-dispatcher-1.4.pom HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/sonatype/plexus/plexus-sec-dispatcher/1.4/plexus-sec-dispatcher-1.4.pom HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 2961[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "af1d7fad28b5b4082da180458e50ff24"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/xml[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Wed, 16 Dec 2009 13:55:20 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: af1d7fad28b5b4082da180458e50ff24[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 18c39936b9da963de44b4d68adf204f7c5df3a45[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 723606[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 61[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.147909,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 2961
[DEBUG] http-outgoing-0 << ETag: "af1d7fad28b5b4082da180458e50ff24"
[DEBUG] http-outgoing-0 << Content-Type: text/xml
[DEBUG] http-outgoing-0 << Last-Modified: Wed, 16 Dec 2009 13:55:20 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: af1d7fad28b5b4082da180458e50ff24
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 18c39936b9da963de44b4d68adf204f7c5df3a45
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 723606
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 61
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.147909,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "<?xml version="1.0"?>[\n]"
[DEBUG] http-outgoing-0 << "<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">[\n]"
[DEBUG] http-outgoing-0 << "  <parent>[\n]"
[DEBUG] http-outgoing-0 << "    <groupId>org.sonatype.spice</groupId>[\n]"
[DEBUG] http-outgoing-0 << "    <artifactId>spice-parent</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "    <version>12</version>[\n]"
[DEBUG] http-outgoing-0 << "  </parent>[\n]"
[DEBUG] http-outgoing-0 << "  [\n]"
[DEBUG] http-outgoing-0 << "  <modelVersion>4.0.0</modelVersion>[\n]"
[DEBUG] http-outgoing-0 << "  <groupId>org.sonatype.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "  <artifactId>plexus-sec-dispatcher</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "  <url>http://spice.sonatype.org/${project.artifactId}</url>[\n]"
[DEBUG] http-outgoing-0 << "  [\n]"
[DEBUG] http-outgoing-0 << "  <name>Plexus Security Dispatcher Component</name>[\n]"
[DEBUG] http-outgoing-0 << "  <version>1.4</version>[\n]"
[DEBUG] http-outgoing-0 << "  [\n]"
[DEBUG] http-outgoing-0 << "  <distributionManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <site>[\n]"
[DEBUG] http-outgoing-0 << "      <id>sonatype.org-sites</id>[\n]"
[DEBUG] http-outgoing-0 << "      <url>${spiceSiteBaseUrl}/${project.artifactId}</url>[\n]"
[DEBUG] http-outgoing-0 << "    </site>[\n]"
[DEBUG] http-outgoing-0 << "  </distributionManagement>[\n]"
[DEBUG] http-outgoing-0 << "  [\n]"
[DEBUG] http-outgoing-0 << "  <build>[\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>plexus-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>1.3.5</version>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>descriptor</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-compiler-plugin[\n]"
[DEBUG] http-outgoing-0 << "        </artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <source>1.4</source>[\n]"
[DEBUG] http-outgoing-0 << "          <target>1.4</target>[\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.codehaus.modello</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>modello-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <version>1.0.0</version>[\n]"
[DEBUG] http-outgoing-0 << "          <models>[\n]"
[DEBUG] http-outgoing-0 << "            <model>src/main/mdo/settings-security.mdo</model>[\n]"
[DEBUG] http-outgoing-0 << "          </models>[\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>standard</id>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>java</goal>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>xpp3-reader</goal>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>xpp3-writer</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "    </plugins>[\n]"
[DEBUG] http-outgoing-0 << "  </build>[\n]"
[DEBUG] http-outgoing-0 << "  [\n]"
[DEBUG] http-outgoing-0 << "  <dependencies>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>plexus-utils</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.sonatype.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>plexus-cipher</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>1.4</version>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>plexus-container-default</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>1.0-alpha-9-stable-1</version>[\n]"
[DEBUG] http-outgoing-0 << "      <scope>provided</scope>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>junit</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>junit</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>3.8.2</version>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "  </dependencies>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <scm>[\n]"
[DEBUG] http-outgoing-0 << "    <connection>scm:svn:http://svn.sonatype.org/spice/tags/plexus-sec-dispatcher-1.4</connection>[\n]"
[DEBUG] http-outgoing-0 << "    <developerConnection>scm:svn:https://svn.sonatype.org/spice/tags/plexus-sec-dispatcher-1.4</developerConnection>[\n]"
[DEBUG] http-outgoing-0 << "    <url>http://svn.sonatype.org/spice/tags/plexus-sec-dispatcher-1.4</url>[\n]"
[DEBUG] http-outgoing-0 << "  </scm>[\n]"
[DEBUG] http-outgoing-0 << "</project>[\n]"
Progress (1): plexus-sec-dispatcher-1.4.pom (3.0 kB)
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/sonatype/plexus/plexus-sec-dispatcher/1.4/plexus-sec-dispatcher-1.4.pom.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/sonatype/plexus/plexus-sec-dispatcher/1.4/plexus-sec-dispatcher-1.4.pom.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/sonatype/plexus/plexus-sec-dispatcher/1.4/plexus-sec-dispatcher-1.4.pom.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "41eef56ebfc4d33c560972290ba85b02"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Wed, 16 Dec 2009 13:55:20 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 41eef56ebfc4d33c560972290ba85b02[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: c30d9f4b717ffb1ef8666ff87f0d1c0f1b610e29[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 1325689[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 8[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.161688,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "41eef56ebfc4d33c560972290ba85b02"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Wed, 16 Dec 2009 13:55:20 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 41eef56ebfc4d33c560972290ba85b02
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: c30d9f4b717ffb1ef8666ff87f0d1c0f1b610e29
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 1325689
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 8
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.161688,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "18c39936b9da963de44b4d68adf204f7c5df3a45"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
                                                    
Downloaded from central: https://repo.maven.apache.org/maven2/org/sonatype/plexus/plexus-sec-dispatcher/1.4/plexus-sec-dispatcher-1.4.pom (3.0 kB at 156 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonatype/plexus/plexus-sec-dispatcher/1.4/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonatype/plexus/plexus-sec-dispatcher/1.4/plexus-sec-dispatcher-1.4.pom.lastUpdated
[DEBUG] Resolving artifact org.sonatype.spice:spice-parent:pom:12 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/org/sonatype/spice/spice-parent/12/spice-parent-12.pom
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/sonatype/spice/spice-parent/12/spice-parent-12.pom HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/sonatype/spice/spice-parent/12/spice-parent-12.pom HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/sonatype/spice/spice-parent/12/spice-parent-12.pom HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 6798[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "11c20e3915ca679b5f49ab5bdad68f6f"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/xml[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Mon, 27 Apr 2009 16:26:25 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 11c20e3915ca679b5f49ab5bdad68f6f[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: e86b2d826f53093e27dc579bea3becbf1425d9ba[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 1952900[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 3116[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.174134,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 6798
[DEBUG] http-outgoing-0 << ETag: "11c20e3915ca679b5f49ab5bdad68f6f"
[DEBUG] http-outgoing-0 << Content-Type: text/xml
[DEBUG] http-outgoing-0 << Last-Modified: Mon, 27 Apr 2009 16:26:25 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 11c20e3915ca679b5f49ab5bdad68f6f
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: e86b2d826f53093e27dc579bea3becbf1425d9ba
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 1952900
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 3116
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.174134,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">[\n]"
[DEBUG] http-outgoing-0 << "  <modelVersion>4.0.0</modelVersion>[\n]"
[DEBUG] http-outgoing-0 << "  <parent>[\n]"
[DEBUG] http-outgoing-0 << "    <groupId>org.sonatype.forge</groupId>[\n]"
[DEBUG] http-outgoing-0 << "    <artifactId>forge-parent</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "    <version>4</version>[\n]"
[DEBUG] http-outgoing-0 << "  </parent>[\n]"
[DEBUG] http-outgoing-0 << "  <groupId>org.sonatype.spice</groupId>[\n]"
[DEBUG] http-outgoing-0 << "  <artifactId>spice-parent</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "  <version>12</version>[\n]"
[DEBUG] http-outgoing-0 << "  <packaging>pom</packaging>[\n]"
[DEBUG] http-outgoing-0 << "  <name>Sonatype Spice Components</name>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <scm>[\n]"
[DEBUG] http-outgoing-0 << "    <connection>scm:svn:http://svn.sonatype.org/spice/tags/spice-parent-12</connection>[\n]"
[DEBUG] http-outgoing-0 << "    <url>http://svn.sonatype.org/spice/tags/spice-parent-12</url>[\n]"
[DEBUG] http-outgoing-0 << "    <developerConnection>scm:svn:https://svn.sonatype.org/spice/tags/spice-parent-12</developerConnection>[\n]"
[DEBUG] http-outgoing-0 << "  </scm>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <licenses>[\n]"
[DEBUG] http-outgoing-0 << "    <license>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Apache Public License 2.0</name>[\n]"
[DEBUG] http-outgoing-0 << "      <url>http://www.apache.org/licenses/LICENSE-2.0</url>[\n]"
[DEBUG] http-outgoing-0 << "      <distribution>repo</distribution>[\n]"
[DEBUG] http-outgoing-0 << "    </license>[\n]"
[DEBUG] http-outgoing-0 << "  </licenses>[\n]"
[DEBUG] http-outgoing-0 << "  [\n]"
[DEBUG] http-outgoing-0 << "[0x9]<ciManagement>[\n]"
[DEBUG] http-outgoing-0 << "[0x9]  <system>Hudson</system>[\n]"
[DEBUG] http-outgoing-0 << "[0x9]  <url>https://grid.sonatype.org/ci/view/Spice/</url>[\n]"
[DEBUG] http-outgoing-0 << "[0x9]</ciManagement>[\n]"
[DEBUG] http-outgoing-0 << "[0x9][\n]"
[DEBUG] http-outgoing-0 << "  <issueManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <system>JIRA</system>[\n]"
[DEBUG] http-outgoing-0 << "    <url>https://issues.sonatype.org/browse/SPICE</url>[\n]"
[DEBUG] http-outgoing-0 << "  </issueManagement>[\n]"
[DEBUG] http-outgoing-0 << "  [\n]"
[DEBUG] http-outgoing-0 << "  <properties>[\n]"
[DEBUG] http-outgoing-0 << "    <!--[\n]"
[DEBUG] http-outgoing-0 << "      2008.12.07 Oleg: reverted to 6.1.12 as http transport only works with that version | Please always sync with[\n]"
[DEBUG] http-outgoing-0 << "      mercury-http-transport !![\n]"
[DEBUG] http-outgoing-0 << "    -->[\n]"
[DEBUG] http-outgoing-0 << "    <jetty.version>6.1.12</jetty.version>[\n]"
[DEBUG] http-outgoing-0 << "    <plexus.version>1.0-beta-3.0.5</plexus.version>[\n]"
[DEBUG] http-outgoing-0 << "  </properties>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <dependencyManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <dependencies>[\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>plexus-container-default</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>${plexus.version}</version>[\n]"
[DEBUG] http-outgoing-0 << "        <scope>provided</scope>[\n]"
[DEBUG] http-outgoing-0 << "        <exclusions>[\n]"
[DEBUG] http-outgoing-0 << "          <exclusion>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>commons-logging</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>commons-logging</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          </exclusion>[\n]"
[DEBUG] http-outgoing-0 << "          <exclusion>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>commons-logging</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>commons-logging-api</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          </exclusion>[\n]"
[DEBUG] http-outgoing-0 << "          <exclusion>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>log4j</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>log4j</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          </exclusion>[\n]"
[DEBUG] http-outgoing-0 << "        </exclusions>[\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>plexus-component-annotations</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>${plexus.version}</version>[\n]"
[DEBUG] http-outgoing-0 << "        <scope>provided</scope>[\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>plexus-utils</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>1.5.5</version>[\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.mortbay.jetty</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>jetty</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>${jetty.version}</version>[\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.mortbay.jetty</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>jetty-client</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>${jetty.version}</version>[\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.mortbay.jetty</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>jetty-util</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>${jetty.version}</version>[\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>junit</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>junit</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>4.5</version>[\n]"
[DEBUG] http-outgoing-0 << "        <scope>test</scope>[\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    </dependencies>[\n]"
[DEBUG] http-outgoing-0 << "  </dependencyManagement>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <build>[\n]"
[DEBUG] http-outgoing-0 << "    <pluginManagement>[\n]"
[DEBUG] http-outgoing-0 << "      <plugins>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>plexus-component-metadata</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>${plexus.version}</version>[\n]"
[DEBUG] http-outgoing-0 << "          <executions>[\n]"
[DEBUG] http-outgoing-0 << "            <execution>[\n]"
[DEBUG] http-outgoing-0 << "              <id>process-classes</id>[\n]"
[DEBUG] http-outgoing-0 << "              <goals>[\n]"
[DEBUG] http-outgoing-0 << "                <goal>generate-metadata</goal>[\n]"
[DEBUG] http-outgoing-0 << "              </goals>[\n]"
[DEBUG] http-outgoing-0 << "            </execution>[\n]"
[DEBUG] http-outgoing-0 << "            <execution>[\n]"
[DEBUG] http-outgoing-0 << "              <id>process-test-classes</id>[\n]"
[DEBUG] http-outgoing-0 << "              <goals>[\n]"
[DEBUG] http-outgoing-0 << "                <goal>generate-test-metadata</goal>[\n]"
[DEBUG] http-outgoing-0 << "              </goals>[\n]"
[DEBUG] http-outgoing-0 << "            </execution>[\n]"
[DEBUG] http-outgoing-0 << "          </executions>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <g"
Progress (1): spice-parent-12.pom (4.1/6.8 kB)
[DEBUG] http-outgoing-0 << "roupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>plexus-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>1.3.8</version>[\n]"
[DEBUG] http-outgoing-0 << "          <executions>[\n]"
[DEBUG] http-outgoing-0 << "            <execution>[\n]"
[DEBUG] http-outgoing-0 << "              <goals>[\n]"
[DEBUG] http-outgoing-0 << "                <goal>descriptor</goal>[\n]"
[DEBUG] http-outgoing-0 << "              </goals>[\n]"
[DEBUG] http-outgoing-0 << "            </execution>[\n]"
[DEBUG] http-outgoing-0 << "          </executions>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      </plugins>[\n]"
[DEBUG] http-outgoing-0 << "    </pluginManagement>[\n]"
[DEBUG] http-outgoing-0 << "  </build>[\n]"
[DEBUG] http-outgoing-0 << "  [\n]"
[DEBUG] http-outgoing-0 << "[0x9]<reporting>[\n]"
[DEBUG] http-outgoing-0 << "[0x9]  <plugins>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.codehaus.mojo</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>cobertura-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>2.2</version>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.codehaus.mojo</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>findbugs-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>1.2</version>[\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <omitVisitors>UnreadFields</omitVisitors>[\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-jxr-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>2.1</version>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-pmd-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>2.4</version>[\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <targetJdk>1.5</targetJdk>[\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-plugin-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>2.5</version>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-javadoc-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>2.5</version>[\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <tagletArtifacts>[\n]"
[DEBUG] http-outgoing-0 << "            <tagletArtifact>[\n]"
[DEBUG] http-outgoing-0 << "              <groupId>org.apache.maven.plugin-tools</groupId>[\n]"
[DEBUG] http-outgoing-0 << "              <artifactId>maven-plugin-tools-javadoc</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "              <version>2.5</version>[\n]"
[DEBUG] http-outgoing-0 << "            </tagletArtifact>[\n]"
[DEBUG] http-outgoing-0 << "            <tagletArtifact>[\n]"
[DEBUG] http-outgoing-0 << "              <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "              <artifactId>plexus-javadoc</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "              <version>1.0</version>[\n]"
[DEBUG] http-outgoing-0 << "            </tagletArtifact>[\n]"
[DEBUG] http-outgoing-0 << "          </tagletArtifacts>[\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-project-info-reports-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>2.1.1</version>[\n]"
[DEBUG] http-outgoing-0 << "        <reportSets>[\n]"
[DEBUG] http-outgoing-0 << "          <reportSet>[\n]"
[DEBUG] http-outgoing-0 << "            <reports>[\n]"
[DEBUG] http-outgoing-0 << "              <!-- Broken, don't know why. -->[\n]"
[DEBUG] http-outgoing-0 << "              <report>dependencies</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>project-team</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>mailing-list</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>cim</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>issue-tracking</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>license</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>scm</report>[\n]"
[DEBUG] http-outgoing-0 << "            </reports>[\n]"
[DEBUG] http-outgoing-0 << "          </reportSet>[\n]"
[DEBUG] http-outgoing-0 << "        </reportSets>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "[0x9]  </plugins>[\n]"
[DEBUG] http-outgoing-0 << "[0x9]</reporting>[\n]"
[DEBUG] http-outgoing-0 << "[0x9][\n]"
[DEBUG] http-outgoing-0 << "</project>"
Progress (1): spice-parent-12.pom (6.8 kB)    
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/sonatype/spice/spice-parent/12/spice-parent-12.pom.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/sonatype/spice/spice-parent/12/spice-parent-12.pom.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/sonatype/spice/spice-parent/12/spice-parent-12.pom.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "023e8e7837b0cfc09364344c4751cf69"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Mon, 27 Apr 2009 16:26:25 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 023e8e7837b0cfc09364344c4751cf69[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: bfa3bd2e354a32403aadefbc80fa84474cce1f43[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 624816[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 1005[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.193155,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "023e8e7837b0cfc09364344c4751cf69"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Mon, 27 Apr 2009 16:26:25 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 023e8e7837b0cfc09364344c4751cf69
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: bfa3bd2e354a32403aadefbc80fa84474cce1f43
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 624816
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 1005
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.193155,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "e86b2d826f53093e27dc579bea3becbf1425d9ba"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
                                          
Downloaded from central: https://repo.maven.apache.org/maven2/org/sonatype/spice/spice-parent/12/spice-parent-12.pom (6.8 kB at 272 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonatype/spice/spice-parent/12/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonatype/spice/spice-parent/12/spice-parent-12.pom.lastUpdated
[DEBUG] Resolving artifact org.sonatype.forge:forge-parent:pom:4 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/org/sonatype/forge/forge-parent/4/forge-parent-4.pom
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/sonatype/forge/forge-parent/4/forge-parent-4.pom HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/sonatype/forge/forge-parent/4/forge-parent-4.pom HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/sonatype/forge/forge-parent/4/forge-parent-4.pom HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 8397[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "27e6b461d034c88651dbc7e74d268525"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/xml[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Fri, 27 Feb 2009 04:50:22 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 27e6b461d034c88651dbc7e74d268525[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 564f266ea9323e57e246f0fca8f04f596663fb86[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 1832531[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 3133[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.205421,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 8397
[DEBUG] http-outgoing-0 << ETag: "27e6b461d034c88651dbc7e74d268525"
[DEBUG] http-outgoing-0 << Content-Type: text/xml
[DEBUG] http-outgoing-0 << Last-Modified: Fri, 27 Feb 2009 04:50:22 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 27e6b461d034c88651dbc7e74d268525
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 564f266ea9323e57e246f0fca8f04f596663fb86
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 1832531
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 3133
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.205421,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">[\r][\n]"
[DEBUG] http-outgoing-0 << "    <modelVersion>4.0.0</modelVersion>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <groupId>org.sonatype.forge</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <artifactId>forge-parent</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <packaging>pom</packaging>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <version>4</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <name>Sonatype Forge Parent Pom</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <inceptionYear>2008</inceptionYear>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <url>http://forge.sonatype.com/</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <scm>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <connection>scm:svn:http://svn.sonatype.org/forge/tags/forge-parent-4</connection>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <url>http://svn.sonatype.org/forge/tags/forge-parent-4</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <developerConnection>scm:svn:https://svn.sonatype.org/forge/tags/forge-parent-4</developerConnection>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </scm>[\r][\n]"
[DEBUG] http-outgoing-0 << " [\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "    <properties>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <forgeReleaseId>forge-releases</forgeReleaseId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <forgeReleaseUrl>http://repository.sonatype.org/content/repositories/releases</forgeReleaseUrl>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <forgeSnapshotId>forge-snapshots</forgeSnapshotId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <forgeSnapshotUrl>http://repository.sonatype.org/content/repositories/snapshots</forgeSnapshotUrl>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </properties>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <distributionManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <repository>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <id>${forgeReleaseId}</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <url>${forgeReleaseUrl}</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </repository>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <snapshotRepository>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <id>${forgeSnapshotId}</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <url>${forgeSnapshotUrl}</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </snapshotRepository>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </distributionManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "    [\r][\n]"
[DEBUG] http-outgoing-0 << "    [\r][\n]"
[DEBUG] http-outgoing-0 << "    [\r][\n]"
[DEBUG] http-outgoing-0 << "    <build>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <!--set the default plugin revs-->[\r][\n]"
[DEBUG] http-outgoing-0 << "        <pluginManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "               <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <artifactId>maven-enforcer-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <version>1.0-alpha-4-sonatype</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <artifactId>maven-eclipse-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <version>2.4</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <artifactId>maven-surefire-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <version>2.3</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <artifactId>maven-clean-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <version>2.2</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <artifactId>maven-deploy-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <version>2.3</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <artifactId>maven-dependency-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <version>2.0</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <artifactId>maven-site-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <version>2.0-beta-6</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <artifactId>maven-jar-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <version>2.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <artifactId>maven-help-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <version>2.0.2</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </plugin>                [\r][\n]"
[DEBUG] http-outgoing-0 << "                <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <artifactId>maven-resources-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <version>2.2</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <artifactId>maven-install-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <version>2.2</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <artifactId>maven-compiler-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <version>2.0.2</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "                        <source>1.5</source>[\r][\n]"
[DEBUG] http-outgoing-0 << "                        <target>1.5</target>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <artifactId>maven-assembly-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <version>2.2-beta-1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "                "
Progress (1): forge-parent-4.pom (4.1/8.4 kB)
[DEBUG] http-outgoing-0 << "</plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <artifactId>maven-release-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <version>2.0-beta-8</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "                      <useReleaseProfile>false</useReleaseProfile>[\r][\n]"
[DEBUG] http-outgoing-0 << "                      <goals>deploy</goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                      <arguments>-Prelease</arguments>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <artifactId>maven-gpg-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <version>1.0-alpha-4</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </pluginManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "    </build>[\r][\n]"
[DEBUG] http-outgoing-0 << "    [\r][\n]"
[DEBUG] http-outgoing-0 << "  <reporting>[\r][\n]"
[DEBUG] http-outgoing-0 << "[0x9][0x9]<plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "[0x9][0x9][0x9]<plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "[0x9][0x9][0x9][0x9]<groupId>org.codehaus.mojo</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "[0x9][0x9][0x9][0x9]<artifactId>cobertura-maven-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "[0x9][0x9][0x9][0x9]<version>2.0</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "[0x9][0x9][0x9]</plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "[0x9][0x9][0x9][0x9]<groupId>org.codehaus.mojo</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "[0x9][0x9][0x9][0x9]<artifactId>findbugs-maven-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "[0x9][0x9][0x9][0x9]<version>1.1.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <omitVisitors>UnreadFields</omitVisitors>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "[0x9][0x9][0x9]</plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "[0x9][0x9][0x9][0x9]<artifactId>maven-jxr-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "[0x9][0x9][0x9][0x9]<version>2.0</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "[0x9][0x9][0x9]</plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "[0x9][0x9][0x9][0x9]<artifactId>maven-pmd-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "[0x9][0x9][0x9][0x9]<version>2.3</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <targetJdk>1.5</targetJdk> [\r][\n]"
[DEBUG] http-outgoing-0 << "                </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "[0x9][0x9][0x9]</plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "[0x9][0x9]</plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "        [\r][\n]"
[DEBUG] http-outgoing-0 << "[0x9]</reporting>[\r][\n]"
[DEBUG] http-outgoing-0 << " [\r][\n]"
[DEBUG] http-outgoing-0 << "  [\r][\n]"
[DEBUG] http-outgoing-0 << "  <profiles>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <id>release</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <build>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <!-- We want to sign the artifact, the POM, and all attached artifacts -->[\r][\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-gpg-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "               <!--  this presumes the correct gpg.passphrase property in the settings "release" proile -->[\r][\n]"
[DEBUG] http-outgoing-0 << "              <passphrase>${gpg.passphrase}</passphrase>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <goal>sign</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <!-- We want to deploy the artifact to a staging location for perusal -->[\r][\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <inherited>true</inherited>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-deploy-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <altDeploymentRepository>${deploy.altRepository}</altDeploymentRepository>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <updateReleaseInfo>true</updateReleaseInfo>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-source-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <id>attach-sources</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <goal>jar</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-javadoc-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <encoding>${project.build.sourceEncoding}</encoding>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <id>attach-javadocs</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <goal>jar</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <!--plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.codehaus.mojo</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>clirr-maven-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <id>clirr-check</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <phase>package</phase>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  "
Progress (1): forge-parent-4.pom (8.2/8.4 kB)
[DEBUG] http-outgoing-0 << "<goal>check</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin-->[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </build>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </profiles>[\r][\n]"
[DEBUG] http-outgoing-0 << "  [\r][\n]"
[DEBUG] http-outgoing-0 << "</project>"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
Progress (1): forge-parent-4.pom (8.4 kB)    
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/sonatype/forge/forge-parent/4/forge-parent-4.pom.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/sonatype/forge/forge-parent/4/forge-parent-4.pom.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/sonatype/forge/forge-parent/4/forge-parent-4.pom.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "253510d5c962058d4aafa9829baf7a09"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Fri, 27 Feb 2009 04:59:12 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 253510d5c962058d4aafa9829baf7a09[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 4b83085669fcb1130a800cc9bcf76f293a2e9e52[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 723331[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 2201[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.223578,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "253510d5c962058d4aafa9829baf7a09"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Fri, 27 Feb 2009 04:59:12 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 253510d5c962058d4aafa9829baf7a09
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 4b83085669fcb1130a800cc9bcf76f293a2e9e52
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 723331
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 2201
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.223578,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "564f266ea9323e57e246f0fca8f04f596663fb86"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
                                         
Downloaded from central: https://repo.maven.apache.org/maven2/org/sonatype/forge/forge-parent/4/forge-parent-4.pom (8.4 kB at 323 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonatype/forge/forge-parent/4/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonatype/forge/forge-parent/4/forge-parent-4.pom.lastUpdated
[DEBUG] Resolving artifact org.codehaus.plexus:plexus-utils:pom:1.5.5 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-utils/1.5.5/plexus-utils-1.5.5.pom
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/codehaus/plexus/plexus-utils/1.5.5/plexus-utils-1.5.5.pom HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/codehaus/plexus/plexus-utils/1.5.5/plexus-utils-1.5.5.pom HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/codehaus/plexus/plexus-utils/1.5.5/plexus-utils-1.5.5.pom HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 5147[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "4ef8462d6c47b1564a4dca078225ebfd"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/xml[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Thu, 03 Jul 2008 19:22:59 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 4ef8462d6c47b1564a4dca078225ebfd[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 6d04eaae6db5f8d879332da294a46df0fd81450f[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 1855300[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 2840[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.237868,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 5147
[DEBUG] http-outgoing-0 << ETag: "4ef8462d6c47b1564a4dca078225ebfd"
[DEBUG] http-outgoing-0 << Content-Type: text/xml
[DEBUG] http-outgoing-0 << Last-Modified: Thu, 03 Jul 2008 19:22:59 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 4ef8462d6c47b1564a4dca078225ebfd
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 6d04eaae6db5f8d879332da294a46df0fd81450f
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 1855300
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 2840
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.237868,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "<?xml version="1.0" encoding="UTF-8"?>[\r][\n]"
[DEBUG] http-outgoing-0 << "<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">[\r][\n]"
[DEBUG] http-outgoing-0 << "  <parent>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <artifactId>plexus</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <groupId>org.codehaus.plexus</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <version>1.0.11</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <relativePath>../pom/pom.xml</relativePath>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </parent>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <modelVersion>4.0.0</modelVersion>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <artifactId>plexus-utils</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <name>Plexus Common Utilities</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <version>1.5.5</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <url>http://plexus.codehaus.org/plexus-utils</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <issueManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <system>JIRA</system>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <url>http://jira.codehaus.org/browse/PLXUTILS</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </issueManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <scm>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <connection>scm:svn:http://svn.codehaus.org/plexus/plexus-utils/tags/plexus-utils-1.5.5</connection>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <developerConnection>scm:svn:https://svn.codehaus.org/plexus/plexus-utils/tags/plexus-utils-1.5.5</developerConnection>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <url>http://fisheye.codehaus.org/browse/plexus/plexus-utils/tags/plexus-utils-1.5.5</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </scm>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <build>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-clean-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <version>2.2</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-compiler-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <version>2.0.2</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <source>1.3</source>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <target>1.3</target>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-jar-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <version>2.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-resources-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <version>2.2</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-surefire-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <version>2.3</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <childDelegation>true</childDelegation>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <excludes>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <exclude>org/codehaus/plexus/util/FileBasedTestCase.java</exclude>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <exclude>**/Test*.java</exclude>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </excludes>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <systemProperties>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <property>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <name>JAVA_HOME</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <value>${JAVA_HOME}</value>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </property>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <property>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <name>M2_HOME</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <value>${M2_HOME}</value>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </property>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </systemProperties>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-shade-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <version>1.0.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <id>shade</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <goal>shade</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <artifactSet>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <includes>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <include>org.codehaus.plexus:plexus-interpolation</include>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </includes>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </artifactSet>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <relocations>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <relocation>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <pattern>org.codehaus.plexus.interpolation</pattern>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </relocation>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </relocations>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <keepDependenciesWithProvidedScope>true</keepDependenciesWithProvidedScope>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-release-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <version>2.0-beta-7</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <useReleaseProfile>true</useReleaseProfile>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <tagBase>https://svn.codehaus.org/plexus/plexus-utils/tags/</tagBase>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <arguments>-Prelease</arguments>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </build>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <profiles>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <id>release</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <build>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-source-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <id>attach-sources</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <goal>jar</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << " "
Progress (1): plexus-utils-1.5.5.pom (4.1/5.1 kB)
[DEBUG] http-outgoing-0 << "         <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-javadoc-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <id>attach-javadocs</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <goal>jar</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </build>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </profiles>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <dependencies>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <groupId>junit</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>junit</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <version>3.8.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <scope>test</scope>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.codehaus.plexus</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>plexus-interpolation</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <version>1.0</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <scope>provided</scope>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </dependencies>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <reporting>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-javadoc-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-jxr-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </reporting>[\r][\n]"
[DEBUG] http-outgoing-0 << "</project>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
Progress (1): plexus-utils-1.5.5.pom (5.1 kB)    
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/codehaus/plexus/plexus-utils/1.5.5/plexus-utils-1.5.5.pom.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/codehaus/plexus/plexus-utils/1.5.5/plexus-utils-1.5.5.pom.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/codehaus/plexus/plexus-utils/1.5.5/plexus-utils-1.5.5.pom.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "2a6dc34b67eb5216493d110a46f1a29f"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Thu, 03 Jul 2008 19:23:01 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 2a6dc34b67eb5216493d110a46f1a29f[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: c789eaf11d5637063822d7de79c7e4628e270aaf[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 4867956[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 922[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.258540,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "2a6dc34b67eb5216493d110a46f1a29f"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Thu, 03 Jul 2008 19:23:01 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 2a6dc34b67eb5216493d110a46f1a29f
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: c789eaf11d5637063822d7de79c7e4628e270aaf
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 4867956
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 922
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.258540,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "6d04eaae6db5f8d879332da294a46df0fd81450f"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
                                             
Downloaded from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-utils/1.5.5/plexus-utils-1.5.5.pom (5.1 kB at 206 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/codehaus/plexus/plexus-utils/1.5.5/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/codehaus/plexus/plexus-utils/1.5.5/plexus-utils-1.5.5.pom.lastUpdated
[DEBUG] Resolving artifact org.codehaus.plexus:plexus:pom:1.0.11 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus/1.0.11/plexus-1.0.11.pom
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/codehaus/plexus/plexus/1.0.11/plexus-1.0.11.pom HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/codehaus/plexus/plexus/1.0.11/plexus-1.0.11.pom HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/codehaus/plexus/plexus/1.0.11/plexus-1.0.11.pom HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 8969[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "bdf8dcfe0877af604f7e19e9ffdf260b"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/xml[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Sat, 10 Mar 2007 16:49:38 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: bdf8dcfe0877af604f7e19e9ffdf260b[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 4693d4512d50c5159bef1c49def1d2690a327c30[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 695822[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 3200[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.267206,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 8969
[DEBUG] http-outgoing-0 << ETag: "bdf8dcfe0877af604f7e19e9ffdf260b"
[DEBUG] http-outgoing-0 << Content-Type: text/xml
[DEBUG] http-outgoing-0 << Last-Modified: Sat, 10 Mar 2007 16:49:38 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: bdf8dcfe0877af604f7e19e9ffdf260b
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 4693d4512d50c5159bef1c49def1d2690a327c30
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 695822
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 3200
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.267206,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">[\n]"
[DEBUG] http-outgoing-0 << "  <modelVersion>4.0.0</modelVersion>[\n]"
[DEBUG] http-outgoing-0 << "  <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "  <artifactId>plexus</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "  <packaging>pom</packaging>[\n]"
[DEBUG] http-outgoing-0 << "  <name>Plexus</name>[\n]"
[DEBUG] http-outgoing-0 << "  <version>1.0.11</version>[\n]"
[DEBUG] http-outgoing-0 << "  <ciManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <notifiers>[\n]"
[DEBUG] http-outgoing-0 << "      <notifier>[\n]"
[DEBUG] http-outgoing-0 << "        <type>mail</type>[\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <address>dev@plexus.codehaus.org</address>[\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "      </notifier>[\n]"
[DEBUG] http-outgoing-0 << "      <notifier>[\n]"
[DEBUG] http-outgoing-0 << "        <type>irc</type>[\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <host>irc.codehaus.org</host>[\n]"
[DEBUG] http-outgoing-0 << "          <port>6667</port>[\n]"
[DEBUG] http-outgoing-0 << "          <channel>#plexus</channel>[\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "      </notifier>      [\n]"
[DEBUG] http-outgoing-0 << "    </notifiers>[\n]"
[DEBUG] http-outgoing-0 << "  </ciManagement>[\n]"
[DEBUG] http-outgoing-0 << "  <inceptionYear>2001</inceptionYear>[\n]"
[DEBUG] http-outgoing-0 << "  <licenses>[\n]"
[DEBUG] http-outgoing-0 << "    <license>[\n]"
[DEBUG] http-outgoing-0 << "      <name>The Apache Software License, Version 2.0</name>[\n]"
[DEBUG] http-outgoing-0 << "      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>[\n]"
[DEBUG] http-outgoing-0 << "      <distribution>repo</distribution>[\n]"
[DEBUG] http-outgoing-0 << "    </license>[\n]"
[DEBUG] http-outgoing-0 << "  </licenses>  [\n]"
[DEBUG] http-outgoing-0 << "  <mailingLists>[\n]"
[DEBUG] http-outgoing-0 << "    <mailingList>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Plexus User List</name>[\n]"
[DEBUG] http-outgoing-0 << "      <subscribe>http://xircles.codehaus.org/manage_email/user%40plexus.codehaus.org</subscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <unsubscribe>http://xircles.codehaus.org/manage_email/user%40plexus.codehaus.org</unsubscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <archive>http://archive.plexus.codehaus.org/user</archive>[\n]"
[DEBUG] http-outgoing-0 << "    </mailingList>[\n]"
[DEBUG] http-outgoing-0 << "    <mailingList>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Plexus Developer List</name>[\n]"
[DEBUG] http-outgoing-0 << "      <subscribe>http://xircles.codehaus.org/manage_email/dev%40plexus.codehaus.org</subscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <unsubscribe>http://xircles.codehaus.org/manage_email/dev%40plexus.codehaus.org</unsubscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <archive>http://archive.plexus.codehaus.org/dev</archive>[\n]"
[DEBUG] http-outgoing-0 << "    </mailingList>[\n]"
[DEBUG] http-outgoing-0 << "    <mailingList>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Plexus Announce List</name>[\n]"
[DEBUG] http-outgoing-0 << "      <subscribe>http://xircles.codehaus.org/manage_email/announce%40plexus.codehaus.org</subscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <unsubscribe>http://xircles.codehaus.org/manage_email/announce%40plexus.codehaus.org</unsubscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <archive>http://archive.plexus.codehaus.org/announce</archive>[\n]"
[DEBUG] http-outgoing-0 << "    </mailingList>[\n]"
[DEBUG] http-outgoing-0 << "    <mailingList>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Plexus Commit List</name>[\n]"
[DEBUG] http-outgoing-0 << "      <subscribe>http://xircles.codehaus.org/manage_email/scm%40plexus.codehaus.org</subscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <unsubscribe>http://xircles.codehaus.org/manage_email/scm%40plexus.codehaus.org</unsubscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <archive>http://archive.plexus.codehaus.org/scm</archive>[\n]"
[DEBUG] http-outgoing-0 << "    </mailingList>[\n]"
[DEBUG] http-outgoing-0 << "  </mailingLists>[\n]"
[DEBUG] http-outgoing-0 << "  <issueManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <system>JIRA</system>[\n]"
[DEBUG] http-outgoing-0 << "    <url>http://jira.codehaus.org/browse/PLX</url>[\n]"
[DEBUG] http-outgoing-0 << "  </issueManagement>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <distributionManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <repository>[\n]"
[DEBUG] http-outgoing-0 << "      <id>codehaus.org</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Plexus Central Repository</name>[\n]"
[DEBUG] http-outgoing-0 << "      <url>dav:https://dav.codehaus.org/repository/plexus</url>[\n]"
[DEBUG] http-outgoing-0 << "    </repository>[\n]"
[DEBUG] http-outgoing-0 << "    <snapshotRepository>[\n]"
[DEBUG] http-outgoing-0 << "      <id>codehaus.org</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Plexus Central Development Repository</name>[\n]"
[DEBUG] http-outgoing-0 << "      <url>dav:https://dav.codehaus.org/snapshots.repository/plexus</url>[\n]"
[DEBUG] http-outgoing-0 << "    </snapshotRepository>[\n]"
[DEBUG] http-outgoing-0 << "    <site>[\n]"
[DEBUG] http-outgoing-0 << "      <id>codehaus.org</id>[\n]"
[DEBUG] http-outgoing-0 << "      <url>dav:https://dav.codehaus.org/plexus</url>[\n]"
[DEBUG] http-outgoing-0 << "    </site>[\n]"
[DEBUG] http-outgoing-0 << "  </distributionManagement>[\n]"
[DEBUG] http-outgoing-0 << "  <repositories>[\n]"
[DEBUG] http-outgoing-0 << "    <repository>[\n]"
[DEBUG] http-outgoing-0 << "      <id>codehaus.snapshots</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Codehaus Snapshot Development Repository</name>[\n]"
[DEBUG] http-outgoing-0 << "      <url>http://snapshots.repository.codehaus.org</url>[\n]"
[DEBUG] http-outgoing-0 << "      <releases>[\n]"
[DEBUG] http-outgoing-0 << "        <enabled>false</enabled>[\n]"
[DEBUG] http-outgoing-0 << "      </releases>[\n]"
[DEBUG] http-outgoing-0 << "    </repository>[\n]"
[DEBUG] http-outgoing-0 << "  </repositories>[\n]"
[DEBUG] http-outgoing-0 << "  [\n]"
[DEBUG] http-outgoing-0 << "  <developers>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>jvanzyl</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Jason van Zyl</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>jason@maven.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Release Manager</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>kaz</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Pete Kazmier</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email />[\n]"
[DEBUG] http-outgoing-0 << "      <organization />[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>jtaylor</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>James Taylor</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>james@jamestaylor.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization />[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <dev"
Progress (1): plexus-1.0.11.pom (4.1/9.0 kB)
[DEBUG] http-outgoing-0 << "eloper>[\n]"
[DEBUG] http-outgoing-0 << "      <id>dandiep</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Dan Diephouse</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>dan@envoisolutions.com</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>Envoi solutions</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>kasper</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Kasper Nielsen</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>apache@kav.dk</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization />[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>bwalding</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Ben Walding</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>bwalding@codehaus.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>Walding Consulting Services</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>mhw</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Mark Wilkinson</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>mhw@kremvax.net</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>michal</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Michal Maczka</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>mmaczka@interia.pl</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>evenisse</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Emmanuel Venisse</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>evenisse@codehaus.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Trygve Laugstol</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>trygvis</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>trygvis@codehaus.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Kenney Westerhof</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>kenney</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>kenney@codehaus.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Carlos Sanchez</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>carlos</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>carlos@codehaus.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Brett Porter</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>brett</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>brett@codehaus.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>John Casey</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>jdcasey</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>jdcasey@codehaus.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Andrew Williams</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>handyande</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>andy@handyande.co.uk</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Rahul Thakur</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>rahul</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>rahul.thakur.xdev@gmail.com</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Joakim Erdfelt</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>joakime</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>joakim@erdfelt.com</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Olivier Lamy</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>olamy</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>olamy@codehaus.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "  </developers>[\n]"
[DEBUG] http-outgoing-0 << "  <dependencies>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>junit</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>junit</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>3.8.1</version>[\n]"
[DEBUG] http-outgoing-0 << "      <scope>test</scope>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "  </dependencies>[\n]"
[DEBUG] http-outgoing-0 << "  <scm>[\n]"
[DEBUG] http-outgoing-0 << "    <connection>scm:svn:http://svn.codehaus.org/plexus/pom/trunk/</connection>[\n]"
[DEBUG] http-outgoing-0 << "    <developerConnection>scm:svn:https://svn.codehaus.org/plexus/pom/trunk/</developerConnection>[\n]"
[DEBUG] http-outgoing-0 << "    <url>http://fisheye.codehaus.org/browse/plexus/pom/trunk/</url>[\n]"
[DEBUG] http-outgoing-0 << "  </scm>[\n]"
[DEBUG] http-outgoing-0 << "  <organization>[\n]"
[DEBUG] http-outgoing-0 << "    <name>Codehaus</name>[\n]"
[DEBUG] http-outgoing-0 << "    <url>http://www.codehaus.org/</url>[\n]"
[DEBUG] http-outgoing-0 << "  </organization>[\n]"
[DEBUG] http-outgoing-0 << "  <build>[\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-compiler-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <source>1.4</source>[\n]"
[DEBUG] http-outgoing-0 << "          <target>1.4</target>[\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <!--[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-remote-resources-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>1.0-alpha-2</version>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>process</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "            <configurat"
Progress (1): plexus-1.0.11.pom (8.2/9.0 kB)
[DEBUG] http-outgoing-0 << "ion>[\n]"
[DEBUG] http-outgoing-0 << "              <resourceBundles>[\n]"
[DEBUG] http-outgoing-0 << "                <resourceBundle>org.apache:apache-jar-resource-bundle:1.1.1-SNAPSHOT</resourceBundle>[\n]"
[DEBUG] http-outgoing-0 << "              </resourceBundles>[\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      -->[\n]"
[DEBUG] http-outgoing-0 << "    </plugins>[\n]"
[DEBUG] http-outgoing-0 << "    <extensions>[\n]"
[DEBUG] http-outgoing-0 << "      <extension>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.wagon</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>wagon-webdav</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>1.0-beta-2</version>[\n]"
[DEBUG] http-outgoing-0 << "      </extension>[\n]"
[DEBUG] http-outgoing-0 << "    </extensions>[\n]"
[DEBUG] http-outgoing-0 << "    <pluginManagement>[\n]"
[DEBUG] http-outgoing-0 << "      <plugins>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-release-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>deploy</goals>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      </plugins>[\n]"
[DEBUG] http-outgoing-0 << "    </pluginManagement>    [\n]"
[DEBUG] http-outgoing-0 << "  </build>[\n]"
[DEBUG] http-outgoing-0 << "</project>[\n]"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
Progress (1): plexus-1.0.11.pom (9.0 kB)    
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/codehaus/plexus/plexus/1.0.11/plexus-1.0.11.pom.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/codehaus/plexus/plexus/1.0.11/plexus-1.0.11.pom.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/codehaus/plexus/plexus/1.0.11/plexus-1.0.11.pom.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "a31c7ab32b14fb8681731a09bbd3ad56"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Sat, 10 Mar 2007 16:49:40 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: a31c7ab32b14fb8681731a09bbd3ad56[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 1a96a8b5fcdb73df6f03e39415c710863d43cbb9[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 641119[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 2408[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.284817,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "a31c7ab32b14fb8681731a09bbd3ad56"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Sat, 10 Mar 2007 16:49:40 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: a31c7ab32b14fb8681731a09bbd3ad56
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 1a96a8b5fcdb73df6f03e39415c710863d43cbb9
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 641119
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 2408
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.284817,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "4693d4512d50c5159bef1c49def1d2690a327c30"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
                                        
Downloaded from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus/1.0.11/plexus-1.0.11.pom (9.0 kB at 359 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/codehaus/plexus/plexus/1.0.11/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/codehaus/plexus/plexus/1.0.11/plexus-1.0.11.pom.lastUpdated
[DEBUG] Using mirror maven-default-http-blocker (http://0.0.0.0/) for codehaus.snapshots (http://snapshots.repository.codehaus.org).
[DEBUG] Resolving artifact org.sonatype.plexus:plexus-cipher:pom:1.4 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/org/sonatype/plexus/plexus-cipher/1.4/plexus-cipher-1.4.pom
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/sonatype/plexus/plexus-cipher/1.4/plexus-cipher-1.4.pom HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/sonatype/plexus/plexus-cipher/1.4/plexus-cipher-1.4.pom HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/sonatype/plexus/plexus-cipher/1.4/plexus-cipher-1.4.pom HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 2065[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "d9598c49bfc5b53719cf2f5bf0c01c79"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/xml[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Wed, 29 Apr 2009 19:40:02 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: d9598c49bfc5b53719cf2f5bf0c01c79[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 8c0bee97c1badb926611bf82358e392fedc07764[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 612368[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 3054[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.320615,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 2065
[DEBUG] http-outgoing-0 << ETag: "d9598c49bfc5b53719cf2f5bf0c01c79"
[DEBUG] http-outgoing-0 << Content-Type: text/xml
[DEBUG] http-outgoing-0 << Last-Modified: Wed, 29 Apr 2009 19:40:02 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: d9598c49bfc5b53719cf2f5bf0c01c79
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 8c0bee97c1badb926611bf82358e392fedc07764
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 612368
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 3054
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.320615,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "<?xml version="1.0"?>[\n]"
[DEBUG] http-outgoing-0 << "<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">[\n]"
[DEBUG] http-outgoing-0 << "  <parent>[\n]"
[DEBUG] http-outgoing-0 << "    <groupId>org.sonatype.spice</groupId>[\n]"
[DEBUG] http-outgoing-0 << "    <artifactId>spice-parent</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "    <version>12</version>[\n]"
[DEBUG] http-outgoing-0 << "  </parent>[\n]"
[DEBUG] http-outgoing-0 << "  [\n]"
[DEBUG] http-outgoing-0 << "  <modelVersion>4.0.0</modelVersion>[\n]"
[DEBUG] http-outgoing-0 << "  <groupId>org.sonatype.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "  <artifactId>plexus-cipher</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "  <url>http://spice.sonatype.org/${project.artifactId}</url>[\n]"
[DEBUG] http-outgoing-0 << "  [\n]"
[DEBUG] http-outgoing-0 << "  <name>Plexus Cipher: encryption/decryption Component</name>[\n]"
[DEBUG] http-outgoing-0 << "  <version>1.4</version>[\n]"
[DEBUG] http-outgoing-0 << "  [\n]"
[DEBUG] http-outgoing-0 << "  <distributionManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <site>[\n]"
[DEBUG] http-outgoing-0 << "      <id>sonatype.org-sites</id>[\n]"
[DEBUG] http-outgoing-0 << "      <url>${spiceSiteBaseUrl}/${project.artifactId}</url>[\n]"
[DEBUG] http-outgoing-0 << "    </site>[\n]"
[DEBUG] http-outgoing-0 << "  </distributionManagement>[\n]"
[DEBUG] http-outgoing-0 << "  [\n]"
[DEBUG] http-outgoing-0 << "  <build>[\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>plexus-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>1.3.5</version>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>descriptor</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-compiler-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <source>1.4</source>[\n]"
[DEBUG] http-outgoing-0 << "          <target>1.4</target>[\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "    </plugins>[\n]"
[DEBUG] http-outgoing-0 << "  </build>[\n]"
[DEBUG] http-outgoing-0 << "  [\n]"
[DEBUG] http-outgoing-0 << "  <dependencies>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>plexus-container-default</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>1.0-alpha-9-stable-1</version>[\n]"
[DEBUG] http-outgoing-0 << "      <scope>provided</scope>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>junit</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>junit</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>3.8.2</version>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "  </dependencies>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <scm>[\n]"
[DEBUG] http-outgoing-0 << "    <connection>scm:svn:http://svn.sonatype.org/spice/tags/plexus-cipher-1.4</connection>[\n]"
[DEBUG] http-outgoing-0 << "    <developerConnection>scm:svn:https://svn.sonatype.org/spice/tags/plexus-cipher-1.4</developerConnection>[\n]"
[DEBUG] http-outgoing-0 << "    <url>http://svn.sonatype.org/spice/tags/plexus-cipher-1.4</url>[\n]"
[DEBUG] http-outgoing-0 << "  </scm>[\n]"
[DEBUG] http-outgoing-0 << "</project>[\n]"
Progress (1): plexus-cipher-1.4.pom (2.1 kB)
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/sonatype/plexus/plexus-cipher/1.4/plexus-cipher-1.4.pom.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/sonatype/plexus/plexus-cipher/1.4/plexus-cipher-1.4.pom.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/sonatype/plexus/plexus-cipher/1.4/plexus-cipher-1.4.pom.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "a70712376f89ae0cb73aa4b7e5feab34"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Wed, 29 Apr 2009 19:40:03 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: a70712376f89ae0cb73aa4b7e5feab34[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 21436b6f958d278bee41eba776f4d1383185632b[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 716971[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 1000[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.356459,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "a70712376f89ae0cb73aa4b7e5feab34"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Wed, 29 Apr 2009 19:40:03 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: a70712376f89ae0cb73aa4b7e5feab34
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 21436b6f958d278bee41eba776f4d1383185632b
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 716971
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 1000
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.356459,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "8c0bee97c1badb926611bf82358e392fedc07764"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
                                            
Downloaded from central: https://repo.maven.apache.org/maven2/org/sonatype/plexus/plexus-cipher/1.4/plexus-cipher-1.4.pom (2.1 kB at 44 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonatype/plexus/plexus-cipher/1.4/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonatype/plexus/plexus-cipher/1.4/plexus-cipher-1.4.pom.lastUpdated
[DEBUG] Resolving artifact org.codehaus.plexus:plexus-utils:pom:3.2.1 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-utils/3.2.1/plexus-utils-3.2.1.pom
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/codehaus/plexus/plexus-utils/3.2.1/plexus-utils-3.2.1.pom HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/codehaus/plexus/plexus-utils/3.2.1/plexus-utils-3.2.1.pom HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/codehaus/plexus/plexus-utils/3.2.1/plexus-utils-3.2.1.pom HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 5342[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "7b684e32814dbcc4ade32fd14cb5cbca"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/xml[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Wed, 10 Jul 2019 18:47:47 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 7b684e32814dbcc4ade32fd14cb5cbca[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 7caaedbb1cfaa1605581d2b0c9b8ced8dea5ebb0[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 693513[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 88[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.386598,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 5342
[DEBUG] http-outgoing-0 << ETag: "7b684e32814dbcc4ade32fd14cb5cbca"
[DEBUG] http-outgoing-0 << Content-Type: text/xml
[DEBUG] http-outgoing-0 << Last-Modified: Wed, 10 Jul 2019 18:47:47 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 7b684e32814dbcc4ade32fd14cb5cbca
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 7caaedbb1cfaa1605581d2b0c9b8ced8dea5ebb0
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 693513
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 88
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.386598,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "<?xml version="1.0" encoding="UTF-8"?>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "<!--[\r][\n]"
[DEBUG] http-outgoing-0 << "Copyright The Codehaus Foundation.[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "Licensed under the Apache License, Version 2.0 (the "License");[\r][\n]"
[DEBUG] http-outgoing-0 << "you may not use this file except in compliance with the License.[\r][\n]"
[DEBUG] http-outgoing-0 << "You may obtain a copy of the License at[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  http://www.apache.org/licenses/LICENSE-2.0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "Unless required by applicable law or agreed to in writing, software[\r][\n]"
[DEBUG] http-outgoing-0 << "distributed under the License is distributed on an "AS IS" BASIS,[\r][\n]"
[DEBUG] http-outgoing-0 << "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.[\r][\n]"
[DEBUG] http-outgoing-0 << "See the License for the specific language governing permissions and[\r][\n]"
[DEBUG] http-outgoing-0 << "limitations under the License.[\r][\n]"
[DEBUG] http-outgoing-0 << "-->[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">[\r][\n]"
[DEBUG] http-outgoing-0 << "  <modelVersion>4.0.0</modelVersion>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <parent>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <groupId>org.codehaus.plexus</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <artifactId>plexus</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <version>5.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </parent>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <artifactId>plexus-utils</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <version>3.2.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <name>Plexus Common Utilities</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <description>A collection of various utility classes to ease working with strings, files, command lines, XML and[\r][\n]"
[DEBUG] http-outgoing-0 << "    more.[\r][\n]"
[DEBUG] http-outgoing-0 << "  </description>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <scm>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <connection>scm:git:git@github.com:codehaus-plexus/plexus-utils.git</connection>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <developerConnection>scm:git:git@github.com:codehaus-plexus/plexus-utils.git</developerConnection>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <url>http://github.com/codehaus-plexus/plexus-utils</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <tag>plexus-utils-3.2.1</tag>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </scm>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <issueManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <system>github</system>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <url>http://github.com/codehaus-plexus/plexus-utils/issues</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </issueManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <distributionManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <site>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <id>github:gh-pages</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <url>${project.scm.developerConnection}</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </site>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </distributionManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <dependencies>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.shared</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-plugin-testing-harness</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <version>1.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <scope>test</scope>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.openjdk.jmh</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>jmh-core</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <version>1.21</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <scope>test</scope>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.openjdk.jmh</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>jmh-generator-annprocess</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <version>1.21</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <scope>test</scope>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </dependencies>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <build>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <pluginManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-javadoc-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>3.0.0</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </pluginManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-scm-publish-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <content>${project.reporting.outputDirectory}</content><!-- mono-module doesn't require site:stage -->[\r][\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <id>scm-publish</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <phase>site-deploy</phase><!-- deploy site with maven-scm-publish-plugin -->[\r][\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <goal>publish-scm</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-surefire-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <!-- required to ensure the test classes are used, not surefire's plexus-utils -->[\r][\n]"
[DEBUG] http-outgoing-0 << "          <childDelegation>true</childDelegation>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <excludes>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <exclude>org/codehaus/plexus/util/FileBasedTestCase.java</exclude>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <exclude>**/Test*.java</exclude>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </excludes>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <systemProperties>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <property>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <name>JAVA_HOME</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <value>${JAVA_HOME}</value>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </property>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <property>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <name>M2_HOME</n"
Progress (1): plexus-utils-3.2.1.pom (4.1/5.3 kB)
[DEBUG] http-outgoing-0 << "ame>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <value>${M2_HOME}</value>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </property>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </systemProperties>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-enforcer-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <id>enforce-java</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <goal>enforce</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <rules>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <requireJavaVersion>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <version>1.7.0</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </requireJavaVersion>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </rules>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </build>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <profiles>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <!-- See https://github.com/codehaus-plexus/plexus-utils/pull/27 -->[\r][\n]"
[DEBUG] http-outgoing-0 << "      <!-- m2e Eclipse plugin doesn't respect the maven-enforcer-plugin 'requireJavaVersion' parameter  -->[\r][\n]"
[DEBUG] http-outgoing-0 << "      <id>eclipse-only-jdk-version</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <activation>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <property>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <name>m2e.version</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </property>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </activation>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <properties>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <javaVersion>7</javaVersion>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </properties>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </profiles>[\r][\n]"
[DEBUG] http-outgoing-0 << "</project>[\r][\n]"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
Progress (1): plexus-utils-3.2.1.pom (5.3 kB)    
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/codehaus/plexus/plexus-utils/3.2.1/plexus-utils-3.2.1.pom.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/codehaus/plexus/plexus-utils/3.2.1/plexus-utils-3.2.1.pom.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/codehaus/plexus/plexus-utils/3.2.1/plexus-utils-3.2.1.pom.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "e5594d0e3583f84cc325fc3da23d4584"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Wed, 10 Jul 2019 18:47:47 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: e5594d0e3583f84cc325fc3da23d4584[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: fc83c62d4ff918633a8b9df093d0995545b20699[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 1952647[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 45[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.404191,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "e5594d0e3583f84cc325fc3da23d4584"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Wed, 10 Jul 2019 18:47:47 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: e5594d0e3583f84cc325fc3da23d4584
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: fc83c62d4ff918633a8b9df093d0995545b20699
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 1952647
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 45
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.404191,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "7caaedbb1cfaa1605581d2b0c9b8ced8dea5ebb0"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
                                             
Downloaded from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-utils/3.2.1/plexus-utils-3.2.1.pom (5.3 kB at 144 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/codehaus/plexus/plexus-utils/3.2.1/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/codehaus/plexus/plexus-utils/3.2.1/plexus-utils-3.2.1.pom.lastUpdated
[DEBUG] Resolving artifact org.codehaus.plexus:plexus:pom:5.1 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus/5.1/plexus-5.1.pom
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/codehaus/plexus/plexus/5.1/plexus-5.1.pom HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/codehaus/plexus/plexus/5.1/plexus-5.1.pom HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/codehaus/plexus/plexus/5.1/plexus-5.1.pom HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 22504[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "24561034d1dbd16a4b13f066a8535eea"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/xml[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Tue, 08 May 2018 09:22:33 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 24561034d1dbd16a4b13f066a8535eea[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 2fca82e2eb5172f6a2909bea7accc733581a8c71[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 1952817[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 1108[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.428744,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 22504
[DEBUG] http-outgoing-0 << ETag: "24561034d1dbd16a4b13f066a8535eea"
[DEBUG] http-outgoing-0 << Content-Type: text/xml
[DEBUG] http-outgoing-0 << Last-Modified: Tue, 08 May 2018 09:22:33 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 24561034d1dbd16a4b13f066a8535eea
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 2fca82e2eb5172f6a2909bea7accc733581a8c71
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 1952817
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 1108
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.428744,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "<?xml version="1.0" encoding="UTF-8"?>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "<!--[\n]"
[DEBUG] http-outgoing-0 << "Copyright The Codehaus Foundation.[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "Licensed under the Apache License, Version 2.0 (the "License");[\n]"
[DEBUG] http-outgoing-0 << "you may not use this file except in compliance with the License.[\n]"
[DEBUG] http-outgoing-0 << "You may obtain a copy of the License at[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  http://www.apache.org/licenses/LICENSE-2.0[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "Unless required by applicable law or agreed to in writing, software[\n]"
[DEBUG] http-outgoing-0 << "distributed under the License is distributed on an "AS IS" BASIS,[\n]"
[DEBUG] http-outgoing-0 << "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.[\n]"
[DEBUG] http-outgoing-0 << "See the License for the specific language governing permissions and[\n]"
[DEBUG] http-outgoing-0 << "limitations under the License.[\n]"
[DEBUG] http-outgoing-0 << "-->[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">[\n]"
[DEBUG] http-outgoing-0 << "  <modelVersion>4.0.0</modelVersion>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "  <artifactId>plexus</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "  <packaging>pom</packaging>[\n]"
[DEBUG] http-outgoing-0 << "  <version>5.1</version>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <name>Plexus</name>[\n]"
[DEBUG] http-outgoing-0 << "  <description>The Plexus project provides a full software stack for creating and executing software projects.</description>[\n]"
[DEBUG] http-outgoing-0 << "  <url>http://codehaus-plexus.github.io/</url>[\n]"
[DEBUG] http-outgoing-0 << "  <inceptionYear>2001</inceptionYear>[\n]"
[DEBUG] http-outgoing-0 << "  <organization>[\n]"
[DEBUG] http-outgoing-0 << "    <name>Codehaus Plexus</name>[\n]"
[DEBUG] http-outgoing-0 << "    <url>http://codehaus-plexus.github.io/</url>[\n]"
[DEBUG] http-outgoing-0 << "  </organization>[\n]"
[DEBUG] http-outgoing-0 << "  <licenses>[\n]"
[DEBUG] http-outgoing-0 << "    <license>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Apache License, Version 2.0</name>[\n]"
[DEBUG] http-outgoing-0 << "      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>[\n]"
[DEBUG] http-outgoing-0 << "      <distribution>repo</distribution>[\n]"
[DEBUG] http-outgoing-0 << "    </license>[\n]"
[DEBUG] http-outgoing-0 << "  </licenses>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <developers>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>jvanzyl</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Jason van Zyl</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>jason@maven.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Release Manager</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>kaz</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Pete Kazmier</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email />[\n]"
[DEBUG] http-outgoing-0 << "      <organization />[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>jtaylor</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>James Taylor</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>james@jamestaylor.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization />[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>dandiep</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Dan Diephouse</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>dan@envoisolutions.com</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>Envoi solutions</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>kasper</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Kasper Nielsen</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>apache@kav.dk</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization />[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>bwalding</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Ben Walding</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>bwalding@codehaus.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <organization>Walding Consulting Services</organization>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>mhw</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Mark Wilkinson</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>mhw@kremvax.net</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>michal</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Michal Maczka</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>mmaczka@interia.pl</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <id>evenisse</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Emmanuel Venisse</name>[\n]"
[DEBUG] http-outgoing-0 << "      <email>evenisse@codehaus.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Trygve Laugst[0xc3][0xb8]l</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>trygvis</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>trygvis@codehaus.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Kenney Westerhof</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>kenney</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>kenney@codehaus.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Carlos Sanchez</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>carlos</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>carlos@codehaus.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Brett Porter</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>brett</id>[\n]"
[DEBUG] http-outgoing-0 << "      "
Progress (1): plexus-5.1.pom (4.1/23 kB)
[DEBUG] http-outgoing-0 << "<email>brett@codehaus.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>John Casey</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>jdcasey</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>jdcasey@codehaus.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Andrew Williams</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>handyande</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>andy@handyande.co.uk</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Rahul Thakur</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>rahul</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>rahul.thakur.xdev@gmail.com</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Joakim Erdfelt</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>joakime</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>joakim@erdfelt.com</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Olivier Lamy</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>olamy</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>olamy@codehaus.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Herv[0xc3][0xa9] Boutemy</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>hboutemy</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>hboutemy@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Oleg Gusakov</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>oleg</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>olegy@codehaus.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Vincent Siveton</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>vsiveton</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>vsiveton@codehaus.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Kristian Rosenvold</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>krosenvold</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>krosenvold@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Andreas Gudian</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>agudian</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>agudian@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Karl Heinz Marbaise</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>khmarbaise</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>khmarbaise@apache.org</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "    <developer>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Michael Osipov</name>[\n]"
[DEBUG] http-outgoing-0 << "      <id>michael-o</id>[\n]"
[DEBUG] http-outgoing-0 << "      <email>1983-01-06@gmx.net</email>[\n]"
[DEBUG] http-outgoing-0 << "      <roles>[\n]"
[DEBUG] http-outgoing-0 << "        <role>Developer</role>[\n]"
[DEBUG] http-outgoing-0 << "      </roles>[\n]"
[DEBUG] http-outgoing-0 << "    </developer>[\n]"
[DEBUG] http-outgoing-0 << "  </developers>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <mailingLists><!-- TODO decide what we do with mailing lists now -->[\n]"
[DEBUG] http-outgoing-0 << "    <mailingList>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Plexus User List</name>[\n]"
[DEBUG] http-outgoing-0 << "      <subscribe>http://xircles.codehaus.org/manage_email/user%40plexus.codehaus.org</subscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <unsubscribe>http://xircles.codehaus.org/manage_email/user%40plexus.codehaus.org</unsubscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <archive>http://archive.plexus.codehaus.org/user</archive>[\n]"
[DEBUG] http-outgoing-0 << "      <post>user@plexus.codehaus.org</post>[\n]"
[DEBUG] http-outgoing-0 << "    </mailingList>[\n]"
[DEBUG] http-outgoing-0 << "    <mailingList>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Plexus Developer List</name>[\n]"
[DEBUG] http-outgoing-0 << "      <subscribe>http://xircles.codehaus.org/manage_email/dev%40plexus.codehaus.org</subscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <unsubscribe>http://xircles.codehaus.org/manage_email/dev%40plexus.codehaus.org</unsubscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <archive>http://archive.plexus.codehaus.org/dev</archive>[\n]"
[DEBUG] http-outgoing-0 << "      <post>dev@plexus.codehaus.org</post>[\n]"
[DEBUG] http-outgoing-0 << "    </mailingList>[\n]"
[DEBUG] http-outgoing-0 << "    <mailingList>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Plexus Announce List</name>[\n]"
[DEBUG] http-outgoing-0 << "      <subscribe>http://xircles.codehaus.org/manage_email/announce%40plexus.codehaus.org</subscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <unsubscribe>http://xircles.codehaus.org/manage_email/announce%40plexus.codehaus.org</unsubscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <archive>http://archive.plexus.codehaus.org/announce</archive>[\n]"
[DEBUG] http-outgoing-0 << "    </mailingList>[\n]"
[DEBUG] http-outgoing-0 << "    <mailingList>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Plexus Commit List</name>[\n]"
[DEBUG] http-outgoing-0 << "      <subscribe>http://xircles.codehaus.org/manage_email/scm%40plexus.codehaus.org</subscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <unsubscribe>http://xircles.codehaus.org/manage_email/scm%40plexus.codehaus.org</unsubscribe>[\n]"
[DEBUG] http-outgoing-0 << "      <archive>http://archive.plexus.codehaus.org/scm</archive>[\n]"
[DEBUG] http-outgoing-0 << "    </mailingList>[\n]"
[DEBUG] http-outgoing-0 << "  </mailingLists>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <scm>[\n]"
[DEBUG] http-outgoing-0 << "    <connection>scm:git:git@github.com:codehaus-plexus/plexus-pom.git</connection>[\n]"
[DEBUG] http-outgoing-0 << "    <developerConnection>scm:g"
Progress (1): plexus-5.1.pom (8.2/23 kB)
[DEBUG] http-outgoing-0 << "it:git@github.com:codehaus-plexus/plexus-pom.git</developerConnection>[\n]"
[DEBUG] http-outgoing-0 << "    <url>http://github.com/codehaus-plexus/plexus-pom/tree/${project.scm.tag}/</url>[\n]"
[DEBUG] http-outgoing-0 << "    <tag>plexus-5.1</tag>[\n]"
[DEBUG] http-outgoing-0 << "  </scm>[\n]"
[DEBUG] http-outgoing-0 << "  <issueManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <system>github</system>[\n]"
[DEBUG] http-outgoing-0 << "    <url>http://github.com/codehaus-plexus/plexus-pom/issues</url>[\n]"
[DEBUG] http-outgoing-0 << "  </issueManagement>[\n]"
[DEBUG] http-outgoing-0 << "  <distributionManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <repository>[\n]"
[DEBUG] http-outgoing-0 << "      <id>plexus-releases</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Plexus Release Repository</name>[\n]"
[DEBUG] http-outgoing-0 << "      <url>https://oss.sonatype.org/service/local/staging/deploy/maven2/</url>[\n]"
[DEBUG] http-outgoing-0 << "    </repository>[\n]"
[DEBUG] http-outgoing-0 << "    <snapshotRepository>[\n]"
[DEBUG] http-outgoing-0 << "      <id>plexus-snapshots</id>[\n]"
[DEBUG] http-outgoing-0 << "      <name>Plexus Snapshot Repository</name>[\n]"
[DEBUG] http-outgoing-0 << "      <url>${plexusDistMgmtSnapshotsUrl}</url>[\n]"
[DEBUG] http-outgoing-0 << "    </snapshotRepository>[\n]"
[DEBUG] http-outgoing-0 << "    <site>[\n]"
[DEBUG] http-outgoing-0 << "      <id>github:gh-pages</id>[\n]"
[DEBUG] http-outgoing-0 << "      <url>scm:git:git@github.com:codehaus-plexus</url><!-- url used only for inheritance -->[\n]"
[DEBUG] http-outgoing-0 << "    </site>[\n]"
[DEBUG] http-outgoing-0 << "  </distributionManagement>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <properties>[\n]"
[DEBUG] http-outgoing-0 << "    <javaVersion>6</javaVersion>[\n]"
[DEBUG] http-outgoing-0 << "    <maven.compiler.source>1.${javaVersion}</maven.compiler.source>[\n]"
[DEBUG] http-outgoing-0 << "    <maven.compiler.target>1.${javaVersion}</maven.compiler.target>    [\n]"
[DEBUG] http-outgoing-0 << "    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>[\n]"
[DEBUG] http-outgoing-0 << "    <plexusDistMgmtSnapshotsUrl>https://oss.sonatype.org/content/repositories/plexus-snapshots</plexusDistMgmtSnapshotsUrl>[\n]"
[DEBUG] http-outgoing-0 << "  </properties>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <dependencyManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <dependencies>[\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>plexus-component-annotations</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>1.6</version>[\n]"
[DEBUG] http-outgoing-0 << "        <scope>compile</scope>[\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    </dependencies>[\n]"
[DEBUG] http-outgoing-0 << "  </dependencyManagement>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <dependencies>[\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <groupId>junit</groupId>[\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>junit</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "      <version>4.12</version>[\n]"
[DEBUG] http-outgoing-0 << "      <scope>test</scope>[\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\n]"
[DEBUG] http-outgoing-0 << "  </dependencies>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <build>[\n]"
[DEBUG] http-outgoing-0 << "    <pluginManagement>[\n]"
[DEBUG] http-outgoing-0 << "      <plugins>[\n]"
[DEBUG] http-outgoing-0 << "        <!-- set versions of common plugins for reproducibility, ordered alphabetically -->[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-checkstyle-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.0.0</version>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <configLocation>config/maven_checks.xml</configLocation>[\n]"
[DEBUG] http-outgoing-0 << "            <headerLocation>https://raw.github.com/codehaus-plexus/plexus-pom/master/src/main/resources/config/plexus-header.txt</headerLocation>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "          <dependencies>[\n]"
[DEBUG] http-outgoing-0 << "            <!-- MCHECKSTYLE-327: the maven_checks.xml was moved to a shared project -->[\n]"
[DEBUG] http-outgoing-0 << "            <dependency>[\n]"
[DEBUG] http-outgoing-0 << "              <groupId>org.apache.maven.shared</groupId>[\n]"
[DEBUG] http-outgoing-0 << "              <artifactId>maven-shared-resources</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "              <version>2</version>[\n]"
[DEBUG] http-outgoing-0 << "            </dependency>[\n]"
[DEBUG] http-outgoing-0 << "          </dependencies>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-clean-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.0.0</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-compiler-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.5.1</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-deploy-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>2.8.2</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-enforcer-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>1.4.1</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-gpg-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>1.6</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-install-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>2.5.2</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-jar-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.0.2</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-javadoc-plugin</ar"
Progress (1): plexus-5.1.pom (12/23 kB) 
[DEBUG] http-outgoing-0 << "tifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>2.10.4</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-jxr-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>2.5</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-plugin-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.5.1</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-pmd-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.8</version>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <targetJdk>${maven.compiler.source}</targetJdk>[\n]"
[DEBUG] http-outgoing-0 << "            <rulesets>[\n]"
[DEBUG] http-outgoing-0 << "              <ruleset>rulesets/maven.xml</ruleset>[\n]"
[DEBUG] http-outgoing-0 << "            </rulesets>[\n]"
[DEBUG] http-outgoing-0 << "            <excludeRoots>[\n]"
[DEBUG] http-outgoing-0 << "              <excludeRoot>${project.build.directory}/generated-sources/modello</excludeRoot>[\n]"
[DEBUG] http-outgoing-0 << "              <excludeRoot>${project.build.directory}/generated-sources/plugin</excludeRoot>[\n]"
[DEBUG] http-outgoing-0 << "            </excludeRoots>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-project-info-reports-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>2.9</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-release-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>2.5.3</version>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>deploy</goals>[\n]"
[DEBUG] http-outgoing-0 << "            <mavenExecutorId>forked-path</mavenExecutorId>[\n]"
[DEBUG] http-outgoing-0 << "            <useReleaseProfile>false</useReleaseProfile>[\n]"
[DEBUG] http-outgoing-0 << "            <arguments>-Pplexus-release</arguments>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-resources-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.0.2</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-scm-publish-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.0.0</version>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <!-- using scm.developerConnection instead of distributionManagement.site.url -->[\n]"
[DEBUG] http-outgoing-0 << "            <pubScmUrl>${project.scm.developerConnection}</pubScmUrl>[\n]"
[DEBUG] http-outgoing-0 << "            <scmBranch>gh-pages</scmBranch>[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-site-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.7.1</version>[\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <skipDeploy>true</skipDeploy><!-- don't deploy site with maven-site-plugin -->[\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-source-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.0.1</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-surefire-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>2.20</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-surefire-report-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>2.20</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.mojo</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>cobertura-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>2.7</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.mojo</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>findbugs-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>3.0.4</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.mojo</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>taglist-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>2.4</version>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.plexus</groupId>[\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>plexus-component-metadata</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          <version>1.7.1</version>[\n]"
[DEBUG] http-outgoing-0 << "          <executions>[\n]"
[DEBUG] http-outgoing-0 << "            <execution>[\n]"
[DEBUG] http-outgoing-0 << "              <id>process-classes</id>[\n]"
[DEBUG] http-outgoing-0 << "              <goals>[\n]"
[DEBUG] http-outgoing-0 << "                <goal>generate-metadata</goal>[\n]"
[DEBUG] http-outgoing-0 << "              </goals>[\n]"
[DEBUG] http-outgoing-0 << "            </execution>[\n]"
[DEBUG] http-outgoing-0 << "            <execution>[\n]"
[DEBUG] http-outgoing-0 << "              <id>proces"
Progress (1): plexus-5.1.pom (16/23 kB)
[DEBUG] http-outgoing-0 << "s-test-classes</id>[\n]"
[DEBUG] http-outgoing-0 << "              <goals>[\n]"
[DEBUG] http-outgoing-0 << "                <goal>generate-test-metadata</goal>[\n]"
[DEBUG] http-outgoing-0 << "              </goals>[\n]"
[DEBUG] http-outgoing-0 << "            </execution>[\n]"
[DEBUG] http-outgoing-0 << "          </executions>[\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      </plugins>[\n]"
[DEBUG] http-outgoing-0 << "    </pluginManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-enforcer-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>enforce-maven</id>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>enforce</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\n]"
[DEBUG] http-outgoing-0 << "              <rules>[\n]"
[DEBUG] http-outgoing-0 << "                <requireMavenVersion>[\n]"
[DEBUG] http-outgoing-0 << "                  <version>3.0.5</version>[\n]"
[DEBUG] http-outgoing-0 << "                  <message>This project requires at least Maven 3.0.5</message>[\n]"
[DEBUG] http-outgoing-0 << "                </requireMavenVersion>[\n]"
[DEBUG] http-outgoing-0 << "            </rules>[\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-site-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\n]"
[DEBUG] http-outgoing-0 << "            <id>attach-descriptor</id>[\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\n]"
[DEBUG] http-outgoing-0 << "              <goal>attach-descriptor</goal>[\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "    </plugins>[\n]"
[DEBUG] http-outgoing-0 << "  </build>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <reporting>[\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-project-info-reports-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <reportSets>[\n]"
[DEBUG] http-outgoing-0 << "          <reportSet>[\n]"
[DEBUG] http-outgoing-0 << "            <reports>[\n]"
[DEBUG] http-outgoing-0 << "              <report>index</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>summary</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>dependency-info</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>modules</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>license</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>project-team</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>scm</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>issue-tracking</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>mailing-list</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>dependency-management</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>dependencies</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>dependency-convergence</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>cim</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>plugin-management</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>plugins</report>[\n]"
[DEBUG] http-outgoing-0 << "              <report>distribution-management</report>[\n]"
[DEBUG] http-outgoing-0 << "            </reports>[\n]"
[DEBUG] http-outgoing-0 << "          </reportSet>[\n]"
[DEBUG] http-outgoing-0 << "        </reportSets>[\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\n]"
[DEBUG] http-outgoing-0 << "    </plugins>[\n]"
[DEBUG] http-outgoing-0 << "  </reporting>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <profiles>[\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\n]"
[DEBUG] http-outgoing-0 << "      <id>reporting</id>[\n]"
[DEBUG] http-outgoing-0 << "      <reporting>[\n]"
[DEBUG] http-outgoing-0 << "        <plugins>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-project-info-reports-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-surefire-report-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-checkstyle-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <reportSets>[\n]"
[DEBUG] http-outgoing-0 << "              <reportSet>[\n]"
[DEBUG] http-outgoing-0 << "                <id>default</id>[\n]"
[DEBUG] http-outgoing-0 << "                <reports>[\n]"
[DEBUG] http-outgoing-0 << "                  <report>checkstyle</report>[\n]"
[DEBUG] http-outgoing-0 << "                </reports>[\n]"
[DEBUG] http-outgoing-0 << "              </reportSet>[\n]"
[DEBUG] http-outgoing-0 << "            </reportSets>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-pmd-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.codehaus.mojo</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>findbugs-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.codehaus.mojo</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>taglist-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-jxr-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <reportSets>[\n]"
[DEBUG] http-outgoing-0 << "              <reportSet>[\n]"
[DEBUG] http-outgoing-0 << "                <id>default</id>[\n]"
[DEBUG] http-outgoing-0 << "                <reports>[\n]"
[DEBUG] http-outgoing-0 << "                  <report>jxr</report>[\n]"
[DEBUG] http-outgoing-0 << "                  <report>test-jxr</report>[\n]"
[DEBUG] http-outgoing-0 << "                </reports>[\n]"
[DEBUG] http-outgoing-0 << "              </reportSet>[\n]"
[DEBUG] http-outgoing-0 << "            </reportSets>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-javadoc-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\n]"
[DEBUG] http-outgoing-0 << "              <detectLinks>true</detectLinks>[\n]"
[DEBUG] http-outgoing-0 << "              <links>[\n]"
[DEBUG] http-outgoing-0 << "         "
Progress (1): plexus-5.1.pom (20/23 kB)
[DEBUG] http-outgoing-0 << "       <link>http://junit.sourceforge.net/javadoc/</link>[\n]"
[DEBUG] http-outgoing-0 << "              </links>[\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <reportSets>[\n]"
[DEBUG] http-outgoing-0 << "              <reportSet>[\n]"
[DEBUG] http-outgoing-0 << "                <id>default</id>[\n]"
[DEBUG] http-outgoing-0 << "                <reports>[\n]"
[DEBUG] http-outgoing-0 << "                  <report>javadoc</report>[\n]"
[DEBUG] http-outgoing-0 << "                  <report>test-javadoc</report>[\n]"
[DEBUG] http-outgoing-0 << "                </reports>[\n]"
[DEBUG] http-outgoing-0 << "              </reportSet>[\n]"
[DEBUG] http-outgoing-0 << "            </reportSets>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.codehaus.mojo</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>cobertura-maven-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        </plugins>[\n]"
[DEBUG] http-outgoing-0 << "      </reporting>[\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\n]"
[DEBUG] http-outgoing-0 << "      <id>plexus-release</id>[\n]"
[DEBUG] http-outgoing-0 << "      <build>[\n]"
[DEBUG] http-outgoing-0 << "        <plugins>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-gpg-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\n]"
[DEBUG] http-outgoing-0 << "              <passphrase>${gpg.passphrase}</passphrase>[\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\n]"
[DEBUG] http-outgoing-0 << "                <id>sign-artifacts</id>[\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\n]"
[DEBUG] http-outgoing-0 << "                  <goal>sign</goal>[\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-source-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\n]"
[DEBUG] http-outgoing-0 << "                <id>attach-sources</id>[\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\n]"
[DEBUG] http-outgoing-0 << "                  <goal>jar-no-fork</goal>[\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-javadoc-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\n]"
[DEBUG] http-outgoing-0 << "                <id>attach-javadocs</id>[\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\n]"
[DEBUG] http-outgoing-0 << "                  <goal>jar</goal>[\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        </plugins>[\n]"
[DEBUG] http-outgoing-0 << "      </build>[\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\n]"
[DEBUG] http-outgoing-0 << "  </profiles>[\n]"
[DEBUG] http-outgoing-0 << "</project>[\n]"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
Progress (1): plexus-5.1.pom (23 kB)   
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/codehaus/plexus/plexus/5.1/plexus-5.1.pom.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/codehaus/plexus/plexus/5.1/plexus-5.1.pom.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/codehaus/plexus/plexus/5.1/plexus-5.1.pom.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "e6f39d22717acde619a14e21715a5e49"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Tue, 08 May 2018 09:22:38 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: e6f39d22717acde619a14e21715a5e49[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 5ff2a26a6d8d75adcdba5c2f10b6afceb199daa1[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 114328[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 140[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251813.475803,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "e6f39d22717acde619a14e21715a5e49"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Tue, 08 May 2018 09:22:38 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: e6f39d22717acde619a14e21715a5e49
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 5ff2a26a6d8d75adcdba5c2f10b6afceb199daa1
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 114328
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 140
[DEBUG] http-outgoing-0 << X-Timer: S1698251813.475803,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "2fca82e2eb5172f6a2909bea7accc733581a8c71"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
                                    
Downloaded from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus/5.1/plexus-5.1.pom (23 kB at 331 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/codehaus/plexus/plexus/5.1/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/codehaus/plexus/plexus/5.1/plexus-5.1.pom.lastUpdated
[DEBUG] Resolving artifact org.sonarsource.scanner.api:sonar-scanner-api:pom:2.16.1.361 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/org/sonarsource/scanner/api/sonar-scanner-api/2.16.1.361/sonar-scanner-api-2.16.1.361.pom
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/sonarsource/scanner/api/sonar-scanner-api/2.16.1.361/sonar-scanner-api-2.16.1.361.pom HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/sonarsource/scanner/api/sonar-scanner-api/2.16.1.361/sonar-scanner-api-2.16.1.361.pom HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/sonarsource/scanner/api/sonar-scanner-api/2.16.1.361/sonar-scanner-api-2.16.1.361.pom HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 5163[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "27aed2070995bd242df6f615cfcab934"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/xml[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Thu, 29 Apr 2021 07:39:28 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 27aed2070995bd242df6f615cfcab934[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: b2adf699dbaeae775e843702bda84cdc3a9df0c3[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 6920[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 1[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251814.505215,VS0,VE1[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 5163
[DEBUG] http-outgoing-0 << ETag: "27aed2070995bd242df6f615cfcab934"
[DEBUG] http-outgoing-0 << Content-Type: text/xml
[DEBUG] http-outgoing-0 << Last-Modified: Thu, 29 Apr 2021 07:39:28 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 27aed2070995bd242df6f615cfcab934
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: b2adf699dbaeae775e843702bda84cdc3a9df0c3
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 6920
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 1
[DEBUG] http-outgoing-0 << X-Timer: S1698251814.505215,VS0,VE1
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "<?xml version="1.0" encoding="UTF-8"?>[\r][\n]"
[DEBUG] http-outgoing-0 << "<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">[\r][\n]"
[DEBUG] http-outgoing-0 << "  <parent>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <artifactId>sonar-scanner-api-parent</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <groupId>org.sonarsource.scanner.api</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <version>2.16.1.361</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </parent>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <modelVersion>4.0.0</modelVersion>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <artifactId>sonar-scanner-api</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <name>SonarQube Scanner API</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <build>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-dependency-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <id>copy</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <phase>process-resources</phase>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <goal>copy</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <artifactItems>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <artifactItem>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <groupId>${project.groupId}</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <artifactId>sonar-scanner-api-batch</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <version>${project.version}</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <type>jar</type>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <overWrite>false</overWrite>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <outputDirectory>${project.build.outputDirectory}</outputDirectory>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <destFileName>sonar-scanner-api-batch.jar</destFileName>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </artifactItem>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </artifactItems>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <overWriteReleases>true</overWriteReleases>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <overWriteSnapshots>true</overWriteSnapshots>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-shade-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <phase>package</phase>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <goal>shade</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <createDependencyReducedPom>true</createDependencyReducedPom>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <minimizeJar>true</minimizeJar>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <relocations>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <relocation>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <pattern>okhttp3</pattern>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <shadedPattern>org.sonarsource.scanner.api.internal.shaded.okhttp</shadedPattern>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </relocation>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <relocation>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <pattern>okio</pattern>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <shadedPattern>org.sonarsource.scanner.api.internal.shaded.okio</shadedPattern>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </relocation>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <relocation>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <pattern>com.eclipsesource.json</pattern>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <shadedPattern>org.sonarsource.scanner.api.internal.shaded.minimaljson</shadedPattern>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </relocation>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </relocations>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </build>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <dependencies>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <groupId>com.google.code.findbugs</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>jsr305</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <version>3.0.2</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <scope>provided</scope>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.sonarsource.scanner.api</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>sonar-scanner-api-batch</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <version>2.16.1.361</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <scope>provided</scope>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <groupId>junit</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>junit</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <version>4.13.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <scope>test</scope>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <exclusions>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <exclusion>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>hamcrest-core</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.hamcrest</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </exclusion>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </exclusions>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.mockito</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>mockito-core</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <version>2.23.4</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <scope>test</scope>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <exclusions>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <exclusion>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>byte-buddy</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>net.bytebuddy</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </exclusion>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <exclusion>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>byte-buddy-agent</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>net.bytebuddy</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </exclusion>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <exclusion>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>objen"
Progress (1): sonar-scanner-api-2.16.1.361.pom (4.1/5.2 kB)
[DEBUG] http-outgoing-0 << "esis</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.objenesis</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </exclusion>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </exclusions>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.mockito</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>mockito-inline</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <version>2.23.4</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <scope>test</scope>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <groupId>org.assertj</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>assertj-core</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <version>3.11.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <scope>test</scope>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <groupId>com.squareup.okhttp3</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>mockwebserver</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <version>3.14.2</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <scope>test</scope>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <groupId>commons-lang</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>commons-lang</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <version>2.6</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <scope>test</scope>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <groupId>commons-codec</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>commons-codec</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <version>1.11</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <scope>test</scope>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </dependencies>[\r][\n]"
[DEBUG] http-outgoing-0 << "</project>[\r][\n]"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
Progress (1): sonar-scanner-api-2.16.1.361.pom (5.2 kB)    
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/sonarsource/scanner/api/sonar-scanner-api/2.16.1.361/sonar-scanner-api-2.16.1.361.pom.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/sonarsource/scanner/api/sonar-scanner-api/2.16.1.361/sonar-scanner-api-2.16.1.361.pom.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/sonarsource/scanner/api/sonar-scanner-api/2.16.1.361/sonar-scanner-api-2.16.1.361.pom.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "4e631aad7f3cc0fb17408786beef8f0e"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Thu, 29 Apr 2021 07:39:28 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 4e631aad7f3cc0fb17408786beef8f0e[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 6e918f15f2f0e87b35101699455f171b182a854a[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 620297[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 1[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251814.524693,VS0,VE1[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "4e631aad7f3cc0fb17408786beef8f0e"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Thu, 29 Apr 2021 07:39:28 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 4e631aad7f3cc0fb17408786beef8f0e
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 6e918f15f2f0e87b35101699455f171b182a854a
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 620297
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 1
[DEBUG] http-outgoing-0 << X-Timer: S1698251814.524693,VS0,VE1
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "b2adf699dbaeae775e843702bda84cdc3a9df0c3"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
                                                       
Downloaded from central: https://repo.maven.apache.org/maven2/org/sonarsource/scanner/api/sonar-scanner-api/2.16.1.361/sonar-scanner-api-2.16.1.361.pom (5.2 kB at 199 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonarsource/scanner/api/sonar-scanner-api/2.16.1.361/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonarsource/scanner/api/sonar-scanner-api/2.16.1.361/sonar-scanner-api-2.16.1.361.pom.lastUpdated
[DEBUG] Resolving artifact org.sonarsource.scanner.api:sonar-scanner-api-parent:pom:2.16.1.361 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/org/sonarsource/scanner/api/sonar-scanner-api-parent/2.16.1.361/sonar-scanner-api-parent-2.16.1.361.pom
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/sonarsource/scanner/api/sonar-scanner-api-parent/2.16.1.361/sonar-scanner-api-parent-2.16.1.361.pom HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/sonarsource/scanner/api/sonar-scanner-api-parent/2.16.1.361/sonar-scanner-api-parent-2.16.1.361.pom HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/sonarsource/scanner/api/sonar-scanner-api-parent/2.16.1.361/sonar-scanner-api-parent-2.16.1.361.pom HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 4786[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "8f8682bbbb949ab5fb40e452882ce381"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/xml[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Thu, 29 Apr 2021 07:39:28 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 8f8682bbbb949ab5fb40e452882ce381[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: a819cb6338fcc985ccb77b88c5bb542c142b4ceb[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 94476[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 3[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251814.544846,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 4786
[DEBUG] http-outgoing-0 << ETag: "8f8682bbbb949ab5fb40e452882ce381"
[DEBUG] http-outgoing-0 << Content-Type: text/xml
[DEBUG] http-outgoing-0 << Last-Modified: Thu, 29 Apr 2021 07:39:28 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 8f8682bbbb949ab5fb40e452882ce381
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: a819cb6338fcc985ccb77b88c5bb542c142b4ceb
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 94476
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 3
[DEBUG] http-outgoing-0 << X-Timer: S1698251814.544846,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"[\n]"
[DEBUG] http-outgoing-0 << "  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">[\n]"
[DEBUG] http-outgoing-0 << "  <modelVersion>4.0.0</modelVersion>[\n]"
[DEBUG] http-outgoing-0 << "  <parent>[\n]"
[DEBUG] http-outgoing-0 << "    <groupId>org.sonarsource.parent</groupId>[\n]"
[DEBUG] http-outgoing-0 << "    <artifactId>parent</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "    <version>59.0.29</version>[\n]"
[DEBUG] http-outgoing-0 << "  </parent>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <groupId>org.sonarsource.scanner.api</groupId>[\n]"
[DEBUG] http-outgoing-0 << "  <artifactId>sonar-scanner-api-parent</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "  <version>2.16.1.361</version>[\n]"
[DEBUG] http-outgoing-0 << "  <packaging>pom</packaging>[\n]"
[DEBUG] http-outgoing-0 << "  <name>SonarQube Scanner API - Parent</name>[\n]"
[DEBUG] http-outgoing-0 << "  <description>API used by all SonarQube Scanners (Maven, Gradle, Ant, CLI)</description>[\n]"
[DEBUG] http-outgoing-0 << "  <url>https://github.com/SonarSource/sonar-scanner-api</url>[\n]"
[DEBUG] http-outgoing-0 << "  <inceptionYear>2011</inceptionYear>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <organization>[\n]"
[DEBUG] http-outgoing-0 << "    <name>SonarSource</name>[\n]"
[DEBUG] http-outgoing-0 << "    <url>http://www.sonarsource.com</url>[\n]"
[DEBUG] http-outgoing-0 << "  </organization>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <licenses>[\n]"
[DEBUG] http-outgoing-0 << "    <license>[\n]"
[DEBUG] http-outgoing-0 << "      <name>GNU LGPL 3</name>[\n]"
[DEBUG] http-outgoing-0 << "      <url>http://www.gnu.org/licenses/lgpl.txt</url>[\n]"
[DEBUG] http-outgoing-0 << "      <distribution>repo</distribution>[\n]"
[DEBUG] http-outgoing-0 << "    </license>[\n]"
[DEBUG] http-outgoing-0 << "  </licenses>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <modules>[\n]"
[DEBUG] http-outgoing-0 << "    <module>api</module>[\n]"
[DEBUG] http-outgoing-0 << "    <module>batch</module>[\n]"
[DEBUG] http-outgoing-0 << "    <module>batch-interface</module>[\n]"
[DEBUG] http-outgoing-0 << "  </modules>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <scm>[\n]"
[DEBUG] http-outgoing-0 << "    <connection>scm:git:git@github.com:SonarSource/sonar-scanner-api.git</connection>[\n]"
[DEBUG] http-outgoing-0 << "    <developerConnection>scm:git:git@github.com:SonarSource/sonar-scanner-api.git</developerConnection>[\n]"
[DEBUG] http-outgoing-0 << "    <url>https://github.com/SonarSource/sonar-scanner-api</url>[\n]"
[DEBUG] http-outgoing-0 << "    <tag>HEAD</tag>[\n]"
[DEBUG] http-outgoing-0 << "  </scm>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <issueManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <system>JIRA</system>[\n]"
[DEBUG] http-outgoing-0 << "    <url>https://jira.sonarsource.com/browse/SCANNERAPI</url>[\n]"
[DEBUG] http-outgoing-0 << "  </issueManagement>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <properties>[\n]"
[DEBUG] http-outgoing-0 << "    <maven.test.redirectTestOutputToFile>true</maven.test.redirectTestOutputToFile>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "    <!-- used for deployment to SonarSource Artifactory -->[\n]"
[DEBUG] http-outgoing-0 << "    <gitRepositoryName>sonar-scanner-api</gitRepositoryName>[\n]"
[DEBUG] http-outgoing-0 << "    <okhttp.version>3.14.2</okhttp.version>[\n]"
[DEBUG] http-outgoing-0 << "    <mockito.version>2.23.4</mockito.version>[\n]"
[DEBUG] http-outgoing-0 << "  </properties>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <dependencyManagement>[\n]"
[DEBUG] http-outgoing-0 << "    <dependencies>[\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>commons-lang</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>commons-lang</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>2.6</version>[\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>commons-io</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>commons-io</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>2.6</version>[\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>com.squareup.okhttp3</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>okhttp</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>${okhttp.version}</version>[\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>com.squareup.okhttp3</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>okhttp-urlconnection</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>${okhttp.version}</version>[\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>com.eclipsesource.minimal-json</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>minimal-json</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>0.9.5</version>[\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>com.squareup.okhttp3</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>mockwebserver</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>${okhttp.version}</version>[\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>com.google.code.findbugs</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>jsr305</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>3.0.2</version>[\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>junit</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>junit</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>4.13.1</version>[\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.mockito</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>mockito-core</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>${mockito.version}</version>[\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.mockito</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>mockito-inline</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>${mockito.version}</version>[\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.assertj</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>assertj-core</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>3.11.1</version>[\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\n]"
[DEBUG] http-outgoing-0 << "      <dependency>[\n]"
[DEBUG] http-outgoing-0 << "        <groupId>commons-codec</groupId>[\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>commons-codec</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "        <version>1.11</version>[\n]"
[DEBUG] http-outgoing-0 << "      </dependency>[\n]"
[DEBUG] http-outgoing-0 << "    </dependencies>[\n]"
[DEBUG] http-outgoing-0 << "  </dependencyManagement>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "  <profiles>[\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\n]"
[DEBUG] http-outgoing-0 << "      <id>release</id>[\n]"
[DEBUG] http-outgoing-0 << "      <build>[\n]"
[DEBUG] http-outgoing-0 << "        <plugins>[\n]"
[DEBUG] http-outgoing-0 << "    "
Progress (1): sonar-scanner-api-parent-2.16.1.361.pom (4.1/4.8 kB)
[DEBUG] http-outgoing-0 << "      <plugin>[\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-javadoc-plugin</artifactId>[\n]"
[DEBUG] http-outgoing-0 << "            <version>3.1.0</version>[\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\n]"
[DEBUG] http-outgoing-0 << "              <source>8</source>[\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\n]"
[DEBUG] http-outgoing-0 << "                <id>attach-javadocs</id>[\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\n]"
[DEBUG] http-outgoing-0 << "                  <goal>jar</goal>[\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\n]"
[DEBUG] http-outgoing-0 << "        </plugins>[\n]"
[DEBUG] http-outgoing-0 << "      </build>[\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\n]"
[DEBUG] http-outgoing-0 << "      <id>its</id>[\n]"
[DEBUG] http-outgoing-0 << "      <modules>[\n]"
[DEBUG] http-outgoing-0 << "        <module>its</module>[\n]"
[DEBUG] http-outgoing-0 << "      </modules>[\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\n]"
[DEBUG] http-outgoing-0 << "  </profiles>[\n]"
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "</project>[\n]"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
Progress (1): sonar-scanner-api-parent-2.16.1.361.pom (4.8 kB)    
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/sonarsource/scanner/api/sonar-scanner-api-parent/2.16.1.361/sonar-scanner-api-parent-2.16.1.361.pom.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/sonarsource/scanner/api/sonar-scanner-api-parent/2.16.1.361/sonar-scanner-api-parent-2.16.1.361.pom.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/sonarsource/scanner/api/sonar-scanner-api-parent/2.16.1.361/sonar-scanner-api-parent-2.16.1.361.pom.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "265221987617cf0afe83e2ab5d9a0427"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Thu, 29 Apr 2021 07:39:28 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 265221987617cf0afe83e2ab5d9a0427[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 34bbe9eb506459ff6752679386f9ec6d8140d8e0[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 105512[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 1[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251814.551097,VS0,VE1[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "265221987617cf0afe83e2ab5d9a0427"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Thu, 29 Apr 2021 07:39:28 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 265221987617cf0afe83e2ab5d9a0427
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 34bbe9eb506459ff6752679386f9ec6d8140d8e0
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 105512
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 1
[DEBUG] http-outgoing-0 << X-Timer: S1698251814.551097,VS0,VE1
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "a819cb6338fcc985ccb77b88c5bb542c142b4ceb"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
                                                              
Downloaded from central: https://repo.maven.apache.org/maven2/org/sonarsource/scanner/api/sonar-scanner-api-parent/2.16.1.361/sonar-scanner-api-parent-2.16.1.361.pom (4.8 kB at 319 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonarsource/scanner/api/sonar-scanner-api-parent/2.16.1.361/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonarsource/scanner/api/sonar-scanner-api-parent/2.16.1.361/sonar-scanner-api-parent-2.16.1.361.pom.lastUpdated
[DEBUG] Resolving artifact commons-lang:commons-lang:pom:2.6 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/commons-lang/commons-lang/2.6/commons-lang-2.6.pom
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/commons-lang/commons-lang/2.6/commons-lang-2.6.pom HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/commons-lang/commons-lang/2.6/commons-lang-2.6.pom HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/commons-lang/commons-lang/2.6/commons-lang-2.6.pom HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 17494[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "cca9ee287cb26a44a2f65450a24957cd"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/xml[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Sun, 16 Jan 2011 22:21:47 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: cca9ee287cb26a44a2f65450a24957cd[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 347d60b180fa80e5699d8e2cb72c99c93dda5454[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 1307294[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 1737[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251814.578981,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 17494
[DEBUG] http-outgoing-0 << ETag: "cca9ee287cb26a44a2f65450a24957cd"
[DEBUG] http-outgoing-0 << Content-Type: text/xml
[DEBUG] http-outgoing-0 << Last-Modified: Sun, 16 Jan 2011 22:21:47 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: cca9ee287cb26a44a2f65450a24957cd
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 347d60b180fa80e5699d8e2cb72c99c93dda5454
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 1307294
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 1737
[DEBUG] http-outgoing-0 << X-Timer: S1698251814.578981,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "<?xml version="1.0" encoding="UTF-8"?>[\r][\n]"
[DEBUG] http-outgoing-0 << "<!--[\r][\n]"
[DEBUG] http-outgoing-0 << "   Licensed to the Apache Software Foundation (ASF) under one or more[\r][\n]"
[DEBUG] http-outgoing-0 << "   contributor license agreements.  See the NOTICE file distributed with[\r][\n]"
[DEBUG] http-outgoing-0 << "   this work for additional information regarding copyright ownership.[\r][\n]"
[DEBUG] http-outgoing-0 << "   The ASF licenses this file to You under the Apache License, Version 2.0[\r][\n]"
[DEBUG] http-outgoing-0 << "   (the "License"); you may not use this file except in compliance with[\r][\n]"
[DEBUG] http-outgoing-0 << "   the License.  You may obtain a copy of the License at[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "       http://www.apache.org/licenses/LICENSE-2.0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "   Unless required by applicable law or agreed to in writing, software[\r][\n]"
[DEBUG] http-outgoing-0 << "   distributed under the License is distributed on an "AS IS" BASIS,[\r][\n]"
[DEBUG] http-outgoing-0 << "   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.[\r][\n]"
[DEBUG] http-outgoing-0 << "   See the License for the specific language governing permissions and[\r][\n]"
[DEBUG] http-outgoing-0 << "   limitations under the License.[\r][\n]"
[DEBUG] http-outgoing-0 << "-->[\r][\n]"
[DEBUG] http-outgoing-0 << "<project[\r][\n]"
[DEBUG] http-outgoing-0 << "    xmlns="http://maven.apache.org/POM/4.0.0"[\r][\n]"
[DEBUG] http-outgoing-0 << "    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"[\r][\n]"
[DEBUG] http-outgoing-0 << "    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">[\r][\n]"
[DEBUG] http-outgoing-0 << "  <parent>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <groupId>org.apache.commons</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <artifactId>commons-parent</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <version>17</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </parent>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <modelVersion>4.0.0</modelVersion>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <groupId>commons-lang</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <artifactId>commons-lang</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <version>2.6</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <name>Commons Lang</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <inceptionYear>2001</inceptionYear>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <description>[\r][\n]"
[DEBUG] http-outgoing-0 << "        Commons Lang, a package of Java utility classes for the[\r][\n]"
[DEBUG] http-outgoing-0 << "        classes that are in java.lang's hierarchy, or are considered to be so[\r][\n]"
[DEBUG] http-outgoing-0 << "        standard as to justify existence in java.lang.[\r][\n]"
[DEBUG] http-outgoing-0 << "    </description>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <url>http://commons.apache.org/lang/</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <issueManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <system>jira</system>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <url>http://issues.apache.org/jira/browse/LANG</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </issueManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <scm>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <connection>scm:svn:http://svn.apache.org/repos/asf/commons/proper/lang/branches/LANG_2_X</connection>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <developerConnection>scm:svn:https://svn.apache.org/repos/asf/commons/proper/lang/branches/LANG_2_X</developerConnection>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <url>http://svn.apache.org/viewvc/commons/proper/lang/branches/LANG_2_X</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </scm>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "    <developers>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Daniel Rall</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <id>dlr</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <email>dlr@finemaltcoding.com</email>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <organization>CollabNet, Inc.</organization>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <role>Java Developer</role>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Stephen Colebourne</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <id>scolebourne</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <email>scolebourne@joda.org</email>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <organization>SITA ATS Ltd</organization>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <timezone>0</timezone>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <role>Java Developer</role>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Henri Yandell</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <id>bayard</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <email>bayard@apache.org</email>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <organization/>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <role>Java Developer</role>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Steven Caswell</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <id>scaswell</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <email>stevencaswell@apache.org</email>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <organization/>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <role>Java Developer</role>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <timezone>-5</timezone>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Robert Burrell Donkin</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <id>rdonkin</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <email>rdonkin@apache.org</email>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <organization/>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <role>Java Developer</role>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Gary D. Gregory</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <id>ggregory</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <email>ggregory@seagullsw.com</email>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <organization>Seagull Software</organization>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <timezone>-8</timezone>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <role>Java Developer</role>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <"
Progress (1): commons-lang-2.6.pom (4.1/17 kB)
[DEBUG] http-outgoing-0 << "developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Phil Steitz</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <id>psteitz</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <email>phil@steitz.com</email>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <organization/>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <role>Java Developer</role>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Fredrik Westermarck</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <id>fredrik</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <email/>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <organization/>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <role>Java Developer</role>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>James Carman</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <id>jcarman</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <email>jcarman@apache.org</email>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <organization>Carman Consulting, Inc.</organization>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <role>Java Developer</role>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Niall Pemberton</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <id>niallp</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <role>Java Developer</role>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Matt Benson</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <id>mbenson</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <role>Java Developer</role>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Joerg Schaible</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <id>joehni</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <email>joerg.schaible@gmx.de</email>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <role>Java Developer</role>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <timezone>+1</timezone>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <name>Oliver Heger</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <id>oheger</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <email>oheger@apache.org</email>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <timezone>+1</timezone>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <role>Java Developer</role>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <name>Paul Benedict</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <id>pbenedict</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <email>pbenedict@apache.org</email>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <role>Java Developer</role>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </roles>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </developer>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </developers>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <contributors>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>C. Scott Ananian</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Chris Audley</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Stephane Bailliez</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Michael Becke</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Benjamin Bentmann</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Ola Berg</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Nathan Beyer</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Stefan Bodewig</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Janek Bogucki</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Mike Bowler</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Sean Brown</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Alexander Day Chaffee</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Al Chou</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Greg Coladonato</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Maarten Coene</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Justin Couch</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Michael Davey</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Norm Deane</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Ringo De Smet</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Russel Dittmar</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Steve Downey</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Matthias Eichel</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Christopher Elkins</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r]"
Progress (1): commons-lang-2.6.pom (8.2/17 kB)
[DEBUG] http-outgoing-0 << "[\n]"
[DEBUG] http-outgoing-0 << "            <name>Chris Feldhacker</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Pete Gieser</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Jason Gritman</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Matthew Hawthorne</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Michael Heuer</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Chris Hyzer</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Marc Johnson</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Shaun Kalley</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Tetsuya Kaneuchi</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Nissim Karpenstein</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Ed Korthof</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Holger Krauth</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Rafal Krupinski</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Rafal Krzewski</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Craig R. McClanahan</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Rand McNeely</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Hendrik Maryns</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Dave Meikle</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Nikolay Metchev</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Kasper Nielsen</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Tim O'Brien</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Brian S O'Neill</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Andrew C. Oliver</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Alban Peignier</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Moritz Petersen</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Dmitri Plotnikov</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Neeme Praks</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Eric Pugh</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Stephen Putman</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Travis Reeder</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Antony Riley</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Scott Sanders</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Ralph Schaer</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Henning P. Schmiedehausen</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Sean Schofield</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Robert Scholte</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Reuben Sivan</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Ville Skytta</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Jan Sorensen</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Glen Stampoultzis</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Scott Stanchfield</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Jon S. Stevens</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Sean C. Sullivan</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Ashwin Suresh</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Helge Tesgaard</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Arun Mammen Thomas</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Masato Tezuka</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "      "
Progress (1): commons-lang-2.6.pom (12/17 kB) 
[DEBUG] http-outgoing-0 << "  </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Jeff Varszegi</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Chris Webb</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Mario Winterer</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Stepan Koltsov</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Holger Hoffstatte</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <name>Derek C. Ashmore</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </contributor>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </contributors>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <!-- Lang should depend on very little -->[\r][\n]"
[DEBUG] http-outgoing-0 << "  <dependencies>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <groupId>junit</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <artifactId>junit</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <version>3.8.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <scope>test</scope>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </dependencies> [\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <properties>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <maven.compile.source>1.3</maven.compile.source>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <maven.compile.target>1.3</maven.compile.target>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.componentid>lang</commons.componentid>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.release.version>2.6</commons.release.version>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.release.desc>(Java 1.3+)</commons.release.desc>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.release.2.version>3.0-beta</commons.release.2.version>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.release.2.desc>(Java 5.0+)</commons.release.2.desc>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.jira.id>LANG</commons.jira.id>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.jira.pid>12310481</commons.jira.pid>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <random.exclude.test>**/RandomUtilsFreqTest.java</random.exclude.test>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </properties> [\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <build>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-surefire-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <includes>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <include>**/*Test.java</include>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </includes>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <excludes>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <exclude>**/EntitiesPerformanceTest.java</exclude>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <exclude>${random.exclude.test}</exclude>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </excludes>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-assembly-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <descriptors>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <descriptor>src/assembly/bin.xml</descriptor>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <descriptor>src/assembly/src.xml</descriptor>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </descriptors>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <tarLongFileMode>gnu</tarLongFileMode>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </build>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <profiles>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <!--[\r][\n]"
[DEBUG] http-outgoing-0 << "        RandomUtils frequency tests have been put in a separate test case which[\r][\n]"
[DEBUG] http-outgoing-0 << "        is only run when using this profile because it fails too frequently.[\r][\n]"
[DEBUG] http-outgoing-0 << "        See https://issues.apache.org/jira/browse/LANG-592[\r][\n]"
[DEBUG] http-outgoing-0 << "        -->[\r][\n]"
[DEBUG] http-outgoing-0 << "      <profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <id>test-random-freq</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <properties>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <random.exclude.test/>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </properties> [\r][\n]"
[DEBUG] http-outgoing-0 << "        <build>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <artifactId>maven-surefire-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <includes>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <include>**/RandomUtilsFreqTest.java</include>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </includes>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </build>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </profiles>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <reporting>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-changes-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.3</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <xmlPath>${basedir}/src/site/changes/changes.xml</xmlPath>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <issueLinkTemplate>%URL%/%ISSUE%</issueLinkTemplate>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <reportSets>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <reportSet>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <reports>[\r][\n]"
[DEBUG] http-outgoing-0 << "                 <report>changes-report</report>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </reports>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </reportSet>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </reportSets>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-checkstyle-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <version>2.6</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <configLocation>${basedir}/ch"
Progress (1): commons-lang-2.6.pom (16/17 kB)
[DEBUG] http-outgoing-0 << "eckstyle.xml</configLocation>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <enableRulesSummary>false</enableRulesSummary>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <!-- Requires setting 'export MAVEN_OPTS="-Xmx512m" ' -->[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.codehaus.mojo</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>findbugs-maven-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <version>2.3.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <threshold>Normal</threshold>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <effort>Default</effort>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <excludeFilterFile>${basedir}/findbugs-exclude-filter.xml</excludeFilterFile>[\r][\n]"
[DEBUG] http-outgoing-0 << "       </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.codehaus.mojo</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>cobertura-maven-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <version>2.4</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.codehaus.mojo</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>clirr-maven-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <version>2.2.2</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <comparisonVersion>2.5</comparisonVersion>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <minSeverity>info</minSeverity>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </reporting>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "</project>[\r][\n]"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
Progress (1): commons-lang-2.6.pom (17 kB)   
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/commons-lang/commons-lang/2.6/commons-lang-2.6.pom.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/commons-lang/commons-lang/2.6/commons-lang-2.6.pom.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/commons-lang/commons-lang/2.6/commons-lang-2.6.pom.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "8a2f2cd865631e80e05b06756a60d841"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Sun, 16 Jan 2011 22:21:47 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 8a2f2cd865631e80e05b06756a60d841[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 66f6465cadfc8376d12455896f4e22567c81dca0[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 127646[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 121[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251814.609186,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "8a2f2cd865631e80e05b06756a60d841"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Sun, 16 Jan 2011 22:21:47 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 8a2f2cd865631e80e05b06756a60d841
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 66f6465cadfc8376d12455896f4e22567c81dca0
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 127646
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 121
[DEBUG] http-outgoing-0 << X-Timer: S1698251814.609186,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "347d60b180fa80e5699d8e2cb72c99c93dda5454"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
                                          
Downloaded from central: https://repo.maven.apache.org/maven2/commons-lang/commons-lang/2.6/commons-lang-2.6.pom (17 kB at 389 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/commons-lang/commons-lang/2.6/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/commons-lang/commons-lang/2.6/commons-lang-2.6.pom.lastUpdated
[DEBUG] Resolving artifact org.apache.commons:commons-parent:pom:17 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/commons/commons-parent/17/commons-parent-17.pom
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/apache/commons/commons-parent/17/commons-parent-17.pom HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/apache/commons/commons-parent/17/commons-parent-17.pom HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/apache/commons/commons-parent/17/commons-parent-17.pom HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 31185[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "fb87cc504bd54609f3a93e990471b757"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/xml[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Mon, 12 Jul 2010 10:47:21 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: fb87cc504bd54609f3a93e990471b757[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 84bc2f457fac92c947cde9c15c81786ded79b3c1[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 1843888[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 3909[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251814.634730,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 31185
[DEBUG] http-outgoing-0 << ETag: "fb87cc504bd54609f3a93e990471b757"
[DEBUG] http-outgoing-0 << Content-Type: text/xml
[DEBUG] http-outgoing-0 << Last-Modified: Mon, 12 Jul 2010 10:47:21 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: fb87cc504bd54609f3a93e990471b757
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 84bc2f457fac92c947cde9c15c81786ded79b3c1
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 1843888
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 3909
[DEBUG] http-outgoing-0 << X-Timer: S1698251814.634730,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "<?xml version="1.0" encoding="ISO-8859-1"?>[\r][\n]"
[DEBUG] http-outgoing-0 << "<!--[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "   Licensed to the Apache Software Foundation (ASF) under one or more[\r][\n]"
[DEBUG] http-outgoing-0 << "   contributor license agreements.  See the NOTICE file distributed with[\r][\n]"
[DEBUG] http-outgoing-0 << "   this work for additional information regarding copyright ownership.[\r][\n]"
[DEBUG] http-outgoing-0 << "   The ASF licenses this file to You under the Apache License, Version 2.0[\r][\n]"
[DEBUG] http-outgoing-0 << "   (the "License"); you may not use this file except in compliance with[\r][\n]"
[DEBUG] http-outgoing-0 << "   the License.  You may obtain a copy of the License at[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "       http://www.apache.org/licenses/LICENSE-2.0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "   Unless required by applicable law or agreed to in writing, software[\r][\n]"
[DEBUG] http-outgoing-0 << "   distributed under the License is distributed on an "AS IS" BASIS,[\r][\n]"
[DEBUG] http-outgoing-0 << "   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.[\r][\n]"
[DEBUG] http-outgoing-0 << "   See the License for the specific language governing permissions and[\r][\n]"
[DEBUG] http-outgoing-0 << "   limitations under the License.[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "-->[\r][\n]"
[DEBUG] http-outgoing-0 << "<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">[\r][\n]"
[DEBUG] http-outgoing-0 << "  <modelVersion>4.0.0</modelVersion>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <parent>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <groupId>org.apache</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <artifactId>apache</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <version>7</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </parent>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <groupId>org.apache.commons</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <artifactId>commons-parent</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <packaging>pom</packaging>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <!-- TODO: dummy version. In Maven 2.1, this will be auto-versioned being a generic parent -->[\r][\n]"
[DEBUG] http-outgoing-0 << "  <version>17</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <name>Commons Parent</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <url>http://commons.apache.org/</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <ciManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <system>continuum</system>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <url>http://vmbuild.apache.org/continuum/</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </ciManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <!--[\r][\n]"
[DEBUG] http-outgoing-0 << "    This section *must* be overwritten by subprojects. It is only to allow[\r][\n]"
[DEBUG] http-outgoing-0 << "    a release of the commons-parent POM.[\r][\n]"
[DEBUG] http-outgoing-0 << "  -->[\r][\n]"
[DEBUG] http-outgoing-0 << "  <scm>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <connection>scm:svn:http://svn.apache.org/repos/asf/commons/proper/commons-parent/tags/commons-parent-17</connection>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <developerConnection>scm:svn:https://svn.apache.org/repos/asf/commons/proper/commons-parent/tags/commons-parent-17</developerConnection>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <url>http://svn.apache.org/viewvc/commons/proper/commons-parent/tags/commons-parent-17</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </scm>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <mailingLists>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <!-- N.B. commons-site now uses the Apache POM so has its own copy of the mailing list definitions -->[\r][\n]"
[DEBUG] http-outgoing-0 << "    <mailingList>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <name>Commons User List</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <subscribe>user-subscribe@commons.apache.org</subscribe>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <unsubscribe>user-unsubscribe@commons.apache.org</unsubscribe>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <post>user@commons.apache.org</post>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <archive>http://mail-archives.apache.org/mod_mbox/commons-user/</archive>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <otherArchives>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>http://markmail.org/list/org.apache.commons.users/</otherArchive>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>http://old.nabble.com/Commons---User-f319.html</otherArchive>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>http://www.mail-archive.com/user@commons.apache.org/</otherArchive>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>http://news.gmane.org/gmane.comp.jakarta.commons.user</otherArchive>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </otherArchives>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </mailingList>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <mailingList>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <name>Commons Dev List</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <subscribe>dev-subscribe@commons.apache.org</subscribe>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <unsubscribe>dev-unsubscribe@commons.apache.org</unsubscribe>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <post>dev@commons.apache.org</post>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <archive>http://mail-archives.apache.org/mod_mbox/commons-dev/</archive>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <otherArchives>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>http://markmail.org/list/org.apache.commons.dev/</otherArchive>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>http://old.nabble.com/Commons---Dev-f317.html</otherArchive>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>http://www.mail-archive.com/dev@commons.apache.org/</otherArchive>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>http://news.gmane.org/gmane.comp.jakarta.commons.devel</otherArchive>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </otherArchives>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </mailingList>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <mailingList>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <name>Commons Issues List</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <subscribe>issues-subscribe@commons.apache.org</subscribe>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <unsubscribe>issues-unsubscribe@commons.apache.org</unsubscribe>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <archive>http://mail-archives.apache.org/mod_mbox/commons-issues/</archive>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <otherArchives>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <other"
Progress (1): commons-parent-17.pom (4.1/31 kB)
[DEBUG] http-outgoing-0 << "Archive>http://markmail.org/list/org.apache.commons.issues/</otherArchive>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>http://old.nabble.com/Commons---Issues-f25499.html</otherArchive>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>http://www.mail-archive.com/issues@commons.apache.org/</otherArchive>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </otherArchives>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </mailingList>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <mailingList>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <name>Commons Commits List</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <subscribe>commits-subscribe@commons.apache.org</subscribe>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <unsubscribe>commits-unsubscribe@commons.apache.org</unsubscribe>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <archive>http://mail-archives.apache.org/mod_mbox/commons-commits/</archive>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <otherArchives>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>http://markmail.org/list/org.apache.commons.commits/</otherArchive>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>http://www.mail-archive.com/commits@commons.apache.org/</otherArchive>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </otherArchives>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </mailingList>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <mailingList>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <name>Apache Announce List</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <subscribe>announce-subscribe@apache.org</subscribe>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <unsubscribe>announce-unsubscribe@apache.org</unsubscribe>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <archive>http://mail-archives.apache.org/mod_mbox/www-announce/</archive>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <otherArchives>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>http://markmail.org/list/org.apache.announce/</otherArchive>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>http://old.nabble.com/Apache-News-and-Announce-f109.html</otherArchive>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>http://www.mail-archive.com/announce@apache.org/</otherArchive>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <otherArchive>http://news.gmane.org/gmane.comp.apache.announce</otherArchive>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </otherArchives>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </mailingList>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </mailingLists>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <build>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <resources>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <resource>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <directory>${basedir}</directory>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <targetPath>META-INF</targetPath>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <includes>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <include>NOTICE.txt</include>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <include>LICENSE.txt</include>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </includes>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </resource>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </resources>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <pluginManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <!-- org.apache.maven.plugins, alpha order by artifact id -->[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-antrun-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <!-- [\r][\n]"
[DEBUG] http-outgoing-0 << "          Warning: 1.4 has regression bug, see: http://jira.codehaus.org/browse/MANTRUN-143[\r][\n]"
[DEBUG] http-outgoing-0 << "           -->[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>1.3</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-assembly-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.2-beta-5</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-clean-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.4</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-compiler-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <source>${maven.compile.source}</source>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <target>${maven.compile.target}</target>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <encoding>${commons.encoding}</encoding>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <fork>${commons.compiler.fork}</fork>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <compilerVersion>${commons.compiler.compilerVersion}</compilerVersion>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <executable>${commons.compiler.javac}</executable>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-deploy-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.5</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-gpg-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>1.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-install-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.3</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-jar-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.3</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-javadoc-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <ver"
Progress (1): commons-parent-17.pom (8.2/31 kB)
[DEBUG] http-outgoing-0 << "sion>2.5</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <!-- keep only errors and warnings -->[\r][\n]"
[DEBUG] http-outgoing-0 << "            <quiet>true</quiet>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <encoding>${commons.encoding}</encoding>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <docEncoding>${commons.docEncoding}</docEncoding>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <archive>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <manifest>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <addDefaultImplementationEntries>true</addDefaultImplementationEntries>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <addDefaultSpecificationEntries>true</addDefaultSpecificationEntries>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </manifest>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </archive>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-release-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.0</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-remote-resources-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <!-- override version 1.1 from apache parent to ensure JDK 1.4 compatibilty -->[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>1.0</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <!--[\r][\n]"
[DEBUG] http-outgoing-0 << "                apache parent pom automatically adds "LICENSE" and "NOTICE" files[\r][\n]"
[DEBUG] http-outgoing-0 << "                to jars - duplciating the "LICENSE.txt" and "NOTICE.txt"[\r][\n]"
[DEBUG] http-outgoing-0 << "                files that components already have.[\r][\n]"
[DEBUG] http-outgoing-0 << "             -->[\r][\n]"
[DEBUG] http-outgoing-0 << "            <skip>true</skip>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-resources-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.4.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-site-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.0.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-source-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.1.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <archive>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <manifest>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <addDefaultImplementationEntries>true</addDefaultImplementationEntries>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <addDefaultSpecificationEntries>true</addDefaultSpecificationEntries>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </manifest>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </archive>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-surefire-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>${commons.surefire.version}</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <!-- Other plugins, alpha order by groupId and artifactId -->[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.commons</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>commons-build-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>1.3</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <commons.release.name>${commons.release.name}</commons.release.name>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.felix</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-bundle-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <!-- version 1.4.x is required for JDK 1.4 compatibilty -->[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>1.4.3</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <inherited>true</inherited>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </pluginManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-compiler-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-surefire-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <jvm>${commons.surefire.java}</jvm>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-jar-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <archive>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <manifestFile>${commons.manifestfile}</manifestFile>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <manifestEntries>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <Specification-Title>${project.name}</Specification-Title>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <Specification-Version>${project.version}</Specification-Version>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <Specification-Vendor>${project.organization.name}</Specification-Vendor>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <Implementation-Title>${project.name}</Implementation-Title>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <Implementation-Version>${project.version}</Implementation-Version>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <I"
Progress (1): commons-parent-17.pom (12/31 kB) 
[DEBUG] http-outgoing-0 << "mplementation-Vendor>${project.organization.name}</Implementation-Vendor>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <Implementation-Vendor-Id>org.apache</Implementation-Vendor-Id>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <X-Compile-Source-JDK>${maven.compile.source}</X-Compile-Source-JDK>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <X-Compile-Target-JDK>${maven.compile.target}</X-Compile-Target-JDK>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </manifestEntries>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </archive>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.felix</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-bundle-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <excludeDependencies>true</excludeDependencies>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <manifestLocation>target/osgi</manifestLocation>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <instructions>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <!-- stops the "uses" clauses being added to "Export-Package" manifest entry -->[\r][\n]"
[DEBUG] http-outgoing-0 << "            <_nouses>true</_nouses>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <!-- Stop the JAVA_1_n_HOME variables from being treated as headers by Bnd -->[\r][\n]"
[DEBUG] http-outgoing-0 << "            <_removeheaders>JAVA_1_3_HOME,JAVA_1_4_HOME,JAVA_1_5_HOME,JAVA_1_6_HOME</_removeheaders>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <Bundle-SymbolicName>${commons.osgi.symbolicName}</Bundle-SymbolicName>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <Export-Package>${commons.osgi.export}</Export-Package>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <Private-Package>${commons.osgi.private}</Private-Package>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <Import-Package>${commons.osgi.import}</Import-Package>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <DynamicImport-Package>${commons.osgi.dynamicImport}</DynamicImport-Package>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <Bundle-DocURL>${project.url}</Bundle-DocURL>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </instructions>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <id>bundle-manifest</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <phase>process-classes</phase>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <goal>manifest</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-idea-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <!--[\r][\n]"
[DEBUG] http-outgoing-0 << "         | N.B. The version is deliberately not provided - see COMMONSSITE-56[\r][\n]"
[DEBUG] http-outgoing-0 << "         |      This may result in some warnings, e.g. from the Maven 2 Eclipse plugin.[\r][\n]"
[DEBUG] http-outgoing-0 << "         -->[\r][\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <jdkLevel>${maven.compile.source}</jdkLevel>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <!--[\r][\n]"
[DEBUG] http-outgoing-0 << "          - Copy LICENSE.txt and NOTICE.txt so that they are included[\r][\n]"
[DEBUG] http-outgoing-0 << "          - in the -javadoc jar file for the component.[\r][\n]"
[DEBUG] http-outgoing-0 << "          -->[\r][\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-antrun-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <id>javadoc.resources</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <phase>generate-sources</phase>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <goal>run</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <tasks>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <copy todir="${project.build.directory}/apidocs/META-INF">[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <fileset dir="${basedir}">[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <include name="LICENSE.txt" />[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <include name="NOTICE.txt" />[\r][\n]"
[DEBUG] http-outgoing-0 << "                  </fileset>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </copy>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </tasks>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.commons</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>commons-build-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </build>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <reporting>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <!-- N.B. plugins defined here in the <reporting> section ignore what's defined in <pluginManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "         in the <build> section above, so we have to define the versions here. -->[\r][\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-project-info-reports-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <version>2.1.2</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-javadoc-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <version>2.5</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <configuration> [\r][\n]"
[DEBUG] http-outgoing-0 << "          <!-- keep only errors and warnings -->[\r][\n]"
[DEBUG] http-outgoing-0 << "          <quiet>true</quiet>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <source>${maven.compile.source}</source>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <encoding>${commons.encoding}</encoding>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <docEncoding>${commons.do"
Progress (1): commons-parent-17.pom (16/31 kB)
[DEBUG] http-outgoing-0 << "cEncoding}</docEncoding>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <linksource>true</linksource>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <links>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <link>http://java.sun.com/javase/6/docs/api/</link>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </links>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </configuration> [\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-jxr-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <version>2.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <configuration> [\r][\n]"
[DEBUG] http-outgoing-0 << "          <aggregate>false</aggregate>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </configuration> [\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-site-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <version>2.0.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <!-- Exclude the navigation file for Maven 1 sites[\r][\n]"
[DEBUG] http-outgoing-0 << "               and the changes file used by the changes-plugin,[\r][\n]"
[DEBUG] http-outgoing-0 << "               as they interfere with the site generation. -->[\r][\n]"
[DEBUG] http-outgoing-0 << "          <moduleExcludes>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <xdoc>navigation.xml,changes.xml</xdoc>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </moduleExcludes>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-surefire-report-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <version>${commons.surefire.version}</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.codehaus.mojo</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>jdepend-maven-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <version>2.0-beta-2</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.codehaus.mojo</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>rat-maven-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <version>1.0-alpha-3</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </reporting>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <profiles>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <id>ci</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <distributionManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <repository>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <id>apache.snapshots</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <name>Apache Development Snapshot Repository</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <url>${commons.deployment.protocol}://people.apache.org/www/people.apache.org/repo/m2-snapshot-repository</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </repository>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <snapshotRepository>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <id>apache.snapshots</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <name>Apache Development Snapshot Repository</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <url>${commons.deployment.protocol}://people.apache.org/www/people.apache.org/repo/m2-snapshot-repository</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </snapshotRepository>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </distributionManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <id>release</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <build>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <!-- We want to sign the artifact, the POM, and all attached artifacts -->[\r][\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-gpg-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <passphrase>${gpg.passphrase}</passphrase>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <id>sign-artifacts</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <phase>verify</phase>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <goal>sign</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-install-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <createChecksum>true</createChecksum>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-source-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <id>create-source-jar</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <goal>jar</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <phase>package</phase>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-release-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <!-- Pass these arguments to the deploy plugin. -->[\r][\n]"
[DEBUG] http-outgoing-0 << "              <arguments>-Prelease</arguments>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-javadoc-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <id>create-javadoc-jar</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <goal>javadoc</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <goal>jar</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "            "
Progress (1): commons-parent-17.pom (20/31 kB)
[DEBUG] http-outgoing-0 << "    <phase>package</phase>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <source>${maven.compile.source}</source>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-assembly-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <goal>attached</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <phase>package</phase>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </build>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <id>rc</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <distributionManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <repository>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <id>apache.snapshots</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <name>Apache Development Snapshot Repository</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <url>${commons.deployment.protocol}://people.apache.org/www/people.apache.org/builds/commons/${commons.componentid}/${commons.release.version}/${commons.rc.version}/staged</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </repository>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </distributionManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <build>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <!-- We want to sign the artifact, the POM, and all attached artifacts -->[\r][\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-gpg-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <passphrase>${gpg.passphrase}</passphrase>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <id>sign-artifacts</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <phase>verify</phase>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <goal>sign</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-install-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <createChecksum>true</createChecksum>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-source-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <id>create-source-jar</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <goal>jar</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <phase>package</phase>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-release-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <!-- Pass these arguments to the deploy plugin. -->[\r][\n]"
[DEBUG] http-outgoing-0 << "              <arguments>-Prc</arguments>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-javadoc-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <id>create-javadoc-jar</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <goal>javadoc</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <goal>jar</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <phase>package</phase>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <source>${maven.compile.source}</source>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-assembly-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <goal>attached</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <phase>package</phase>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </build>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "    <!--[\r][\n]"
[DEBUG] http-outgoing-0 << "       Profile for running the build using JDK 1.3[\r][\n]"
[DEBUG] http-outgoing-0 << "       (JAVA_1_3_HOME needs to be defined, e.g. in settings.xml or an environment variable)[\r][\n]"
[DEBUG] http-outgoing-0 << "      -->[\r][\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <id>java-1.3</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <properties>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <commons.compiler.fork>true</commons.compiler.fork>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <commons.compiler.compilerVersion>1.3</commons.compiler.compilerVersion>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <commons.compiler.javac>${JAVA_1_3_HOME}/bin/javac</commons.compiler.javac>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <commons.surefire.java>${JAVA_1_3_HOME}/bin/java</commons.surefire.java>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <commons.surefire.version>2.2</commons.surefire.version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </properties>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "    <!--[\r][\n]"
Progress (1): commons-parent-17.pom (25/31 kB)
[DEBUG] http-outgoing-0 << "       Profile for running the build using JDK 1.4[\r][\n]"
[DEBUG] http-outgoing-0 << "       (JAVA_1_4_HOME needs to be defined, e.g. in settings.xml or an environment variable)[\r][\n]"
[DEBUG] http-outgoing-0 << "      -->[\r][\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <id>java-1.4</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <properties>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <commons.compiler.fork>true</commons.compiler.fork>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <commons.compiler.compilerVersion>1.4</commons.compiler.compilerVersion>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <commons.compiler.javac>${JAVA_1_4_HOME}/bin/javac</commons.compiler.javac>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <commons.surefire.java>${JAVA_1_4_HOME}/bin/java</commons.surefire.java>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </properties>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "    <!--[\r][\n]"
[DEBUG] http-outgoing-0 << "       Profile for running the build using JDK 1.5[\r][\n]"
[DEBUG] http-outgoing-0 << "       (JAVA_1_5_HOME needs to be defined, e.g. in settings.xml or an environment variable)[\r][\n]"
[DEBUG] http-outgoing-0 << "      -->[\r][\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <id>java-1.5</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <properties>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <commons.compiler.fork>true</commons.compiler.fork>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <commons.compiler.compilerVersion>1.5</commons.compiler.compilerVersion>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <commons.compiler.javac>${JAVA_1_5_HOME}/bin/javac</commons.compiler.javac>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <commons.surefire.java>${JAVA_1_5_HOME}/bin/java</commons.surefire.java>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </properties>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "    <!--[\r][\n]"
[DEBUG] http-outgoing-0 << "       Profile for running the build using JDK 1.6[\r][\n]"
[DEBUG] http-outgoing-0 << "       (JAVA_1_6_HOME needs to be defined, e.g. in settings.xml or an environment variable)[\r][\n]"
[DEBUG] http-outgoing-0 << "      -->[\r][\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <id>java-1.6</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <properties>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <commons.compiler.fork>true</commons.compiler.fork>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <commons.compiler.compilerVersion>1.6</commons.compiler.compilerVersion>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <commons.compiler.javac>${JAVA_1_6_HOME}/bin/javac</commons.compiler.javac>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <commons.surefire.java>${JAVA_1_6_HOME}/bin/java</commons.surefire.java>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </properties>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "    <!-- N.B. when adding new java profiles, be sure to update [\r][\n]"
[DEBUG] http-outgoing-0 << "         the _removeheaders list in the maven_bundle_plugin configuration -->[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "    <!-- [\r][\n]"
[DEBUG] http-outgoing-0 << "     | Profile to allow testing of deploy phase[\r][\n]"
[DEBUG] http-outgoing-0 << "     | e.g.[\r][\n]"
[DEBUG] http-outgoing-0 << "     | mvn deploy -Ptest-deploy -Prelease -Dgpg.skip[\r][\n]"
[DEBUG] http-outgoing-0 << "     -->[\r][\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <id>test-deploy</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <properties>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <altDeploymentRepository>id::default::file:target/deploy</altDeploymentRepository>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </properties>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "   [\r][\n]"
[DEBUG] http-outgoing-0 << "    <!--[\r][\n]"
[DEBUG] http-outgoing-0 << "      Profile to build all Commons "proper" components.[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "      The trunks of all "proper" components can be checked out using:[\r][\n]"
[DEBUG] http-outgoing-0 << "          https://svn.apache.org/repos/asf/commons/trunks-proper/[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "      This profile is a convenience which can be used, for example, to build all the component sites:[\r][\n]"
[DEBUG] http-outgoing-0 << "          mvn -Ptrunks-proper site[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "      or, to clean up:[\r][\n]"
[DEBUG] http-outgoing-0 << "          mvn -Ptrunks-proper clean[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "      see http://issues.apache.org/jira/browse/COMMONSSITE-30[\r][\n]"
[DEBUG] http-outgoing-0 << "      -->[\r][\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <id>trunks-proper</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <modules>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../beanutils</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../betwixt</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../chain</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../cli</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../codec</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../collections</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../compress</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../configuration</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../daemon</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../dbcp</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../dbutils</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../digester</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../discovery</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../el</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../email</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../exec</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../fileupload</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../io</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../jci</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../jexl</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../jxpath</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../lang</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../launcher</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../logging</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../math</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../modeler</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../net</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../pool</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../primitives</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../proxy</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../sanselan</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../scxml</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../validator</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <module>../vfs</module>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </modules>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </profiles>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <properties>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "    <!-- Default configuration for compiler source and "
Progress (1): commons-parent-17.pom (29/31 kB)
[DEBUG] http-outgoing-0 << "target JVM -->[\r][\n]"
[DEBUG] http-outgoing-0 << "    <maven.compile.source>1.3</maven.compile.source>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <maven.compile.target>1.3</maven.compile.target>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "    <!-- compiler and surefire plugin settings for "java" profiles -->[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.compiler.fork>false</commons.compiler.fork>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.compiler.compilerVersion />[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.compiler.javac />[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.surefire.java />[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.surefire.version>2.5</commons.surefire.version>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "    <!-- Default values for the download-page generation by commons-build-plugin -->[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.release.name>${project.artifactId}-${commons.release.version}</commons.release.name>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.release.desc />[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.binary.suffix>-bin</commons.binary.suffix>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.release.2.name>${project.artifactId}-${commons.release.2.version}</commons.release.2.name>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.release.2.desc />[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.release.2.binary.suffix>-bin</commons.release.2.binary.suffix>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "    <!-- Commons Component Id -->[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.componentid>${project.artifactId}</commons.componentid>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "    <!-- The RC version used in the staging repository URL. -->[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.rc.version>RC1</commons.rc.version>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "    <!-- Configuration properties for the OSGi maven-bundle-plugin -->[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.osgi.symbolicName>org.apache.commons.${commons.componentid}</commons.osgi.symbolicName>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.osgi.export>org.apache.commons.*;version=${project.version};-noimport:=true</commons.osgi.export>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.osgi.import>*</commons.osgi.import>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.osgi.dynamicImport />[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.osgi.private />[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "    <!-- location of any manifest file used by maven-jar-plugin -->[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.manifestfile>target/osgi/MANIFEST.MF</commons.manifestfile>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "    <!--[\r][\n]"
[DEBUG] http-outgoing-0 << "      Make the deployment protocol pluggable. This allows to switch to[\r][\n]"
[DEBUG] http-outgoing-0 << "      other protocols like scpexe, which some users prefer over scp.[\r][\n]"
[DEBUG] http-outgoing-0 << "    -->[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.deployment.protocol>scp</commons.deployment.protocol>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "    <!--[\r][\n]"
[DEBUG] http-outgoing-0 << "      Encoding of Java source files: Make sure, that the compiler and[\r][\n]"
[DEBUG] http-outgoing-0 << "      the javadoc generator use the right encoding. Subprojects may[\r][\n]"
[DEBUG] http-outgoing-0 << "      overwrite this, if they are using another encoding.[\r][\n]"
[DEBUG] http-outgoing-0 << "    -->[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.encoding>iso-8859-1</commons.encoding>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <commons.docEncoding>${commons.encoding}</commons.docEncoding>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <!-- Define encoding for filtering -->[\r][\n]"
[DEBUG] http-outgoing-0 << "    <project.build.sourceEncoding>${commons.encoding}</project.build.sourceEncoding>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <project.reporting.outputEncoding>${commons.encoding}</project.reporting.outputEncoding>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  </properties>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "</project>[\r][\n]"
Progress (1): commons-parent-17.pom (31 kB)   
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/apache/commons/commons-parent/17/commons-parent-17.pom.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/apache/commons/commons-parent/17/commons-parent-17.pom.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/apache/commons/commons-parent/17/commons-parent-17.pom.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "9f94b944de818509083cb8a1e5195132"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Mon, 12 Jul 2010 10:47:23 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 9f94b944de818509083cb8a1e5195132[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: f289d3d6b49a03b9e80068f1cfebca3908e02b8b[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 707667[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 132[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251814.665147,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "9f94b944de818509083cb8a1e5195132"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Mon, 12 Jul 2010 10:47:23 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 9f94b944de818509083cb8a1e5195132
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: f289d3d6b49a03b9e80068f1cfebca3908e02b8b
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 707667
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 132
[DEBUG] http-outgoing-0 << X-Timer: S1698251814.665147,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "84bc2f457fac92c947cde9c15c81786ded79b3c1"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
                                           
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/commons/commons-parent/17/commons-parent-17.pom (31 kB at 725 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/apache/commons/commons-parent/17/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/apache/commons/commons-parent/17/commons-parent-17.pom.lastUpdated
[DEBUG] Resolving artifact org.apache:apache:pom:7 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/apache/7/apache-7.pom
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/apache/apache/7/apache-7.pom HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/apache/apache/7/apache-7.pom HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/apache/apache/7/apache-7.pom HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 14430[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "0b59b8840bbda3984e6b1596794e9eff"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/xml[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Sat, 26 Dec 2009 12:23:18 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 0b59b8840bbda3984e6b1596794e9eff[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: a5f679b14bb06a3cb3769eb04e228c8b9e12908f[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 5562817[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 5151[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251814.687000,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 14430
[DEBUG] http-outgoing-0 << ETag: "0b59b8840bbda3984e6b1596794e9eff"
[DEBUG] http-outgoing-0 << Content-Type: text/xml
[DEBUG] http-outgoing-0 << Last-Modified: Sat, 26 Dec 2009 12:23:18 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 0b59b8840bbda3984e6b1596794e9eff
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: a5f679b14bb06a3cb3769eb04e228c8b9e12908f
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 5562817
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 5151
[DEBUG] http-outgoing-0 << X-Timer: S1698251814.687000,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "<?xml version="1.0" encoding="UTF-8"?>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "<!--[\r][\n]"
[DEBUG] http-outgoing-0 << "Licensed to the Apache Software Foundation (ASF) under one[\r][\n]"
[DEBUG] http-outgoing-0 << "or more contributor license agreements.  See the NOTICE file[\r][\n]"
[DEBUG] http-outgoing-0 << "distributed with this work for additional information[\r][\n]"
[DEBUG] http-outgoing-0 << "regarding copyright ownership.  The ASF licenses this file[\r][\n]"
[DEBUG] http-outgoing-0 << "to you under the Apache License, Version 2.0 (the[\r][\n]"
[DEBUG] http-outgoing-0 << ""License"); you may not use this file except in compliance[\r][\n]"
[DEBUG] http-outgoing-0 << "with the License.  You may obtain a copy of the License at[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  http://www.apache.org/licenses/LICENSE-2.0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "Unless required by applicable law or agreed to in writing,[\r][\n]"
[DEBUG] http-outgoing-0 << "software distributed under the License is distributed on an[\r][\n]"
[DEBUG] http-outgoing-0 << ""AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY[\r][\n]"
[DEBUG] http-outgoing-0 << "KIND, either express or implied.  See the License for the[\r][\n]"
[DEBUG] http-outgoing-0 << "specific language governing permissions and limitations[\r][\n]"
[DEBUG] http-outgoing-0 << "under the License.[\r][\n]"
[DEBUG] http-outgoing-0 << "-->[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">[\r][\n]"
[DEBUG] http-outgoing-0 << "  <modelVersion>4.0.0</modelVersion>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <!-- Shared parent. Doesn't define a lot of things about Apache like general mailing lists, but does[\r][\n]"
[DEBUG] http-outgoing-0 << "       define the settings common to all projects at Apache [\r][\n]"
[DEBUG] http-outgoing-0 << "       [\r][\n]"
[DEBUG] http-outgoing-0 << "       As of Version 6, this includes a standard release profile that all projects can use. If the profile is not appropriate for your project, define your own release profile and change the release plugin configuration parameter <arguments> </arguments> to activate your profile instead of the apache-release profile.[\r][\n]"
[DEBUG] http-outgoing-0 << "       [\r][\n]"
[DEBUG] http-outgoing-0 << "      Standard versions of plugins are also defined, these may be overridden by individual projects as well.[\r][\n]"
[DEBUG] http-outgoing-0 << "       -->[\r][\n]"
[DEBUG] http-outgoing-0 << "  <groupId>org.apache</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <artifactId>apache</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <version>7</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <packaging>pom</packaging>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <name>The Apache Software Foundation</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <description>[\r][\n]"
[DEBUG] http-outgoing-0 << "    The Apache Software Foundation provides support for the Apache community of open-source software projects.[\r][\n]"
[DEBUG] http-outgoing-0 << "    The Apache projects are characterized by a collaborative, consensus based development process, an open and[\r][\n]"
[DEBUG] http-outgoing-0 << "    pragmatic software license, and a desire to create high quality software that leads the way in its field.[\r][\n]"
[DEBUG] http-outgoing-0 << "    We consider ourselves not simply a group of projects sharing a server, but rather a community of developers[\r][\n]"
[DEBUG] http-outgoing-0 << "    and users.[\r][\n]"
[DEBUG] http-outgoing-0 << "  </description>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <licenses>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <license>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <name>The Apache Software License, Version 2.0</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <distribution>repo</distribution>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </license>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </licenses>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <organization>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <name>The Apache Software Foundation</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <url>http://www.apache.org/</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </organization>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <url>http://www.apache.org/</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <repositories>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <repository>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <id>apache.snapshots</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <name>Apache Snapshot Repository</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <url>http://repository.apache.org/snapshots</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <releases>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <enabled>false</enabled>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </releases>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </repository>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </repositories>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <distributionManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <!-- Site omitted - each project must provide their own -->[\r][\n]"
[DEBUG] http-outgoing-0 << "    <repository>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <id>apache.releases.https</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <name>Apache Release Distribution Repository</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <url>https://repository.apache.org/service/local/staging/deploy/maven2</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </repository>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <snapshotRepository>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <id>apache.snapshots.https</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <name>${distMgmtSnapshotsName}</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <url>${distMgmtSnapshotsUrl}</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </snapshotRepository>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </distributionManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <mailingLists>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <mailingList>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <name>Apache Announce List</name>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <subscribe>announce-subscribe@apache.org</subscribe>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <unsubscribe>announce-unsubscribe@apache.org</unsubscribe>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <post>announce@apache.org</post>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <archive>http://mail-archives.apache.org/mod_mbox/www-announce/</archive>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </mailingList>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </mailingLists>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <properties>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <distMgmtSnapshotsName>Apache Development Snapshot Repository</distMgmtSnapshotsName>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <distMgmtSnapshotsUrl>https://repository.apache.org/content/rep"
Progress (1): apache-7.pom (4.1/14 kB)
[DEBUG] http-outgoing-0 << "ositories/snapshots</distMgmtSnapshotsUrl>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <organization.logo>http://www.apache.org/images/asf_logo_wide.gif</organization.logo>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <sourceReleaseAssemblyDescriptor>source-release</sourceReleaseAssemblyDescriptor>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </properties>[\r][\n]"
[DEBUG] http-outgoing-0 << "  <scm>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <connection>scm:svn:http://svn.apache.org/repos/asf/maven/pom/tags/apache-7</connection>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <developerConnection>scm:svn:https://svn.apache.org/repos/asf/maven/pom/tags/apache-7</developerConnection>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <url>http://svn.apache.org/viewvc/maven/pom/tags/apache-7</url>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </scm>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <build>[\r][\n]"
[DEBUG] http-outgoing-0 << "     <pluginManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <!-- set versions of common plugins for reproducibility, ordered alphabetically -->[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-antrun-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>1.3</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-assembly-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.2-beta-5</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-clean-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.3</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-compiler-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.0.2</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <source>1.4</source>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <target>1.4</target>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <encoding>${project.build.sourceEncoding}</encoding>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-deploy-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.5</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-docck-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>1.0</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-enforcer-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>1.0-beta-1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-gpg-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>1.0-alpha-4</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-install-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.3</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-invoker-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>1.5</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-jar-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.3</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <archive>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <manifest>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <addDefaultSpecificationEntries>true</addDefaultSpecificationEntries>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <addDefaultImplementationEntries>true</addDefaultImplementationEntries>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </manifest>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </archive>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-javadoc-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <!-- NOTE: 2.6.x is generally unusuable due to MJAVADOC-275 -->[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.5</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-plugin-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.5.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <!-- START SNIPPET: release-plugin-configuration -->[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-release-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.0-beta-9</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <useReleaseProfile>false</useReleaseProfile>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <goals>deploy</goals>[\r][\n]"
[DEBUG] http-outgoing-0 << " "
Progress (1): apache-7.pom (8.2/14 kB)
[DEBUG] http-outgoing-0 << "           <arguments>-Papache-release</arguments>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <!-- END SNIPPET: release-plugin-configuration -->[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-remote-resources-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>1.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-resources-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.4</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <encoding>${project.build.sourceEncoding}</encoding>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-scm-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>1.2</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-site-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.0.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-source-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.1.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>maven-surefire-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.4.3</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.mojo</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>clirr-maven-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>2.2.2</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.plexus</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>plexus-maven-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>1.3.8</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <groupId>org.codehaus.modello</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <artifactId>modello-maven-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <version>1.1</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </pluginManagement>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <!-- We want to package up license resources in the JARs produced -->[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-remote-resources-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <goal>process</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <resourceBundles>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <resourceBundle>org.apache:apache-jar-resource-bundle:1.4</resourceBundle>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </resourceBundles>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </plugins> [\r][\n]"
[DEBUG] http-outgoing-0 << " </build>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << " <reporting>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <artifactId>maven-project-info-reports-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <version>2.1.2</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "  </reporting>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << "  <profiles>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <!-- START SNIPPET: release-profile -->[\r][\n]"
[DEBUG] http-outgoing-0 << "    <profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <id>apache-release</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "      <build>[\r][\n]"
[DEBUG] http-outgoing-0 << "        <plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <!-- Create a source-release artifact that contains the fully buildable [\r][\n]"
[DEBUG] http-outgoing-0 << "               project directory source structure. This is the artifact which is [\r][\n]"
[DEBUG] http-outgoing-0 << "               the official subject of any release vote. -->[\r][\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-assembly-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <dependencies>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <groupId>org.apache.apache.resources</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <artifactId>apache-source-release-assembly-descriptor</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <version>1.0.2</version>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </dependency>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </dependencies>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <id>source-release-assembly</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <phase>package</phase>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <goal>single</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <runOnlyAtExecutionRoot>true</runOnlyAtExecutionRoot>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <descriptorRefs>[\r][\n]"
[DEBUG] http-outgoing-0 << "                    <descriptorRef>${sourceReleaseAssemblyDescriptor}</descriptorRef>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  </descriptorRefs>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <"
Progress (1): apache-7.pom (12/14 kB) 
[DEBUG] http-outgoing-0 << "tarLongFileFormat>gnu</tarLongFileFormat>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <!-- We want to sign the artifact, the POM, and all attached artifacts -->[\r][\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-gpg-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <passphrase>${gpg.passphrase}</passphrase>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <goal>sign</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <!-- We want to deploy the artifact to a staging location for perusal -->[\r][\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <inherited>true</inherited>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-deploy-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <updateReleaseInfo>true</updateReleaseInfo>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-source-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <id>attach-sources</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <goal>jar</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "          <plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <groupId>org.apache.maven.plugins</groupId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <artifactId>maven-javadoc-plugin</artifactId>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <encoding>${project.build.sourceEncoding}</encoding>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </configuration>[\r][\n]"
[DEBUG] http-outgoing-0 << "            <executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "              <execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <id>attach-javadocs</id>[\r][\n]"
[DEBUG] http-outgoing-0 << "                <goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "                  <goal>jar</goal>[\r][\n]"
[DEBUG] http-outgoing-0 << "                </goals>[\r][\n]"
[DEBUG] http-outgoing-0 << "              </execution>[\r][\n]"
[DEBUG] http-outgoing-0 << "            </executions>[\r][\n]"
[DEBUG] http-outgoing-0 << "          </plugin>[\r][\n]"
[DEBUG] http-outgoing-0 << "        </plugins>[\r][\n]"
[DEBUG] http-outgoing-0 << "      </build>[\r][\n]"
[DEBUG] http-outgoing-0 << "    </profile>[\r][\n]"
[DEBUG] http-outgoing-0 << "    <!-- END SNIPPET: release-profile -->[\r][\n]"
[DEBUG] http-outgoing-0 << "  </profiles>[\r][\n]"
[DEBUG] http-outgoing-0 << "</project>[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
Progress (1): apache-7.pom (14 kB)   
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/apache/apache/7/apache-7.pom.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/apache/apache/7/apache-7.pom.sha1 HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/apache/apache/7/apache-7.pom.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "da1db7627fbf75157b69f11050dff5da"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Sat, 26 Dec 2009 12:23:19 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: da1db7627fbf75157b69f11050dff5da[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 082c9bb1c23afd5623fec6fee812337ae12d03cc[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 1912807[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 797[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251814.719801,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 40
[DEBUG] http-outgoing-0 << ETag: "da1db7627fbf75157b69f11050dff5da"
[DEBUG] http-outgoing-0 << Content-Type: text/plain
[DEBUG] http-outgoing-0 << Last-Modified: Sat, 26 Dec 2009 12:23:19 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: da1db7627fbf75157b69f11050dff5da
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 082c9bb1c23afd5623fec6fee812337ae12d03cc
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 1912807
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 797
[DEBUG] http-outgoing-0 << X-Timer: S1698251814.719801,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-0 << "a5f679b14bb06a3cb3769eb04e228c8b9e12908f"
[DEBUG] Connection [id: 0][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] Connection released: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
                                  
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/apache/7/apache-7.pom (14 kB at 361 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/apache/apache/7/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/apache/apache/7/apache-7.pom.lastUpdated
[DEBUG] Using mirror maven-default-http-blocker (http://0.0.0.0/) for apache.snapshots (http://repository.apache.org/snapshots).
[DEBUG] Dependency collection stats {ConflictMarker.analyzeTime=94452, ConflictMarker.markTime=68209, ConflictMarker.nodeCount=7, ConflictIdSorter.graphTime=39717, ConflictIdSorter.topsortTime=19126, ConflictIdSorter.conflictIdCount=6, ConflictIdSorter.conflictIdCycleCount=0, ConflictResolver.totalTime=283517, ConflictResolver.conflictItemCount=7, DefaultDependencyCollector.collectTime=616991648, DefaultDependencyCollector.transformTime=582266}
[DEBUG] org.sonarsource.scanner.maven:sonar-maven-plugin:jar:3.9.0.2155
[DEBUG]    org.sonatype.plexus:plexus-sec-dispatcher:jar:1.4:compile
[DEBUG]       org.sonatype.plexus:plexus-cipher:jar:1.4:compile
[DEBUG]    org.codehaus.plexus:plexus-utils:jar:3.2.1:compile
[DEBUG]    org.sonarsource.scanner.api:sonar-scanner-api:jar:2.16.1.361:compile
[DEBUG]    commons-lang:commons-lang:jar:2.6:compile
[DEBUG] Resolving artifact org.sonatype.plexus:plexus-sec-dispatcher:jar:1.4 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Resolving artifact org.sonatype.plexus:plexus-cipher:jar:1.4 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Resolving artifact org.codehaus.plexus:plexus-utils:jar:3.2.1 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Resolving artifact org.sonarsource.scanner.api:sonar-scanner-api:jar:2.16.1.361 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Resolving artifact commons-lang:commons-lang:jar:2.6 from [central (https://repo.maven.apache.org/maven2, default, releases)]
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://repo.maven.apache.org/maven2
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://repo.maven.apache.org/maven2
Downloading from central: https://repo.maven.apache.org/maven2/org/sonatype/plexus/plexus-cipher/1.4/plexus-cipher-1.4.jar
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 2; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] Connection leased: [id: 0][route: {s}->https://repo.maven.apache.org:443][total available: 1; route allocated: 2 of 20; total allocated: 2 of 40]
[DEBUG] http-outgoing-0: set socket timeout to 0
[DEBUG] http-outgoing-0: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/sonatype/plexus/plexus-cipher/1.4/plexus-cipher-1.4.jar HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-0 >> GET /maven2/org/sonatype/plexus/plexus-cipher/1.4/plexus-cipher-1.4.jar HTTP/1.1
[DEBUG] http-outgoing-0 >> Cache-control: no-cache
[DEBUG] http-outgoing-0 >> Pragma: no-cache
[DEBUG] http-outgoing-0 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-0 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-0 >> Connection: Keep-Alive
[DEBUG] http-outgoing-0 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-0 >> "GET /maven2/org/sonatype/plexus/plexus-cipher/1.4/plexus-cipher-1.4.jar HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-0 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-0 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-0 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-0 >> "[\r][\n]"
Downloading from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-utils/3.2.1/plexus-utils-3.2.1.jar
[DEBUG] http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-0 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Length: 13494[\r][\n]"
[DEBUG] http-outgoing-0 << "ETag: "7b2d6fcf0d5800d5b1ce09d98d98dcaf"[\r][\n]"
[DEBUG] http-outgoing-0 << "Content-Type: application/java-archive[\r][\n]"
[DEBUG] http-outgoing-0 << "Last-Modified: Wed, 29 Apr 2009 19:40:02 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-MD5: 7b2d6fcf0d5800d5b1ce09d98d98dcaf[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Checksum-SHA1: 50ade46f23bb38cd984b4ec560c46223432aac38[\r][\n]"
[DEBUG] http-outgoing-0 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-0 << "Date: Wed, 25 Oct 2023 16:36:53 GMT[\r][\n]"
[DEBUG] http-outgoing-0 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-0 << "Age: 1904456[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Served-By: cache-iad-kjyo7100162-IAD[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Cache-Hits: 2669[\r][\n]"
[DEBUG] http-outgoing-0 << "X-Timer: S1698251814.758281,VS0,VE0[\r][\n]"
[DEBUG] http-outgoing-0 << "[\r][\n]"
[DEBUG] http-outgoing-0 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-0 << Connection: keep-alive
[DEBUG] http-outgoing-0 << Content-Length: 13494
[DEBUG] http-outgoing-0 << ETag: "7b2d6fcf0d5800d5b1ce09d98d98dcaf"
[DEBUG] http-outgoing-0 << Content-Type: application/java-archive
[DEBUG] http-outgoing-0 << Last-Modified: Wed, 29 Apr 2009 19:40:02 GMT
[DEBUG] http-outgoing-0 << X-Checksum-MD5: 7b2d6fcf0d5800d5b1ce09d98d98dcaf
[DEBUG] http-outgoing-0 << X-Checksum-SHA1: 50ade46f23bb38cd984b4ec560c46223432aac38
[DEBUG] http-outgoing-0 << Accept-Ranges: bytes
[DEBUG] http-outgoing-0 << Date: Wed, 25 Oct 2023 16:36:53 GMT
[DEBUG] http-outgoing-0 << Via: 1.1 varnish
[DEBUG] http-outgoing-0 << Age: 1904456
[DEBUG] http-outgoing-0 << X-Served-By: cache-iad-kjyo7100162-IAD
[DEBUG] http-outgoing-0 << X-Cache: HIT
[DEBUG] http-outgoing-0 << X-Cache-Hits: 2669
[DEBUG] http-outgoing-0 << X-Timer: S1698251814.758281,VS0,VE0
[DEBUG] Connection can be kept alive indefinitely
Downloading from central: https://repo.maven.apache.org/maven2/org/sonatype/plexus/plexus-sec-dispatcher/1.4/plexus-sec-dispatcher-1.4.jar
[DEBUG] http-outgoing-0 << "PK[0x3][0x4][\n]"
[DEBUG] // more debugging
[x14][0x0][0x8][0x8][0x8][0x0][0x83]9[0x9d]R[0xe9]JJr[0x13][0x1][0x0][0x0][0x14][0x2][0x0][0x0]T[0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0xb][0xe8][0x8][0x0]META-INF/maven/org.sonarsource.scanner.api/sonar-scanner-api-batch-interface/pom.xmlPK[0x1][0x2][0x14][0x0][0x14][0x0][0x8][0x8][0x8][0x0][0x89]9[0x9d]R[0xfb]e[0xe8][0xc]R[0x0][0x0][0x0]d[0x0][0x0][0x0][[0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0x0][0xa0][0xe9][0x8][0x0]META-INF/maven/org.sonarsource.scanner.api/sonar-scanner-api-batch-interface/pom.propertiesPK[0x5][0x6][0x0][0x0][0x0][0x0]X[0x1]X[0x1][0xf6][0x9f][0x0][0x0]{[0xea][0x8][0x0][0x0][0x0]"
Progress (1): sonar-scanner-api-2.16.1.361.jar (625 kB)    
[DEBUG] Connection [id: 3][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-3: set socket timeout to 0
[DEBUG] Connection released: [id: 3][route: {s}->https://repo.maven.apache.org:443][total available: 4; route allocated: 4 of 20; total allocated: 4 of 40]
[DEBUG] CookieSpec selected: compatibility
[DEBUG] Connection request: [route: {s}->https://repo.maven.apache.org:443][total available: 4; route allocated: 4 of 20; total allocated: 4 of 40]
[DEBUG] Connection leased: [id: 3][route: {s}->https://repo.maven.apache.org:443][total available: 3; route allocated: 4 of 20; total allocated: 4 of 40]
[DEBUG] http-outgoing-3: set socket timeout to 0
[DEBUG] http-outgoing-3: set socket timeout to 1800000
[DEBUG] Executing request GET /maven2/org/sonarsource/scanner/api/sonar-scanner-api/2.16.1.361/sonar-scanner-api-2.16.1.361.jar.sha1 HTTP/1.1
[DEBUG] Target auth state: UNCHALLENGED
[DEBUG] Proxy auth state: UNCHALLENGED
[DEBUG] http-outgoing-3 >> GET /maven2/org/sonarsource/scanner/api/sonar-scanner-api/2.16.1.361/sonar-scanner-api-2.16.1.361.jar.sha1 HTTP/1.1
[DEBUG] http-outgoing-3 >> Cache-control: no-cache
[DEBUG] http-outgoing-3 >> Pragma: no-cache
[DEBUG] http-outgoing-3 >> User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)
[DEBUG] http-outgoing-3 >> Host: repo.maven.apache.org
[DEBUG] http-outgoing-3 >> Connection: Keep-Alive
[DEBUG] http-outgoing-3 >> Accept-Encoding: gzip,deflate
[DEBUG] http-outgoing-3 >> "GET /maven2/org/sonarsource/scanner/api/sonar-scanner-api/2.16.1.361/sonar-scanner-api-2.16.1.361.jar.sha1 HTTP/1.1[\r][\n]"
[DEBUG] http-outgoing-3 >> "Cache-control: no-cache[\r][\n]"
[DEBUG] http-outgoing-3 >> "Pragma: no-cache[\r][\n]"
[DEBUG] http-outgoing-3 >> "User-Agent: Apache-Maven/3.8.4 (Java 17.0.9; Linux 6.1.56-82.125.amzn2023.x86_64)[\r][\n]"
[DEBUG] http-outgoing-3 >> "Host: repo.maven.apache.org[\r][\n]"
[DEBUG] http-outgoing-3 >> "Connection: Keep-Alive[\r][\n]"
[DEBUG] http-outgoing-3 >> "Accept-Encoding: gzip,deflate[\r][\n]"
[DEBUG] http-outgoing-3 >> "[\r][\n]"
[DEBUG] http-outgoing-3 << "HTTP/1.1 200 OK[\r][\n]"
[DEBUG] http-outgoing-3 << "Connection: keep-alive[\r][\n]"
[DEBUG] http-outgoing-3 << "Content-Length: 40[\r][\n]"
[DEBUG] http-outgoing-3 << "ETag: "4cc9c19a8728e573fa89a24afcd01dee"[\r][\n]"
[DEBUG] http-outgoing-3 << "Content-Type: text/plain[\r][\n]"
[DEBUG] http-outgoing-3 << "Last-Modified: Thu, 29 Apr 2021 07:39:27 GMT[\r][\n]"
[DEBUG] http-outgoing-3 << "X-Checksum-MD5: 4cc9c19a8728e573fa89a24afcd01dee[\r][\n]"
[DEBUG] http-outgoing-3 << "X-Checksum-SHA1: 39864b2685830d87a0dc77315c59c47c03e9d54c[\r][\n]"
[DEBUG] http-outgoing-3 << "Accept-Ranges: bytes[\r][\n]"
[DEBUG] http-outgoing-3 << "Date: Wed, 25 Oct 2023 16:36:54 GMT[\r][\n]"
[DEBUG] http-outgoing-3 << "Via: 1.1 varnish[\r][\n]"
[DEBUG] http-outgoing-3 << "Age: 1304861[\r][\n]"
[DEBUG] http-outgoing-3 << "X-Served-By: cache-iad-kjyo7100057-IAD[\r][\n]"
[DEBUG] http-outgoing-3 << "X-Cache: HIT[\r][\n]"
[DEBUG] http-outgoing-3 << "X-Cache-Hits: 1[\r][\n]"
[DEBUG] http-outgoing-3 << "X-Timer: S1698251814.473876,VS0,VE1[\r][\n]"
[DEBUG] http-outgoing-3 << "[\r][\n]"
[DEBUG] http-outgoing-3 << HTTP/1.1 200 OK
[DEBUG] http-outgoing-3 << Connection: keep-alive
[DEBUG] http-outgoing-3 << Content-Length: 40
[DEBUG] http-outgoing-3 << ETag: "4cc9c19a8728e573fa89a24afcd01dee"
[DEBUG] http-outgoing-3 << Content-Type: text/plain
[DEBUG] http-outgoing-3 << Last-Modified: Thu, 29 Apr 2021 07:39:27 GMT
[DEBUG] http-outgoing-3 << X-Checksum-MD5: 4cc9c19a8728e573fa89a24afcd01dee
[DEBUG] http-outgoing-3 << X-Checksum-SHA1: 39864b2685830d87a0dc77315c59c47c03e9d54c
[DEBUG] http-outgoing-3 << Accept-Ranges: bytes
[DEBUG] http-outgoing-3 << Date: Wed, 25 Oct 2023 16:36:54 GMT
[DEBUG] http-outgoing-3 << Via: 1.1 varnish
[DEBUG] http-outgoing-3 << Age: 1304861
[DEBUG] http-outgoing-3 << X-Served-By: cache-iad-kjyo7100057-IAD
[DEBUG] http-outgoing-3 << X-Cache: HIT
[DEBUG] http-outgoing-3 << X-Cache-Hits: 1
[DEBUG] http-outgoing-3 << X-Timer: S1698251814.473876,VS0,VE1
[DEBUG] Connection can be kept alive indefinitely
[DEBUG] http-outgoing-3 << "b3e93b8fba7bd0c1d9414c6296c3818cf48536b2"
[DEBUG] Connection [id: 3][route: {s}->https://repo.maven.apache.org:443] can be kept alive indefinitely
[DEBUG] http-outgoing-3: set socket timeout to 0
[DEBUG] Connection released: [id: 3][route: {s}->https://repo.maven.apache.org:443][total available: 4; route allocated: 4 of 20; total allocated: 4 of 40]
                                                       
Downloaded from central: https://repo.maven.apache.org/maven2/org/sonarsource/scanner/api/sonar-scanner-api/2.16.1.361/sonar-scanner-api-2.16.1.361.jar (625 kB at 870 kB/s)
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonatype/plexus/plexus-sec-dispatcher/1.4/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonatype/plexus/plexus-sec-dispatcher/1.4/plexus-sec-dispatcher-1.4.jar.lastUpdated
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonatype/plexus/plexus-cipher/1.4/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonatype/plexus/plexus-cipher/1.4/plexus-cipher-1.4.jar.lastUpdated
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/codehaus/plexus/plexus-utils/3.2.1/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/codehaus/plexus/plexus-utils/3.2.1/plexus-utils-3.2.1.jar.lastUpdated
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonarsource/scanner/api/sonar-scanner-api/2.16.1.361/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/org/sonarsource/scanner/api/sonar-scanner-api/2.16.1.361/sonar-scanner-api-2.16.1.361.jar.lastUpdated
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/commons-lang/commons-lang/2.6/_remote.repositories
[DEBUG] Writing tracking file /home/ec2-user/.m2/repository/commons-lang/commons-lang/2.6/commons-lang-2.6.jar.lastUpdated
[DEBUG] Created new class realm plugin>org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155
[DEBUG] Importing foreign packages into class realm plugin>org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155
[DEBUG]   Imported:  < maven.api
[DEBUG] Populating class realm plugin>org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155
[DEBUG]   Included: org.sonarsource.scanner.maven:sonar-maven-plugin:jar:3.9.0.2155
[DEBUG]   Included: org.sonatype.plexus:plexus-sec-dispatcher:jar:1.4
[DEBUG]   Included: org.sonatype.plexus:plexus-cipher:jar:1.4
[DEBUG]   Included: org.codehaus.plexus:plexus-utils:jar:3.2.1
[DEBUG]   Included: org.sonarsource.scanner.api:sonar-scanner-api:jar:2.16.1.361
[DEBUG]   Included: commons-lang:commons-lang:jar:2.6
[DEBUG] Configuring mojo org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar from plugin realm ClassRealm[plugin>org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155, parent: jdk.internal.loader.ClassLoaders$AppClassLoader@5cb0d902]
[DEBUG] Configuring mojo 'org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar' with basic configurator -->
[DEBUG]   (f) mojoExecution = org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar {execution: default-cli}
[DEBUG]   (f) session = org.apache.maven.execution.MavenSession@26844abb
[DEBUG]   (f) skip = false
[DEBUG] -- end configuration --
[DEBUG] 16:36:54.659 keyStore is : 
[DEBUG] 16:36:54.681 keyStore type is : pkcs12
[DEBUG] 16:36:54.681 keyStore provider is : 
[DEBUG] 16:36:54.681 init keystore
[DEBUG] 16:36:54.681 init keymanager of type SunX509
[DEBUG] 16:36:54.689 Create: /home/ec2-user/.sonar/cache
[INFO] 16:36:54.690 User cache: /home/ec2-user/.sonar/cache
[DEBUG] 16:36:54.690 Create: /home/ec2-user/.sonar/cache/_tmp
[DEBUG] 16:36:54.692 Extract sonar-scanner-api-batch in temp...
[DEBUG] 16:36:54.702 Get bootstrap index...
[DEBUG] 16:36:54.702 Download: http://localhost:9000/batch/index
[DEBUG] 16:36:54.791 Get bootstrap completed
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  4.350 s
[INFO] Finished at: 2023-10-25T16:36:54Z
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar (default-cli) on project your-project-name: Unable to execute SonarScanner analysis: Fail to parse entry in bootstrap index: -> [Help 1]
org.apache.maven.lifecycle.LifecycleExecutionException: Failed to execute goal org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar (default-cli) on project your-project-name: Unable to execute SonarScanner analysis
    at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:215)
    at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:156)
    at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:148)
    at org.apache.maven.lifecycle.internal.LifecycleModuleBuilder.buildProject (LifecycleModuleBuilder.java:117)
    at org.apache.maven.lifecycle.internal.LifecycleModuleBuilder.buildProject (LifecycleModuleBuilder.java:81)
    at org.apache.maven.lifecycle.internal.builder.singlethreaded.SingleThreadedBuilder.build (SingleThreadedBuilder.java:56)
    at org.apache.maven.lifecycle.internal.LifecycleStarter.execute (LifecycleStarter.java:128)
    at org.apache.maven.DefaultMaven.doExecute (DefaultMaven.java:305)
    at org.apache.maven.DefaultMaven.doExecute (DefaultMaven.java:192)
    at org.apache.maven.DefaultMaven.execute (DefaultMaven.java:105)
    at org.apache.maven.cli.MavenCli.execute (MavenCli.java:972)
    at org.apache.maven.cli.MavenCli.doMain (MavenCli.java:293)
    at org.apache.maven.cli.MavenCli.main (MavenCli.java:196)
    at jdk.internal.reflect.NativeMethodAccessorImpl.invoke0 (Native Method)
    at jdk.internal.reflect.NativeMethodAccessorImpl.invoke (NativeMethodAccessorImpl.java:77)
    at jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke (DelegatingMethodAccessorImpl.java:43)
    at java.lang.reflect.Method.invoke (Method.java:568)
    at org.codehaus.plexus.classworlds.launcher.Launcher.launchEnhanced (Launcher.java:282)
    at org.codehaus.plexus.classworlds.launcher.Launcher.launch (Launcher.java:225)
    at org.codehaus.plexus.classworlds.launcher.Launcher.mainWithExitCode (Launcher.java:406)
    at org.codehaus.plexus.classworlds.launcher.Launcher.main (Launcher.java:347)
Caused by: org.apache.maven.plugin.MojoExecutionException: Unable to execute SonarScanner analysis
    at org.sonarsource.scanner.maven.bootstrap.ScannerBootstrapper.execute (ScannerBootstrapper.java:67)
    at org.sonarsource.scanner.maven.SonarQubeMojo.execute (SonarQubeMojo.java:108)
    at org.apache.maven.plugin.DefaultBuildPluginManager.executeMojo (DefaultBuildPluginManager.java:137)
    at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:210)
    at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:156)
    at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:148)
    at org.apache.maven.lifecycle.internal.LifecycleModuleBuilder.buildProject (LifecycleModuleBuilder.java:117)
    at org.apache.maven.lifecycle.internal.LifecycleModuleBuilder.buildProject (LifecycleModuleBuilder.java:81)
    at org.apache.maven.lifecycle.internal.builder.singlethreaded.SingleThreadedBuilder.build (SingleThreadedBuilder.java:56)
    at org.apache.maven.lifecycle.internal.LifecycleStarter.execute (LifecycleStarter.java:128)
    at org.apache.maven.DefaultMaven.doExecute (DefaultMaven.java:305)
    at org.apache.maven.DefaultMaven.doExecute (DefaultMaven.java:192)
    at org.apache.maven.DefaultMaven.execute (DefaultMaven.java:105)
    at org.apache.maven.cli.MavenCli.execute (MavenCli.java:972)
    at org.apache.maven.cli.MavenCli.doMain (MavenCli.java:293)
    at org.apache.maven.cli.MavenCli.main (MavenCli.java:196)
    at jdk.internal.reflect.NativeMethodAccessorImpl.invoke0 (Native Method)
    at jdk.internal.reflect.NativeMethodAccessorImpl.invoke (NativeMethodAccessorImpl.java:77)
    at jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke (DelegatingMethodAccessorImpl.java:43)
    at java.lang.reflect.Method.invoke (Method.java:568)
    at org.codehaus.plexus.classworlds.launcher.Launcher.launchEnhanced (Launcher.java:282)
    at org.codehaus.plexus.classworlds.launcher.Launcher.launch (Launcher.java:225)
    at org.codehaus.plexus.classworlds.launcher.Launcher.mainWithExitCode (Launcher.java:406)
    at org.codehaus.plexus.classworlds.launcher.Launcher.main (Launcher.java:347)
Caused by: org.sonarsource.scanner.api.internal.ScannerException: Unable to execute SonarScanner analysis
    at org.sonarsource.scanner.api.internal.IsolatedLauncherFactory.lambda$createLauncher$0 (IsolatedLauncherFactory.java:85)
    at java.security.AccessController.doPrivileged (AccessController.java:318)
    at org.sonarsource.scanner.api.internal.IsolatedLauncherFactory.createLauncher (IsolatedLauncherFactory.java:74)
    at org.sonarsource.scanner.api.internal.IsolatedLauncherFactory.createLauncher (IsolatedLauncherFactory.java:70)
    at org.sonarsource.scanner.api.EmbeddedScanner.doStart (EmbeddedScanner.java:185)
    at org.sonarsource.scanner.api.EmbeddedScanner.start (EmbeddedScanner.java:123)
    at org.sonarsource.scanner.maven.bootstrap.ScannerBootstrapper.execute (ScannerBootstrapper.java:56)
    at org.sonarsource.scanner.maven.SonarQubeMojo.execute (SonarQubeMojo.java:108)
    at org.apache.maven.plugin.DefaultBuildPluginManager.executeMojo (DefaultBuildPluginManager.java:137)
    at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:210)
    at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:156)
    at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:148)
    at org.apache.maven.lifecycle.internal.LifecycleModuleBuilder.buildProject (LifecycleModuleBuilder.java:117)
    at org.apache.maven.lifecycle.internal.LifecycleModuleBuilder.buildProject (LifecycleModuleBuilder.java:81)
    at org.apache.maven.lifecycle.internal.builder.singlethreaded.SingleThreadedBuilder.build (SingleThreadedBuilder.java:56)
    at org.apache.maven.lifecycle.internal.LifecycleStarter.execute (LifecycleStarter.java:128)
    at org.apache.maven.DefaultMaven.doExecute (DefaultMaven.java:305)
    at org.apache.maven.DefaultMaven.doExecute (DefaultMaven.java:192)
    at org.apache.maven.DefaultMaven.execute (DefaultMaven.java:105)
    at org.apache.maven.cli.MavenCli.execute (MavenCli.java:972)
    at org.apache.maven.cli.MavenCli.doMain (MavenCli.java:293)
    at org.apache.maven.cli.MavenCli.main (MavenCli.java:196)
    at jdk.internal.reflect.NativeMethodAccessorImpl.invoke0 (Native Method)
    at jdk.internal.reflect.NativeMethodAccessorImpl.invoke (NativeMethodAccessorImpl.java:77)
    at jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke (DelegatingMethodAccessorImpl.java:43)
    at java.lang.reflect.Method.invoke (Method.java:568)
    at org.codehaus.plexus.classworlds.launcher.Launcher.launchEnhanced (Launcher.java:282)
    at org.codehaus.plexus.classworlds.launcher.Launcher.launch (Launcher.java:225)
    at org.codehaus.plexus.classworlds.launcher.Launcher.mainWithExitCode (Launcher.java:406)
    at org.codehaus.plexus.classworlds.launcher.Launcher.main (Launcher.java:347)
Caused by: java.lang.IllegalStateException: Fail to parse entry in bootstrap index: 
    at org.sonarsource.scanner.api.internal.BootstrapIndexDownloader.parse (BootstrapIndexDownloader.java:59)
    at org.sonarsource.scanner.api.internal.BootstrapIndexDownloader.getIndex (BootstrapIndexDownloader.java:44)
    at org.sonarsource.scanner.api.internal.JarDownloader.getScannerEngineFiles (JarDownloader.java:58)
    at org.sonarsource.scanner.api.internal.JarDownloader.download (JarDownloader.java:53)
    at org.sonarsource.scanner.api.internal.IsolatedLauncherFactory.lambda$createLauncher$0 (IsolatedLauncherFactory.java:76)
    at java.security.AccessController.doPrivileged (AccessController.java:318)
    at org.sonarsource.scanner.api.internal.IsolatedLauncherFactory.createLauncher (IsolatedLauncherFactory.java:74)
    at org.sonarsource.scanner.api.internal.IsolatedLauncherFactory.createLauncher (IsolatedLauncherFactory.java:70)
    at org.sonarsource.scanner.api.EmbeddedScanner.doStart (EmbeddedScanner.java:185)
    at org.sonarsource.scanner.api.EmbeddedScanner.start (EmbeddedScanner.java:123)
    at org.sonarsource.scanner.maven.bootstrap.ScannerBootstrapper.execute (ScannerBootstrapper.java:56)
    at org.sonarsource.scanner.maven.SonarQubeMojo.execute (SonarQubeMojo.java:108)
    at org.apache.maven.plugin.DefaultBuildPluginManager.executeMojo (DefaultBuildPluginManager.java:137)
    at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:210)
    at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:156)
    at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:148)
    at org.apache.maven.lifecycle.internal.LifecycleModuleBuilder.buildProject (LifecycleModuleBuilder.java:117)
    at org.apache.maven.lifecycle.internal.LifecycleModuleBuilder.buildProject (LifecycleModuleBuilder.java:81)
    at org.apache.maven.lifecycle.internal.builder.singlethreaded.SingleThreadedBuilder.build (SingleThreadedBuilder.java:56)
    at org.apache.maven.lifecycle.internal.LifecycleStarter.execute (LifecycleStarter.java:128)
    at org.apache.maven.DefaultMaven.doExecute (DefaultMaven.java:305)
    at org.apache.maven.DefaultMaven.doExecute (DefaultMaven.java:192)
    at org.apache.maven.DefaultMaven.execute (DefaultMaven.java:105)
    at org.apache.maven.cli.MavenCli.execute (MavenCli.java:972)
    at org.apache.maven.cli.MavenCli.doMain (MavenCli.java:293)
    at org.apache.maven.cli.MavenCli.main (MavenCli.java:196)
    at jdk.internal.reflect.NativeMethodAccessorImpl.invoke0 (Native Method)
    at jdk.internal.reflect.NativeMethodAccessorImpl.invoke (NativeMethodAccessorImpl.java:77)
    at jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke (DelegatingMethodAccessorImpl.java:43)
    at java.lang.reflect.Method.invoke (Method.java:568)
    at org.codehaus.plexus.classworlds.launcher.Launcher.launchEnhanced (Launcher.java:282)
    at org.codehaus.plexus.classworlds.launcher.Launcher.launch (Launcher.java:225)
    at org.codehaus.plexus.classworlds.launcher.Launcher.mainWithExitCode (Launcher.java:406)
    at org.codehaus.plexus.classworlds.launcher.Launcher.main (Launcher.java:347)
[ERROR] 
[ERROR] 
[ERROR] For more information about the errors and possible solutions, please read the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoExecutionException
[Pipeline] withEnv
[Pipeline] {
[Pipeline] sh
+ docker stop 181597122d0da626310841183a742c8a039debed466ebb5eb9ed35d31af32acd
181597122d0da626310841183a742c8a039debed466ebb5eb9ed35d31af32acd
+ docker rm -f --volumes 181597122d0da626310841183a742c8a039debed466ebb5eb9ed35d31af32acd
181597122d0da626310841183a742c8a039debed466ebb5eb9ed35d31af32acd
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
ERROR: script returned exit code 1
[Pipeline] // catchError
[Pipeline] }
[Pipeline] // script
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Build Docker Image)
[Pipeline] script
[Pipeline] {
[Pipeline] sh
+ docker build -t myapp my-java-app/.
#0 building with "default" instance using docker driver

#1 [internal] load build definition from Dockerfile
#1 transferring dockerfile: 538B done
#1 DONE 0.0s

#2 [internal] load .dockerignore
#2 transferring context: 2B done
#2 DONE 0.1s

#3 [internal] load metadata for docker.io/library/openjdk:11-jre-slim
#3 DONE 0.4s

#4 [internal] load build context
#4 transferring context: 730B done
#4 DONE 0.0s

#5 [1/5] FROM docker.io/library/openjdk:11-jre-slim@sha256:93af7df2308c5141a751c4830e6b6c5717db102b3b31f012ea29d842dc4f2b02
#5 resolve docker.io/library/openjdk:11-jre-slim@sha256:93af7df2308c5141a751c4830e6b6c5717db102b3b31f012ea29d842dc4f2b02 0.0s done
#5 sha256:12cca292b13cb58fadde25af113ddc4ac3b0c5e39ab3f1290a6ba62ec8237afd 0B / 212B 0.1s
#5 sha256:93af7df2308c5141a751c4830e6b6c5717db102b3b31f012ea29d842dc4f2b02 549B / 549B done
#5 sha256:884c08d0f406a81ae1b5786932abaf399c335b997da7eea6a30cc51529220b66 1.16kB / 1.16kB done
#5 sha256:764a04af3eff09cc6a29bcc19cf6315dbea455d7392c1a588a5deb331a929c29 7.55kB / 7.55kB done
#5 sha256:1efc276f4ff952c055dea726cfc96ec6a4fdb8b62d9eed816bd2b788f2860ad7 2.10MB / 31.37MB 0.1s
#5 sha256:a2f2f93da48276873890ac821b3c991d53a7e864791aaf82c39b7863c908b93b 0B / 1.58MB 0.1s
#5 sha256:1efc276f4ff952c055dea726cfc96ec6a4fdb8b62d9eed816bd2b788f2860ad7 15.73MB / 31.37MB 0.2s
#5 sha256:a2f2f93da48276873890ac821b3c991d53a7e864791aaf82c39b7863c908b93b 1.58MB / 1.58MB 0.1s done
#5 sha256:d73cf48caaac2e45ad76a2a9eb3b311d0e4eb1d804e3d2b9cf075a1fa31e6f92 0B / 46.04MB 0.2s
#5 sha256:12cca292b13cb58fadde25af113ddc4ac3b0c5e39ab3f1290a6ba62ec8237afd 212B / 212B 0.3s done
#5 sha256:1efc276f4ff952c055dea726cfc96ec6a4fdb8b62d9eed816bd2b788f2860ad7 25.17MB / 31.37MB 0.3s
#5 sha256:1efc276f4ff952c055dea726cfc96ec6a4fdb8b62d9eed816bd2b788f2860ad7 31.37MB / 31.37MB 0.4s
#5 sha256:d73cf48caaac2e45ad76a2a9eb3b311d0e4eb1d804e3d2b9cf075a1fa31e6f92 7.34MB / 46.04MB 0.4s
#5 extracting sha256:1efc276f4ff952c055dea726cfc96ec6a4fdb8b62d9eed816bd2b788f2860ad7
#5 sha256:1efc276f4ff952c055dea726cfc96ec6a4fdb8b62d9eed816bd2b788f2860ad7 31.37MB / 31.37MB 0.4s done
#5 sha256:d73cf48caaac2e45ad76a2a9eb3b311d0e4eb1d804e3d2b9cf075a1fa31e6f92 17.83MB / 46.04MB 0.6s
#5 sha256:d73cf48caaac2e45ad76a2a9eb3b311d0e4eb1d804e3d2b9cf075a1fa31e6f92 20.97MB / 46.04MB 0.7s
#5 sha256:d73cf48caaac2e45ad76a2a9eb3b311d0e4eb1d804e3d2b9cf075a1fa31e6f92 29.36MB / 46.04MB 0.8s
#5 sha256:d73cf48caaac2e45ad76a2a9eb3b311d0e4eb1d804e3d2b9cf075a1fa31e6f92 35.65MB / 46.04MB 0.9s
#5 sha256:d73cf48caaac2e45ad76a2a9eb3b311d0e4eb1d804e3d2b9cf075a1fa31e6f92 40.89MB / 46.04MB 1.1s
#5 sha256:d73cf48caaac2e45ad76a2a9eb3b311d0e4eb1d804e3d2b9cf075a1fa31e6f92 44.04MB / 46.04MB 1.2s
#5 sha256:d73cf48caaac2e45ad76a2a9eb3b311d0e4eb1d804e3d2b9cf075a1fa31e6f92 46.04MB / 46.04MB 1.4s done
#5 extracting sha256:1efc276f4ff952c055dea726cfc96ec6a4fdb8b62d9eed816bd2b788f2860ad7 2.4s done
#5 extracting sha256:a2f2f93da48276873890ac821b3c991d53a7e864791aaf82c39b7863c908b93b
#5 extracting sha256:a2f2f93da48276873890ac821b3c991d53a7e864791aaf82c39b7863c908b93b 0.1s done
#5 extracting sha256:12cca292b13cb58fadde25af113ddc4ac3b0c5e39ab3f1290a6ba62ec8237afd
#5 extracting sha256:12cca292b13cb58fadde25af113ddc4ac3b0c5e39ab3f1290a6ba62ec8237afd done
#5 extracting sha256:d73cf48caaac2e45ad76a2a9eb3b311d0e4eb1d804e3d2b9cf075a1fa31e6f92
#5 extracting sha256:d73cf48caaac2e45ad76a2a9eb3b311d0e4eb1d804e3d2b9cf075a1fa31e6f92 1.4s done
#5 DONE 4.7s

#6 [2/5] WORKDIR /app
#6 DONE 2.7s

#7 [3/5] COPY src /app/src
#7 DONE 0.0s

#8 [4/5] RUN apt-get update && apt-get install -y openjdk-11-jdk
#8 0.356 Get:1 http://deb.debian.org/debian bullseye InRelease [116 kB]
#8 0.369 Get:2 http://deb.debian.org/debian-security bullseye-security InRelease [48.4 kB]
#8 0.370 Get:3 http://deb.debian.org/debian bullseye-updates InRelease [44.1 kB]
#8 0.480 Get:4 http://deb.debian.org/debian bullseye/main amd64 Packages [8062 kB]
#8 0.605 Get:5 http://deb.debian.org/debian-security bullseye-security/main amd64 Packages [256 kB]
#8 0.703 Get:6 http://deb.debian.org/debian bullseye-updates/main amd64 Packages [17.4 kB]
#8 1.698 Fetched 8544 kB in 1s (6326 kB/s)
#8 1.698 Reading package lists...
#8 2.339 Reading package lists...
#8 2.957 Building dependency tree...
#8 3.100 Reading state information...
#8 3.250 The following additional packages will be installed:
#8 3.250   alsa-topology-conf alsa-ucm-conf at-spi2-core ca-certificates-java dbus
#8 3.250   fontconfig-config fonts-dejavu-core fonts-dejavu-extra java-common
#8 3.250   libapparmor1 libasound2 libasound2-data libatk-bridge2.0-0
#8 3.250   libatk-wrapper-java libatk-wrapper-java-jni libatk1.0-0 libatk1.0-data
#8 3.250   libatspi2.0-0 libavahi-client3 libavahi-common-data libavahi-common3
#8 3.250   libbrotli1 libbsd0 libcups2 libdbus-1-3 libdrm-amdgpu1 libdrm-common
#8 3.250   libdrm-intel1 libdrm-nouveau2 libdrm-radeon1 libdrm2 libedit2 libelf1
#8 3.250   libexpat1 libfontconfig1 libfontenc1 libfreetype6 libgif7 libgl1
#8 3.250   libgl1-mesa-dri libglapi-mesa libglib2.0-0 libglib2.0-data libglvnd0
#8 3.250   libglx-mesa0 libglx0 libgraphite2-3 libharfbuzz0b libice-dev libice6
#8 3.250   libicu67 libjpeg62-turbo liblcms2-2 libllvm11 libmd0 libnspr4 libnss3
#8 3.251   libpciaccess0 libpcsclite1 libpng16-16 libpthread-stubs0-dev
#8 3.251   libsensors-config libsensors5 libsm-dev libsm6 libsqlite3-0 libvulkan1
#8 3.251   libwayland-client0 libx11-6 libx11-data libx11-dev libx11-xcb1 libxau-dev
#8 3.251   libxau6 libxaw7 libxcb-dri2-0 libxcb-dri3-0 libxcb-glx0 libxcb-present0
#8 3.251   libxcb-randr0 libxcb-shape0 libxcb-shm0 libxcb-sync1 libxcb-xfixes0 libxcb1
#8 3.251   libxcb1-dev libxcomposite1 libxdamage1 libxdmcp-dev libxdmcp6 libxext6
#8 3.251   libxfixes3 libxft2 libxi6 libxinerama1 libxkbfile1 libxml2 libxmu6 libxmuu1
#8 3.251   libxpm4 libxrandr2 libxrender1 libxshmfence1 libxt-dev libxt6 libxtst6
#8 3.251   libxv1 libxxf86dga1 libxxf86vm1 libz3-4 mesa-vulkan-drivers
#8 3.251   openjdk-11-jdk-headless openjdk-11-jre openjdk-11-jre-headless
#8 3.251   sensible-utils shared-mime-info ucf x11-common x11-utils x11proto-dev
#8 3.251   xdg-user-dirs xorg-sgml-doctools xtrans-dev
#8 3.252 Suggested packages:
#8 3.252   default-dbus-session-bus | dbus-session-bus default-jre libasound2-plugins
#8 3.252   alsa-utils cups-common libice-doc liblcms2-utils pciutils pcscd lm-sensors
#8 3.252   libsm-doc libx11-doc libxcb-doc libxt-doc openjdk-11-demo openjdk-11-source
#8 3.252   visualvm libnss-mdns fonts-ipafont-gothic fonts-ipafont-mincho
#8 3.252   fonts-wqy-microhei | fonts-wqy-zenhei fonts-indic mesa-utils
#8 3.871 The following NEW packages will be installed:
#8 3.871   alsa-topology-conf alsa-ucm-conf at-spi2-core ca-certificates-java dbus
#8 3.871   fontconfig-config fonts-dejavu-core fonts-dejavu-extra java-common
#8 3.871   libapparmor1 libasound2 libasound2-data libatk-bridge2.0-0
#8 3.871   libatk-wrapper-java libatk-wrapper-java-jni libatk1.0-0 libatk1.0-data
#8 3.871   libatspi2.0-0 libavahi-client3 libavahi-common-data libavahi-common3
#8 3.871   libbrotli1 libbsd0 libcups2 libdbus-1-3 libdrm-amdgpu1 libdrm-common
#8 3.871   libdrm-intel1 libdrm-nouveau2 libdrm-radeon1 libdrm2 libedit2 libelf1
#8 3.871   libexpat1 libfontconfig1 libfontenc1 libfreetype6 libgif7 libgl1
#8 3.871   libgl1-mesa-dri libglapi-mesa libglib2.0-0 libglib2.0-data libglvnd0
#8 3.872   libglx-mesa0 libglx0 libgraphite2-3 libharfbuzz0b libice-dev libice6
#8 3.872   libicu67 libjpeg62-turbo liblcms2-2 libllvm11 libmd0 libnspr4 libnss3
#8 3.872   libpciaccess0 libpcsclite1 libpng16-16 libpthread-stubs0-dev
#8 3.872   libsensors-config libsensors5 libsm-dev libsm6 libsqlite3-0 libvulkan1
#8 3.872   libwayland-client0 libx11-6 libx11-data libx11-dev libx11-xcb1 libxau-dev
#8 3.872   libxau6 libxaw7 libxcb-dri2-0 libxcb-dri3-0 libxcb-glx0 libxcb-present0
#8 3.872   libxcb-randr0 libxcb-shape0 libxcb-shm0 libxcb-sync1 libxcb-xfixes0 libxcb1
#8 3.872   libxcb1-dev libxcomposite1 libxdamage1 libxdmcp-dev libxdmcp6 libxext6
#8 3.872   libxfixes3 libxft2 libxi6 libxinerama1 libxkbfile1 libxml2 libxmu6 libxmuu1
#8 3.872   libxpm4 libxrandr2 libxrender1 libxshmfence1 libxt-dev libxt6 libxtst6
#8 3.872   libxv1 libxxf86dga1 libxxf86vm1 libz3-4 mesa-vulkan-drivers openjdk-11-jdk
#8 3.872   openjdk-11-jdk-headless openjdk-11-jre openjdk-11-jre-headless
#8 3.872   sensible-utils shared-mime-info ucf x11-common x11-utils x11proto-dev
#8 3.872   xdg-user-dirs xorg-sgml-doctools xtrans-dev
#8 3.897 0 upgraded, 124 newly installed, 0 to remove and 25 not upgraded.
#8 3.897 Need to get 184 MB of archives.
#8 3.897 After this operation, 531 MB of additional disk space will be used.
#8 3.897 Get:1 http://deb.debian.org/debian bullseye/main amd64 libapparmor1 amd64 2.13.6-10 [99.3 kB]
#8 3.901 Get:2 http://deb.debian.org/debian bullseye/main amd64 libdbus-1-3 amd64 1.12.28-0+deb11u1 [223 kB]
#8 3.904 Get:3 http://deb.debian.org/debian bullseye/main amd64 libexpat1 amd64 2.2.10-2+deb11u5 [98.2 kB]
#8 3.907 Get:4 http://deb.debian.org/debian bullseye/main amd64 dbus amd64 1.12.28-0+deb11u1 [244 kB]
#8 3.908 Get:5 http://deb.debian.org/debian bullseye/main amd64 sensible-utils all 0.0.14 [14.8 kB]
#8 3.908 Get:6 http://deb.debian.org/debian bullseye/main amd64 ucf all 3.0043 [74.0 kB]
#8 3.909 Get:7 http://deb.debian.org/debian bullseye/main amd64 alsa-topology-conf all 1.2.4-1 [12.8 kB]
#8 3.910 Get:8 http://deb.debian.org/debian bullseye/main amd64 libasound2-data all 1.2.4-1.1 [38.2 kB]
#8 3.910 Get:9 http://deb.debian.org/debian bullseye/main amd64 libasound2 amd64 1.2.4-1.1 [356 kB]
#8 3.917 Get:10 http://deb.debian.org/debian bullseye/main amd64 alsa-ucm-conf all 1.2.4-2 [28.1 kB]
#8 3.917 Get:11 http://deb.debian.org/debian bullseye/main amd64 libglib2.0-0 amd64 2.66.8-1 [1370 kB]
#8 3.930 Get:12 http://deb.debian.org/debian bullseye/main amd64 libxau6 amd64 1:1.0.9-1 [19.7 kB]
#8 3.931 Get:13 http://deb.debian.org/debian bullseye/main amd64 libmd0 amd64 1.0.3-3 [28.0 kB]
#8 3.932 Get:14 http://deb.debian.org/debian bullseye/main amd64 libbsd0 amd64 0.11.3-1+deb11u1 [108 kB]
#8 3.933 Get:15 http://deb.debian.org/debian bullseye/main amd64 libxdmcp6 amd64 1:1.1.2-3 [26.3 kB]
#8 3.933 Get:16 http://deb.debian.org/debian bullseye/main amd64 libxcb1 amd64 1.14-3 [140 kB]
#8 3.935 Get:17 http://deb.debian.org/debian-security bullseye-security/main amd64 libx11-data all 2:1.7.2-1+deb11u2 [311 kB]
#8 3.938 Get:18 http://deb.debian.org/debian-security bullseye-security/main amd64 libx11-6 amd64 2:1.7.2-1+deb11u2 [772 kB]
#8 3.944 Get:19 http://deb.debian.org/debian bullseye/main amd64 libatspi2.0-0 amd64 2.38.0-4+deb11u1 [72.4 kB]
#8 3.944 Get:20 http://deb.debian.org/debian bullseye/main amd64 libxext6 amd64 2:1.3.3-1.1 [52.7 kB]
#8 3.945 Get:21 http://deb.debian.org/debian bullseye/main amd64 libxi6 amd64 2:1.7.10-1 [83.4 kB]
#8 3.946 Get:22 http://deb.debian.org/debian bullseye/main amd64 x11-common all 1:7.7+22 [252 kB]
#8 3.948 Get:23 http://deb.debian.org/debian bullseye/main amd64 libxtst6 amd64 2:1.2.3-1 [27.8 kB]
#8 3.949 Get:24 http://deb.debian.org/debian bullseye/main amd64 at-spi2-core amd64 2.38.0-4+deb11u1 [72.5 kB]
#8 3.950 Get:25 http://deb.debian.org/debian bullseye/main amd64 java-common all 0.72 [14.5 kB]
#8 3.951 Get:26 http://deb.debian.org/debian bullseye/main amd64 libavahi-common-data amd64 0.8-5+deb11u2 [124 kB]
#8 3.952 Get:27 http://deb.debian.org/debian bullseye/main amd64 libavahi-common3 amd64 0.8-5+deb11u2 [58.7 kB]
#8 3.953 Get:28 http://deb.debian.org/debian bullseye/main amd64 libavahi-client3 amd64 0.8-5+deb11u2 [62.6 kB]
#8 3.954 Get:29 http://deb.debian.org/debian bullseye/main amd64 libcups2 amd64 2.3.3op2-3+deb11u6 [351 kB]
#8 3.958 Get:30 http://deb.debian.org/debian bullseye/main amd64 liblcms2-2 amd64 2.12~rc1-2 [150 kB]
#8 3.959 Get:31 http://deb.debian.org/debian bullseye/main amd64 libjpeg62-turbo amd64 1:2.0.6-4 [151 kB]
#8 3.961 Get:32 http://deb.debian.org/debian bullseye/main amd64 libbrotli1 amd64 1.0.9-2+b2 [279 kB]
#8 3.964 Get:33 http://deb.debian.org/debian bullseye/main amd64 libpng16-16 amd64 1.6.37-3 [294 kB]
#8 3.967 Get:34 http://deb.debian.org/debian bullseye/main amd64 libfreetype6 amd64 2.10.4+dfsg-1+deb11u1 [418 kB]
#8 3.971 Get:35 http://deb.debian.org/debian bullseye/main amd64 fonts-dejavu-core all 2.37-2 [1069 kB]
#8 3.982 Get:36 http://deb.debian.org/debian bullseye/main amd64 fontconfig-config all 2.13.1-4.2 [281 kB]
#8 3.984 Get:37 http://deb.debian.org/debian bullseye/main amd64 libfontconfig1 amd64 2.13.1-4.2 [347 kB]
#8 3.988 Get:38 http://deb.debian.org/debian bullseye/main amd64 libnspr4 amd64 2:4.29-1 [112 kB]
#8 3.989 Get:39 http://deb.debian.org/debian bullseye/main amd64 libsqlite3-0 amd64 3.34.1-3 [797 kB]
#8 3.997 Get:40 http://deb.debian.org/debian bullseye/main amd64 libnss3 amd64 2:3.61-1+deb11u3 [1305 kB]
#8 4.010 Get:41 http://deb.debian.org/debian bullseye/main amd64 libgraphite2-3 amd64 1.3.14-1 [81.2 kB]
#8 4.011 Get:42 http://deb.debian.org/debian bullseye/main amd64 libharfbuzz0b amd64 2.7.4-1 [1471 kB]
#8 4.025 Get:43 http://deb.debian.org/debian bullseye/main amd64 libpcsclite1 amd64 1.9.1-1 [60.2 kB]
#8 4.025 Get:44 http://deb.debian.org/debian bullseye/main amd64 openjdk-11-jre-headless amd64 11.0.20+8-1~deb11u1 [38.2 MB]
#8 4.395 Get:45 http://deb.debian.org/debian bullseye/main amd64 ca-certificates-java all 20190909+deb11u1 [15.9 kB]
#8 4.396 Get:46 http://deb.debian.org/debian bullseye/main amd64 fonts-dejavu-extra all 2.37-2 [2070 kB]
#8 4.411 Get:47 http://deb.debian.org/debian bullseye/main amd64 libatk1.0-data all 2.36.0-2 [149 kB]
#8 4.412 Get:48 http://deb.debian.org/debian bullseye/main amd64 libatk1.0-0 amd64 2.36.0-2 [52.2 kB]
#8 4.413 Get:49 http://deb.debian.org/debian bullseye/main amd64 libatk-bridge2.0-0 amd64 2.38.0-1 [64.6 kB]
#8 4.414 Get:50 http://deb.debian.org/debian bullseye/main amd64 libfontenc1 amd64 1:1.1.4-1 [24.3 kB]
#8 4.414 Get:51 http://deb.debian.org/debian bullseye/main amd64 libglvnd0 amd64 1.3.2-1 [53.6 kB]
#8 4.415 Get:52 http://deb.debian.org/debian bullseye/main amd64 libdrm-common all 2.4.104-1 [14.9 kB]
#8 4.416 Get:53 http://deb.debian.org/debian bullseye/main amd64 libdrm2 amd64 2.4.104-1 [41.5 kB]
#8 4.416 Get:54 http://deb.debian.org/debian bullseye/main amd64 libglapi-mesa amd64 20.3.5-1 [71.7 kB]
#8 4.418 Get:55 http://deb.debian.org/debian-security bullseye-security/main amd64 libx11-xcb1 amd64 2:1.7.2-1+deb11u2 [204 kB]
#8 4.420 Get:56 http://deb.debian.org/debian bullseye/main amd64 libxcb-dri2-0 amd64 1.14-3 [103 kB]
#8 4.421 Get:57 http://deb.debian.org/debian bullseye/main amd64 libxcb-dri3-0 amd64 1.14-3 [102 kB]
#8 4.422 Get:58 http://deb.debian.org/debian bullseye/main amd64 libxcb-glx0 amd64 1.14-3 [118 kB]
#8 4.423 Get:59 http://deb.debian.org/debian bullseye/main amd64 libxcb-present0 amd64 1.14-3 [101 kB]
#8 4.424 Get:60 http://deb.debian.org/debian bullseye/main amd64 libxcb-shm0 amd64 1.14-3 [101 kB]
#8 4.425 Get:61 http://deb.debian.org/debian bullseye/main amd64 libxcb-sync1 amd64 1.14-3 [105 kB]
#8 4.427 Get:62 http://deb.debian.org/debian bullseye/main amd64 libxcb-xfixes0 amd64 1.14-3 [105 kB]
#8 4.428 Get:63 http://deb.debian.org/debian bullseye/main amd64 libxdamage1 amd64 1:1.1.5-2 [15.7 kB]
#8 4.428 Get:64 http://deb.debian.org/debian bullseye/main amd64 libxfixes3 amd64 1:5.0.3-2 [22.1 kB]
#8 4.429 Get:65 http://deb.debian.org/debian bullseye/main amd64 libxshmfence1 amd64 1.3-1 [8820 B]
#8 4.430 Get:66 http://deb.debian.org/debian bullseye/main amd64 libxxf86vm1 amd64 1:1.1.4-1+b2 [20.8 kB]
#8 4.430 Get:67 http://deb.debian.org/debian bullseye/main amd64 libdrm-amdgpu1 amd64 2.4.104-1 [28.5 kB]
#8 4.431 Get:68 http://deb.debian.org/debian bullseye/main amd64 libpciaccess0 amd64 0.16-1 [53.6 kB]
#8 4.442 Get:69 http://deb.debian.org/debian bullseye/main amd64 libdrm-intel1 amd64 2.4.104-1 [71.8 kB]
#8 4.443 Get:70 http://deb.debian.org/debian bullseye/main amd64 libdrm-nouveau2 amd64 2.4.104-1 [26.8 kB]
#8 4.444 Get:71 http://deb.debian.org/debian bullseye/main amd64 libdrm-radeon1 amd64 2.4.104-1 [30.2 kB]
#8 4.444 Get:72 http://deb.debian.org/debian bullseye/main amd64 libelf1 amd64 0.183-1 [165 kB]
#8 4.445 Get:73 http://deb.debian.org/debian bullseye/main amd64 libedit2 amd64 3.1-20191231-2+b1 [96.7 kB]
#8 4.445 Get:74 http://deb.debian.org/debian bullseye/main amd64 libz3-4 amd64 4.8.10-1 [6949 kB]
#8 4.503 Get:75 http://deb.debian.org/debian bullseye/main amd64 libllvm11 amd64 1:11.0.1-2 [17.9 MB]
#8 4.670 Get:76 http://deb.debian.org/debian bullseye/main amd64 libsensors-config all 1:3.6.0-7 [32.3 kB]
#8 4.670 Get:77 http://deb.debian.org/debian bullseye/main amd64 libsensors5 amd64 1:3.6.0-7 [52.3 kB]
#8 4.673 Get:78 http://deb.debian.org/debian bullseye/main amd64 libvulkan1 amd64 1.2.162.0-1 [103 kB]
#8 4.674 Get:79 http://deb.debian.org/debian bullseye/main amd64 libgl1-mesa-dri amd64 20.3.5-1 [9633 kB]
#8 4.760 Get:80 http://deb.debian.org/debian bullseye/main amd64 libglx-mesa0 amd64 20.3.5-1 [186 kB]
#8 4.761 Get:81 http://deb.debian.org/debian bullseye/main amd64 libglx0 amd64 1.3.2-1 [35.7 kB]
#8 4.762 Get:82 http://deb.debian.org/debian bullseye/main amd64 libgl1 amd64 1.3.2-1 [89.5 kB]
#8 4.763 Get:83 http://deb.debian.org/debian bullseye/main amd64 libice6 amd64 2:1.0.10-1 [58.5 kB]
#8 4.764 Get:84 http://deb.debian.org/debian bullseye/main amd64 libsm6 amd64 2:1.2.3-1 [35.1 kB]
#8 4.766 Get:85 http://deb.debian.org/debian bullseye/main amd64 libxt6 amd64 1:1.2.0-1 [189 kB]
#8 4.767 Get:86 http://deb.debian.org/debian bullseye/main amd64 libxmu6 amd64 2:1.1.2-2+b3 [60.8 kB]
#8 4.767 Get:87 http://deb.debian.org/debian-security bullseye-security/main amd64 libxpm4 amd64 1:3.5.12-1.1+deb11u1 [50.0 kB]
#8 4.768 Get:88 http://deb.debian.org/debian bullseye/main amd64 libxaw7 amd64 2:1.0.13-1.1 [202 kB]
#8 4.771 Get:89 http://deb.debian.org/debian bullseye/main amd64 libxcb-shape0 amd64 1.14-3 [102 kB]
#8 4.772 Get:90 http://deb.debian.org/debian bullseye/main amd64 libxcomposite1 amd64 1:0.4.5-1 [16.6 kB]
#8 4.772 Get:91 http://deb.debian.org/debian bullseye/main amd64 libxrender1 amd64 1:0.9.10-1 [33.0 kB]
#8 4.773 Get:92 http://deb.debian.org/debian bullseye/main amd64 libxft2 amd64 2.3.2-2 [57.2 kB]
#8 4.774 Get:93 http://deb.debian.org/debian bullseye/main amd64 libxinerama1 amd64 2:1.1.4-2 [17.7 kB]
#8 4.774 Get:94 http://deb.debian.org/debian bullseye/main amd64 libxkbfile1 amd64 1:1.1.0-1 [75.2 kB]
#8 4.775 Get:95 http://deb.debian.org/debian bullseye/main amd64 libxmuu1 amd64 2:1.1.2-2+b3 [23.9 kB]
#8 4.776 Get:96 http://deb.debian.org/debian bullseye/main amd64 libxrandr2 amd64 2:1.5.1-1 [37.5 kB]
#8 4.777 Get:97 http://deb.debian.org/debian bullseye/main amd64 libxv1 amd64 2:1.0.11-1 [24.6 kB]
#8 4.777 Get:98 http://deb.debian.org/debian bullseye/main amd64 libxxf86dga1 amd64 2:1.1.4-1+b3 [22.1 kB]
#8 4.778 Get:99 http://deb.debian.org/debian bullseye/main amd64 x11-utils amd64 7.7+5 [202 kB]
#8 4.780 Get:100 http://deb.debian.org/debian bullseye/main amd64 libatk-wrapper-java all 0.38.0-2+deb11u1 [64.7 kB]
#8 4.781 Get:101 http://deb.debian.org/debian bullseye/main amd64 libatk-wrapper-java-jni amd64 0.38.0-2+deb11u1 [56.2 kB]
#8 4.782 Get:102 http://deb.debian.org/debian bullseye/main amd64 libgif7 amd64 5.1.9-2 [45.1 kB]
#8 4.783 Get:103 http://deb.debian.org/debian bullseye/main amd64 libglib2.0-data all 2.66.8-1 [1164 kB]
#8 4.796 Get:104 http://deb.debian.org/debian bullseye/main amd64 xorg-sgml-doctools all 1:1.11-1.1 [22.1 kB]
#8 4.796 Get:105 http://deb.debian.org/debian bullseye/main amd64 x11proto-dev all 2020.1-1 [594 kB]
#8 4.803 Get:106 http://deb.debian.org/debian bullseye/main amd64 libice-dev amd64 2:1.0.10-1 [67.1 kB]
#8 4.803 Get:107 http://deb.debian.org/debian bullseye/main amd64 libicu67 amd64 67.1-7 [8622 kB]
#8 4.880 Get:108 http://deb.debian.org/debian bullseye/main amd64 libpthread-stubs0-dev amd64 0.4-1 [5344 B]
#8 4.881 Get:109 http://deb.debian.org/debian bullseye/main amd64 libsm-dev amd64 2:1.2.3-1 [38.0 kB]
#8 4.881 Get:110 http://deb.debian.org/debian bullseye/main amd64 libwayland-client0 amd64 1.18.0-2~exp1.1 [26.9 kB]
#8 4.882 Get:111 http://deb.debian.org/debian bullseye/main amd64 libxau-dev amd64 1:1.0.9-1 [22.9 kB]
#8 4.882 Get:112 http://deb.debian.org/debian bullseye/main amd64 libxdmcp-dev amd64 1:1.1.2-3 [42.2 kB]
#8 4.883 Get:113 http://deb.debian.org/debian bullseye/main amd64 xtrans-dev all 1.4.0-1 [98.7 kB]
#8 4.884 Get:114 http://deb.debian.org/debian bullseye/main amd64 libxcb1-dev amd64 1.14-3 [176 kB]
#8 4.885 Get:115 http://deb.debian.org/debian-security bullseye-security/main amd64 libx11-dev amd64 2:1.7.2-1+deb11u2 [845 kB]
#8 4.890 Get:116 http://deb.debian.org/debian bullseye/main amd64 libxcb-randr0 amd64 1.14-3 [113 kB]
#8 4.891 Get:117 http://deb.debian.org/debian bullseye/main amd64 libxml2 amd64 2.9.10+dfsg-6.7+deb11u4 [693 kB]
#8 4.896 Get:118 http://deb.debian.org/debian bullseye/main amd64 libxt-dev amd64 1:1.2.0-1 [407 kB]
#8 4.901 Get:119 http://deb.debian.org/debian bullseye/main amd64 mesa-vulkan-drivers amd64 20.3.5-1 [4086 kB]
#8 4.945 Get:120 http://deb.debian.org/debian bullseye/main amd64 openjdk-11-jre amd64 11.0.20+8-1~deb11u1 [194 kB]
#8 4.947 Get:121 http://deb.debian.org/debian bullseye/main amd64 openjdk-11-jdk-headless amd64 11.0.20+8-1~deb11u1 [73.6 MB]
#8 5.589 Get:122 http://deb.debian.org/debian bullseye/main amd64 openjdk-11-jdk amd64 11.0.20+8-1~deb11u1 [2057 kB]
#8 5.604 Get:123 http://deb.debian.org/debian bullseye/main amd64 shared-mime-info amd64 2.0-1 [701 kB]
#8 5.608 Get:124 http://deb.debian.org/debian bullseye/main amd64 xdg-user-dirs amd64 0.17-2 [53.8 kB]
#8 5.748 debconf: delaying package configuration, since apt-utils is not installed
#8 5.779 Fetched 184 MB in 2s (107 MB/s)
#8 5.807 Selecting previously unselected package libapparmor1:amd64.
#8 5.807 (Reading database ... 
(Reading database ... 5%
(Reading database ... 10%
(Reading database ... 15%
(Reading database ... 20%
(Reading database ... 25%
(Reading database ... 30%
(Reading database ... 35%
(Reading database ... 40%
(Reading database ... 45%
(Reading database ... 50%
(Reading database ... 55%
(Reading database ... 60%
(Reading database ... 65%
(Reading database ... 70%
(Reading database ... 75%
(Reading database ... 80%
(Reading database ... 85%
(Reading database ... 90%
(Reading database ... 95%
(Reading database ... 100%
(Reading database ... 7071 files and directories currently installed.)
#8 5.814 Preparing to unpack .../000-libapparmor1_2.13.6-10_amd64.deb ...
#8 5.821 Unpacking libapparmor1:amd64 (2.13.6-10) ...
#8 5.866 Selecting previously unselected package libdbus-1-3:amd64.
#8 5.867 Preparing to unpack .../001-libdbus-1-3_1.12.28-0+deb11u1_amd64.deb ...
#8 5.871 Unpacking libdbus-1-3:amd64 (1.12.28-0+deb11u1) ...
#8 5.927 Selecting previously unselected package libexpat1:amd64.
#8 5.929 Preparing to unpack .../002-libexpat1_2.2.10-2+deb11u5_amd64.deb ...
#8 5.932 Unpacking libexpat1:amd64 (2.2.10-2+deb11u5) ...
#8 5.985 Selecting previously unselected package dbus.
#8 5.986 Preparing to unpack .../003-dbus_1.12.28-0+deb11u1_amd64.deb ...
#8 6.000 Unpacking dbus (1.12.28-0+deb11u1) ...
#8 6.053 Selecting previously unselected package sensible-utils.
#8 6.055 Preparing to unpack .../004-sensible-utils_0.0.14_all.deb ...
#8 6.058 Unpacking sensible-utils (0.0.14) ...
#8 6.102 Selecting previously unselected package ucf.
#8 6.103 Preparing to unpack .../005-ucf_3.0043_all.deb ...
#8 6.109 Moving old data out of the way
#8 6.110 Unpacking ucf (3.0043) ...
#8 6.151 Selecting previously unselected package alsa-topology-conf.
#8 6.152 Preparing to unpack .../006-alsa-topology-conf_1.2.4-1_all.deb ...
#8 6.158 Unpacking alsa-topology-conf (1.2.4-1) ...
#8 6.188 Selecting previously unselected package libasound2-data.
#8 6.189 Preparing to unpack .../007-libasound2-data_1.2.4-1.1_all.deb ...
#8 6.192 Unpacking libasound2-data (1.2.4-1.1) ...
#8 6.247 Selecting previously unselected package libasound2:amd64.
#8 6.248 Preparing to unpack .../008-libasound2_1.2.4-1.1_amd64.deb ...
#8 6.254 Unpacking libasound2:amd64 (1.2.4-1.1) ...
#8 6.318 Selecting previously unselected package alsa-ucm-conf.
#8 6.319 Preparing to unpack .../009-alsa-ucm-conf_1.2.4-2_all.deb ...
#8 6.322 Unpacking alsa-ucm-conf (1.2.4-2) ...
#8 6.408 Selecting previously unselected package libglib2.0-0:amd64.
#8 6.410 Preparing to unpack .../010-libglib2.0-0_2.66.8-1_amd64.deb ...
#8 6.414 Unpacking libglib2.0-0:amd64 (2.66.8-1) ...
#8 6.573 Selecting previously unselected package libxau6:amd64.
#8 6.574 Preparing to unpack .../011-libxau6_1%3a1.0.9-1_amd64.deb ...
#8 6.578 Unpacking libxau6:amd64 (1:1.0.9-1) ...
#8 6.614 Selecting previously unselected package libmd0:amd64.
#8 6.616 Preparing to unpack .../012-libmd0_1.0.3-3_amd64.deb ...
#8 6.619 Unpacking libmd0:amd64 (1.0.3-3) ...
#8 6.660 Selecting previously unselected package libbsd0:amd64.
#8 6.662 Preparing to unpack .../013-libbsd0_0.11.3-1+deb11u1_amd64.deb ...
#8 6.666 Unpacking libbsd0:amd64 (0.11.3-1+deb11u1) ...
#8 6.706 Selecting previously unselected package libxdmcp6:amd64.
#8 6.707 Preparing to unpack .../014-libxdmcp6_1%3a1.1.2-3_amd64.deb ...
#8 6.712 Unpacking libxdmcp6:amd64 (1:1.1.2-3) ...
#8 6.753 Selecting previously unselected package libxcb1:amd64.
#8 6.754 Preparing to unpack .../015-libxcb1_1.14-3_amd64.deb ...
#8 6.758 Unpacking libxcb1:amd64 (1.14-3) ...
#8 6.793 Selecting previously unselected package libx11-data.
#8 6.794 Preparing to unpack .../016-libx11-data_2%3a1.7.2-1+deb11u2_all.deb ...
#8 6.798 Unpacking libx11-data (2:1.7.2-1+deb11u2) ...
#8 6.887 Selecting previously unselected package libx11-6:amd64.
#8 6.888 Preparing to unpack .../017-libx11-6_2%3a1.7.2-1+deb11u2_amd64.deb ...
#8 6.893 Unpacking libx11-6:amd64 (2:1.7.2-1+deb11u2) ...
#8 6.987 Selecting previously unselected package libatspi2.0-0:amd64.
#8 6.989 Preparing to unpack .../018-libatspi2.0-0_2.38.0-4+deb11u1_amd64.deb ...
#8 6.994 Unpacking libatspi2.0-0:amd64 (2.38.0-4+deb11u1) ...
#8 7.036 Selecting previously unselected package libxext6:amd64.
#8 7.037 Preparing to unpack .../019-libxext6_2%3a1.3.3-1.1_amd64.deb ...
#8 7.043 Unpacking libxext6:amd64 (2:1.3.3-1.1) ...
#8 7.081 Selecting previously unselected package libxi6:amd64.
#8 7.083 Preparing to unpack .../020-libxi6_2%3a1.7.10-1_amd64.deb ...
#8 7.087 Unpacking libxi6:amd64 (2:1.7.10-1) ...
#8 7.138 Selecting previously unselected package x11-common.
#8 7.139 Preparing to unpack .../021-x11-common_1%3a7.7+22_all.deb ...
#8 7.144 Unpacking x11-common (1:7.7+22) ...
#8 7.189 Selecting previously unselected package libxtst6:amd64.
#8 7.191 Preparing to unpack .../022-libxtst6_2%3a1.2.3-1_amd64.deb ...
#8 7.195 Unpacking libxtst6:amd64 (2:1.2.3-1) ...
#8 7.232 Selecting previously unselected package at-spi2-core.
#8 7.234 Preparing to unpack .../023-at-spi2-core_2.38.0-4+deb11u1_amd64.deb ...
#8 7.238 Unpacking at-spi2-core (2.38.0-4+deb11u1) ...
#8 7.286 Selecting previously unselected package java-common.
#8 7.288 Preparing to unpack .../024-java-common_0.72_all.deb ...
#8 7.292 Unpacking java-common (0.72) ...
#8 7.323 Selecting previously unselected package libavahi-common-data:amd64.
#8 7.324 Preparing to unpack .../025-libavahi-common-data_0.8-5+deb11u2_amd64.deb ...
#8 7.332 Unpacking libavahi-common-data:amd64 (0.8-5+deb11u2) ...
#8 7.379 Selecting previously unselected package libavahi-common3:amd64.
#8 7.381 Preparing to unpack .../026-libavahi-common3_0.8-5+deb11u2_amd64.deb ...
#8 7.388 Unpacking libavahi-common3:amd64 (0.8-5+deb11u2) ...
#8 7.430 Selecting previously unselected package libavahi-client3:amd64.
#8 7.431 Preparing to unpack .../027-libavahi-client3_0.8-5+deb11u2_amd64.deb ...
#8 7.435 Unpacking libavahi-client3:amd64 (0.8-5+deb11u2) ...
#8 7.480 Selecting previously unselected package libcups2:amd64.
#8 7.482 Preparing to unpack .../028-libcups2_2.3.3op2-3+deb11u6_amd64.deb ...
#8 7.487 Unpacking libcups2:amd64 (2.3.3op2-3+deb11u6) ...
#8 7.546 Selecting previously unselected package liblcms2-2:amd64.
#8 7.548 Preparing to unpack .../029-liblcms2-2_2.12~rc1-2_amd64.deb ...
#8 7.556 Unpacking liblcms2-2:amd64 (2.12~rc1-2) ...
#8 7.601 Selecting previously unselected package libjpeg62-turbo:amd64.
#8 7.603 Preparing to unpack .../030-libjpeg62-turbo_1%3a2.0.6-4_amd64.deb ...
#8 7.607 Unpacking libjpeg62-turbo:amd64 (1:2.0.6-4) ...
#8 7.660 Selecting previously unselected package libbrotli1:amd64.
#8 7.661 Preparing to unpack .../031-libbrotli1_1.0.9-2+b2_amd64.deb ...
#8 7.667 Unpacking libbrotli1:amd64 (1.0.9-2+b2) ...
#8 7.726 Selecting previously unselected package libpng16-16:amd64.
#8 7.728 Preparing to unpack .../032-libpng16-16_1.6.37-3_amd64.deb ...
#8 7.732 Unpacking libpng16-16:amd64 (1.6.37-3) ...
#8 7.792 Selecting previously unselected package libfreetype6:amd64.
#8 7.793 Preparing to unpack .../033-libfreetype6_2.10.4+dfsg-1+deb11u1_amd64.deb ...
#8 7.797 Unpacking libfreetype6:amd64 (2.10.4+dfsg-1+deb11u1) ...
#8 7.864 Selecting previously unselected package fonts-dejavu-core.
#8 7.866 Preparing to unpack .../034-fonts-dejavu-core_2.37-2_all.deb ...
#8 7.871 Unpacking fonts-dejavu-core (2.37-2) ...
#8 8.017 Selecting previously unselected package fontconfig-config.
#8 8.018 Preparing to unpack .../035-fontconfig-config_2.13.1-4.2_all.deb ...
#8 8.149 Unpacking fontconfig-config (2.13.1-4.2) ...
#8 8.204 Selecting previously unselected package libfontconfig1:amd64.
#8 8.206 Preparing to unpack .../036-libfontconfig1_2.13.1-4.2_amd64.deb ...
#8 8.211 Unpacking libfontconfig1:amd64 (2.13.1-4.2) ...
#8 8.265 Selecting previously unselected package libnspr4:amd64.
#8 8.267 Preparing to unpack .../037-libnspr4_2%3a4.29-1_amd64.deb ...
#8 8.270 Unpacking libnspr4:amd64 (2:4.29-1) ...
#8 8.317 Selecting previously unselected package libsqlite3-0:amd64.
#8 8.318 Preparing to unpack .../038-libsqlite3-0_3.34.1-3_amd64.deb ...
#8 8.322 Unpacking libsqlite3-0:amd64 (3.34.1-3) ...
#8 8.430 Selecting previously unselected package libnss3:amd64.
#8 8.432 Preparing to unpack .../039-libnss3_2%3a3.61-1+deb11u3_amd64.deb ...
#8 8.437 Unpacking libnss3:amd64 (2:3.61-1+deb11u3) ...
#8 8.594 Selecting previously unselected package libgraphite2-3:amd64.
#8 8.596 Preparing to unpack .../040-libgraphite2-3_1.3.14-1_amd64.deb ...
#8 8.599 Unpacking libgraphite2-3:amd64 (1.3.14-1) ...
#8 8.644 Selecting previously unselected package libharfbuzz0b:amd64.
#8 8.646 Preparing to unpack .../041-libharfbuzz0b_2.7.4-1_amd64.deb ...
#8 8.650 Unpacking libharfbuzz0b:amd64 (2.7.4-1) ...
#8 8.728 Selecting previously unselected package libpcsclite1:amd64.
#8 8.730 Preparing to unpack .../042-libpcsclite1_1.9.1-1_amd64.deb ...
#8 8.735 Unpacking libpcsclite1:amd64 (1.9.1-1) ...
#8 8.782 Selecting previously unselected package openjdk-11-jre-headless:amd64.
#8 8.784 Preparing to unpack .../043-openjdk-11-jre-headless_11.0.20+8-1~deb11u1_amd64.deb ...
#8 8.788 Unpacking openjdk-11-jre-headless:amd64 (11.0.20+8-1~deb11u1) ...
#8 12.58 Selecting previously unselected package ca-certificates-java.
#8 12.58 Preparing to unpack .../044-ca-certificates-java_20190909+deb11u1_all.deb ...
#8 12.60 Unpacking ca-certificates-java (20190909+deb11u1) ...
#8 12.63 Selecting previously unselected package fonts-dejavu-extra.
#8 12.63 Preparing to unpack .../045-fonts-dejavu-extra_2.37-2_all.deb ...
#8 12.64 Unpacking fonts-dejavu-extra (2.37-2) ...
#8 12.87 Selecting previously unselected package libatk1.0-data.
#8 12.87 Preparing to unpack .../046-libatk1.0-data_2.36.0-2_all.deb ...
#8 12.87 Unpacking libatk1.0-data (2.36.0-2) ...
#8 12.93 Selecting previously unselected package libatk1.0-0:amd64.
#8 12.93 Preparing to unpack .../047-libatk1.0-0_2.36.0-2_amd64.deb ...
#8 12.94 Unpacking libatk1.0-0:amd64 (2.36.0-2) ...
#8 12.98 Selecting previously unselected package libatk-bridge2.0-0:amd64.
#8 12.98 Preparing to unpack .../048-libatk-bridge2.0-0_2.38.0-1_amd64.deb ...
#8 12.98 Unpacking libatk-bridge2.0-0:amd64 (2.38.0-1) ...
#8 13.03 Selecting previously unselected package libfontenc1:amd64.
#8 13.03 Preparing to unpack .../049-libfontenc1_1%3a1.1.4-1_amd64.deb ...
#8 13.03 Unpacking libfontenc1:amd64 (1:1.1.4-1) ...
#8 13.07 Selecting previously unselected package libglvnd0:amd64.
#8 13.07 Preparing to unpack .../050-libglvnd0_1.3.2-1_amd64.deb ...
#8 13.08 Unpacking libglvnd0:amd64 (1.3.2-1) ...
#8 13.12 Selecting previously unselected package libdrm-common.
#8 13.12 Preparing to unpack .../051-libdrm-common_2.4.104-1_all.deb ...
#8 13.13 Unpacking libdrm-common (2.4.104-1) ...
#8 13.16 Selecting previously unselected package libdrm2:amd64.
#8 13.17 Preparing to unpack .../052-libdrm2_2.4.104-1_amd64.deb ...
#8 13.17 Unpacking libdrm2:amd64 (2.4.104-1) ...
#8 13.21 Selecting previously unselected package libglapi-mesa:amd64.
#8 13.21 Preparing to unpack .../053-libglapi-mesa_20.3.5-1_amd64.deb ...
#8 13.22 Unpacking libglapi-mesa:amd64 (20.3.5-1) ...
#8 13.26 Selecting previously unselected package libx11-xcb1:amd64.
#8 13.26 Preparing to unpack .../054-libx11-xcb1_2%3a1.7.2-1+deb11u2_amd64.deb ...
#8 13.26 Unpacking libx11-xcb1:amd64 (2:1.7.2-1+deb11u2) ...
#8 13.31 Selecting previously unselected package libxcb-dri2-0:amd64.
#8 13.31 Preparing to unpack .../055-libxcb-dri2-0_1.14-3_amd64.deb ...
#8 13.32 Unpacking libxcb-dri2-0:amd64 (1.14-3) ...
#8 13.36 Selecting previously unselected package libxcb-dri3-0:amd64.
#8 13.36 Preparing to unpack .../056-libxcb-dri3-0_1.14-3_amd64.deb ...
#8 13.36 Unpacking libxcb-dri3-0:amd64 (1.14-3) ...
#8 13.42 Selecting previously unselected package libxcb-glx0:amd64.
#8 13.42 Preparing to unpack .../057-libxcb-glx0_1.14-3_amd64.deb ...
#8 13.42 Unpacking libxcb-glx0:amd64 (1.14-3) ...
#8 13.47 Selecting previously unselected package libxcb-present0:amd64.
#8 13.47 Preparing to unpack .../058-libxcb-present0_1.14-3_amd64.deb ...
#8 13.48 Unpacking libxcb-present0:amd64 (1.14-3) ...
#8 13.52 Selecting previously unselected package libxcb-shm0:amd64.
#8 13.52 Preparing to unpack .../059-libxcb-shm0_1.14-3_amd64.deb ...
#8 13.53 Unpacking libxcb-shm0:amd64 (1.14-3) ...
#8 13.58 Selecting previously unselected package libxcb-sync1:amd64.
#8 13.58 Preparing to unpack .../060-libxcb-sync1_1.14-3_amd64.deb ...
#8 13.58 Unpacking libxcb-sync1:amd64 (1.14-3) ...
#8 13.63 Selecting previously unselected package libxcb-xfixes0:amd64.
#8 13.63 Preparing to unpack .../061-libxcb-xfixes0_1.14-3_amd64.deb ...
#8 13.64 Unpacking libxcb-xfixes0:amd64 (1.14-3) ...
#8 13.68 Selecting previously unselected package libxdamage1:amd64.
#8 13.68 Preparing to unpack .../062-libxdamage1_1%3a1.1.5-2_amd64.deb ...
#8 13.69 Unpacking libxdamage1:amd64 (1:1.1.5-2) ...
#8 13.73 Selecting previously unselected package libxfixes3:amd64.
#8 13.73 Preparing to unpack .../063-libxfixes3_1%3a5.0.3-2_amd64.deb ...
#8 13.74 Unpacking libxfixes3:amd64 (1:5.0.3-2) ...
#8 13.77 Selecting previously unselected package libxshmfence1:amd64.
#8 13.77 Preparing to unpack .../064-libxshmfence1_1.3-1_amd64.deb ...
#8 13.78 Unpacking libxshmfence1:amd64 (1.3-1) ...
#8 13.82 Selecting previously unselected package libxxf86vm1:amd64.
#8 13.82 Preparing to unpack .../065-libxxf86vm1_1%3a1.1.4-1+b2_amd64.deb ...
#8 13.82 Unpacking libxxf86vm1:amd64 (1:1.1.4-1+b2) ...
#8 13.86 Selecting previously unselected package libdrm-amdgpu1:amd64.
#8 13.86 Preparing to unpack .../066-libdrm-amdgpu1_2.4.104-1_amd64.deb ...
#8 13.87 Unpacking libdrm-amdgpu1:amd64 (2.4.104-1) ...
#8 13.91 Selecting previously unselected package libpciaccess0:amd64.
#8 13.91 Preparing to unpack .../067-libpciaccess0_0.16-1_amd64.deb ...
#8 13.91 Unpacking libpciaccess0:amd64 (0.16-1) ...
#8 13.96 Selecting previously unselected package libdrm-intel1:amd64.
#8 13.96 Preparing to unpack .../068-libdrm-intel1_2.4.104-1_amd64.deb ...
#8 13.96 Unpacking libdrm-intel1:amd64 (2.4.104-1) ...
#8 14.01 Selecting previously unselected package libdrm-nouveau2:amd64.
#8 14.01 Preparing to unpack .../069-libdrm-nouveau2_2.4.104-1_amd64.deb ...
#8 14.02 Unpacking libdrm-nouveau2:amd64 (2.4.104-1) ...
#8 14.05 Selecting previously unselected package libdrm-radeon1:amd64.
#8 14.05 Preparing to unpack .../070-libdrm-radeon1_2.4.104-1_amd64.deb ...
#8 14.06 Unpacking libdrm-radeon1:amd64 (2.4.104-1) ...
#8 14.10 Selecting previously unselected package libelf1:amd64.
#8 14.10 Preparing to unpack .../071-libelf1_0.183-1_amd64.deb ...
#8 14.10 Unpacking libelf1:amd64 (0.183-1) ...
#8 14.17 Selecting previously unselected package libedit2:amd64.
#8 14.17 Preparing to unpack .../072-libedit2_3.1-20191231-2+b1_amd64.deb ...
#8 14.18 Unpacking libedit2:amd64 (3.1-20191231-2+b1) ...
#8 14.22 Selecting previously unselected package libz3-4:amd64.
#8 14.22 Preparing to unpack .../073-libz3-4_4.8.10-1_amd64.deb ...
#8 14.23 Unpacking libz3-4:amd64 (4.8.10-1) ...
#8 14.92 Selecting previously unselected package libllvm11:amd64.
#8 14.92 Preparing to unpack .../074-libllvm11_1%3a11.0.1-2_amd64.deb ...
#8 14.92 Unpacking libllvm11:amd64 (1:11.0.1-2) ...
#8 16.75 Selecting previously unselected package libsensors-config.
#8 16.76 Preparing to unpack .../075-libsensors-config_1%3a3.6.0-7_all.deb ...
#8 16.76 Unpacking libsensors-config (1:3.6.0-7) ...
#8 16.81 Selecting previously unselected package libsensors5:amd64.
#8 16.81 Preparing to unpack .../076-libsensors5_1%3a3.6.0-7_amd64.deb ...
#8 16.84 Unpacking libsensors5:amd64 (1:3.6.0-7) ...
#8 16.89 Selecting previously unselected package libvulkan1:amd64.
#8 16.89 Preparing to unpack .../077-libvulkan1_1.2.162.0-1_amd64.deb ...
#8 16.90 Unpacking libvulkan1:amd64 (1.2.162.0-1) ...
#8 16.94 Selecting previously unselected package libgl1-mesa-dri:amd64.
#8 16.94 Preparing to unpack .../078-libgl1-mesa-dri_20.3.5-1_amd64.deb ...
#8 16.95 Unpacking libgl1-mesa-dri:amd64 (20.3.5-1) ...
#8 17.89 Selecting previously unselected package libglx-mesa0:amd64.
#8 17.89 Preparing to unpack .../079-libglx-mesa0_20.3.5-1_amd64.deb ...
#8 17.89 Unpacking libglx-mesa0:amd64 (20.3.5-1) ...
#8 17.94 Selecting previously unselected package libglx0:amd64.
#8 17.94 Preparing to unpack .../080-libglx0_1.3.2-1_amd64.deb ...
#8 17.95 Unpacking libglx0:amd64 (1.3.2-1) ...
#8 17.98 Selecting previously unselected package libgl1:amd64.
#8 17.98 Preparing to unpack .../081-libgl1_1.3.2-1_amd64.deb ...
#8 17.99 Unpacking libgl1:amd64 (1.3.2-1) ...
#8 18.03 Selecting previously unselected package libice6:amd64.
#8 18.03 Preparing to unpack .../082-libice6_2%3a1.0.10-1_amd64.deb ...
#8 18.03 Unpacking libice6:amd64 (2:1.0.10-1) ...
#8 18.08 Selecting previously unselected package libsm6:amd64.
#8 18.09 Preparing to unpack .../083-libsm6_2%3a1.2.3-1_amd64.deb ...
#8 18.09 Unpacking libsm6:amd64 (2:1.2.3-1) ...
#8 18.12 Selecting previously unselected package libxt6:amd64.
#8 18.13 Preparing to unpack .../084-libxt6_1%3a1.2.0-1_amd64.deb ...
#8 18.13 Unpacking libxt6:amd64 (1:1.2.0-1) ...
#8 18.18 Selecting previously unselected package libxmu6:amd64.
#8 18.18 Preparing to unpack .../085-libxmu6_2%3a1.1.2-2+b3_amd64.deb ...
#8 18.18 Unpacking libxmu6:amd64 (2:1.1.2-2+b3) ...
#8 18.22 Selecting previously unselected package libxpm4:amd64.
#8 18.22 Preparing to unpack .../086-libxpm4_1%3a3.5.12-1.1+deb11u1_amd64.deb ...
#8 18.22 Unpacking libxpm4:amd64 (1:3.5.12-1.1+deb11u1) ...
#8 18.26 Selecting previously unselected package libxaw7:amd64.
#8 18.26 Preparing to unpack .../087-libxaw7_2%3a1.0.13-1.1_amd64.deb ...
#8 18.26 Unpacking libxaw7:amd64 (2:1.0.13-1.1) ...
#8 18.32 Selecting previously unselected package libxcb-shape0:amd64.
#8 18.32 Preparing to unpack .../088-libxcb-shape0_1.14-3_amd64.deb ...
#8 18.32 Unpacking libxcb-shape0:amd64 (1.14-3) ...
#8 18.36 Selecting previously unselected package libxcomposite1:amd64.
#8 18.36 Preparing to unpack .../089-libxcomposite1_1%3a0.4.5-1_amd64.deb ...
#8 18.37 Unpacking libxcomposite1:amd64 (1:0.4.5-1) ...
#8 18.40 Selecting previously unselected package libxrender1:amd64.
#8 18.40 Preparing to unpack .../090-libxrender1_1%3a0.9.10-1_amd64.deb ...
#8 18.40 Unpacking libxrender1:amd64 (1:0.9.10-1) ...
#8 18.43 Selecting previously unselected package libxft2:amd64.
#8 18.44 Preparing to unpack .../091-libxft2_2.3.2-2_amd64.deb ...
#8 18.44 Unpacking libxft2:amd64 (2.3.2-2) ...
#8 18.47 Selecting previously unselected package libxinerama1:amd64.
#8 18.48 Preparing to unpack .../092-libxinerama1_2%3a1.1.4-2_amd64.deb ...
#8 18.48 Unpacking libxinerama1:amd64 (2:1.1.4-2) ...
#8 18.51 Selecting previously unselected package libxkbfile1:amd64.
#8 18.51 Preparing to unpack .../093-libxkbfile1_1%3a1.1.0-1_amd64.deb ...
#8 18.52 Unpacking libxkbfile1:amd64 (1:1.1.0-1) ...
#8 18.56 Selecting previously unselected package libxmuu1:amd64.
#8 18.56 Preparing to unpack .../094-libxmuu1_2%3a1.1.2-2+b3_amd64.deb ...
#8 18.56 Unpacking libxmuu1:amd64 (2:1.1.2-2+b3) ...
#8 18.60 Selecting previously unselected package libxrandr2:amd64.
#8 18.60 Preparing to unpack .../095-libxrandr2_2%3a1.5.1-1_amd64.deb ...
#8 18.60 Unpacking libxrandr2:amd64 (2:1.5.1-1) ...
#8 18.63 Selecting previously unselected package libxv1:amd64.
#8 18.64 Preparing to unpack .../096-libxv1_2%3a1.0.11-1_amd64.deb ...
#8 18.64 Unpacking libxv1:amd64 (2:1.0.11-1) ...
#8 18.67 Selecting previously unselected package libxxf86dga1:amd64.
#8 18.67 Preparing to unpack .../097-libxxf86dga1_2%3a1.1.4-1+b3_amd64.deb ...
#8 18.68 Unpacking libxxf86dga1:amd64 (2:1.1.4-1+b3) ...
#8 18.71 Selecting previously unselected package x11-utils.
#8 18.71 Preparing to unpack .../098-x11-utils_7.7+5_amd64.deb ...
#8 18.72 Unpacking x11-utils (7.7+5) ...
#8 18.76 Selecting previously unselected package libatk-wrapper-java.
#8 18.76 Preparing to unpack .../099-libatk-wrapper-java_0.38.0-2+deb11u1_all.deb ...
#8 18.77 Unpacking libatk-wrapper-java (0.38.0-2+deb11u1) ...
#8 18.80 Selecting previously unselected package libatk-wrapper-java-jni:amd64.
#8 18.80 Preparing to unpack .../100-libatk-wrapper-java-jni_0.38.0-2+deb11u1_amd64.deb ...
#8 18.80 Unpacking libatk-wrapper-java-jni:amd64 (0.38.0-2+deb11u1) ...
#8 18.84 Selecting previously unselected package libgif7:amd64.
#8 18.84 Preparing to unpack .../101-libgif7_5.1.9-2_amd64.deb ...
#8 18.85 Unpacking libgif7:amd64 (5.1.9-2) ...
#8 18.87 Selecting previously unselected package libglib2.0-data.
#8 18.88 Preparing to unpack .../102-libglib2.0-data_2.66.8-1_all.deb ...
#8 18.88 Unpacking libglib2.0-data (2.66.8-1) ...
#8 19.04 Selecting previously unselected package xorg-sgml-doctools.
#8 19.05 Preparing to unpack .../103-xorg-sgml-doctools_1%3a1.11-1.1_all.deb ...
#8 19.05 Unpacking xorg-sgml-doctools (1:1.11-1.1) ...
#8 19.08 Selecting previously unselected package x11proto-dev.
#8 19.08 Preparing to unpack .../104-x11proto-dev_2020.1-1_all.deb ...
#8 19.09 Unpacking x11proto-dev (2020.1-1) ...
#8 19.17 Selecting previously unselected package libice-dev:amd64.
#8 19.17 Preparing to unpack .../105-libice-dev_2%3a1.0.10-1_amd64.deb ...
#8 19.18 Unpacking libice-dev:amd64 (2:1.0.10-1) ...
#8 19.21 Selecting previously unselected package libicu67:amd64.
#8 19.22 Preparing to unpack .../106-libicu67_67.1-7_amd64.deb ...
#8 19.22 Unpacking libicu67:amd64 (67.1-7) ...
#8 20.14 Selecting previously unselected package libpthread-stubs0-dev:amd64.
#8 20.14 Preparing to unpack .../107-libpthread-stubs0-dev_0.4-1_amd64.deb ...
#8 20.16 Unpacking libpthread-stubs0-dev:amd64 (0.4-1) ...
#8 20.20 Selecting previously unselected package libsm-dev:amd64.
#8 20.20 Preparing to unpack .../108-libsm-dev_2%3a1.2.3-1_amd64.deb ...
#8 20.20 Unpacking libsm-dev:amd64 (2:1.2.3-1) ...
#8 20.24 Selecting previously unselected package libwayland-client0:amd64.
#8 20.24 Preparing to unpack .../109-libwayland-client0_1.18.0-2~exp1.1_amd64.deb ...
#8 20.24 Unpacking libwayland-client0:amd64 (1.18.0-2~exp1.1) ...
#8 20.27 Selecting previously unselected package libxau-dev:amd64.
#8 20.27 Preparing to unpack .../110-libxau-dev_1%3a1.0.9-1_amd64.deb ...
#8 20.28 Unpacking libxau-dev:amd64 (1:1.0.9-1) ...
#8 20.31 Selecting previously unselected package libxdmcp-dev:amd64.
#8 20.31 Preparing to unpack .../111-libxdmcp-dev_1%3a1.1.2-3_amd64.deb ...
#8 20.31 Unpacking libxdmcp-dev:amd64 (1:1.1.2-3) ...
#8 20.34 Selecting previously unselected package xtrans-dev.
#8 20.34 Preparing to unpack .../112-xtrans-dev_1.4.0-1_all.deb ...
#8 20.35 Unpacking xtrans-dev (1.4.0-1) ...
#8 20.38 Selecting previously unselected package libxcb1-dev:amd64.
#8 20.38 Preparing to unpack .../113-libxcb1-dev_1.14-3_amd64.deb ...
#8 20.39 Unpacking libxcb1-dev:amd64 (1.14-3) ...
#8 20.43 Selecting previously unselected package libx11-dev:amd64.
#8 20.43 Preparing to unpack .../114-libx11-dev_2%3a1.7.2-1+deb11u2_amd64.deb ...
#8 20.44 Unpacking libx11-dev:amd64 (2:1.7.2-1+deb11u2) ...
#8 20.54 Selecting previously unselected package libxcb-randr0:amd64.
#8 20.54 Preparing to unpack .../115-libxcb-randr0_1.14-3_amd64.deb ...
#8 20.54 Unpacking libxcb-randr0:amd64 (1.14-3) ...
#8 20.59 Selecting previously unselected package libxml2:amd64.
#8 20.59 Preparing to unpack .../116-libxml2_2.9.10+dfsg-6.7+deb11u4_amd64.deb ...
#8 20.59 Unpacking libxml2:amd64 (2.9.10+dfsg-6.7+deb11u4) ...
#8 20.69 Selecting previously unselected package libxt-dev:amd64.
#8 20.69 Preparing to unpack .../117-libxt-dev_1%3a1.2.0-1_amd64.deb ...
#8 20.69 Unpacking libxt-dev:amd64 (1:1.2.0-1) ...
#8 20.76 Selecting previously unselected package mesa-vulkan-drivers:amd64.
#8 20.77 Preparing to unpack .../118-mesa-vulkan-drivers_20.3.5-1_amd64.deb ...
#8 20.77 Unpacking mesa-vulkan-drivers:amd64 (20.3.5-1) ...
#8 21.19 Selecting previously unselected package openjdk-11-jre:amd64.
#8 21.19 Preparing to unpack .../119-openjdk-11-jre_11.0.20+8-1~deb11u1_amd64.deb ...
#8 21.20 Unpacking openjdk-11-jre:amd64 (11.0.20+8-1~deb11u1) ...
#8 21.25 Selecting previously unselected package openjdk-11-jdk-headless:amd64.
#8 21.25 Preparing to unpack .../120-openjdk-11-jdk-headless_11.0.20+8-1~deb11u1_amd64.deb ...
#8 21.26 Unpacking openjdk-11-jdk-headless:amd64 (11.0.20+8-1~deb11u1) ...
#8 26.38 Selecting previously unselected package openjdk-11-jdk:amd64.
#8 26.38 Preparing to unpack .../121-openjdk-11-jdk_11.0.20+8-1~deb11u1_amd64.deb ...
#8 26.38 Unpacking openjdk-11-jdk:amd64 (11.0.20+8-1~deb11u1) ...
#8 26.52 Selecting previously unselected package shared-mime-info.
#8 26.52 Preparing to unpack .../122-shared-mime-info_2.0-1_amd64.deb ...
#8 26.53 Unpacking shared-mime-info (2.0-1) ...
#8 26.64 Selecting previously unselected package xdg-user-dirs.
#8 26.64 Preparing to unpack .../123-xdg-user-dirs_0.17-2_amd64.deb ...
#8 26.65 Unpacking xdg-user-dirs (0.17-2) ...
#8 26.70 Setting up libexpat1:amd64 (2.2.10-2+deb11u5) ...
#8 26.71 Setting up libgraphite2-3:amd64 (1.3.14-1) ...
#8 26.72 Setting up liblcms2-2:amd64 (2.12~rc1-2) ...
#8 26.73 Setting up libpciaccess0:amd64 (0.16-1) ...
#8 26.74 Setting up libxau6:amd64 (1:1.0.9-1) ...
#8 26.76 Setting up libapparmor1:amd64 (2.13.6-10) ...
#8 26.77 Setting up java-common (0.72) ...
#8 26.78 Setting up libicu67:amd64 (67.1-7) ...
#8 26.79 Setting up xdg-user-dirs (0.17-2) ...
#8 26.82 Setting up libglib2.0-0:amd64 (2.66.8-1) ...
#8 26.85 No schema files found: doing nothing.
#8 26.85 Setting up libglvnd0:amd64 (1.3.2-1) ...
#8 26.87 Setting up libbrotli1:amd64 (1.0.9-2+b2) ...
#8 26.88 Setting up libsqlite3-0:amd64 (3.34.1-3) ...
#8 26.89 Setting up x11-common (1:7.7+22) ...
#8 27.03 debconf: unable to initialize frontend: Dialog
#8 27.03 debconf: (TERM is not set, so the dialog frontend is not usable.)
#8 27.03 debconf: falling back to frontend: Readline
#8 27.03 debconf: unable to initialize frontend: Readline
#8 27.03 debconf: (Can't locate Term/ReadLine.pm in @INC (you may need to install the Term::ReadLine module) (@INC contains: /etc/perl /usr/local/lib/x86_64-linux-gnu/perl/5.32.1 /usr/local/share/perl/5.32.1 /usr/lib/x86_64-linux-gnu/perl5/5.32 /usr/share/perl5 /usr/lib/x86_64-linux-gnu/perl-base /usr/lib/x86_64-linux-gnu/perl/5.32 /usr/share/perl/5.32 /usr/local/lib/site_perl) at /usr/share/perl5/Debconf/FrontEnd/Readline.pm line 7.)
#8 27.03 debconf: falling back to frontend: Teletype
#8 27.05 invoke-rc.d: could not determine current runlevel
#8 27.06 invoke-rc.d: policy-rc.d denied execution of start.
#8 27.06 Setting up libsensors-config (1:3.6.0-7) ...
#8 27.08 Setting up libpthread-stubs0-dev:amd64 (0.4-1) ...
#8 27.09 Setting up libasound2-data (1.2.4-1.1) ...
#8 27.10 Setting up xtrans-dev (1.4.0-1) ...
#8 27.11 Setting up libfontenc1:amd64 (1:1.1.4-1) ...
#8 27.12 Setting up libz3-4:amd64 (4.8.10-1) ...
#8 27.13 Setting up libglib2.0-data (2.66.8-1) ...
#8 27.14 Setting up libjpeg62-turbo:amd64 (1:2.0.6-4) ...
#8 27.15 Setting up libx11-data (2:1.7.2-1+deb11u2) ...
#8 27.16 Setting up libnspr4:amd64 (2:4.29-1) ...
#8 27.17 Setting up libavahi-common-data:amd64 (0.8-5+deb11u2) ...
#8 27.18 Setting up libdbus-1-3:amd64 (1.12.28-0+deb11u1) ...
#8 27.19 Setting up dbus (1.12.28-0+deb11u1) ...
#8 27.33 invoke-rc.d: could not determine current runlevel
#8 27.34 invoke-rc.d: policy-rc.d denied execution of start.
#8 27.34 Setting up libpng16-16:amd64 (1.6.37-3) ...
#8 27.35 Setting up fonts-dejavu-core (2.37-2) ...
#8 27.40 Setting up libpcsclite1:amd64 (1.9.1-1) ...
#8 27.41 Setting up libsensors5:amd64 (1:3.6.0-7) ...
#8 27.42 Setting up libglapi-mesa:amd64 (20.3.5-1) ...
#8 27.43 Setting up libvulkan1:amd64 (1.2.162.0-1) ...
#8 27.44 Setting up libgif7:amd64 (5.1.9-2) ...
#8 27.45 Setting up libatk1.0-data (2.36.0-2) ...
#8 27.47 Setting up fonts-dejavu-extra (2.37-2) ...
#8 27.48 Setting up libmd0:amd64 (1.0.3-3) ...
#8 27.48 Setting up alsa-topology-conf (1.2.4-1) ...
#8 27.49 Setting up sensible-utils (0.0.14) ...
#8 27.51 Setting up libxshmfence1:amd64 (1.3-1) ...
#8 27.52 Setting up libasound2:amd64 (1.2.4-1.1) ...
#8 27.53 Setting up xorg-sgml-doctools (1:1.11-1.1) ...
#8 27.54 Setting up libatk1.0-0:amd64 (2.36.0-2) ...
#8 27.55 Setting up libbsd0:amd64 (0.11.3-1+deb11u1) ...
#8 27.56 Setting up libdrm-common (2.4.104-1) ...
#8 27.57 Setting up libelf1:amd64 (0.183-1) ...
#8 27.58 Setting up libxml2:amd64 (2.9.10+dfsg-6.7+deb11u4) ...
#8 27.59 Setting up libwayland-client0:amd64 (1.18.0-2~exp1.1) ...
#8 27.60 Setting up x11proto-dev (2020.1-1) ...
#8 27.61 Setting up libice6:amd64 (2:1.0.10-1) ...
#8 27.62 Setting up libxdmcp6:amd64 (1:1.1.2-3) ...
#8 27.63 Setting up libxcb1:amd64 (1.14-3) ...
#8 27.64 Setting up libxcb-xfixes0:amd64 (1.14-3) ...
#8 27.65 Setting up libxau-dev:amd64 (1:1.0.9-1) ...
#8 27.66 Setting up libice-dev:amd64 (2:1.0.10-1) ...
#8 27.67 Setting up alsa-ucm-conf (1.2.4-2) ...
#8 27.68 Setting up libxcb-glx0:amd64 (1.14-3) ...
#8 27.69 Setting up libedit2:amd64 (3.1-20191231-2+b1) ...
#8 27.70 Setting up libxcb-shape0:amd64 (1.14-3) ...
#8 27.71 Setting up libavahi-common3:amd64 (0.8-5+deb11u2) ...
#8 27.72 Setting up libnss3:amd64 (2:3.61-1+deb11u3) ...
#8 27.74 Setting up libxcb-shm0:amd64 (1.14-3) ...
#8 27.75 Setting up libxcb-present0:amd64 (1.14-3) ...
#8 27.76 Setting up libxdmcp-dev:amd64 (1:1.1.2-3) ...
#8 27.77 Setting up libllvm11:amd64 (1:11.0.1-2) ...
#8 27.78 Setting up libfreetype6:amd64 (2.10.4+dfsg-1+deb11u1) ...
#8 27.79 Setting up libxcb-sync1:amd64 (1.14-3) ...
#8 27.80 Setting up shared-mime-info (2.0-1) ...
#8 29.65 Setting up ucf (3.0043) ...
#8 29.75 debconf: unable to initialize frontend: Dialog
#8 29.75 debconf: (TERM is not set, so the dialog frontend is not usable.)
#8 29.75 debconf: falling back to frontend: Readline
#8 29.75 debconf: unable to initialize frontend: Readline
#8 29.75 debconf: (Can't locate Term/ReadLine.pm in @INC (you may need to install the Term::ReadLine module) (@INC contains: /etc/perl /usr/local/lib/x86_64-linux-gnu/perl/5.32.1 /usr/local/share/perl/5.32.1 /usr/lib/x86_64-linux-gnu/perl5/5.32 /usr/share/perl5 /usr/lib/x86_64-linux-gnu/perl-base /usr/lib/x86_64-linux-gnu/perl/5.32 /usr/share/perl/5.32 /usr/local/lib/site_perl) at /usr/share/perl5/Debconf/FrontEnd/Readline.pm line 7.)
#8 29.75 debconf: falling back to frontend: Teletype
#8 29.78 Setting up libxcb-dri2-0:amd64 (1.14-3) ...
#8 29.80 Setting up libdrm2:amd64 (2.4.104-1) ...
#8 29.81 Setting up libxcb-randr0:amd64 (1.14-3) ...
#8 29.82 Setting up libx11-6:amd64 (2:1.7.2-1+deb11u2) ...
#8 29.84 Setting up libharfbuzz0b:amd64 (2.7.4-1) ...
#8 29.85 Setting up libxkbfile1:amd64 (1:1.1.0-1) ...
#8 29.86 Setting up libxcomposite1:amd64 (1:0.4.5-1) ...
#8 29.87 Setting up libsm6:amd64 (2:1.2.3-1) ...
#8 29.88 Setting up libavahi-client3:amd64 (0.8-5+deb11u2) ...
#8 29.89 Setting up libxmuu1:amd64 (2:1.1.2-2+b3) ...
#8 29.90 Setting up libdrm-amdgpu1:amd64 (2.4.104-1) ...
#8 29.91 Setting up libxcb-dri3-0:amd64 (1.14-3) ...
#8 29.92 Setting up libx11-xcb1:amd64 (2:1.7.2-1+deb11u2) ...
#8 29.93 Setting up libdrm-nouveau2:amd64 (2.4.104-1) ...
#8 29.95 Setting up libxdamage1:amd64 (1:1.1.5-2) ...
#8 29.96 Setting up libxcb1-dev:amd64 (1.14-3) ...
#8 29.97 Setting up libxpm4:amd64 (1:3.5.12-1.1+deb11u1) ...
#8 29.98 Setting up libxrender1:amd64 (1:0.9.10-1) ...
#8 29.99 Setting up libsm-dev:amd64 (2:1.2.3-1) ...
#8 30.00 Setting up libdrm-radeon1:amd64 (2.4.104-1) ...
#8 30.01 Setting up fontconfig-config (2.13.1-4.2) ...
#8 30.11 debconf: unable to initialize frontend: Dialog
#8 30.11 debconf: (TERM is not set, so the dialog frontend is not usable.)
#8 30.11 debconf: falling back to frontend: Readline
#8 30.11 debconf: unable to initialize frontend: Readline
#8 30.11 debconf: (Can't locate Term/ReadLine.pm in @INC (you may need to install the Term::ReadLine module) (@INC contains: /etc/perl /usr/local/lib/x86_64-linux-gnu/perl/5.32.1 /usr/local/share/perl/5.32.1 /usr/lib/x86_64-linux-gnu/perl5/5.32 /usr/share/perl5 /usr/lib/x86_64-linux-gnu/perl-base /usr/lib/x86_64-linux-gnu/perl/5.32 /usr/share/perl/5.32 /usr/local/lib/site_perl) at /usr/share/perl5/Debconf/FrontEnd/Readline.pm line 7.)
#8 30.11 debconf: falling back to frontend: Teletype
#8 30.29 Setting up libdrm-intel1:amd64 (2.4.104-1) ...
#8 30.30 Setting up libgl1-mesa-dri:amd64 (20.3.5-1) ...
#8 30.31 Setting up libx11-dev:amd64 (2:1.7.2-1+deb11u2) ...
#8 30.32 Setting up libxext6:amd64 (2:1.3.3-1.1) ...
#8 30.33 Setting up libatspi2.0-0:amd64 (2.38.0-4+deb11u1) ...
#8 30.34 Setting up libxxf86vm1:amd64 (1:1.1.4-1+b2) ...
#8 30.35 Setting up libatk-bridge2.0-0:amd64 (2.38.0-1) ...
#8 30.37 Setting up libxfixes3:amd64 (1:5.0.3-2) ...
#8 30.38 Setting up libxinerama1:amd64 (2:1.1.4-2) ...
#8 30.39 Setting up libxv1:amd64 (2:1.0.11-1) ...
#8 30.40 Setting up libxrandr2:amd64 (2:1.5.1-1) ...
#8 30.41 Setting up libxt6:amd64 (1:1.2.0-1) ...
#8 30.42 Setting up libcups2:amd64 (2.3.3op2-3+deb11u6) ...
#8 30.43 Setting up libfontconfig1:amd64 (2.13.1-4.2) ...
#8 30.44 Setting up mesa-vulkan-drivers:amd64 (20.3.5-1) ...
#8 30.45 Setting up libxft2:amd64 (2.3.2-2) ...
#8 30.46 Setting up libxmu6:amd64 (2:1.1.2-2+b3) ...
#8 30.47 Setting up libglx-mesa0:amd64 (20.3.5-1) ...
#8 30.48 Setting up libxi6:amd64 (2:1.7.10-1) ...
#8 30.49 Setting up libglx0:amd64 (1.3.2-1) ...
#8 30.50 Setting up libxtst6:amd64 (2:1.2.3-1) ...
#8 30.51 Setting up libxxf86dga1:amd64 (2:1.1.4-1+b3) ...
#8 30.52 Setting up libxaw7:amd64 (2:1.0.13-1.1) ...
#8 30.54 Setting up libgl1:amd64 (1.3.2-1) ...
#8 30.55 Setting up libxt-dev:amd64 (1:1.2.0-1) ...
#8 30.55 Setting up at-spi2-core (2.38.0-4+deb11u1) ...
#8 30.58 Setting up x11-utils (7.7+5) ...
#8 30.61 Setting up libatk-wrapper-java (0.38.0-2+deb11u1) ...
#8 30.63 Setting up libatk-wrapper-java-jni:amd64 (0.38.0-2+deb11u1) ...
#8 30.64 Setting up openjdk-11-jre-headless:amd64 (11.0.20+8-1~deb11u1) ...
#8 30.74 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/java to provide /usr/bin/java (java) in auto mode
#8 30.74 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/jjs to provide /usr/bin/jjs (jjs) in auto mode
#8 30.74 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/keytool to provide /usr/bin/keytool (keytool) in auto mode
#8 30.75 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/rmid to provide /usr/bin/rmid (rmid) in auto mode
#8 30.75 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/rmiregistry to provide /usr/bin/rmiregistry (rmiregistry) in auto mode
#8 30.76 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/pack200 to provide /usr/bin/pack200 (pack200) in auto mode
#8 30.76 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/unpack200 to provide /usr/bin/unpack200 (unpack200) in auto mode
#8 30.76 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/lib/jexec to provide /usr/bin/jexec (jexec) in auto mode
#8 31.31 Setting up openjdk-11-jre:amd64 (11.0.20+8-1~deb11u1) ...
#8 31.32 Setting up openjdk-11-jdk-headless:amd64 (11.0.20+8-1~deb11u1) ...
#8 31.34 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/jar to provide /usr/bin/jar (jar) in auto mode
#8 31.34 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/jarsigner to provide /usr/bin/jarsigner (jarsigner) in auto mode
#8 31.34 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/javac to provide /usr/bin/javac (javac) in auto mode
#8 31.35 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/javadoc to provide /usr/bin/javadoc (javadoc) in auto mode
#8 31.35 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/javap to provide /usr/bin/javap (javap) in auto mode
#8 31.36 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/jcmd to provide /usr/bin/jcmd (jcmd) in auto mode
#8 31.36 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/jdb to provide /usr/bin/jdb (jdb) in auto mode
#8 31.36 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/jdeprscan to provide /usr/bin/jdeprscan (jdeprscan) in auto mode
#8 31.36 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/jdeps to provide /usr/bin/jdeps (jdeps) in auto mode
#8 31.37 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/jfr to provide /usr/bin/jfr (jfr) in auto mode
#8 31.37 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/jimage to provide /usr/bin/jimage (jimage) in auto mode
#8 31.38 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/jinfo to provide /usr/bin/jinfo (jinfo) in auto mode
#8 31.38 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/jlink to provide /usr/bin/jlink (jlink) in auto mode
#8 31.38 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/jmap to provide /usr/bin/jmap (jmap) in auto mode
#8 31.39 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/jmod to provide /usr/bin/jmod (jmod) in auto mode
#8 31.39 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/jps to provide /usr/bin/jps (jps) in auto mode
#8 31.40 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/jrunscript to provide /usr/bin/jrunscript (jrunscript) in auto mode
#8 31.40 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/jshell to provide /usr/bin/jshell (jshell) in auto mode
#8 31.40 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/jstack to provide /usr/bin/jstack (jstack) in auto mode
#8 31.41 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/jstat to provide /usr/bin/jstat (jstat) in auto mode
#8 31.41 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/jstatd to provide /usr/bin/jstatd (jstatd) in auto mode
#8 31.41 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/rmic to provide /usr/bin/rmic (rmic) in auto mode
#8 31.42 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/serialver to provide /usr/bin/serialver (serialver) in auto mode
#8 31.42 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/jaotc to provide /usr/bin/jaotc (jaotc) in auto mode
#8 31.43 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/jhsdb to provide /usr/bin/jhsdb (jhsdb) in auto mode
#8 31.43 Setting up openjdk-11-jdk:amd64 (11.0.20+8-1~deb11u1) ...
#8 31.45 update-alternatives: using /usr/lib/jvm/java-11-openjdk-amd64/bin/jconsole to provide /usr/bin/jconsole (jconsole) in auto mode
#8 31.45 Setting up ca-certificates-java (20190909+deb11u1) ...
#8 31.48 head: cannot open '/etc/ssl/certs/java/cacerts' for reading: No such file or directory
#8 31.75 Adding debian:ACCVRAIZ1.pem
#8 31.75 Adding debian:AC_RAIZ_FNMT-RCM.pem
#8 31.76 Adding debian:Actalis_Authentication_Root_CA.pem
#8 31.76 Adding debian:AffirmTrust_Commercial.pem
#8 31.77 Adding debian:AffirmTrust_Networking.pem
#8 31.77 Adding debian:AffirmTrust_Premium.pem
#8 31.81 Adding debian:AffirmTrust_Premium_ECC.pem
#8 31.81 Adding debian:Amazon_Root_CA_1.pem
#8 31.81 Adding debian:Amazon_Root_CA_2.pem
#8 31.82 Adding debian:Amazon_Root_CA_3.pem
#8 31.82 Adding debian:Amazon_Root_CA_4.pem
#8 31.82 Adding debian:Atos_TrustedRoot_2011.pem
#8 31.83 Adding debian:Autoridad_de_Certificacion_Firmaprofesional_CIF_A62634068.pem
#8 31.83 Adding debian:Baltimore_CyberTrust_Root.pem
#8 31.84 Adding debian:Buypass_Class_2_Root_CA.pem
#8 31.85 Adding debian:Buypass_Class_3_Root_CA.pem
#8 31.85 Adding debian:CA_Disig_Root_R2.pem
#8 31.86 Adding debian:CFCA_EV_ROOT.pem
#8 31.87 Adding debian:COMODO_Certification_Authority.pem
#8 31.87 Adding debian:COMODO_ECC_Certification_Authority.pem
#8 31.87 Adding debian:COMODO_RSA_Certification_Authority.pem
#8 31.89 Adding debian:Certigna.pem
#8 31.89 Adding debian:Certigna_Root_CA.pem
#8 31.90 Adding debian:Certum_Trusted_Network_CA.pem
#8 31.90 Adding debian:Certum_Trusted_Network_CA_2.pem
#8 31.91 Adding debian:Chambers_of_Commerce_Root_-_2008.pem
#8 31.91 Adding debian:Comodo_AAA_Services_root.pem
#8 31.91 Adding debian:Cybertrust_Global_Root.pem
#8 31.91 Adding debian:D-TRUST_Root_Class_3_CA_2_2009.pem
#8 31.92 Adding debian:D-TRUST_Root_Class_3_CA_2_EV_2009.pem
#8 31.92 Adding debian:DST_Root_CA_X3.pem
#8 31.92 Adding debian:DigiCert_Assured_ID_Root_CA.pem
#8 31.92 Adding debian:DigiCert_Assured_ID_Root_G2.pem
#8 31.93 Adding debian:DigiCert_Assured_ID_Root_G3.pem
#8 31.93 Adding debian:DigiCert_Global_Root_CA.pem
#8 31.93 Adding debian:DigiCert_Global_Root_G2.pem
#8 31.93 Adding debian:DigiCert_Global_Root_G3.pem
#8 31.94 Adding debian:DigiCert_High_Assurance_EV_Root_CA.pem
#8 31.94 Adding debian:DigiCert_Trusted_Root_G4.pem
#8 31.95 Adding debian:E-Tugra_Certification_Authority.pem
#8 31.95 Adding debian:EC-ACC.pem
#8 31.96 Adding debian:Entrust.net_Premium_2048_Secure_Server_CA.pem
#8 31.96 Adding debian:Entrust_Root_Certification_Authority.pem
#8 31.97 Adding debian:Entrust_Root_Certification_Authority_-_EC1.pem
#8 31.97 Adding debian:Entrust_Root_Certification_Authority_-_G2.pem
#8 31.97 Adding debian:Entrust_Root_Certification_Authority_-_G4.pem
#8 31.98 Adding debian:GDCA_TrustAUTH_R5_ROOT.pem
#8 31.98 Adding debian:GTS_Root_R1.pem
#8 31.98 Adding debian:GTS_Root_R2.pem
#8 31.99 Adding debian:GTS_Root_R3.pem
#8 31.99 Adding debian:GTS_Root_R4.pem
#8 31.99 Adding debian:GeoTrust_Primary_Certification_Authority_-_G2.pem
#8 31.99 Adding debian:GlobalSign_ECC_Root_CA_-_R4.pem
#8 32.00 Adding debian:GlobalSign_ECC_Root_CA_-_R5.pem
#8 32.00 Adding debian:GlobalSign_Root_CA.pem
#8 32.00 Adding debian:GlobalSign_Root_CA_-_R2.pem
#8 32.01 Adding debian:GlobalSign_Root_CA_-_R3.pem
#8 32.01 Adding debian:GlobalSign_Root_CA_-_R6.pem
#8 32.02 Adding debian:Global_Chambersign_Root_-_2008.pem
#8 32.02 Adding debian:Go_Daddy_Class_2_CA.pem
#8 32.02 Adding debian:Go_Daddy_Root_Certificate_Authority_-_G2.pem
#8 32.03 Adding debian:Hellenic_Academic_and_Research_Institutions_ECC_RootCA_2015.pem
#8 32.03 Adding debian:Hellenic_Academic_and_Research_Institutions_RootCA_2011.pem
#8 32.03 Adding debian:Hellenic_Academic_and_Research_Institutions_RootCA_2015.pem
#8 32.04 Adding debian:Hongkong_Post_Root_CA_1.pem
#8 32.04 Adding debian:Hongkong_Post_Root_CA_3.pem
#8 32.05 Adding debian:ISRG_Root_X1.pem
#8 32.05 Adding debian:IdenTrust_Commercial_Root_CA_1.pem
#8 32.06 Adding debian:IdenTrust_Public_Sector_Root_CA_1.pem
#8 32.06 Adding debian:Izenpe.com.pem
#8 32.06 Adding debian:Microsec_e-Szigno_Root_CA_2009.pem
#8 32.07 Adding debian:Microsoft_ECC_Root_Certificate_Authority_2017.pem
#8 32.07 Adding debian:Microsoft_RSA_Root_Certificate_Authority_2017.pem
#8 32.07 Adding debian:NAVER_Global_Root_Certification_Authority.pem
#8 32.08 Adding debian:NetLock_Arany_=Class_Gold=_Főtanúsítvány.pem
#8 32.08 Adding debian:Network_Solutions_Certificate_Authority.pem
#8 32.09 Adding debian:OISTE_WISeKey_Global_Root_GB_CA.pem
#8 32.09 Adding debian:OISTE_WISeKey_Global_Root_GC_CA.pem
#8 32.10 Adding debian:QuoVadis_Root_CA.pem
#8 32.10 Adding debian:QuoVadis_Root_CA_1_G3.pem
#8 32.11 Adding debian:QuoVadis_Root_CA_2.pem
#8 32.12 Adding debian:QuoVadis_Root_CA_2_G3.pem
#8 32.12 Adding debian:QuoVadis_Root_CA_3.pem
#8 32.13 Adding debian:QuoVadis_Root_CA_3_G3.pem
#8 32.13 Adding debian:SSL.com_EV_Root_Certification_Authority_ECC.pem
#8 32.14 Adding debian:SSL.com_EV_Root_Certification_Authority_RSA_R2.pem
#8 32.15 Adding debian:SSL.com_Root_Certification_Authority_ECC.pem
#8 32.16 Adding debian:SSL.com_Root_Certification_Authority_RSA.pem
#8 32.16 Adding debian:SZAFIR_ROOT_CA2.pem
#8 32.17 Adding debian:SecureSign_RootCA11.pem
#8 32.18 Adding debian:SecureTrust_CA.pem
#8 32.18 Adding debian:Secure_Global_CA.pem
#8 32.19 Adding debian:Security_Communication_RootCA2.pem
#8 32.19 Adding debian:Security_Communication_Root_CA.pem
#8 32.21 Adding debian:Sonera_Class_2_Root_CA.pem
#8 32.22 Adding debian:Staat_der_Nederlanden_EV_Root_CA.pem
#8 32.22 Adding debian:Staat_der_Nederlanden_Root_CA_-_G3.pem
#8 32.23 Adding debian:Starfield_Class_2_CA.pem
#8 32.23 Adding debian:Starfield_Root_Certificate_Authority_-_G2.pem
#8 32.24 Adding debian:Starfield_Services_Root_Certificate_Authority_-_G2.pem
#8 32.24 Adding debian:SwissSign_Gold_CA_-_G2.pem
#8 32.25 Adding debian:SwissSign_Silver_CA_-_G2.pem
#8 32.26 Adding debian:T-TeleSec_GlobalRoot_Class_2.pem
#8 32.27 Adding debian:T-TeleSec_GlobalRoot_Class_3.pem
#8 32.27 Adding debian:TUBITAK_Kamu_SM_SSL_Kok_Sertifikasi_-_Surum_1.pem
#8 32.28 Adding debian:TWCA_Global_Root_CA.pem
#8 32.28 Adding debian:TWCA_Root_Certification_Authority.pem
#8 32.30 Adding debian:TeliaSonera_Root_CA_v1.pem
#8 32.30 Adding debian:TrustCor_ECA-1.pem
#8 32.31 Adding debian:TrustCor_RootCert_CA-1.pem
#8 32.31 Adding debian:TrustCor_RootCert_CA-2.pem
#8 32.32 Adding debian:Trustis_FPS_Root_CA.pem
#8 32.33 Adding debian:Trustwave_Global_Certification_Authority.pem
#8 32.33 Adding debian:Trustwave_Global_ECC_P256_Certification_Authority.pem
#8 32.33 Adding debian:Trustwave_Global_ECC_P384_Certification_Authority.pem
#8 32.34 Adding debian:UCA_Extended_Validation_Root.pem
#8 32.34 Adding debian:UCA_Global_G2_Root.pem
#8 32.34 Adding debian:USERTrust_ECC_Certification_Authority.pem
#8 32.35 Adding debian:USERTrust_RSA_Certification_Authority.pem
#8 32.35 Adding debian:VeriSign_Universal_Root_Certification_Authority.pem
#8 32.35 Adding debian:XRamp_Global_CA_Root.pem
#8 32.35 Adding debian:certSIGN_ROOT_CA.pem
#8 32.36 Adding debian:certSIGN_Root_CA_G2.pem
#8 32.36 Adding debian:e-Szigno_Root_CA_2017.pem
#8 32.36 Adding debian:ePKI_Root_Certification_Authority.pem
#8 32.36 Adding debian:emSign_ECC_Root_CA_-_C3.pem
#8 32.37 Adding debian:emSign_ECC_Root_CA_-_G3.pem
#8 32.37 Adding debian:emSign_Root_CA_-_C1.pem
#8 32.37 Adding debian:emSign_Root_CA_-_G1.pem
#8 32.40 done.
#8 32.41 Processing triggers for libc-bin (2.31-13+deb11u3) ...
#8 32.44 Processing triggers for ca-certificates (20210119) ...
#8 32.45 Updating certificates in /etc/ssl/certs...
#8 33.04 0 added, 0 removed; done.
#8 33.04 Running hooks in /etc/ca-certificates/update.d...
#8 33.09 
#8 33.37 done.
#8 33.37 done.
#8 DONE 35.6s

#9 [5/5] RUN javac /app/src/main/java/com/example/MyApp.java
#9 DONE 1.3s

#10 exporting to image
#10 exporting layers
#10 exporting layers 3.0s done
#10 writing image sha256:78f7f68a2a864509aa832d613a9a4c454b8aa946823feb54b0177b568aa57620 done
#10 naming to docker.io/library/myapp done
#10 DONE 3.0s
[Pipeline] }
[Pipeline] // script
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Tag and Push Docker Image)
[Pipeline] script
[Pipeline] {
[Pipeline] sh
+ docker tag myapp:latest harrierpanels/myapp:108
[Pipeline] sh
+ docker tag myapp:latest harrierpanels/myapp:latest
[Pipeline] withCredentials
Masking supported pattern matches of $DOCKER_HUB_USERNAME or $DOCKER_HUB_PASSWORD
[Pipeline] {
[Pipeline] sh
+ echo ****
+ docker login -u **** --password-stdin
WARNING! Your password will be stored unencrypted in /home/ec2-user/.docker/config.json.
Configure a credential helper to remove this warning. See
https://docs.docker.com/engine/reference/commandline/login/#credentials-store

Login Succeeded
[Pipeline] sh
Warning: A secret was passed to "sh" using Groovy String interpolation, which is insecure.
		 Affected argument(s) used the following variable(s): [DOCKER_HUB_USERNAME]
		 See https://jenkins.io/redirect/groovy-string-interpolation for details.
+ docker push ****/myapp:108
The push refers to repository [docker.io/****/myapp]
6387c33bb34f: Preparing
95f4235d6053: Preparing
991a4ed705ce: Preparing
db1cb5bd1f55: Preparing
d7802b8508af: Preparing
e3abdc2e9252: Preparing
eafe6e032dbd: Preparing
92a4e8a3140f: Preparing
e3abdc2e9252: Waiting
eafe6e032dbd: Waiting
92a4e8a3140f: Waiting
d7802b8508af: Layer already exists
e3abdc2e9252: Layer already exists
eafe6e032dbd: Layer already exists
92a4e8a3140f: Layer already exists
991a4ed705ce: Pushed
db1cb5bd1f55: Pushed
6387c33bb34f: Pushed
95f4235d6053: Pushed
108: digest: sha256:1ccfdeb2968a8dc88ae28e175f5f0fe1497a9ab0b5e5e94c058d9d7d8f2bf014 size: 1992
[Pipeline] }
[Pipeline] sh
+ docker push ****/myapp:latest
The push refers to repository [docker.io/****/myapp]
6387c33bb34f: Pushed
95f4235d6053: Pushed
991a4ed705ce: Pushed
db1cb5bd1f55: Pushed
d7802b8508af: Layer already exists
e3abdc2e9252: Layer already exists
eafe6e032dbd: Layer already exists
92a4e8a3140f: Layer already exists
sha256:1ccfdeb2968a8dc88ae28e175f5f0fe1497a9ab0b5e5e94c058d9d7d8f2bf014 size: 1992
[Pipeline] }
[Pipeline] // withCredentials
[Pipeline] }
[Pipeline] // script
[Pipeline] }
[Pipeline] // stage
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
[Pipeline] // node
[Pipeline] End of Pipeline
Finished: SUCCESS
```
## Create a CD pipeline:
1. Options at startup:
a. Environment name (dev/qa)
b. Version number (ie image tag, version or latest). This list should be dynamically pulled from Dockerhub
2. Deployment of the image on the selected Eivironment
3. Stage with healthcheck of deployed env (curl endpoint or smth else)
4. Notifications on MS Teams
#### Prerequisits
> - Java based HTTP Server [application](my-java-app/src/main/java/com/example/MyApp.java)<br>
<sup>A simple Java application that creates an HTTP server and listens on port 80. It responds to any incoming HTTP requests with an HTML page. The application uses the com.sun.net.httpserver package to create the server and handle incoming requests. The main method creates an instance of HttpServer, binds it to port 80, sets the executor to null, and starts the server. The MyHandler class implements the HttpHandler interface and overrides the handle method to send the response to the client. The response is an HTML page that includes an image, a message, and the current year. The handle method sends the response headers and body to the client using the HttpExchange object.</sup><br>
> Docker Hub repo <a href="https://hub.docker.com/repository/docker/harrierpanels/myapp">harrierpanels/myapp</a><br>
> Pre-deployed environments: <b>dev</b> - an AWS EKS cluster with a managed node group, <b>qa</b> - an AWS ECS cluster.<br>
> Pre-configured Webhook for MS Teams notifications by [Office 365 Connector Jenkins plugin](https://teams.microsoft.com/l/message/19:nrrgA8KPRE6A8DqMK1O4K9ADCOa3aqbPg4blQHV1BhY1@thread.tacv2/1698260828041?tenantId=b41b72d0-4e9f-4c26-8a69-f949f367c91d&groupId=8dab633d-3412-4b31-992e-3e7f036cdd1f&parentMessageId=1698260828041&teamName=EPAM%20DevOps%20UA%20Internal%20course&channelName=General&createdTime=1698260828041).
#### Project structure:
```
my-java-project/
├── Jenkinsfile-1
├── dev-deployment-service.yaml
├── healthcheck.sh
├── qa-deployment-service.json
└── my-java-app/
    └── src/
        └── main/
            └── java/
                └── com/
                    └── example/
                        └── MyApp.java
```
#### Fles:
> [Jenkinsfile-1](./Jenkinsfile-1) <br>
> <sup>The Jenkinsfile covers various aspects of a typical deployment process, including source code retrieval, Docker image version selection, environment choice, deployment, and health checks. It also handles notifications for different outcomes.</sup><br>

    Agent Configuration: The pipeline is configured to run on an agent labeled 'aws2023,' which is
    provisioned using the Amazon EC2 Plugin.

    Environment Variables: The pipeline defines environment variables, including AWS_CREDS, to store 
    credentials and other environment-specific values.

    Stages:
        Checkout: This stage checks out the source code from the version control system (e.g., Git).
        Fetch Docker Hub Tags: It fetches available tags for a Docker Hub repository and lets you choose 
	a version interactively.
        Select Environment: It allows you to select the deployment environment (either 'dev' or 'qa').
        Deploy to Environment: This stage deploys the application to the selected environment. It includes 
	different deployment processes for 'dev' and 'qa.'

    Health Check: This stage performs a health check on the deployed application. The healthcheck.sh script 
    is executed to ensure the application is responding as expected. It uses regular expressions to look for 
    either 'Harrier' or 'Maven' in the response.

    Post Section: In case of success or failure of the pipeline, the post section sends notifications using 
    the Office 365 Connector Jenkins plugin. Successful deployment triggers a success message, while a failed 
    deployment triggers a failure message.


> [dev-deployment-service.yaml](./dev-deployment-service.yaml) <br>
> <sup>The file is a Kubernetes configuration file used to deploy the myapp container to the dev environment.</sup><br>

    Namespace Configuration:
        It defines a Kubernetes namespace named 'dev-namespace' with the label 'deploy: dev.' Namespaces 
	are used to logically isolate resources within a Kubernetes cluster.

    Deployment Configuration:
        A Deployment named 'dev-deployment' is specified in the 'dev-namespace.'
        It requests two replicas of the 'myapp' container.
        The deployment manages pods based on the label 'deploy: dev.'
        The container uses the Docker image 'harrierpanels/myapp:latest.'

    Service Configuration:
        A Service named 'dev-service' is defined in the 'dev-namespace.'
        The service selects pods with the label 'deploy: dev.'
        It exposes port 80 and forwards traffic to the same port on the pods.
        The service type is set to 'LoadBalancer,' indicating that an external load balancer is used 
	to route incoming traffic to the pods.


> [healthcheck.sh](./healthcheck.sh) <br>
> <sup>The script is used in a pipeline for performing health checks on a deployed application. In the context of a deployment pipeline, this script is used to check whether the deployed application is responsive and functioning correctly. If the application doesn't respond within the specified retries, it exits with an error status, indicating that the application is not healthy. Otherwise, it reports that the application is up.</sup><br>

    retry Function:
        This function allows for retrying a given command until it succeeds or until a maximum number 
	of retries is reached.
        It takes as arguments a command or function to execute, a maximum retry count, and a sleep interval.
        It repeatedly executes the provided command or function.
        If the command succeeds (returns exit code 0), it breaks out of the loop.
        If the command fails, it waits for the specified sleep interval and then retries.
        If the maximum retry count is reached without success, it exits with an error code.

    healthcheck Function:
        This function performs a health check on a given URL.
        It takes two arguments: the URL to check and a pattern (regex) to search for in the response.
        It uses curl to retrieve the content from the URL and grep to search for the specified pattern.
        If the URL is unreachable (curl fails), it returns an error and prints "Host unreachable."
        If the pattern is found in the response, it returns a success and prints "Host UP."

    Health Check:
        The script invokes the retry function with the healthcheck function and the provided arguments.
        The provided arguments are the URL to check and the regex pattern to search for in the response.
        The health check is retried as specified by the retry function. 
	
 > [qa-deployment-service.json](./qa-deployment-service.json) <br>
 > <sup>The file is a Kubernetes configuration file used for deploying the myapp container to the QA environment,
 > which is an AWS ECS cluster. The file is a JSON definition specifying the parameters for the deployment. It is
 > a task definition for deploying the myapp container to an AWS ECS cluster in the QA environment. The file can be customized for different configurations and deployments as needed.</sup><br>

    Family: "qa-fargate"
        Specifies the family name for the task definition.

    Container Definitions (Array):

        Defines an array of container definitions, each for a specific container within the task.

        Container Definition:

            Name: "myapp"
                Specifies the name of the container as "myapp."

            Image: "harrierpanels/myapp:latest"
                Specifies the Docker image to use for this container, including its repository and tag.

            CPU: 0
                Sets the CPU units allocated to the container.

            Port Mappings (Array):

                Maps ports from the container to the host.

                Port Mapping:

                    Container Port: 80
                        Specifies that the container listens on port 80.

                    Host Port: 80
                        Specifies that the host should forward traffic from its port 80 to the container's port 80.

                    Protocol: "tcp"
                        Defines the communication protocol as TCP.

            Essential: true
                Indicates that this container is essential for the task to run successfully.

            Environment: []
                Defines an empty array for environment variables, which can be added to pass configuration to the container.

            Mount Points: []
                Specifies an empty array for mounting volumes in the container.

            Volumes From: []
                Defines an empty array for volumes that can be mounted from other containers.

    Execution Role Arn:
        Specifies the AWS Identity and Access Management (IAM) role used for the task execution.
        The placeholder "arn:aws:iam::aws_account_id:role/ecsTaskExecutionRole" should be replaced with the actual IAM role's ARN, which is used to grant permissions to tasks in the cluster.

    Network Mode: "awsvpc"
        Sets the network mode to "awsvpc," which is a mode that uses Amazon VPC for networking.

    CPU: "256"
        Specifies the amount of CPU units allocated to the task.

    Memory: "512"
        Defines the amount of memory allocated to the task.


> [MyApp.java](./my-java-app/src/main/java/com/example/MyApp.java)<br>
> <sup>MyApp.java creates a basic HTTP server that responds with a simple web page when accessed.</sup><br>

    It defines a package named com.example.

    The MyApp class contains the main method, which serves as the entry point of the program. Inside the main method:
        It creates an HttpServer instance.
        Binds the server to listen on port 80.
        Associates a custom MyHandler with the root context ("/").
        Sets the executor to null.
        Starts the server and prints a message indicating that the server has started.

    The MyHandler class is a nested static class that implements the HttpHandler interface, responsible for handling incoming HTTP requests. Inside the handle method:
        It constructs an HTML response as a string.
        Sends an HTTP 200 (OK) response with the response length.
        Writes the response to the output stream and closes it.

    The HTML response includes a simple webpage:
        It displays an image and a message.
        The image is sourced from a URL.
        There's a link to an external webpage.
        The page is generated with Java and includes the current year using Calendar.

```
Started by upstream project "test-aws" build number 10
originally caused by:
 Started by user a
Obtained Jenkinsfile-1 from git https://github.com/HarrierPanels/my-java-project.git
[Pipeline] Start of Pipeline
[Pipeline] node
Still waiting to schedule task
All nodes of label ‘aws2023’ are offline
Running on EC2 (Amazon-EC2) - aws-ec2-agent1 (i-00595c8913d0c9cdd) in /home/ec2-user/jenkins/workspace/cd-pipeline
[Pipeline] {
[Pipeline] stage
[Pipeline] { (Declarative: Checkout SCM)
[Pipeline] checkout
Selected Git installation does not exist. Using Default
The recommended git tool is: NONE
using credential github-creds-user-token
Cloning the remote Git repository
Cloning repository https://github.com/HarrierPanels/my-java-project.git
 > git init /home/ec2-user/jenkins/workspace/cd-pipeline # timeout=10
Fetching upstream changes from https://github.com/HarrierPanels/my-java-project.git
 > git --version # timeout=10
 > git --version # 'git version 2.40.1'
using GIT_ASKPASS to set credentials github-creds-user-token
 > git fetch --tags --force --progress -- https://github.com/HarrierPanels/my-java-project.git +refs/heads/*:refs/remotes/origin/* # timeout=10
 > git config remote.origin.url https://github.com/HarrierPanels/my-java-project.git # timeout=10
 > git config --add remote.origin.fetch +refs/heads/*:refs/remotes/origin/* # timeout=10
Avoid second fetch
Checking out Revision 73cf457b5384706a98691a8c52a016bdc7e89301 (refs/remotes/origin/main)
 > git rev-parse refs/remotes/origin/main^{commit} # timeout=10
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 73cf457b5384706a98691a8c52a016bdc7e89301 # timeout=10
 > git branch -a -v --no-abbrev # timeout=10
 > git checkout -b main 73cf457b5384706a98691a8c52a016bdc7e89301 # timeout=10
Commit message: "CD Pipeline"
 > git rev-list --no-walk 4dfa8241059aeb1b3faa31c1c4b4ce9fb8cf8abc # timeout=10
Cleaning workspace
[Pipeline] }
[Pipeline] // stage
[Pipeline] withEnv
[Pipeline] {
[Pipeline] withCredentials
WARNING: Unknown parameter(s) found for class type 'com.cloudbees.jenkins.plugins.awscredentials.AmazonWebServicesCredentialsBinding': keyIdVariable,secretVariable
Masking supported pattern matches of $AWS_ACCESS_KEY_ID or $AWS_SECRET_ACCESS_KEY
[Pipeline] {
[Pipeline] stage
[Pipeline] { (Checkout)
[Pipeline] checkout
Selected Git installation does not exist. Using Default
The recommended git tool is: NONE
 > git rev-parse --verify HEAD # timeout=10
Resetting working tree
 > git reset --hard # timeout=10
 > git clean -fdx # timeout=10
using credential github-creds-user-token
Fetching changes from the remote Git repository
Cleaning workspace
 > git rev-parse --resolve-git-dir /home/ec2-user/jenkins/workspace/cd-pipeline/.git # timeout=10
 > git config remote.origin.url https://github.com/HarrierPanels/my-java-project.git # timeout=10
 > git rev-parse --verify HEAD # timeout=10
Resetting working tree
 > git reset --hard # timeout=10
 > git clean -fdx # timeout=10
Fetching upstream changes from https://github.com/HarrierPanels/my-java-project.git
 > git --version # timeout=10
 > git --version # 'git version 2.40.1'
using GIT_ASKPASS to set credentials github-creds-user-token
 > git fetch --tags --force --progress -- https://github.com/HarrierPanels/my-java-project.git +refs/heads/*:refs/remotes/origin/* # timeout=10
Checking out Revision 73cf457b5384706a98691a8c52a016bdc7e89301 (refs/remotes/origin/main)
Commit message: "CD Pipeline"
Cleaning workspace
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Fetch Docker Hub Tags)
 > git rev-parse refs/remotes/origin/main^{commit} # timeout=10
 > git config core.sparsecheckout # timeout=10
 > git checkout -f 73cf457b5384706a98691a8c52a016bdc7e89301 # timeout=10
 > git branch -a -v --no-abbrev # timeout=10
 > git branch -D main # timeout=10
 > git checkout -b main 73cf457b5384706a98691a8c52a016bdc7e89301 # timeout=10
 > git rev-parse --verify HEAD # timeout=10
Resetting working tree
 > git reset --hard # timeout=10
 > git clean -fdx # timeout=10
[Pipeline] script
[Pipeline] {
[Pipeline] sh
+ curl -s https://hub.docker.com/v2/repositories/harrierpanels/myapp/tags
[Pipeline] input
Input requested
Approved by a
[Pipeline] }
[Pipeline] // script
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Select Environment)
[Pipeline] script
[Pipeline] {
[Pipeline] input
Input requested
Approved by a
[Pipeline] }
[Pipeline] // script
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Deploy to Environment)
[Pipeline] script
[Pipeline] {
[Pipeline] echo
latest chosen for dev:
[Pipeline] sh
+ aws eks update-kubeconfig --region us-east-1 --name dev-cluster
Added new context arn:aws:eks:us-east-1:449040158477:cluster/dev-cluster to /home/ec2-user/.kube/config
[Pipeline] sh
+ sed -i 's/harrierpanels\/myapp:latest/harrierpanels\/myapp:latest/g' dev-deployment-service.yaml
[Pipeline] sh
+ kubectl apply -f dev-deployment-service.yaml
namespace/dev-namespace created
deployment.apps/dev-deployment created
service/dev-service created
[Pipeline] echo
Waiting for the LoadBalancer to be assigned an external IP...
[Pipeline] sh
+ kubectl get svc dev-service -n dev-namespace -o 'jsonpath={.status.loadBalancer.ingress[0].hostname}'
[Pipeline] sleep
Sleeping for 5 sec
[Pipeline] sh
+ kubectl get svc dev-service -n dev-namespace -o 'jsonpath={.status.loadBalancer.ingress[0].hostname}'
[Pipeline] }
[Pipeline] // script
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Health Check)
[Pipeline] timeout
Timeout set to expire in 3 min 0 sec
[Pipeline] {
[Pipeline] script
[Pipeline] {
[Pipeline] sh
+ chmod +x healthcheck.sh
[Pipeline] sh
+ ./healthcheck.sh a2ae3efcdad444b3d9836e10045fb2a0-1187648468.us-east-1.elb.amazonaws.com 'Harrier|Maven'
Host unreachable
Command failed, retrying after 5 seconds... 1/60
Host unreachable
Command failed, retrying after 5 seconds... 2/60
Host unreachable
Command failed, retrying after 5 seconds... 3/60
Host unreachable
Command failed, retrying after 5 seconds... 4/60
Host unreachable
Command failed, retrying after 5 seconds... 5/60
Host unreachable
Command failed, retrying after 5 seconds... 6/60
Host unreachable
Command failed, retrying after 5 seconds... 7/60
Host unreachable
Command failed, retrying after 5 seconds... 8/60
Host unreachable
Command failed, retrying after 5 seconds... 9/60
Host unreachable
Command failed, retrying after 5 seconds... 10/60
Host unreachable
Command failed, retrying after 5 seconds... 11/60
Host unreachable
Command failed, retrying after 5 seconds... 12/60
Host unreachable
Command failed, retrying after 5 seconds... 13/60
Host UP
[Pipeline] echo
Health check passed
[Pipeline] }
[Pipeline] // script
[Pipeline] }
[Pipeline] // timeout
[Pipeline] }
[Pipeline] // stage
[Pipeline] stage
[Pipeline] { (Declarative: Post Actions)
[Pipeline] office365ConnectorSend
[Pipeline] }
[Pipeline] // stage
[Pipeline] }
[Pipeline] // withCredentials
[Pipeline] }
[Pipeline] // withEnv
[Pipeline] }
[Pipeline] // node
[Pipeline] End of Pipeline
Finished: SUCCESS
```
```
Step	Arguments			Status
Start of Pipeline - (1 min 56 sec in block)			
	
node - (1 min 54 sec in block)	aws2023		
node block - (1 min 54 sec in block)			
	
stage - (5.7 sec in block)	Declarative: Checkout SCM		
stage block (Declarative: Checkout SCM) - (5.3 sec in block)			
	
checkout - (5.1 sec in self)			
withEnv - (1 min 48 sec in block)	GIT_BRANCH, GIT_COMMIT, GIT_LOCAL_BRANCH, GIT_PREVIOUS_COMMIT, GIT_PREVIOUS_SUCCESSFUL_COMMIT, GIT_URL		
withEnv block - (1 min 48 sec in block)			
	
withCredentials - (1 min 48 sec in block)			
withCredentials block - (1 min 48 sec in block)			
	
stage - (3.7 sec in block)	Checkout		
stage block (Checkout) - (3.6 sec in block)			
	
checkout - (3.4 sec in self)			
stage - (10 sec in block)	Fetch Docker Hub Tags		
stage block (Fetch Docker Hub Tags) - (10 sec in block)			
	
script - (10 sec in block)			
script block - (9.9 sec in block)			
	
sh - (3.4 sec in self)	curl -s https://hub.docker.com/v2/repositories/harrierpanels/myapp/tags		
input - (6.3 sec in self)			
stage - (16 sec in block)	Select Environment		
stage block (Select Environment) - (16 sec in block)			
	
script - (16 sec in block)			
script block - (16 sec in block)			
	
input - (15 sec in self)			
stage - (1 min 9 sec in block)	Deploy to Environment		
stage block (Deploy to Environment) - (1 min 9 sec in block)			
	
script - (1 min 8 sec in block)			
script block - (1 min 8 sec in block)			
	
echo - (26 ms in self)	109 chosen for qa:		
sh - (3.1 sec in self)	aws iam get-role --role-name ecsTaskExecutionRole		
sh - (4.2 sec in self)	aws iam create-role --role-name ecsTaskExecutionRole --assume-role-policy-document '{"Version":"2012-10-17","Statement":[{"Effect":"Allow","Principal":{"Service":"ecs-tasks.amazonaws.com"},"Action":"sts:AssumeRole"}]}'		
sh - (4.1 sec in self)	aws iam put-role-policy --role-name ecsTaskExecutionRole --policy-name ECSTaskExecutionRolePolicy --policy-document '{"Version":"2012-10-17","Statement":[{"Sid":"ECSTaskExecutionRolePermissions","Effect":"Allow","Action":["ecr:GetAuthorizationToken","ecr:BatchCheckLayerAvailability","ecr:GetDownloadUrlForLayer","ecr:BatchGetImage","ecs:RunTask","ecs:StopTask","logs:CreateLogGroup","logs:CreateLogStream","logs:PutLogEvents"],"Resource":"*"}]}'		
sh - (3.2 sec in self)	aws sts get-caller-identity --query Account --output text | tr -d '\n'		
sh - (3 sec in self)	sed -i 's/aws_account_id/449040158477/g' qa-deployment-service.json		
sh - (3 sec in self)	sed -i 's/harrierpanels\/myapp:latest/harrierpanels\/myapp:109/g' qa-deployment-service.json		
sh - (3 sec in self)	aws ecs register-task-definition --cli-input-json file://qa-deployment-service.json		
sh - (3.3 sec in self)	aws ec2 describe-security-groups --filters "Name=group-name,Values=Secjen" --query "SecurityGroups[0].GroupId" --output text		
sh - (4.5 sec in self)	aws ec2 describe-subnets --filters "Name=vpc-id,Values=$(aws ec2 describe-vpcs --filters "Name=isDefault,Values=true" --query "Vpcs[0].VpcId" --output text)" --query "Subnets[0].SubnetId" --output text		
sh - (3 sec in self)	aws ecs create-service --cluster qa-cluster --service-name qa-service --task-definition qa-fargate --desired-count 1 --launch-type 'FARGATE' --network-configuration 'awsvpcConfiguration={subnets=[subnet-05aad6e4e6ab6159b],securityGroups=[sg-07efc7f47b9444016],assignPublicIp=ENABLED}'		
sh - (3.2 sec in self)	aws ecs list-tasks --cluster qa-cluster --service qa-service --query 'taskArns[0]' --output text | tr -d '\n'		
sleep - (10 sec in self)	10		
sh - (3.6 sec in self)	aws ecs list-tasks --cluster qa-cluster --service qa-service --query 'taskArns[0]' --output text | tr -d '\n'		
sh - (3.3 sec in self)	aws ecs describe-tasks --cluster qa-cluster --tasks arn:aws:ecs:us-east-1:449040158477:task/qa-cluster/b2a64f5120fc4d81a7b8f803bfb7d470 --query 'tasks[0].attachments[0].details[1].value' --output text | tr -d '\n'		
sh - (3.3 sec in self)	aws ecs describe-services --cluster qa-cluster --services qa-service --query 'services[0].networkConfiguration.awsvpcConfiguration.assignPublicIp'		
sh - (3.3 sec in self)	aws ecs describe-services --cluster qa-cluster --services qa-service --query 'services[0].networkConfiguration.awsvpcConfiguration.subnets[0]' --output text | tr -d '\n'		
sh - (3.3 sec in self)	aws ec2 describe-network-interfaces --network-interface-ids eni-012d1b4b15dc02308 --query 'NetworkInterfaces[0].PrivateIpAddress' --output text | tr -d '\n'		
sh - (3.3 sec in self)	aws ec2 describe-network-interfaces --filters Name=subnet-id,Values=subnet-05aad6e4e6ab6159b Name=private-ip-address,Values=172.31.22.31 --query 'NetworkInterfaces[0].Association.PublicIp' --output text | tr -d '\n'		
stage - (7.6 sec in block)	Health Check		
stage block (Health Check) - (7.5 sec in block)			
	
timeout - (7.3 sec in block)			
timeout block - (7.2 sec in block)			
	
script - (7 sec in block)			
script block - (6.9 sec in block)			
	
sh - (3.3 sec in self)	chmod +x healthcheck.sh		
sh - (3.5 sec in self)	./healthcheck.sh 54.236.6.254 'Harrier|Maven'		
echo - (16 ms in self)	Health check passed		
stage - (0.45 sec in block)	Declarative: Post Actions		
stage block (Declarative: Post Actions) - (0.25 sec in block)			
	
office365ConnectorSend - (0.12 sec in self)	
```
```
[ec2-user@ip-172-31-47-155 ~]$ eksctl get cluster dev-cluster
NAME            VERSION STATUS  CREATED                 VPC                     SUBNETS
                                                                                SECURITYGROUPS    PROVIDER
dev-cluster     1.28    ACTIVE  2023-11-02T18:48:45Z    vpc-0b9d5cac7a83856ce   subnet-0520c4825a4947dc9,subnet-0561ad10a233e9324,subnet-0d909b9ebdc6adaa9,subnet-0f33771ceea46831f
                EKS

[ec2-user@ip-172-31-47-155 ~]$ kubectl cluster-info
Kubernetes control plane is running at https://37D7D5AC41E16C94F043D8FFEE3ECCE2.gr7.us-east-1.eks.amazonaws.com
CoreDNS is running at https://37D7D5AC41E16C94F043D8FFEE3ECCE2.gr7.us-east-1.eks.amazonaws.com/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy


[ec2-user@ip-172-31-47-155 ~]$ kubectl get svc -n dev-namespace
NAME          TYPE           CLUSTER-IP      EXTERNAL-IP                                                               PORT(S)        AGE
dev-service   LoadBalancer   10.100.252.47   a2ae3efcdad444b3d9836e10045fb2a0-1187648468.us-east-1.elb.amazonaws.com   80:30655/TCP   12m

[ec2-user@ip-172-31-47-155 ~]$ curl -Ls a2ae3efcdad444b3d9836e10045fb2a0-1187648468.us-east-1.elb.amazonaws.com
<html><head><title>Welcome to Harrier Panels Page!</title></head><body><div style="text-align:center"><img src="https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEid812UJHFiQs8-SCK2BoakeB1zGxXmqfVk1sfHzudQhd5wjnaoUbePaL-uR0Bqqx4sIW6grWYEk2QuhUjefeynN2wSIsLOo0kQI0MTfDn60VB84CnN6KPo-A98s7vzyg/s220/hp.png"><br>Welcome to <a href="https://aviasimulator.blogspot.com">Harrier Panels</a> Page!<br>Powered by Java &copy; Harrier Panels 2023</div></body></html>

[ec2-user@ip-172-31-47-155 ~]$ kubectl get pods -n dev-namespace
NAME                              READY   STATUS    RESTARTS   AGE
dev-deployment-5775796674-22mtf   1/1     Running   0          15m
dev-deployment-5775796674-6wrf4   1/1     Running   0          15m

[ec2-user@ip-172-31-47-155 ~]$ kubectl get nodes -n dev-namespace
NAME                              STATUS   ROLES    AGE   VERSION
ip-192-168-159-193.ec2.internal   Ready    <none>   23m   v1.28.2-eks-a5df82a
ip-192-168-199-206.ec2.internal   Ready    <none>   22m   v1.28.2-eks-a5df82a

[ec2-user@ip-172-31-47-155 ~]$ kubectl describe deployments -n dev-namespace
Name:                   dev-deployment
Namespace:              dev-namespace
CreationTimestamp:      Thu, 02 Nov 2023 19:07:59 +0000
Labels:                 <none>
Annotations:            deployment.kubernetes.io/revision: 1
Selector:               deploy=dev
Replicas:               2 desired | 2 updated | 2 total | 2 available | 0 unavailable
StrategyType:           RollingUpdate
MinReadySeconds:        0
RollingUpdateStrategy:  25% max unavailable, 25% max surge
Pod Template:
  Labels:  deploy=dev
  Containers:
   myapp:
    Image:        harrierpanels/myapp:latest
    Port:         <none>
    Host Port:    <none>
    Environment:  <none>
    Mounts:       <none>
  Volumes:        <none>
Conditions:
  Type           Status  Reason
  ----           ------  ------
  Available      True    MinimumReplicasAvailable
  Progressing    True    NewReplicaSetAvailable
OldReplicaSets:  <none>
NewReplicaSet:   dev-deployment-5775796674 (2/2 replicas created)
Events:
  Type    Reason             Age   From                   Message
  ----    ------             ----  ----                   -------
  Normal  ScalingReplicaSet  18m   deployment-controller  Scaled up replica set dev-deployment-5775796674 to 2
  
```
