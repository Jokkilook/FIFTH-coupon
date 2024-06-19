# [FIFTH-COUPON]

2024년 1학기 모바일최신기술 프로젝트

사용 언어 : Kotlin (Gradle)\
IDE : IntelliJ IDEA\
Framework : Spring

유저별 쿠폰을 생성해 유효기간(세션)을 부여하고
유효성 체크, 사용 등을 수행하는 API

---

### 유저 데이터 구조
id : Long,\
username : String,\
password : String

### 쿠폰 데이터 구조
couponId : Long\
userId : String,\
couponCode : String,

### API (SWAGGER-UI)
http://localhost:8080/swagger-ui/index.html

---

### 테스팅 코드

FIFTH-coupon\src\test\kotlin\com\FIFTH\coupon

#### large test

#### medium test

#### small test



---

### 도커 데스크탑 사용하기
1. 그리들 탭 -> execute Gradle Task - Gradle bootjar
2. 터미널 -> cd .\deploy\
3. 터미널 -> docker-compose up -d
