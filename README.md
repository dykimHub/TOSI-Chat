# :speech_balloon: TOSI-Chat

**TOSI 서비스**에서 등장인물과의 채팅과 관련된 기능을 **채팅 서비스**로 분리한 프로젝트 입니다.  
[TOSI(The Only Story In the World, 토씨) 프로젝트](https://github.com/dykimHub/TOSI)에서 전체 구조를 확인하실 수 있습니다.

## 📅 마이그레이션 & 리팩토링 개요

- **기간**: 2024.09.24 ~

채팅 서비스를 분리하면서 전반적인 코드 재사용성과 로직을 개선하였습니다.  
채팅 프롬프트를 테스트하면서 더 알맞은 답변을 생성하도록 바꾸었습니다.  
쿠버네티스를 도입하여 오케스트레이션을 개선하였습니다.

## :computer: 기술 스택

![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Visual Studio Code](https://img.shields.io/badge/Visual%20Studio%20Code-007ACC?style=for-the-badge&logo=Visual%20Studio%20Code&logoColor=white)

![Spring Boot](https://img.shields.io/badge/spring%20boot-%236DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)

![Redis](https://img.shields.io/badge/Redis-%23DC382D.svg?style=for-the-badge&logo=redis&logoColor=white)

![OpenAI](https://img.shields.io/badge/OpenAI-%23000000.svg?style=for-the-badge&logo=OpenAI&logoColor=white)

![Docker](https://img.shields.io/badge/Docker-%232496ED.svg?style=for-the-badge&logo=docker&logoColor=white)
![Kubernetes](https://img.shields.io/badge/kubernetes-%23326CE5.svg?style=for-the-badge&logo=kubernetes&logoColor=white)
![EKS](https://img.shields.io/badge/AWS%20EKS-%23FF9900.svg?style=for-the-badge&logo=amazoneks&logoColor=white)
![ROUTE53](https://img.shields.io/badge/AWS%20route53-%23FF9900.svg?style=for-the-badge&logo=amazonroute53&logoColor=white)

## 📖 API 문서

프로젝트의 API는 **Swagger UI**를 통해 쉽게 확인하고 테스트할 수 있습니다.

- **채팅 API 문서 주소**: 🔗 [https://www.tosi.world/swagger-ui/index.html?urls.primaryName=%EC%B1%84%ED%8C%85](https://www.tosi.world/swagger-ui/index.html?urls.primaryName=%EC%B1%84%ED%8C%85)

#### 🔐 사용자 인증이 필요한 API 사용 방법

일부 API는 인증 헤더가 필요합니다. 먼저 **Access Token**을 획득하셔야 합니다.

1. **회원 API 문서**에 접속하여 **로그인 API**를 실행합니다:

   - **회원 API 문서 주소** 🔗 [https://www.tosi.world/swagger-ui/index.html?urls.primaryName=%ED%9A%8C%EC%9B%90](https://www.tosi.world/swagger-ui/index.html?urls.primaryName=%ED%9A%8C%EC%9B%90)

2. 로그인 API 요청 본문에 아래 정보를 입력하세요:
   ```json
   {
     "email": "test@test.com",
     "password": "test"
   }
   ```
3. 발급된 Access Token을 복사합니다.
4. 테스트할 API의 우측 좌물쇠를 클릭한 후 복사한 토큰을 붙여 넣고 Authorize 버튼을 클릭합니다.
5. Try it out 버튼을 클릭하고 Authorization Header 칸에 `Bearer {발급받은 토큰}`을 입력한 후 Execute 버튼을 클릭합니다.

#### :sparkles: 채팅 방법

1. 채팅 시작 API를 클릭하고 인증을 완료합니다.

2. 채팅 요청 객체에 원하는 이름, 등장인물, 동화 번호를 입력하고 API를 실행하고 응답을 복사합니다.  
   2-1. 동화 번호는 동화 API 문서의 동화 목록 API에서 확인할 수 있습니다.

   - **동화 API 문서 주소**: 🔗 [https://www.tosi.world/swagger-ui/index.html?urls.primaryName=%EB%8F%99%ED%99%94](https://www.tosi.world/swagger-ui/index.html?urls.primaryName=%EB%8F%99%ED%99%94)

3. 채팅 진행 API를 클릭하고 인증을 완료합니다.
4. 복사한 응답을 preMultiChatMessageList에 붙여 넣고 multiChatMessage에 새로운 메시지를 입력한 후 실행합니다.
