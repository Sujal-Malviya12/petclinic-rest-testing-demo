pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        JMETER_HOME     = "C:\\tools\\apache-jmeter-5.6.3"
        SONAR_PROJECT  = "petclinic-rest-testing"
        PERF_THRESHOLD = "10"
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

        // ---------------- SONAR ----------------

        stage('SonarQube Analysis') {
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
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        // ---------------- JMETER ----------------

        stage('JMeter Performance') {
            steps {
                bat """
                %JMETER_HOME%\\bin\\jmeter.bat -n ^
                -t jmeter\\petclinic-smoke.jmx ^
                -l result.csv
                """
            }
        }

        // -------- FETCH BASELINE FROM MASTER (PRS ONLY) --------

        stage('Fetch Baseline') {
            when {
                not { branch 'master' }
            }
            steps {
                bat """
                if not exist perf mkdir perf
                copy C:\\ProgramData\\Jenkins\\.jenkins\\workspace\\petclinic-multibranch_master\\perf\\baseline.csv perf\\baseline.csv
                """
            }
        }

        // -------- REGRESSION GATE (PRS ONLY) --------

        stage('Regression Gate') {
            when {
                not { branch 'master' }
            }
            steps {
                bat """
                C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell.exe ^
                -ExecutionPolicy Bypass ^
                -File perf\\compare.ps1 perf\\baseline.csv result.csv %PERF_THRESHOLD%
                """
            }
        }

        // -------- UPDATE BASELINE (MASTER ONLY) --------

        stage('Update Baseline') {
            when {
                branch 'master'
            }
            steps {
                bat """
                if not exist perf mkdir perf
                copy result.csv perf\\baseline.csv
                """
            }
            post {
                always {
                    archiveArtifacts artifacts: 'perf/baseline.csv', fingerprint: true
                }
            }
        }

        // -------- REVIEWER OVERRIDE --------

        stage('Reviewer Override') {
            when {
                expression { currentBuild.result == 'FAILURE' }
            }
            steps {
                input message: "Performance regression detected. Override merge?"
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'result.csv', fingerprint: true
        }
    }
}
