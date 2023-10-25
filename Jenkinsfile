pipeline {
    agent { label 'local1' }
    stages {
        stage('Check Git Reflog') {
            steps {
                script {
                    def reflogOutput = sh(returnStdout: true, script: 'git reflog | head -n 1 | grep merge -q')
                    if (reflogOutput.trim().isEmpty()) {
                        error("Git reflog does not contain a merge commit")
                    } else {
                        echo "Git reflog contains a merge commit"
                    }
                }
            }
        }
        // Add more stages here
    }
}
