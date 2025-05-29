# :rabbit: TOSI(The Only Story In the world)를 소개합니다. :tada:

<div align="center">
<img src="assets/tosi.png" width="" height="150"></img>

**TOSI는 어린이들이 혼자서도 재미있게 독서할 수 있는 인터랙티브 동화 구연 서비스입니다.** <br>
저희 서비스를 통해 '토씨 하나 빼놓지 않고' 이야기에 집중할 수 있습니다. <br>
**TOSI-Chat**은 토씨에서 등장인물과의 채팅과 관련된 기능을 **채팅 서비스**로 분리한 프로젝트 입니다.

</div>

# :sparkle: 서비스 목표

동화 **등장인물의 이름을 아이의 이름으로 바꿔** TTS로 읽어주며, <br>
아이 혼자서도 동화에 몰입할 수 있어 미디어 소비 대신 독서 습관 형성을 돕습니다.

동화가 끝나면 OpenAI API를 활용해 **원하는 등장인물과 채팅**하면서, <br>
동화 속 캐릭터들과 상호작용하여 어휘력 및 사회성을 향상시킵니다.

**원하는 키워드와 배경**으로 OpenAI API를 활용해 **커스텀 동화를 제작**하면서, <br>
매번 새로운 이야기와 삽화로 아이의 상상력을 자극합니다.

</div>

# :date: 기간

_SSAFY 10기 공통 프로젝트_  
**2024.01.03 - 2024.02.16 (7주)**  
 우수상🏆

_1차 리팩토링_  
**24.09.09 - 24.10.18**

_2차 리팩토링_  
**25.02.06 - 25.02.23**

## :computer: Team. 먼똑귀

| 이름         | 역할          | 기능                                                                                                                                                                                                |
| ------------ | ------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 천우진(팀장) | BE, FE        | 일반 동화 관리, 메인페이지 UI 구성                                                                                                                                                                  |
| 우지민       | BE, FE        | TTS 재생/정지/배속/볼륨 적용, TTS 제어 UI 구성                                                                                                                                                      |
| 김다윤       | BE, FE, INFRA | 등장인물을 회원 이름으로 변경, 회원 관심 동화 관리, 이름 선택 및 동화책 UI 구성 <br> 1차 리팩토링: MSA 마이그레이션, 쿠버네티스 도입, AWS EKS 배포<br> 2차 리팩토링: Redis 도입, MSA 공통 로직 설계 |
| 양성주       | BE, FE        | 커스텀 동화 생성 및 저장, (비)공개 커스텀 동화 관리, 커스텀 동화 제작 UI 구성                                                                                                                       |
| 이아진       | BE, FE, INFRA | 등장인물과의 채팅, 채팅방 UI 구성, AWS EC2 배포                                                                                                                                                     |
| 김소연       | BE, FE        | JWT 기반 회원 인증 및 인가, 로그인/회원가입/마이페이지 UI 구성                                                                                                                                      |

## :pushpin: TOSI 서비스 아키텍처

<img src="assets/tosi_msa_chat.png" alt="System Architecture" width="700"/>

## :deciduous_tree: TOSI-CustomTale 기술 스택

| Section      | Stack                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
| ------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **Backend**  | ![Spring Boot](https://img.shields.io/badge/spring%20boot-%236DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white) ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) ![OpenAI](https://img.shields.io/badge/OpenAI_API-F4F4F5?style=for-the-badge&logo=openai&logoColor=black) ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)                                                                                                              |
| **Database** | ![MySQL](https://img.shields.io/badge/MySQL-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white) ![Redis](https://img.shields.io/badge/Redis-DC382D.svg?style=for-the-badge&logo=redis&logoColor=white)                                                                                                                                                                                                                                                                                                                                                                                    |
| **Infra**    | ![Docker](https://img.shields.io/badge/Docker-2496ED.svg?style=for-the-badge&logo=docker&logoColor=white) ![Kubernetes](https://img.shields.io/badge/kubernetes-%23326CE5.svg?style=for-the-badge&logo=kubernetes&logoColor=white) ![Amazon RDS](https://img.shields.io/badge/Amazon%20RDS-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white) ![Route 53](https://img.shields.io/badge/Route%2053-6A34D1.svg?style=for-the-badge&logo=amazonroute53&logoColor=white) ![AWS EKS](https://img.shields.io/badge/AWS%20EKS-%23FF9900.svg?style=for-the-badge&logo=amazoneks&logoColor=white) |
| **DevTool**  | ![GitHub](https://img.shields.io/badge/GitHub-181717.svg?style=for-the-badge&logo=github&logoColor=white) ![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF.svg?style=for-the-badge&logo=githubactions&logoColor=white) ![OpenAPI](https://img.shields.io/badge/OpenAPI-85EA2D.svg?style=for-the-badge&logo=swagger&logoColor=black) ![IntelliJ](https://img.shields.io/badge/IntelliJ%20IDEA-000000?style=for-the-badge&logo=intellijidea&logoColor=white) ![VSCode](https://img.shields.io/badge/Visual%20Studio%20Code-007ACC?style=for-the-badge&logo=&logoColor=white) |

## :art: TOSI-Chat 관련 서비스 화면

<table>
  <tr>
    <th width="50%">등장인물과의 채팅 준비</th>
    <th width="50%">등장인물과의 채팅</th>
  </tr>
  <tr>
    <td><img src="./assets/bookend.gif" width="100%"></td>
    <td><img src="./assets/chat.gif" width="100%"></td>
  </tr>
  <tr>
    <td>- 동화 구연이 끝날 때마다 등장인물 중 한 명이 랜덤 메세지를 보냅니다. <br> - 원하는 등장인물에게 채팅을 걸 수 있습니다. 선택한 어린이의 이름으로 채팅방이 형성됩니다. <br> - 선택한 등장인물이 입장했다는 메세지가 뜹니다. 해당 등장인물에게 5번의 대답을 들을 수 있습니다. </td>
    <td>  - 메세지가 추가될 때마다 스크롤을 최하단으로 내려 최신 메세지를 바로 볼 수 있도록 합니다. <br> - 5번의 대답이 끝나면 등장인물이 작별 인사를 보내고 대화를 종료합니다. 더 이상 채팅을 입력할 수 없고 나가기를 눌러 동화 목록 페이지로 갈 수 있습니다. </td>
  </tr>
</table>
