
## 프로젝트 소개
물류 관리 및 배송 시스템을 위한 MSA 기반 플랫폼 개발


### 개요

- 본 프로젝트는 B2B 환경에서 물류 및 배송 흐름을 디지털화하고, 이를 MSA 아키텍처 기반으로 구현한 시스템입니다.
- 각 지역 허브 간의 상품 이동, 주문, 재고 관리, 배송까지 모든 물류 프로세스를 모델링하였습니다.

### 구현 의도

- 기존의 모놀로틱 구조가 아닌 MSA 구조로 개발해 봄으로써 여러 서비스 간 데이터 흐름과 비즈니스 로직의 분리를 경험하고자 했습니다.
- 각 서비스를 독립적으로 구성하여 책임을 분리함으로써 협업 시 발생할 수 있는 다양한 상황을 경험해보고 해결하고자 했습니다.
- 실무에서의 MSA 구조와 유사하게 기능별로 서비스를 구분하여 서로 통신하는 구조를 구현함으로써 확장 가능성을 고려하고 실무 중심의 협업 방식을 습득하고자 했습니다.

<br><br>

## 팀원 소개
![단체샷](https://github.com/user-attachments/assets/bf9676d9-c205-46e4-9155-5f5e6427b508)

| [김도원](https://github.com/dowon0113) | [노현지](https://github.com/nodajida) | [송예지](https://github.com/yejiscore) | [안중건](https://github.com/AnJungGeon) | [정다예](https://github.com/Jungdaye89) |
| --- | --- | --- | --- | --- |
| <a href="https://github.com/dowon0113"><img height="170px" width="150px" src="https://github.com/user-attachments/assets/5faed894-3b56-49ae-8022-993aba3cda9f"/></a> | <a href="https://github.com/nodajida"><img height="170px" width="150px" src="https://github.com/user-attachments/assets/c504b0e2-1e62-4851-b983-2d8457e78d18"/></a> | <a href="https://github.com/yejiscore"><img height="220px" width="140px" src="https://github.com/user-attachments/assets/9fb4e72d-c93a-4405-bace-17cc068b6e57"/></a> | <a href="https://github.com/AnJungGeon"><img height="170px" width="150px" src="https://github.com/user-attachments/assets/b6a775a8-a651-42b7-9597-fd81ca9bff39"/></a> | <a href="https://github.com/Jungdaye89"><img height="170px" width="150px" src="https://github.com/user-attachments/assets/ec27002f-e35e-43ec-88f3-c4d9e5725ecb"/></a> |
| 주문, 주문 아이템 | Auth, User | 업체, 상품 | 허브, 허브 간 이동 | 배송, 배송 경로, 배송 담당자 |



<br><br>

## 프로젝트 기간

2025.03.11 ~ 2025.03.25


<br><br>

## 프로젝트 구조

```jsx
babko
├── user                           # 'user' 모듈 (사용자 관련)
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com.chone.user
│       │   │       ├── user                  # user 애그리거트
│       │   │       │   ├── application  # 애플리케이션 계층 (서비스 로직, DTO 등)
│       │   │       │   ├── dto          # 데이터 전송 객체 관련
│       │   │       │   │   ├── request  # API 요청 DTO
│       │   │       │   │   ├── response # API 응답 DTO
│       │   │       │   │   └── mapper   # DTO와 Entity 간 변환 매퍼
│       │   │       │   └── service      # 사용자 관련 비즈니스 로직을 처리하는 서비스
│       │   │       ├── domain           # 도메인 계층 (비즈니스 모델)
│       │   │       │   ├── entity       # 사용자 엔티티 및 Value Object
│       │   │       │   └── repository   # 도메인 레벨의 Repository 인터페이스
│       │   │       ├── infrastructure # 인프라 계층 (외부 연동, DB 구현체 등)
│       │   │       │   ├── repository   # JPA, QueryDSL 등 실제 DB 접근 구현체
│       │   │       │   └── client       # 외부 API 통신 클라이언트 (예: REST API)
│       │   │       └── presentation     # 프레젠테이션 계층 (웹 API)
│       │   │           └── controller   # REST API 엔드포인트 컨트롤러
│       │   │       └── deliverydriver        # deliverydriver 애그리거트
│       │   └── resources
│       │       └── application.yml  # Spring Boot 설정
│       └── test
│           └── java
│               └── com.chone.user   # 테스트 코드
├── product                        # 'product' 모듈 (상품 관련)
│   └── src
│       └──  main
│           └──  java
│               └── com.business.product
│                   ├── product              # product 애그리거트
│                   └── inventory            # inventory 애그리거트
├── auth                            # 'auth' 모듈 (인증, 인가)
├── delivery                        # 'delivery' 모듈 (배송, 배송 담당자)
├── eureka                          # 'eureka' (서비스 디스커버리 서버)
├── gateway                         # 'gateway' (API 게이트웨이)
├── order                           # 'order' (주문, 주문 아이템 관리)
├── hub                             # 'hub' (허브, 허브 간 이동 관리)
├── company                         # 'company' (업체 관리)
├── common                          # 공통 모듈 (유틸, 공통 설정)
├── build.gradle                     # 루트 빌드 스크립트 (Gradle)
└── settings.gradle                  # 프로젝트 설정 파일 (Gradle settings)
```
<br><br>

## 개발 환경

본 프로젝트는 Java와 Spring Boot 기반으로 개발되었으며, MSA 구조에서 각 서비스 간 유기적인 연동을 위해 Eureka 기반의 서비스 레지스트리와 FeignClient를 활용한 REST 통신을 구성했습니다. 인증과 인가는 Spring Security와 JWT를 통해 stateless 기반의 보안 처리를 구현했으며, 데이터 처리에는 Spring Data JPA와 QueryDSL을 적용하여 복잡한 조건의 데이터 조회도 유연하게 처리할 수 있도록 했습니다.
각 도메인 별 데이터 독립성을 고려하여 서비스별 DB 분리를 지향했으나, 학습 환경의 제약으로 인해 하나의 데이터베이스 내에서 스키마를 분리하는 방식으로 논리적인 격리를 구현하였습니다.

로컬에서도 전체 MSA 환경을 손쉽게 구동할 수 있도록 Docker와 Docker Compose를 활용하여 서비스 실행 환경을 통합 구성했습니다. 이를 통해 개발자 간 환경 차이를 줄이고, 테스트 및 배포 전 과정을 효율적으로 수행할 수 있도록 했습니다.

### Back-end
<div>
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Java.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/SpringBoot.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/SpringSecurity.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/SpringDataJPA.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/JWT.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Qeurydsl.png?raw=true" width="80">
<img src="https://github.com/user-attachments/assets/08e68472-fbdc-4a86-a2af-b28bb37ff132?raw=true" width="80" height="85">
</div>

### Tools
<div>
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Github.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Notion.png?raw=true" width="80">
</div>

### Infra
<div>
<img src="https://raw.githubusercontent.com/yewon-Noh/readme-template/refs/heads/main/skills/Docker.png?raw=true" width="80">
</div>

<br><br>

## 프로젝트 실행 방법
### 1. 로컬 실행
```
# 1. Git 저장소 클론 및 이동
git clone https://github.com/3babko5/babko
cd babko

# 2. 환경 변수 설정 (.yml 파일 생성)
# Eureka
spring:
  application:
    name: eureka

server:
  port: 8761

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false


# Eureka-Client
spring:
  application:
    name: user-service

  datasource:
    url: jdbc:postgresql://localhost:5432/babko
    username: postgres
    password: 1234
    driver-class-name: org.postgresql.Driver

server:
  port: 8084
  
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

  instance:
    prefer-ip-address: true
    instance-id: ${spring.cloud.client.ip-address}:${spring.application.name}:${server.port}

# 3. 의존성 설치 및 빌드
./gradlew clean build -x test

# 4. PostgreSQL 실행 (로컬 DB 사용 시)
docker run -d --name postgres-db -p 5432:5432 \
  -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=1234 postgres:17

# 5. 서비스 실행 (순서대로 실행)
# Eureka (서비스 디스커버리)
cd eureka
java -jar build/libs/eureka.jar

# Gateway 실행
cd ../gateway
java -jar build/libs/gateway.jar

# 나머지 서비스 실행 (예: auth, user 등)
cd ../auth
java -jar build/libs/auth.jar
# ... (필요한 다른 서비스도 동일하게 실행)

```

### 2. Docker 실행
```
# 1. Git 저장소 클론 및 이동
git clone https://github.com/3babko5/babko
cd babko

# 2. 환경 변수 설정 (.env 파일 생성)
# 로컬 실행과 동일

# 3. 배포 스크립트 실행 또는 Docker Compose 실행
# 배포 스크립트 사용 시:
chmod +x deploy.sh
./deploy.sh

# 또는, Docker Compose로 직접 실행
docker-compose up -d

# 4. 실행 상태 확인 (예: Eureka는 http://localhost:8761, Gateway는 http://localhost:8080)
docker ps

```
### 인증 인가 플로우
![Untitled diagram-2025-03-18-104254 (3)](https://github.com/user-attachments/assets/34f33b6d-41f8-4235-bc6d-5dad7a8f4387)

## 개발 산출물

### 트러블 슈팅
#### 공통 트러블 슈팅 1
![image](https://github.com/user-attachments/assets/45d84e52-799f-433a-bf88-b233c351b4ab)

#### 공통 트러블 슈팅 2
![image](https://github.com/user-attachments/assets/6953f2d3-0f54-4027-b2f1-152f33eda5bb)

### 설계 대비 API 구현률
API Count Summary
==========================
[order] - OrderController.java
    ├── GET APIs:        2
    ├── POST APIs:        1
    ├── PATCH APIs:        2
==========================
[auth] - AuthController.java
    ├── POST APIs:        2
    ├── PUT APIs:        1
==========================
[user] - DeliveryDriverController.java
    ├── GET APIs:        2
    ├── POST APIs:        2
    ├── PATCH APIs:        2
[user] - UserController.java
    ├── GET APIs:        3
    ├── POST APIs:        1
    ├── PUT APIs:        2
    └── DELETE APIs:        1
==========================
[product] - ProductController.java
    ├── GET APIs:        1
    ├── POST APIs:        1
[product] - InventoryController.java
    ├── PUT APIs:        1
==========================
[common] - CommonController.java
    ├── GET APIs:        1
==========================
[ai] - AiController.java
    ├── GET APIs:        1
==========================
[delivery] - DeliveryController.java
    ├── GET APIs:        2
    ├── POST APIs:        1
    ├── PATCH APIs:        2
    └── DELETE APIs:        1
==========================
[slack] - SlackController.java
    ├── GET APIs:        1
==========================
[hub] - HubMovementController.java
    ├── GET APIs:        6
    ├── POST APIs:        1
    ├── PATCH APIs:        1
    └── DELETE APIs:        1
[hub] - HubController.java
    ├── GET APIs:        3
    ├── POST APIs:        1
    ├── PATCH APIs:        1
    └── DELETE APIs:        1
==========================
[company] - CompanyController.java
    ├── GET APIs:        1
    ├── POST APIs:        1
    └── DELETE APIs:        1
==========================
API Statistics
==========================
- GET APIs: 23
- POST APIs: 11
- PATCH APIs: 8
- PUT APIs: 4
- DELETE APIs: 5
- Total APIs: 51
==========================

### 잘한점
![image](https://github.com/user-attachments/assets/c2510225-852f-483f-b52b-4ffd22c260f4)

### 아쉬웠던 점
![image](https://github.com/user-attachments/assets/86ae7c3d-fc71-4279-93ab-b6e710a7f5c4)

### 고도화
![image](https://github.com/user-attachments/assets/9abdb103-0346-4ebb-881f-374efa036aa1)




