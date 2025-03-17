# OpenJDK 17
FROM openjdk:17

# 작업 디렉터리 설정
WORKDIR /app

# delivery 서브모듈에서 빌드된 JAR 파일을 컨테이너 내 /app 디렉토리로 복사
COPY build/libs/delivery.jar delivery.jar

# 컨테이너 실행 시, eureka.jar 파일을 실행하도록 설정
ENTRYPOINT ["java", "-jar", "delivery.jar"]