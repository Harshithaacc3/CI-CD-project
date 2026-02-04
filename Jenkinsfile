pipeline {
    agent any

    tools {
        maven 'M3'
    }

    environment {
        PROJECT_KEY = "Ci-Cd"
        IMAGE_NAME  = "ci-cd-calculator"
        VERSION = "1.0.${BUILD_NUMBER}"
        GROUP_ID    = "com.example"
        ARTIFACT_ID = "calculator-java"
        NEXUS_URL   = "3.6.223.15:30002"
        ECR_REPO    = "671669616800.dkr.ecr.ap-south-1.amazonaws.com/ci-cd"
        GIT_REPO    = "https://github.com/Harshithaacc3/CI-CD-using-Argocd.git"
    }

    stages {

        stage('SCM') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Harshithaacc3/CI-CD-project.git'
            }
        }

        stage('Sonar Analysis') {
            steps {
                withSonarQubeEnv('sonarcreds') 
                {
                    sh """
                    mvn clean verify sonar:sonar \
                    -Dsonar.projectKey=${PROJECT_KEY} \
                    -Dsonar.projectName=${PROJECT_KEY}
                    """
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Build Jar') {
            steps {
                sh "mvn clean package -Drevision=${VERSION}"
            }
        }

        stage('Upload to Nexus') {
            steps {
                nexusArtifactUploader(
                    nexusVersion: 'nexus3',
                    protocol: 'http',
                    nexusUrl: "${NEXUS_URL}",
                    groupId: "${GROUP_ID}",
                    version: "${VERSION}",
                    repository: 'maven-releases',
                    credentialsId: 'nexuscreds',
                    artifacts: [
                        [
                            artifactId: "${ARTIFACT_ID}",
                            classifier: '',
                            file: "target/${ARTIFACT_ID}-${VERSION}.jar",
                            type: 'jar'
                        ]
                    ]
                )
            }
        }

        stage('Build Docker Image') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'nexuscreds',
                    usernameVariable: 'NEXUS_USER',
                    passwordVariable: 'NEXUS_PASS'
                )]) {
                    sh """
                    docker build \
                    --build-arg NEXUS_USER=${NEXUS_USER} \
                    --build-arg NEXUS_PASS=${NEXUS_PASS} \
                    --build-arg NEXUS_URL=${NEXUS_URL} \
                    --build-arg GROUP_ID=${GROUP_ID} \
                    --build-arg ARTIFACT_ID=${ARTIFACT_ID} \
                    --build-arg VERSION=${VERSION} \
                    -t ${IMAGE_NAME}:${VERSION} .
                    """
                }
            }
        }

        stage('Push Image to ECR') {
            steps {
                withAWS(credentials: 'ecr-user', region: 'ap-south-1') {
                    sh """
                    aws ecr get-login-password --region ap-south-1 | docker login --username AWS --password-stdin 671669616800.dkr.ecr.ap-south-1.amazonaws.com
                    docker tag ${IMAGE_NAME}:${VERSION} ${ECR_REPO}:${VERSION}
                    docker push ${ECR_REPO}:${VERSION}
                    """
                }
            }
        }

        stage('Update ArgoCD Repo') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'argocdcreds',
                    usernameVariable: 'GIT_USER',
                    passwordVariable: 'GIT_TOKEN'
                )]) {
                    sh """
                    rm -rf CI-CD-using-Argocd
                    git clone https://${GIT_USER}:${GIT_TOKEN}@github.com/Harshithaacc3/CI-CD-using-Argocd.git
                    cd CI-CD-using-Argocd

                    sed -i 's|image:.*|image: ${ECR_REPO}:${VERSION}|' argocd.yaml

                    git config user.name "argouser"
                    git config user.email "argouser@jenkins.com"

                    git add .
                    git commit -m "Update image to ${VERSION}"
                    git push origin main
                    """
                }
            }
        }
    }

    post {
        success {
            echo "CI/CD complete. ArgoCD will now deploy ${VERSION}"
        }
        failure {
            echo "Pipeline failed"
        }
    }
}
