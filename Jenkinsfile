pipeline {
    agent 'aws2023'

    environment {
        DOCKER_HUB_USERNAME = credentials('DockerHubCredentials').username
        DOCKER_HUB_PASSWORD = credentials('DockerHubCredentials').password
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
                    docker.image('sonarqube:latest').withRun('--name sonarqube -p 9000:9000 -p 9092:9092') { c ->
                        // Wait for SonarQube to be up and running
                        sh 'while ! curl -s -f -o /dev/null http://localhost:9000; do sleep 5; done'
                        
                        // Perform the analysis
                        sh 'mvn sonar:sonar'
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build your Docker image as needed
                    sh 'docker build -t myapp .'
                }
            }
        }

        stage('Tag and Push Docker Image') {
            steps {
                script {
                    // Tag the Docker image
                    sh 'docker tag myapp:latest myapp:build_version'

                    // Log in to Docker Hub using the Docker Hub credentials
                    withCredentials([usernamePassword(credentialsId: DOCKER_HUB_CREDENTIALS, usernameVariable: 'DOCKER_HUB_USERNAME', passwordVariable: 'DOCKER_HUB_PASSWORD')]) {
                        sh "docker login -u $DOCKER_HUB_USERNAME -p $DOCKER_HUB_PASSWORD"
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
