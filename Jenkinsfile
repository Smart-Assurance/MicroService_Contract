pipeline {
    agent any
    
    environment {
        JENKINS_SSH_KEY = '/var/lib/jenkins/.ssh/id_rsa'
        REMOTE_USER = 'mohcineboudenjal'
        REMOTE_HOST = 'production-server'
        REMOTE_PATH = '/home/mohcineboudenjal/smartassurance/prod'
        JENKINS_HOME = '/var/lib/jenkins'
        JAR_NAME = 'microservice-contract'  // Replace with your actual jar name
    }

    stages {
    stage('Generate Dockerfile on Jenkins') {
        steps {
            script {
                // Run the script to generate Dockerfile locally
                sh """
                    cd ${JENKINS_HOME}
                    ./generate_dockerfile.sh ${JAR_NAME}
                """
            }
        }
    }

    stage('Connect SSH to Remote and Create Directory') {
        steps {
            script {
                // Connect to the remote server and create the directory
                sh """
                    ssh -i ${JENKINS_SSH_KEY} -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} "
                        mkdir -p ${REMOTE_PATH}/${JAR_NAME}
                    "
                """
            }
        }
    }
    
    stage('Copy Dockerfile to Remote Server') {
        steps {
            script {
                // Copy the generated Dockerfile to the remote server
                sh "scp -i ${JENKINS_SSH_KEY} -o StrictHostKeyChecking=no ${JENKINS_HOME}/Dockerfile ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_PATH}/${JAR_NAME}/"
            }
        }
    }



        stage('Build and Copy JAR file into directory') {
            steps {
                script {
                    // Build the app locally
                    sh "mvn clean install"

                    // Copy the JAR to the remote path
                    sh "scp -i ${JENKINS_SSH_KEY} -o StrictHostKeyChecking=no /var/lib/jenkins/workspace/${JAR_NAME}/target/${JAR_NAME}-0.0.1-SNAPSHOT.jar ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_PATH}/${JAR_NAME}"
                }
            }
        }
    stage('reload docker images') {
        steps {
            script {
                // Reconnect to SSH
                sh "ssh -i ${JENKINS_SSH_KEY} -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} 'cd ${REMOTE_PATH}'"

                // Remove existing containers
                sh "ssh -i ${JENKINS_SSH_KEY} -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} 'cd ${REMOTE_PATH} && docker rm -f \$(docker ps -aq)'"

                // Run Docker Compose
                sh "ssh -i ${JENKINS_SSH_KEY} -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} 'cd ${REMOTE_PATH} && docker run --rm -d -v /var/run/docker.sock:/var/run/docker.sock -v \"/home/mohcineboudenjal/smartassurance/prod:/home/mohcineboudenjal/smartassurance/prod\" -w=\"/home/mohcineboudenjal/smartassurance/prod\" docker/compose:1.25.5 up'"
            }
        }
    }
  }
}
