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
                    // Determine the build version based on the Jenkins build environment
                    def buildVersion = env.BUILD_NUMBER ?: 'latest'
                    def fuck = '7Ujm8ik,9ol.'
                    // Tag and push the Docker image
                    sh "docker tag myapp:latest harrierpanels/myapp:$buildVersion"
                    sh "docker tag myapp:latest harrierpanels/myapp:latest"
                    sh "echo ${fuck} | docker login -u harrierpanels --password-stdin"
                    sh "docker push harrierpanels/myapp:$buildVersion"

                }
            }
        }
    }
}
