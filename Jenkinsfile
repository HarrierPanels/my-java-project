pipeline {

    agent {
        label 'aws2023'
    }

    environment {
        // Define an environment variable to store the DockerHub password
        DOCKERHUB_PASSWORD = credentials('DockerHubPwd')
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout your source code from your version control system
                checkout scm
            }
        }

        //

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
                    // Define Docker Hub credentials using a specific credential ID
                    // Log in to Docker Hub using the Docker Hub credentials
                    withCredentials([usernamePassword(credentialsId: 'DockerHubCredentials', usernameVariable: 'DOCKER_HUB_USERNAME', passwordVariable: 'DOCKER_HUB_PASSWORD')]) {
                    }

                    // Determine the build version based on the Jenkins build environment
                    def buildVersion = env.BUILD_NUMBER ?: 'latest'
                    // Tag and push the Docker image
                    sh "docker tag myapp:latest harrierpanels/myapp:$buildVersion"
                    sh "docker tag myapp:latest harrierpanels/myapp:latest"
                    sh '''
                        echo ${DOCKERHUB_PASSWORD} | docker login -u harrierpanels --password-stdin
                    '''
                    sh "docker push harrierpanels/myapp:$buildVersion"

                }
            }
        }
    }
}
