pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        JMETER_HOME = "C:\\tools\\apache-jmeter-5.6.3"
        SONAR_HOST = "http://localhost:9000"
        SONAR_PROJECT = "petclinic-rest-testing"
        PERF_THRESHOLD = "10"   // % allowed slowdown
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build + Tests') {
            steps {
                bat "mvn clean verify"
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Scan') {
    steps {
        withSonarQubeEnv('SonarQube') {
            withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                bat """
                mvn sonar:sonar ^
                -Dsonar.projectKey=%SONAR_PROJECT% ^
                -Dsonar.token=%SONAR_TOKEN%
                """
            }
        }
    }
}


        stage('Quality Gate') {
    steps {
        timeout(time: 5, unit: 'MINUTES') {
            waitForQualityGate abortPipeline: true
        }
    }
}


        stage('JMeter Performance') {
            steps {
                bat """
                %JMETER_HOME%\\bin\\jmeter.bat -n ^
                -t jmeter\\petclinic.jmx ^
                -l result.csv
                """
            }
        }

        stage('Compare Performance') {
            steps {
                powershell """
                if (Test-Path baseline.csv) {
                    ./perf/compare.ps1 baseline.csv result.csv ${PERF_THRESHOLD}
                }
                Copy-Item result.csv baseline.csv -Force
                """
            }
        }

        stage('Reviewer Override') {
            when {
                expression { currentBuild.result == 'FAILURE' }
            }
            steps {
                input message: "Performance regression detected. Override?"
            }
        }
    }
}
