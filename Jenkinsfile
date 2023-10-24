pipeline {
    agent {
        label 'aws2023'
    }

    environment {
        DOCKER_IMAGE_NAME = 'myapp'
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
                    catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                        // Pull the SonarQube Docker image and start the container
                        docker.image('sonarqube:latest').withRun('-p 9000:9000 -p 9092:9092') { sonarqubeContainer ->
                            waitForSonarQube()
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
                    // Build your Docker image
                    sh "docker build -t $DOCKER_IMAGE_NAME my-java-app/."
                }
            }
        }

        stage('Tag and Push Docker Image') {
            steps {
                script {
                    def buildVersion = env.BUILD_NUMBER ?: 'latest'
                    def dockerHubRepo = 'harrierpanels/docker'

                    // Tag the Docker image
                    sh "docker tag $DOCKER_IMAGE_NAME:latest $DOCKER_IMAGE_NAME:$buildVersion"

                    // Log in to Docker Hub using credentials
                    withCredentials([usernamePassword(credentialsId: 'DockerHubCredentials', usernameVariable: 'DOCKER_HUB_USERNAME', passwordVariable: 'DOCKER_HUB_PASSWORD')]) {
                        sh "echo \$DOCKER_HUB_PASSWORD | docker login -u \$DOCKER_HUB_USERNAME --password-stdin"

                        // Push the Docker image to Docker Hub
                        sh "docker push $dockerHubRepo:$buildVersion"
                    }
                }
            }
        }
    }

    post {
        always {
            // Clean up Docker containers
            sh 'docker stop sonarqube'
            sh 'docker rm sonarqube'
        }
    }
}

def waitForSonarQube() {
    // Wait for SonarQube to be up and running
    sh 'while ! curl -s -f -o /dev/null http://localhost:9000; do sleep 5; done'
}
