## 96TALK 🕤 - 양방향 통신 웹 메신저 서비스 📨

> 2023 서일대학교 NINETO6 팀 프로젝트

<br/>

### 🧑‍💻 팀원 소개

<table>
  <tr>
  <td align="center">
	    <a href="https://github.com/chang-seop">
	    	<img src="https://avatars.githubusercontent.com/u/66265875?v=4" width="100px;" alt=""/>
	    	<br/>
	    	<sub>
	    	<b>신창섭</b>
	    	<br/>
	    	<img src="https://us-central1-progress-markdown.cloudfunctions.net/progress/100"/>
	        </sub>
	    </a>
	    <br />
	</td>
  <td align="center">
	    <a href="https://github.com/ezurno">
	    	<img src="https://avatars.githubusercontent.com/u/108059303?v=4?s=100" width="100px;" alt=""/>
	    	<br/>
	    	<sub>
	    	<b>이준모</b>
	    	<br/>
	    	<img src="https://us-central1-progress-markdown.cloudfunctions.net/progress/100"/>
	        </sub>
	    </a>
	</td>
  </tr>
</table>

<br/>

<hr/>

### ⚡프로젝트 소개

> 2023.06 - 2023.11

**양방향 통신 웹 메신저 서비스**

평소에 자주쓰던 'KAKAOTALK', 'LINE' 을 보며 웹에서도 구현해볼 수 있을까? 라는 의문에 시작하게 된 프로젝트.

웹 브라우저에는 채팅방을 생성하여 실시간으로 채팅할 수 있는 플랫폼이 많이 없다고 생각했었는데

비 연결성 프로토콜인 `Http` 통신 방식에 관해 공부하고 있던 중 `Websocket` 과 같은 양방향 네트워크 프로토콜 기반으로 동작하는

`Simple Text Oriented Messaging Protocol(STOMP)`에 대해 알게 되어 구현해 보고자 했다.

 <ul>
  <li>실시간으로 서로의 채팅을 볼 순 없을까? 연결형 프로토을 사용하자!</li>
  <li>전송 후에 연결이 끊기는 `HTTP` 를 유지 할 순 없을까? `WebSocket` 을 활용하자! 클라이언트와 서버 간에 데이터를 양방향으로 전송</li>
  <li>메세지를 보냄으로서 생기는 지연은 어떻게 해결할까? `Message Broker` 를 사용하자!</li>
</ul>

<hr/>
<br/>

### ⚙️ 기술 정보

#### Frontend

<div>
<img src="https://img.shields.io/badge/javascript-F7DF1E?style=flat-square&logo=javascript&logoColor=000000"/>
<img src="https://img.shields.io/badge/typescript-3178C6?style=flat-square&logo=typescript&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/react-61DAFB?style=flat-square&logo=react&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/html5-E34F26?style=flat-square&logo=html5&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/css3-1572B6?style=flat-square&logo=css3&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/tailwindcss-06B6D4?style=flat-square&logo=tailwindcss&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/vite-646CFF?style=flat-square&logo=vite&logoColor=FEC111"/>
</div>

#### Backend

<div>
<img src="https://img.shields.io/badge/java-007396?style=flat-square&logo=java&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/spring-6DB33F?style=flat-square&logo=spring&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/springboot-6DB33F?style=flat-square&logo=springboot&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/springsecurity-6DB33F?style=flat-square&logo=springsecurity&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/JWT-007396?style=flat-square&logo=jWT&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/STOMP-007396?style=flat-square&logo=STOMP&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/mybatis-001202?style=flat-square&logo=mybatis&logoColor=FFFFFF"/>

<img src="https://img.shields.io/badge/mysql-4479A1?style=flat-square&logo=mysql&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/redis-DC382D?style=flat-square&logo=redis&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/mongodb-47A248?style=flat-square&logo=mongodb&logoColor=FFFFFF"/>

</div>

#### Tool

<div>
<img src="https://img.shields.io/badge/visualstudiocode-007ACC?style=flat-square&logo=visualstudiocode&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/intellij-000000?style=flat-square&logo=intellijidea&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/github-181717?style=flat-square&logo=github&logoColor=FFFFFF"/>
<img src="https://img.shields.io/badge/swagger-85EA2D?style=flat-square&logo=swagger&logoColor=FFFFFF"/>
</div>

<br/>
<hr/>

### 🚧 버전 관리 및 진행

#### Ver 1.0

- [x] DB 및 ERD-CLOUD 설계
- [x] Token Hiding
- [x] Refreash Token 구현
- [x] SSL, TLS 인증서 등록 (HTTPS)
- [x] Security 추가
- [x] JWT 인증 방식 추가
- [x] Web Server Engine X 설정
- [x] Swagger 로 관리
- [x] 프로필 수정, 프로필 상세 메세지
- [x] 친구추가 및 채팅방 개설 구현
- [x] 친구목록 조회
- [x] 채팅리스트 조회
- [x] 실시간 채팅 구현
- [x] 채팅 버블 및 안읽은 채팅 표시 구현
- [x] 알람기능 구현
- [x] 각 요청 Validation

#### Ver 2.0

- [ ] 채팅 메세지 Lazy-Loading 구현
- [ ] 단체 채팅방
- [ ] 이미지 업로드 ( 30일 뒤 제거 배치 후순위 )

<br/>
<hr/>

## 🔍 프로젝트 상세
문제점...

해결방안...

어려웠던 점...

해결...
1. 조회를 자주하는 컬럼에 인덱스를 추가하여 조회 성능 올린다.
2. 멀티쓰레드로 인하여 동시에 예를 들어 회원가입이 이루어질 경우 유니크 제약조건 설정하여 DB 단에서도 막을 수 있게 변경
3. MongoDB를 왜 썼는지
채팅 메세지는 보통 읽기와 쓰기가 빈번하게 이루어진다. 심지어 사용자는 단답으로 끊어서 채팅을 치는 사용자도 많다. 이런 읽기 쓰기를 빠르게 조회하여 데이터를 가져올 수 있게 하는 NoSQL인 MongoDB를 선택했다.

#### Swagger

[Swagger API 명세서](https://nineto6.p-e.kr/api/swagger-ui/index.html#/)

![image-swagger](./assets/image-swagger.jpg)

#### ERD-Cloud

![image-erd](./assets/image-erd.png)

<br/>
<hr/>

### 🌳 개발 환경

#### Frontend

- Project: React ^18.2.0
- Language: typescript ^4.9.5
- Dependencies
  - react-hook-form: ^7.48.2
  - react-helmet: ^6.1.0
  - axios: ^1.6.1
  - sockjs-client: ^1.6.1
  - @stomp/stompjs: ^7.0.0
  - tailwindcss: ^3.3.5

#### Backend

- Project: Gradle
- SpringBoot: 2.7.17
- Language: Java 11
- Dependencies
  - jjwt: 0.9.1
  - json-simple: 1.1.1
  - jaxb-runtime: 2.3.2
  - springdoc-openapi-ui: 1.7.0
  - mybatis-spring-boot-starter:2.3.1
  - spring-boot-starter-security
  - spring-boot-starter-web
  - spring-boot-starter-websocket
  - spring-boot-starter-validation
  - spring-boot-starter-data-mongodb
  - spring-boot-starter-data-redis
  - spring-boot-starter-test
  - mysql-connector-j
  - lombok

<br/>