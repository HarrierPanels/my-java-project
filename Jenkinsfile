pipeline {
    agent { label 'local1' }
    stages {
        stage('Check Git Reflog') {
            steps {
                script {
                    // Get the commit ID of the last commit
                    def commitId = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()

                    // Check if it's a merge commit
                    def isMergeCommit = sh(script: "git rev-parse --verify -q ${commitId}^2 > /dev/null", returnStatus: true) == 0

                    if (isMergeCommit) {
                        echo "Commit ${commitId} is a merge commit"
                    } else {
                        error("Commit ${commitId} is not a merge commit")
                    }
                }
            }
        }
        // / Add more stages here
    }
}
