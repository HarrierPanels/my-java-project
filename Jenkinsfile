pipeline {
    agent {
        label 'local1'
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout your source code from your version control system
                checkout scm
            }
        }

        stage('GitHub Webhook Check') {
            steps {
                script {
                    def githubEvent = currentBuild.rawBuild.getCause(hudson.triggers.SCMTrigger.SCMTriggerCause)
                    if (githubEvent) {
                        def branch = githubEvent.getBranch()
                        if (branch == 'refs/heads/dev') {
                            echo "Push event from 'dev' branch detected."
                            currentBuild.resultIsWorse = 'FAILURE'
                        } else {
                            echo "Push event, but not from 'dev' branch. Ignoring."
                        }
                    } else {
                        echo "Not triggered by a GitHub push event."
                    }
                }
            }
        }

}
