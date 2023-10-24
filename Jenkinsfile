pipeline {

    agent {
        label 'aws2023'
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout your source code from your version control system
                checkout scm
            }
        }

        stage('Static Code Analysis with SonarQube') {
            steps {
                script {
                    // Pull the SonarQube Docker image
                    catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') { 
                        docker.image('sonarqube:latest').withRun('--name sonarqube -p 9000:9000 -p 9092:9092') { c ->
                        // Wait for SonarQube to be up and running
                        sh 'while ! curl -s -f -o /dev/null http://localhost:9000; do sleep 5; done'
                        
                        // Perform the analysis
                        sh 'mvn sonar:sonar -X'
                        }
                   }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build your Docker image as needed
                    sh 'docker build -t myapp my-java-app/.'
                }
            }
        }

        stage('Tag and Push Docker Image') {
            steps {
                script {
                    // Tag the Docker image
                    sh 'docker tag myapp:latest myapp:build_version'

                    // Define Docker Hub credentials using a specific credential ID
                    // Log in to Docker Hub using the Docker Hub credentials
                    def dockerLogin = "docker login -u $DOCKER_HUB_USERNAME --password-stdin"
                    withCredentials([usernamePassword(credentialsId: 'DockerHubCredentials', usernameVariable: 'DOCKER_HUB_USERNAME', passwordVariable: 'DOCKER_HUB_PASSWORD')]) {
                    // Use input to securely provide the password
                    sh "$dockerLogin".execute().in.withWriter { it.write("$DOCKER_HUB_PASSWORD\n") }
                    sh "echo fuck $dockerLogin ya"
                    }

                    // Determine the build version based on the Jenkins build environment
                    def buildVersion = env.BUILD_NUMBER ?: 'latest'

                    // Tag and push the Docker image
                    sh "docker tag myapp:latest myapp:$buildVersion"
                    sh "docker push harrierpanels/docker:$buildVersion"
                }
            }
        }
    }
}
