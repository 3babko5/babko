#!/bin/bash

# ===================================================
# 배포 자동화 스크립트 - Babko 마이크로서비스 아키텍처
# ===================================================

# 스크립트 시작
echo "배포 프로세스를 시작합니다..."

# ==========================
# 1단계: JAR 파일 확인 및 필요시 빌드
# ==========================
echo "1단계: JAR 파일 확인 중..."

# 프로젝트 루트 디렉토리로 이동
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# 필요시 빌드 디렉토리 정리 (JAR 파일이 손상된 경우 사용)
if [ "$1" == "--clean-build" ]; then
  echo "빌드 디렉토리 정리 중..."
  for service in eureka gateway auth user; do
    if [ -d "./$service/build" ]; then
      echo "$service 빌드 디렉토리 삭제 중..."
      rm -rf "./$service/build"
    fi
  done
  echo "빌드 디렉토리 정리 완료. JAR 파일이 없으므로 배포 프로세스를 계속합니다."
fi

# JAR 파일이 존재하는지 확인
JAR_MISSING=false
for service in eureka gateway auth user; do
  if [ ! -f "./$service/build/libs/$service.jar" ]; then
    echo "$service.jar 파일이 없습니다."
    JAR_MISSING=true
  else
    echo "$service.jar 파일이 존재합니다. 기존 파일을 사용합니다."
  fi
done

# 빌드 스킵 (JAR 파일을 모두 사용함)
echo "Gradle 빌드를 건너뛰고 기존 JAR 파일을 사용합니다."
echo "JAR 파일 빌드 문제가 있다면 './deploy.sh --clean-build' 명령으로 빌드 디렉토리를 정리한 후 수동으로 빌드하세요."

# 각 서비스의 JAR 파일이 생성되었는지 최종 확인
for service in eureka gateway auth user; do
  if [ -f "./$service/build/libs/$service.jar" ]; then
    echo "$service.jar 확인 완료"
  else
    echo "경고: $service.jar 파일이 없지만 계속 진행합니다."
  fi
done

echo "JAR 파일 확인 완료"

# ==========================
# 2단계: PostgreSQL 준비
# ==========================
echo "2단계: PostgreSQL 준비 중..."

# PostgreSQL 컨테이너가 실행 중인지 확인
POSTGRES_RUNNING=$(docker ps -q -f name=postgres-db)
if [ -z "$POSTGRES_RUNNING" ]; then
  echo "PostgreSQL 컨테이너가 실행 중이 아닙니다. 시작합니다..."
  docker-compose up -d postgres
else
  echo "PostgreSQL 컨테이너가 이미 실행 중입니다."
fi

# PostgreSQL이 완전히 시작될 때까지 대기
echo "PostgreSQL이 준비될 때까지 대기 중..."
while ! docker exec postgres-db pg_isready -U postgres -h localhost > /dev/null 2>&1; do
  echo "PostgreSQL이 아직 준비되지 않았습니다. 5초 후 다시 확인합니다..."
  sleep 5
done
echo "PostgreSQL이 준비되었습니다."

# ==========================
# 3단계: 데이터베이스 초기화
# ==========================
echo "3단계: 데이터베이스 초기화 중..."

# 데이터베이스 생성 명령 실행
echo "필요한 데이터베이스 생성 중..."
docker exec postgres-db psql -U postgres -c "CREATE DATABASE user_db;" || echo "user_db가 이미 존재하거나 생성할 수 없습니다."
docker exec postgres-db psql -U postgres -c "CREATE DATABASE auth_db;" || echo "auth_db가 이미 존재하거나 생성할 수 없습니다."

# 권한 부여
echo "데이터베이스 권한 부여 중..."
docker exec postgres-db psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE user_db TO postgres;"
docker exec postgres-db psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE auth_db TO postgres;"

echo "데이터베이스 초기화가 완료되었습니다."

# ==========================
# 4단계: 기존 서비스 컨테이너 정리
# ==========================
echo "4단계: 기존 서비스 컨테이너 정리 중..."

# 실행 중인 서비스 컨테이너 중지 및 삭제 (PostgreSQL 제외)
for service in eureka-server api-gateway auth user; do
  CONTAINER_ID=$(docker ps -aq -f name=$service)
  if [ ! -z "$CONTAINER_ID" ]; then
    echo "컨테이너 $service 중지 및 삭제 중..."
    docker stop $CONTAINER_ID
    docker rm $CONTAINER_ID
  fi
done

# ==========================
# 4.5단계: 기존 서비스 이미지 삭제
# ==========================
echo "4.5단계: 기존 서비스 이미지 삭제 중..."

# 기존 이미지 삭제 (PostgreSQL 제외)
for service in eureka-server api-gateway auth user; do
  IMAGE_ID=$(docker images -q babko_$service 2>/dev/null)
  if [ ! -z "$IMAGE_ID" ]; then
    echo "이미지 babko_$service 삭제 중..."
    docker rmi $IMAGE_ID
  fi
done

# ==========================
# 5단계: 서비스 이미지 재빌드
# ==========================
echo "5단계: 서비스 이미지 재빌드 중..."
docker-compose build --no-cache eureka-server api-gateway auth user

# ==========================
# 6단계: 서비스 실행
# ==========================
echo "6단계: 서비스 실행 중..."

# Eureka Server 먼저 시작
echo "Eureka Server 시작 중..."
docker-compose up -d eureka-server

# Eureka Server가 준비될 때까지 대기
echo "Eureka Server가 준비될 때까지 대기 중..."
while ! curl -s http://localhost:8761/actuator/health | grep "UP" > /dev/null; do
  echo "Eureka Server가 아직 준비되지 않았습니다. 5초 후 다시 확인합니다..."
  sleep 5
done
echo "Eureka Server가 준비되었습니다."

# 나머지 서비스 시작
echo "나머지 서비스 시작 중..."
docker-compose up -d api-gateway auth user

# ==========================
# 배포 완료
# ==========================
echo "배포가 완료되었습니다!"
echo "서비스 엔드포인트:"
echo "- Eureka: http://localhost:8761"
echo "- API Gateway: http://localhost:8080"
echo "- Auth Service: http://localhost:8090"
echo "- User Service: http://localhost:8084" 