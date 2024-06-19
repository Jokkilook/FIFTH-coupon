# [FIFTH-COUPON]

2024년 1학기 모바일최신기술 프로젝트

5조\
조장 : 조성윤\
조원 : 박관우 이정욱 정재승 정석현 정종원

사용 언어 : Kotlin (Gradle)\
IDE : IntelliJ IDEA\
Framework : Spring boot\
library : Redis

유저별 쿠폰을 생성해 유효기간(세션)을 부여하고\
유효성 체크, 사용 등을 수행하는 API 및 테스팅 코드\
도커 파일 포함으로 환경 이미지화 가능

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
  * 쿠폰 사용 테스트
  * 쿠폰 유효성 체크 테스트
  * 쿠폰 대량 발급 테스트코드

#### medium test
  * 회원가입 테스트
  * 쿠폰생성 테스트

#### small test
  * 기한이 만료된 쿠폰 체크
  * 쿠폰 발급 대상 유저 유효성 테스트
  * 쿠폰 번호 중복 체크
  * 쿠폰 사용후 번호 삭제 테스트
  * 쿠폰 데이터 정상 저장 여부 체크

---

### 도커 데스크탑 사용하기
1. 그리들 탭 -> execute Gradle Task - Gradle bootjar
2. 터미널 -> cd .\deploy\
3. 터미널 -> docker-compose up -d
