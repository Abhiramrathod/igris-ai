pipeline {
    agent any

    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk'
        MAVEN_HOME = '/opt/apache-maven-3.9.16'
        PATH = "${MAVEN_HOME}/bin:${JAVA_HOME}/bin:${PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Setup Build Tools') {
            steps {
                sh '''
                    # Install Maven
                    curl -fsSL https://archive.apache.org/dist/maven/maven-3/3.9.16/binaries/apache-maven-3.9.16-bin.tar.gz | tar -xz -C /opt
                    mvn --version
                '''
            }
        }

        stage('Frontend Type Check') {
            steps {
                dir('ai-igris-ui') {
                    sh '''
                        curl -fsSL https://deb.nodesource.com/setup_20.x | bash -
                        apt-get install -y nodejs
                        npm ci
                        npx tsc --noEmit
                    '''
                }
            }
        }

        stage('Frontend Tests') {
            steps {
                dir('ai-igris-ui') {
                    sh 'npx vitest run'
                }
            }
        }

        stage('Initialize Kafka') {
            steps {
                sh '''
                    docker compose up -d kafka
                    echo "Waiting for Kafka..."
                    for i in $(seq 1 30); do
                        if echo > /dev/tcp/kafka/29092 2>/dev/null; then
                            echo "Kafka is ready!"
                            exit 0
                        fi
                        echo "Waiting... ($i/30)"
                        sleep 2
                    done
                    echo "Kafka failed to start"
                    docker compose logs kafka
                    exit 1
                '''
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn -B clean verify'
            }
        }

        stage('Upload Artifact') {
            when {
                branch 'main'
            }
            steps {
                archiveArtifacts artifacts: 'ai-igris-app/target/*.jar', fingerprint: true
            }
        }
    }

    post {
        always {
            sh 'docker compose down -v || true'
            cleanWs()
        }
        success {
            echo 'Build succeeded!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}
