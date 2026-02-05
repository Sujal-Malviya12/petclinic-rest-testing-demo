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
        APP_PORT       = "9966"
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

        // -------- SAVE TREND --------

        stage('Record Performance') {
            steps {
                bat """
                if not exist perf\\history mkdir perf\\history

                C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell.exe ^
                -ExecutionPolicy Bypass ^
                -File perf\\extract.ps1 result.csv perf\\history\\trend.csv
                """
            }
        }

        // -------- FETCH BASELINE (PRS ONLY) --------

        stage('Fetch Baseline') {
            when { not { branch 'master' } }
            steps {
                bat """
                if not exist perf mkdir perf
                copy C:\\ProgramData\\Jenkins\\.jenkins\\workspace\\petclinic-multibranch_master\\perf\\baseline.csv perf\\baseline.csv
                """
            }
        }

        // -------- PERFORMANCE GATE --------

        stage('Performance Gate') {
            when { not { branch 'master' } }
            steps {
                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                    bat """
                    C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell.exe ^
                    -ExecutionPolicy Bypass ^
                    -File perf\\compare.ps1 perf\\baseline.csv result.csv %PERF_THRESHOLD%
                    """
                }
            }
        }

        // -------- REVIEWER OVERRIDE --------

        stage('Reviewer Override') {
            when {
                expression { currentBuild.currentResult == 'FAILURE' }
            }
            steps {
                input message: "Performance regression detected. Override merge?"
                script {
                    currentBuild.result = 'SUCCESS'
                }
            }
        }

        // -------- UPDATE BASELINE (MASTER ONLY) --------

        stage('Update Baseline') {
            when { branch 'master' }
            steps {
                bat """
                if not exist perf mkdir perf
                copy result.csv perf\\baseline.csv
                """
            }
        }

        // -------- PERFORMANCE CHART --------

        stage('Performance Chart') {
    steps {
        plot(
            group: 'Performance',
            title: 'Response Time Trend',
            style: 'line',
            yaxis: 'Milliseconds',
            csvFileName: 'trend.csv',
            csvSeries: [
                [
                    file: 'perf/history/trend.csv',
                    label: 'Avg Response Time'
                ]
            ]
        )
    }
}


    }

    post {
        always {
            archiveArtifacts artifacts: 'result.csv, perf/baseline.csv, perf/history/trend.csv', fingerprint: true
        }
    }
}
