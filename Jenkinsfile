pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        APP_PORT = "9966"
        JMETER_HOME = "C:\\tools\\apache-jmeter-5.6.3"
        SONAR_HOST = "http://localhost:9000"
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
        withSonarQubeEnv('SonarQube-Server') {
            bat """
            mvn -B clean verify sonar:sonar ^
            -Dsonar.projectKey=%SONAR_PROJECT_KEY%
            """
        }
    }
}


        stage('Start App (for JMeter)') {
            steps {
                bat """
                    start "petclinic" /B mvn spring-boot:run ^
                    -Dspring-boot.run.arguments=--server.port=%APP_PORT%
                    timeout /t 20
                """
            }
        }

        stage('JMeter Performance Test') {
            steps {
                bat """
                    "%JMETER_HOME%\\bin\\jmeter.bat" -n ^
                    -t jmeter\\petclinic-smoke.jmx ^
                    -l target\\jmeter-results.jtl
                """
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
        }
        cleanup {
            cleanWs()
        }
    }
}

