pipeline {
  agent any

  stages {
    stage('Build + Unit Tests') {
      steps {
        bat 'mvnw clean verify'
      }
    }
  }

  post {
    always {
      junit 'target/surefire-reports/*.xml'
    }
    success {
      githubNotify context: 'continuous/jenkin-test', status: 'SUCCESS', description: 'Jenkins build passed'
    }
    failure {
      githubNotify context: 'continuous/jenkin-test', status: 'FAILURE', description: 'Jenkins build failed'
    }
  }
}
