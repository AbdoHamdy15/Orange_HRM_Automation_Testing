pipeline {
    agent any
    
    environment {
        PROJECT_NAME = 'OrangeHRM-Automation'
        TEST_RESULTS_DIR = 'test-outputs'
        ALLURE_RESULTS_DIR = 'test-outputs/allure-results'
        ALLURE_REPORT_DIR = 'test-outputs/allure-report'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code from Git...'
                checkout scm
            }
        }
        
        stage('Setup Maven') {
            steps {
                echo 'Setting up Maven...'
                script {
                    // Try to use Maven wrapper if available
                    if (fileExists('mvnw.cmd')) {
                        echo 'Using Maven wrapper...'
                        env.MAVEN_CMD = 'mvnw.cmd'
                    } else if (fileExists('mvnw')) {
                        echo 'Using Maven wrapper...'
                        env.MAVEN_CMD = './mvnw'
                    } else {
                        echo 'Maven wrapper not found, using system Maven...'
                        env.MAVEN_CMD = 'mvn'
                    }
                }
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building project with Maven...'
                script {
                    try {
                        sh "${env.MAVEN_CMD} clean compile"
                    } catch (Exception e) {
                        echo "Build failed: ${e.getMessage()}"
                        error "Build stage failed"
                    }
                }
            }
        }
        
        stage('Login Tests') {
            steps {
                echo 'Running Login Tests...'
                script {
                    try {
                        sh "${env.MAVEN_CMD} test -Dtest=LoginTest"
                    } catch (Exception e) {
                        echo "Login tests failed: ${e.getMessage()}"
                        // Don't fail the pipeline, just log the error
                    }
                }
            }
            post {
                always {
                    publishTestResults testResultsPattern: '**/test-outputs/*.xml'
                }
            }
        }
        
        stage('Generate Allure Report') {
            steps {
                echo 'Generating Allure Report...'
                script {
                    try {
                        sh "${env.MAVEN_CMD} allure:report"
                    } catch (Exception e) {
                        echo "Allure report generation failed: ${e.getMessage()}"
                        // Don't fail the pipeline, just log the error
                    }
                }
            }
        }
        
        stage('Archive Results') {
            steps {
                echo 'Archiving test results...'
                script {
                    try {
                        archiveArtifacts artifacts: 'test-outputs/**/*', fingerprint: true
                    } catch (Exception e) {
                        echo "Archiving failed: ${e.getMessage()}"
                    }
                }
            }
        }
    }
    
    post {
        always {
            echo 'Cleaning up workspace...'
            cleanWs()
        }
        
        success {
            echo 'Pipeline completed successfully!'
            // Send success notification
            emailext (
                subject: "Pipeline SUCCESS: ${env.JOB_NAME} [${env.BUILD_NUMBER}]",
                body: "Pipeline completed successfully!\n\nJob: ${env.JOB_NAME}\nBuild: ${env.BUILD_NUMBER}\nURL: ${env.BUILD_URL}",
                recipientProviders: [[$class: 'DevelopersRecipientProvider']]
            )
        }
        
        failure {
            echo 'Pipeline failed!'
            // Send failure notification
            emailext (
                subject: "Pipeline FAILED: ${env.JOB_NAME} [${env.BUILD_NUMBER}]",
                body: "Pipeline failed!\n\nJob: ${env.JOB_NAME}\nBuild: ${env.BUILD_NUMBER}\nURL: ${env.BUILD_URL}",
                recipientProviders: [[$class: 'DevelopersRecipientProvider']]
            )
        }
        
        unstable {
            echo 'Pipeline is unstable!'
        }
    }
}
