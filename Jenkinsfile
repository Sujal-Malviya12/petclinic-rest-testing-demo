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

        stage('SonarQube Analysis') {
    steps {
        withSonarQubeEnv('SonarQube') {
            withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                bat """
                mvn clean verify sonar:sonar ^
                -Dsonar.projectKey=petclinic-rest-testing ^
                -Dsonar.token=%SONAR_TOKEN%
                """
            }
        }
    }
}

        stage('Quality Gate') {
    steps {
        timeout(time: 10, unit: 'MINUTES') {
            waitForQualityGate abortPipeline: true
        }
    }
}




        stage('JMeter Performance') {
            steps {
                bat """
                %JMETER_HOME%\\bin\\jmeter.bat -n ^
                -t jmeter\\petclinic-smoke.jmx ^
                -l result.csv
                """
            }
        }

        stage('Compare Performance') {
    steps {
        bat """
        C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell.exe -ExecutionPolicy Bypass -File perf\\compare.ps1 baseline.csv result.csv 10
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
