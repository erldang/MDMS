# MDMS: 데이터 대동여지도

## 프로젝트 설명
MDMS는 행정안전부 표준 용어 및 단어에 맞춘 데이터베이스 테이블 생성 보조 및 메타 데이터 맵 출력 사이트입니다. 이 프로젝트는 데이터 정원사 팀에 의해 개발되었으며, 사용자가 공통 표준 용어를 활용하여 데이터베이스 테이블을 생성, 수정, 조회할 수 있는 기능을 제공합니다. 또한, 데이터 맵을 시각화하여 사용자가 데이터를 효율적으로 관리하고 이해할 수 있도록 도와줍니다.

## 팀 구성
- **팀장**: [Recyclingbottle](https://github.com/Recyclingbottle)
  - 역할: 기획 관리 및 일정 관리, 팀원 관리, 문서 작성 및 멘토링 관리, FE 리드로 전반적인 페이지 코드 작성 및 구조 관리
  - 기술 스택: Front-End

- **팀원 1**: [lixixi131](https://github.com/lixixi131)
  - 역할: API 서버, 백엔드 Spring Boot 애플리케이션, DB 설정 및 스키마 작성
  - 기술 스택: Back-End

- **팀원 2**: [erldang](https://github.com/erldang)
  - 역할: 데이터 맵 생성 페이지 개발, 프로젝트 리포지토리 및 브랜치 관리
  - 기술 스택: Front-End

## 기술 스택 및 채용 이유
### 프론트엔드
- **Vue.js**: 현대적인 JavaScript 프레임워크로, 빠르고 효율적인 SPA 개발이 가능하며, 컴포넌트 기반의 구조로 코드의 재사용성과 유지보수성이 뛰어납니다.
- **JavaScript**: 웹 개발의 기본 언어로, Vue.js와 함께 사용하여 동적인 웹 페이지를 구현합니다.
- **HTML/CSS**: 웹 페이지의 구조와 스타일을 정의하며, Vue.js와 함께 사용하여 반응형 웹 디자인을 구현합니다.

### 백엔드
- **Spring Boot**: Java 기반의 프레임워크로, 빠른 애플리케이션 개발과 설정의 용이성을 제공하며, 대규모 시스템에서도 안정적으로 운영될 수 있습니다.
- **Java**: 안정성과 성능이 뛰어나며, Spring Boot와 함께 사용하여 강력한 백엔드 시스템을 구축합니다.

### 데이터베이스
- **MySQL**: 관계형 데이터베이스 관리 시스템으로, 데이터의 일관성과 무결성을 유지하며, 다양한 쿼리 기능을 제공하여 복잡한 데이터 처리에 적합합니다.

### 빌드 및 배포
- **Gradle**: 빌드 자동화 도구로, 프로젝트의 종속성을 관리하고, 빌드 프로세스를 자동화하여 개발 효율성을 높입니다.

### 버전 관리
- **Git**: 분산 버전 관리 시스템으로, 코드의 변경 이력을 추적하고, 협업을 용이하게 합니다.
- **GitHub**: 코드 호스팅 플랫폼으로, Git을 사용하여 프로젝트를 관리하고, 팀원 간의 협업을 지원합니다.

## 프로젝트 기능 설계
### 회원 관리
- **등록**: 회원 정보 등록 기능
- **수정**: 회원 정보 수정 기능
- **탈퇴**: 사용자 이력 삭제
- **로그인/로그아웃**: 세션 처리
- **아이디/비밀번호 찾기**: 아이디 및 비밀번호 찾기 기능

### 표준 용어 관리
- **조회**: 사용자가 입력한 데이터에 맞는 표준 용어 목록 출력
- **변경**: 사용자가 표준 용어 목록을 확인하고 변경

### 테이블 생성
- **생성**: 표준 용어 지침에 따른 테이블 생성문 작성
- **쿼리문 조회**: 표준 용어 지침에 따른 테이블 쿼리문 출력
- **수정**: 표준 용어 지침에 따른 테이블 수정 쿼리문 출력
- **삭제**: 테이블 삭제

### 데이터맵
- **그래프 조회**: 카테고리별 히트맵 방식 조회, 사용자가 입력한 데이터와 유사한 데이터 맵 조회

### 사용자 표준 진단 이력 조회
- **조회**: 사용자 표준 이력 조회
- **등록/수정/삭제**: 표준 용어 및 커스텀 표준 용어 등록, 수정, 삭제 기능

## API 명세서
1. **GET /v1/user**: USER TABLE 전체 조회
2. **POST /v1/user/signup**: 회원가입
3. **POST /v1/user/user/authentication/send**: 이메일 인증 요청
4. **POST /v1/user/login**: 로그인
5. **POST /v1/user/modify**: 유저 정보 수정
6. **POST /v1/user/logout**: 로그아웃
7. **DELETE /v1/user/delete**: 회원 탈퇴
8. **POST /v1/user/check**: 토큰과 email, 비밀번호를 대조하여 유저 검사
9. **GET /v1/user/specific**: 토큰으로 유저 정보 가져오기
10. **POST /v1/user/findId**: 회원 아이디 찾기
11. **POST /v1/user/findPwd**: 회원 비밀번호 찾기
12. **POST /v1/user/duplicationCheck**: 아이디(이메일) 중복 확인
13. **GET /v1/table**: DB에 있는 TABLE 전체 조회
14. **POST /v1/table/create**: 쿼리를 받아 table 생성
15. **GET /v1/table/physicalJoin**: 물리적 이름으로 사용자가 생성한 테이블 조회
16. **GET /v1/table/logicalJoin**: 논리적 이름으로 사용자가 생성한 테이블 조회
17. **GET /v1/domain/inputData**: 백엔드에서 데이터 넣기(백 구현 시 사용)
18. **GET /v1/domain**: 전체 공통 표준 데이터 값 조회
19. **GET /v1/terminology/inputData**: 공통 표준 용어 데이터 넣기(백에서만 사용)
20. **GET /v1/terminology**: 공통 표준 용어 데이터 전체 가져오기
21. **POST /v1/terminology/add**: 커스텀 표준 용어 추가
22. **GET /v1/history**: 전체 회원의 테이블 생성 이력 가져오기
23. **GET /v1/history/join**: 특정 회원의 테이블 생성 이력을 이메일로 조회하기

## 데이터베이스 설계
![E-R 다이어그램](https://github.com/erldang/MDMS/assets/101244968/8d0ccf0f-2460-4a7c-9462-e60f6275d654)


## [UI 설계서 ](https://docs.google.com/spreadsheets/d/1-piBkA4FkIdjUb_ZGKXOE0O88-cbBQFD/edit?usp=drive_link&ouid=101712970891109683164&rtpof=true&sd=true)

## 행정안전부 표준 용어 및 단어 JSON 데이터 파싱 문서
| 공통표준도메인 | 공통표준용어 |
| -------------- | ------------ |
| 엑셀값 | json값 | 엑셀값 | json값 |
| NO | no | 번호 | no |
| 제정차수 | degree | 차수 | degree |
| 공통표준도메인그룹명 | groupName | 공통표준용어명 | standardTerminology |
| 공통표준도메인분류명 | classificationName | 공통표준용어설명 | description |
| 공통표준도메인명 | name | 공통표준용어영문약어명 | englishAbbreviation |
| 공통표준도메인설명 | description | 공통표준도메인명 | domain.name |
| 데이터타입 | dataType | 허용값 | domain.tolerance |
| 데이터길이 | dataLength | 저장 형식 | domain.saveFormat |
| 데이터소수점길이 | decimalPointLength | 표현 형식 | domain.expressionForm |
| 저장형식 | saveFormat | 행정표준코드명 | standardCode |
| 표현형식 | expressionForm | 소관기관명 | relevantOrganization |
| 단위 | unit | 없음 | isCustom |
| 허용값 | tolerance |  |  |

