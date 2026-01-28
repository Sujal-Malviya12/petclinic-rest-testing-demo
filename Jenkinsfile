pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        APP_PORT = "9966"
        JMETER_HOME = "C:\\tools\\apache-jmeter-5.6.3"
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

        stage('SonarQube Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
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
            archiveArtifacts artifacts: 'target/jmeter-results.jtl, target/jmeter-report/**', fingerprint: true
        }
        cleanup {
            cleanWs()
        }
    }
}
