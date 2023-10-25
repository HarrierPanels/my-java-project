pipeline {
    agent { label 'local1' }
    stages {
        stage('Check Git Reflog') {
            steps {
                script {
                    // Get the commit ID of the last commit
                    def commitId = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()

                    // Check if it's a merge commit (has more than one parent)
                    def isMergeCommit = sh(script: "git cat-file -p ${commitId} | grep '^parent ' | wc -l", returnStatus: true) > 1

                    if (isMergeCommit) {
                        echo "Commit ${commitId} is a merge commit"
                    } else {
                        error("Commit ${commitId} is not a merge commit")
                    }
                }
            }
        }
        /// Add more stages here
    }
}
