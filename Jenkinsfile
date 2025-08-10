pipeline {
    agent any
    
    tools {
        maven 'Maven-3.9.6'
        jdk 'JDK-17'
    }
    
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
        
        stage('Build') {
            steps {
                echo 'Building project with Maven...'
                sh 'mvn clean compile'
            }
        }
        
        stage('Test') {
            parallel {
                stage('Smoke Tests') {
                    steps {
                        echo 'Running Smoke Tests...'
                        sh 'mvn test -DsuiteXmlFile=testng-smoke.xml'
                    }
                    post {
                        always {
                            publishTestResults testResultsPattern: '**/test-outputs/*.xml'
                        }
                    }
                }
                
                stage('Cross Browser Tests') {
                    steps {
                        echo 'Running Cross Browser Tests...'
                        sh 'mvn test -DsuiteXmlFile=testng-cross-browser.xml'
                    }
                    post {
                        always {
                            publishTestResults testResultsPattern: '**/test-outputs/*.xml'
                        }
                    }
                }
                
                stage('E2E Tests') {
                    steps {
                        echo 'Running E2E Tests...'
                        sh 'mvn test -DsuiteXmlFile=testng-e2e.xml'
                    }
                    post {
                        always {
                            publishTestResults testResultsPattern: '**/test-outputs/*.xml'
                        }
                    }
                }
            }
        }
        
        stage('Generate Allure Report') {
            steps {
                echo 'Generating Allure Report...'
                sh 'mvn allure:report'
            }
        }
        
        stage('Archive Results') {
            steps {
                echo 'Archiving test results...'
                archiveArtifacts artifacts: 'test-outputs/**/*', fingerprint: true
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
