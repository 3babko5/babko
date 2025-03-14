# 공식 Java 런타임 이미지를 기반으로 사용
FROM openjdk:17

# 컨테이너 내에서 작업 디렉터리 설정
WORKDIR /app

# 빌드된 JAR 파일을 컨테이너의 /app 폴더로 복사
COPY build/libs/gateway.jar gateway.jar

# 게이트웨이가 사용하는 포트 열기 (예시: 8080)
EXPOSE 8080

# JAR 파일 실행
ENTRYPOINT ["java", "-jar", "/app/gateway.jar"]
