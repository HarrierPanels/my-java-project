pipeline {
    agent { label 'local1' }
    stages {
        stage('Check Git Commit') {
            steps {
                script {
                    // Get the commit ID of the current HEAD
                    def commitId = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()

                    // Get the number of parents for the commit
                    def parentCount = sh(returnStdout: true, script: "git rev-list --parents -n 1 ${commitId} | awk '{print NF-1}'").trim().toInteger()

                    if (parentCount > 1) {
                        echo "Commit ${commitId} is a merge commit"
                    } else {
                        error("Commit ${commitId} is not a merge commit")
                    }
                }
            }
        }
        // Add more stages here
    }
}
