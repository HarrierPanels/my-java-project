pipeline {
    agent { label 'local1' }
    stages {
        stage('Check Merge Commit') {
            steps {
                script {
                    // Get the list of parents of the current commit
                    def commitId = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
                    def parents = sh(returnStdout: true, script: "git log --pretty=%P -n 1 ${commitId}").trim()

                    // Check if there are multiple parents (indicating a merge commit)
                    if (parents.contains(' ')) {
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
