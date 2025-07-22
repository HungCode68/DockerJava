pipeline {
    agent any

    environment {
        IMAGE_NAME = 'hungcode68/dockerjava'
        IMAGE_TAG = 'latest'
        CONTAINER_NAME = 'quanlysinhvien_container'
        DOCKERHUB_CREDENTIALS = 'dockerhub-credentials'
        TOMCAT_PATH = 'C:\\apache-tomcat-10.1.41'
    }

    stages {
      stage('Build WAR') {
    steps {
        echo '📦 Compiling and packaging WAR file...'
        bat '''
            rem Xoá thư mục build cũ nếu có
            if exist build rmdir /s /q build
            mkdir build

            rem Biên dịch toàn bộ file .java trong src vào build
            javac -d build -cp "%TOMCAT_PATH%\\lib\\servlet-api.jar" -sourcepath src ^
                src\\model\\*.java ^
                src\\controller\\*.java ^
                src\\utils\\*.java

            rem Copy toàn bộ Web Pages (index.html, sinhvien.jsp, v.v...) vào build
            xcopy "Web Pages\\*" build /E /I /Y

            rem Đóng gói WAR trực tiếp từ thư mục build
            cd build
            jar -cvf QuanLySinhVien.war *
            cd ..
        '''
    }
}


        stage('Deploy to Tomcat') {
            steps {
                echo '🚀 Deploying WAR to Tomcat (local test only)...'
                bat '''
                    if not exist "%TOMCAT_PATH%\\webapps" (
                        echo "Tomcat webapps folder not found!"
                        exit /b 1
                    )
                    copy build\\QuanLySinhVien.war "%TOMCAT_PATH%\\webapps\\" /Y
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                echo '🐳 Building Docker image from WAR...'
                script {
                    docker.build("${IMAGE_NAME}:${IMAGE_TAG}")
                }
            }
        }

        stage('Stop Previous Container') {
            steps {
                echo '🛑 Stopping and removing previous container if exists...'
                script {
                    def containerId = bat(
                        script: "docker ps -aq -f name=${CONTAINER_NAME}",
                        returnStdout: true
                    ).trim()

                    if (containerId) {
                        bat "docker stop ${CONTAINER_NAME} || exit 0"
                        bat "docker rm ${CONTAINER_NAME} || exit 0"
                        echo "✅ Container ${CONTAINER_NAME} stopped and removed."
                    } else {
                        echo "ℹ️ No existing container found. Skipping."
                    }
                }
            }
        }

        stage('Run New Container') {
            steps {
                echo '🚀 Running new container on port 8086...'
                script {
                    def portInUse = bat(
                        script: 'netstat -ano | findstr :8086',
                        returnStatus: true
                    ) == 0

                    if (portInUse) {
                        error "❌ Port 8086 is already in use. Please free the port before retrying."
                    }

                    bat "docker run -d --name ${CONTAINER_NAME} -p 8086:8086 ${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                echo '📦 Pushing image to Docker Hub...'
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKERHUB_CREDENTIALS) {
                        docker.image("${IMAGE_NAME}:${IMAGE_TAG}").push()
                    }
                }
            }
        }
    }

    post {
        success {
            echo '✅ Triển khai Java Web App thành công trên port 8086!'
        }
        failure {
            echo '❌ Có lỗi xảy ra trong quá trình triển khai!'
        }
    }
}
