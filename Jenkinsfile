pipeline {

    agent {
        label 'local1'
    }

    stages {
        stage('GitHub Webhook Check') {
            steps {
                script {
                    def githubEvent = currentBuild.rawBuild.getCause(hudson.triggers.SCMTrigger.SCMTriggerCause)
                    if (githubEvent) {
                        def branch = githubEvent.getBranch()
                        if (branch == 'refs/heads/dev') {
                            echo "Push event from 'dev' branch detected."
                            currentBuild.result = 'FAILURE'
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
}
