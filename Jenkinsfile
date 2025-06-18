pipeline {
    agent any

    tools {
        jdk 'jdk21'
    }

    environment {
        SONAR_SCANNER_OPTS = "-Xmx512m"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/bharatdappili/tic-tac-toe-java.git'
            }
        }

        stage('Compile & Run') {
            steps {
                script {
                    env.JAVA_HOME = tool name: 'jdk21', type: 'hudson.model.JDK'
                }
                sh '''
                    export JAVA_HOME=${JAVA_HOME}
                    export PATH=$JAVA_HOME/bin:$PATH

                    echo "Compiling Java code..."
                    javac tic.java

                    echo "Running Java program..."
                    java tic
                '''
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('MySonarServer') {
                    script {
                        def scannerHome = tool 'SonarScanner'
                        sh """
                            export JAVA_HOME=${JAVA_HOME}
                            export PATH=$JAVA_HOME/bin:$PATH
                            ${scannerHome}/bin/sonar-scanner
                        """
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
            echo 'Build and SonarQube analysis succeeded!'
        }
        failure {
            echo 'Something failed in the build or analysis.'
        }
    }
}
