pipeline {
    agent { label 'local1' }

    environment {
        MAIN_BRANCH = 'main'
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    // Checkout the code from the repository
                    checkout scm
                }
            }
        }
        stage('Check for Merge Commit') {
            when {
                expression { currentBuild.rawBuild.getCause(hudson.model.Cause$UserIdCause) == null && env.BRANCH_NAME == env.MAIN_BRANCH }
            }
            steps {
                script {
                    def mergeCommitExists = sh(script: "git log --merges --oneline origin/${env.MAIN_BRANCH}..${env.BRANCH_NAME}", returnStatus: true) == 0
                    if (mergeCommitExists) {
                        echo "A merge commit exists in the history of the ${env.BRANCH_NAME} branch."
                    } else {
                        error "No merge commit found in the history of the ${env.BRANCH_NAME} branch."
                    }
                }
            }
        }
        // Add more stages as needed //
    }
}
