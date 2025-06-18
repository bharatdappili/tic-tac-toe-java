pipeline {
    agent any

    tools {
        jdk 'jdk21'  // Make sure Java 21 JDK is configured in Jenkins Global Tool Configuration with this name
        sonarScanner 'SonarScanner'
    }

    environment {
        SONAR_PROJECT_KEY = 'tic-tac-toe-java'
        // If you need, add SONAR_HOST_URL or SONAR_LOGIN token here or in your sonar-project.properties
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/bharatdappili/tic-tac-toe-java.git', branch: 'main'
            }
        }

        stage('Compile') {
            steps {
                // Compile all Java files (adjust path if needed)
                sh 'javac TicTacToe.java'
            }
        }

        stage('Run with simulated input') {
            steps {
                // Run the program with predefined input piped into it
                sh '''
                echo -e "0\n0\n1\n0\n2\n0\n0\n1\n1\n1\n2\n1\n" | java TicTacToe
                '''
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('MySonarServer') {
                    // Run SonarScanner CLI instead of Maven
                    sh "sonar-scanner -Dsonar.projectKey=${env.SONAR_PROJECT_KEY} -Dsonar.sources=."
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
