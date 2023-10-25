pipeline {
    agent {
        label 'local1'
    }
    stages {
        stage('GitHub Webhook Check') {
            steps {
                script {
                    def branch = env.BRANCH_NAME
                    if (branch == 'dev') {
                        echo "Push event from 'dev' branch detected."
                        currentBuild.result = 'FAILURE'
                    } else {
                        echo "Push event, but not from 'dev' branch. Ignoring."
                    }
                }
            }
        }
    }
}
