pipeline {
    agent any

    tools {
        jdk 'jdk21'           // Your configured JDK name
        maven 'Maven'         // Your configured Maven name
    }

    environment {
        SONAR_PROJECT_KEY = 'tic-tac-toe-java'
        SONAR_SCANNER_OPTS = "-Xmx1024m"
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/bharatdappili/tic-tac-toe-java.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('MySonarServer') {
                    sh '''
                    mvn sonar:sonar \
                      -Dsonar.projectKey=$SONAR_PROJECT_KEY \
                      -Dsonar.sources=src \
                      -Dsonar.java.binaries=target/classes
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 1, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }

    post {
        success {
            echo 'Build and SonarQube scan succeeded!'
        }
        failure {
            echo 'Build or Quality Gate failed.'
        }
    }
}
