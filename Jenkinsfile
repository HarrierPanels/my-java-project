pipeline {
    agent {
        label 'aws2023'
    }
    stages {
        stage("Docker login") {
            steps {
                withCredentials([string(credentialsId: 'DockerHubPwd', variable: 'dockerpwd')]) {
                    sh "docker login -u username -p ${dockerpwd}"
                }
            }
        }
    }
}
