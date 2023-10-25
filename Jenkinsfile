pipeline {
    agent { label 'local1' }
    stages {
        stage('Check Git Reflog') {
            steps {
                script {
                    // Get the Git reflog
                    def reflogOutput = sh(returnStdout: true, script: 'git reflog')

                    // Print Git Reflog Entries for debugging
                    echo "Git Reflog Entries:"
                    echo reflogOutput

                    // Check if the reflog contains a merge commit
                    def isMergeCommit = reflogOutput.contains("merge dev")

                    if (!isMergeCommit) {
                        error("Git reflog does not contain a merge commit")
                    } else {
                        echo "Git reflog contains a merge commit"
                    }
                }
            }
        }
        // Add more stages here /
    }
}
