#!/usr/bin/env groovy

def call (Map param){
    def deliver libraryResource "deliver.sh"
    pipeline {
        agent {label 'linux1'}
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
            stage('Deliver') {
                steps {
                    sh ((deliver)"$param.ip")
                }
            }
        }
    }
}
