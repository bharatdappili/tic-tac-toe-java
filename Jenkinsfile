pipeline {
    agent any

    tools {
        jdk 'jdk21'
        // remove sonarScanner from here
    }

    environment {
        SONAR_PROJECT_KEY = 'tic-tac-toe-java'
    }

    stages {

        stage('Compile') {
    steps {
        sh '''
        mkdir -p bin
        javac -d bin *.java
        '''
    }
}

        stage('Run with simulated input') {
            steps {
                sh '''
                echo -e "0\n0\n1\n0\n2\n0\n0\n1\n1\n1\n2\n1\n" | java TicTacToe
                '''
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('MySonarServer') {
                    script {
                        def scannerHome = tool 'SonarScanner'  // The exact name configured in Jenkins Global Tool Configuration
                        sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=${env.SONAR_PROJECT_KEY} -Dsonar.sources=."
                    }
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
            echo 'Build, run, and SonarQube scan succeeded!'
        }
        failure {
            echo 'Build, run, or SonarQube scan failed.'
        }
    }
}
