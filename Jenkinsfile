pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps { checkout scm }
        }

        stage('Cleanup Old Containers & Images') {
            steps {
                sh """
                docker ps -a --filter "name=spring-mock" --format "{{.ID}}" | xargs -r docker rm -f

                docker images --filter=reference='spring-mock:*' --format "{{.Repository}}:{{.Tag}}" | grep -v ${env.BUILD_NUMBER} | xargs -r docker rmi -f
                """
            }
        }

        stage('Build & Deploy App') {
            steps {
                script {
                    def imageTag = "spring-mock:${env.BUILD_NUMBER}"
                    sh """
                        docker compose -f docker-compose.yml build spring-mock
                        docker compose -f docker-compose.yml up -d
                    """
                }
            }
        }
    }

    post {
        failure {
            sh 'docker compose -f docker-compose.yml logs spring-mock'
        }
        always {
            sh 'docker image prune -f || true'
        }
    }
}