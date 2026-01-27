pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        APP_PORT = "9966"
        JMETER_HOME = "C:\\Program Files\\ApacheJMeter\\apache-jmeter-5.6.3"
        SONAR_PROJECT_KEY = "petclinic-rest-testing-demo"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build + Unit Tests') {
            steps {
                bat 'mvn -U -B clean verify'
            }
        }

        stage('SonarQube Scan') {
            steps {
                withSonarQubeEnv('Sonar-Qube-Token') {
                    bat """
                    mvn sonar:sonar ^
                    -Dsonar.projectKey=%SONAR_PROJECT_KEY%
                    """
                }
            }
        }

        /*
         NOTE:
         - Webhook issues on localhost cause Jenkins to hang
         - So we DO NOT block pipeline here
         - Sonar results are still visible in dashboard & PR
        */
        stage('SonarQube Quality Gate (Non-blocking)') {
            steps {
                echo "SonarQube analysis submitted. Quality Gate visible in Sonar dashboard."
            }
        }

        stage('Start App (for JMeter)') {
            steps {
                bat """
                echo Starting Petclinic on port %APP_PORT%
                start "petclinic" /B mvn spring-boot:run ^
                -Dspring-boot.run.arguments=--server.port=%APP_PORT%
                ping 127.0.0.1 -n 20 > nul
                """
            }
        }

        stage('JMeter Performance Test') {
            steps {
                bat """
                "%JMETER_HOME%\\bin\\jmeter.bat" -n ^
                -t jmeter\\petclinic-smoke.jmx ^
                -l target\\jmeter-results.jtl ^
                -e -o target\\jmeter-report
                """
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
            archiveArtifacts artifacts: 'target/jmeter-results.jtl', allowEmptyArchive: true
        }
        cleanup {
            cleanWs()
        }
    }
}
