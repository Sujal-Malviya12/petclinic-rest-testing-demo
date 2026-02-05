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

        stage('JMeter Performance') {
            steps {
                bat """
                %JMETER_HOME%\\bin\\jmeter.bat -n ^
                -t jmeter\\petclinic-smoke.jmx ^
                -l result.csv
                """
            }
        }

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

        stage('Fetch Baseline') {
            when { not { branch 'master' } }
            steps {
                bat """
                if not exist perf mkdir perf
                copy C:\\ProgramData\\Jenkins\\.jenkins\\workspace\\petclinic-multibranch_master\\perf\\baseline.csv perf\\baseline.csv
                """
            }
        }

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

        stage('GitHub PR Comment') {
            when { not { branch 'master' } }
            steps {
                withCredentials([string(credentialsId: 'github-token', variable: 'GITHUB_TOKEN')]) {
                    bat """
                    C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell.exe ^
                    -ExecutionPolicy Bypass ^
                    -File perf\\comment.ps1 perf\\baseline.csv result.csv ^
                    Sujal-Malviya12/petclinic-rest-testing-demo %CHANGE_ID% %GITHUB_TOKEN%
                    """
                }
            }
        }

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

        stage('Update Baseline') {
            when { branch 'master' }
            steps {
                bat """
                if not exist perf mkdir perf
                copy result.csv perf\\baseline.csv
                """
            }
        }

        stage('Performance Chart') {
            steps {
                plot(
                    group: 'Performance',
                    title: 'Response Time Trend',
                    style: 'line',
                    yaxis: 'Milliseconds',
                    csvFileName: 'trend.csv',
                    csvSeries: [[
                        file: 'perf/history/trend.csv',
                        label: 'Avg Response Time'
                    ]]
                )
            }
        }

        stage('HTML Dashboard') {
            steps {
                bat """
                C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\powershell.exe ^
                -ExecutionPolicy Bypass ^
                -File perf\\dashboard.ps1
                """
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'perf',
                    reportFiles: 'dashboard.html',
                    reportName: 'Performance Dashboard'
                ])
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'result.csv, perf/baseline.csv, perf/history/trend.csv', fingerprint: true
        }
    }
}
