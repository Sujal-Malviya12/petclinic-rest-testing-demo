pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        MAVEN_CMD = "mvn"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Unit Tests') {
            steps {
                echo "Running Maven build + tests..."
                bat "${env.MAVEN_CMD} -U -e -B clean verify"
            }
        }
    }

    post {
        always {
            echo "Publishing test reports..."
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
        }

        success {
            echo "✅ Build & Tests Passed"
        }

        failure {
            echo "❌ Build or Tests Failed"
        }

        cleanup {
            cleanWs()
        }
    }
}
