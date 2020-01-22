#!/usr/bin/env groovy

def call (Map param){
    pipeline {
        agent any
        stages {
            stage('Build') {
                steps {
                    sh 'mvn -B -DskipTests clean package'
                }
            }
            stage('Test') {
                steps {
                    sh 'mvn test'
                }
                post {
                    always {
                        junit 'target/surefire-reports/*.xml'
                    }
                }
            }
            stage("Interactive_Input") {
                steps {
                    script {

                        // Variables for input
                        def inputTest

                        // Get the input
                        def userInput = input(
                            id: 'userInput', message: 'Enter IP:?',
                            parameters: [
                                    string(defaultValue: 'None',
                                            description: 'Test Info file',
                                            name: 'Test'),
                            ])

                        // Save to variables. Default to empty string if not found.
                        inputTest = userInput.Test?:''

                        // Echo to console
                        echo("Test Info file path: ${inputTest}")
                }
            }
        }
            stage('Deliver') {
                steps {
                    sh "sh jenkins/scripts/deliver.sh"
                }
            }
        }
    }
