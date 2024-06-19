# ✨ 하나뷰 (Hana View)
<p align="center">시간, 장소 제약 없이<br />화상 창구를 통해 <b>비대면 창구 업무 수행</b>
  
<div align="center">
<img width="768" alt="하나뷰 메인 화면" src="https://github.com/HanaView/.github/assets/71822139/055a9ae2-49e6-4ef1-a6a5-b2feef84176a">
</div>

## ✏️ 기능 소개
<p>● Redis, NCP OCR, Message API를 이용한 본인인증 및 로그인</p>
<p>● WebRTC를 이용한 텔러, 고객의 1:1 화상 상담(화상 통화, 화면 공유, 채팅)</p>
<p>● Spring Boot(Deposit, Saving, Card API)를 통한 은행 예,적금/카드 상품 가입</p>
<p>● 관리자 페이지 조회(텔러 및 손님 상담 기록)</p>

## 🎬 시연 영상
https://youtu.be/tNko3bdyv00?si=3jry193DLumVXeOw


## 📀 ERD
![image](https://github.com/HanaView/.github/assets/71822139/bdde02af-8def-4616-b8f6-eb63a522a0a6)
- 요구사항 명세서를 기반으로 ERD를 작성했습니다.
- 서비스의 주체인 User와 Teller의 정보를 관리하는 2개의 테이블과 상담에 대한 상담 내역과 리뷰를 관리하는 2개의 테이블, 그리고 예금, 적금, 카드 상품의 정보를 관리하고 카테고리, 이자율, 혜택 등의 정보를 저장하는 9개의 테이블과
각 user가 만든 상품에 대한 정보를 보관하는 3개의 테이블이 존재합니다.
- 이로써, 총 16개의 테이블로 HanaView의 DataBase를 설계했습니다.


## ⚙️ 시스템 아키텍처
![image](https://github.com/HanaView/.github/assets/71822139/5acd52e3-9892-435b-96dc-744710b9e400)
- 백엔드 서버 배포의 경우, 요구사항의 빠른 반영을 위해 Docker와 GitHub Actions를 이용해 CI/CD 환경을 구축했습니다.
- 도커를 통해 서버의 이미지를 생성하고 이를 Hub에 등록한 후, Docker-Compose와 Github Actions를 이용하여 각각의 EC2 인스턴스에 서버를 배포했습니다.
- 또한, 프론트 서버의 경우 Vercel을 이용함으로써 별다른 설정없이 CI/CD가 가능하도록 하였습니다.
- 저희 서비스는 hanaview.shop이라는 도메인을 이용하였습니다.
- Route53에 AWS Certificate Manager를 통해 발급받은 인증서를 연결하여 https로의 접근을 허용하였습니다.
- 또한, Http 접근의 경우에는 로드밸런서를 통해 https로 리다이렉션 되도록 설정하였습니다.
- 더불어, 각 접근은 로드밸런서를 통해 target-group 으로의 트래픽이 분산됩니다.
- 도메인 뒤에 /api로 이어지는 URL의 경우 백엔드 기능 서버로 접근되며,
- `/rtc`의 경우 WebRTC 서버로 연결됩니다
- 그리고 그 외의 `/`로 이어지는 URL의 경우 웹서버인 nginx를 통해 Vercel로 접근하도록 설정하였습니다.


## 📃 API






## 👨‍💻 Members
| [김민표](https://github.com/rabbitate)                                          | [유다영](https://github.com/allzeroyou)                                          | [강민주](https://github.com/Minjoo-kang123)                                              | [김서윤](https://github.com/yunred)                                              | [이고은](https://github.com/egon6018)                                          | [임탁균](https://github.com/ImTakGyun)                                          |
| ------------------------------------------------------------------------------ | ------------------------------------------------------------------------------- | -------------------------------------------------------------------------------- | ------------------------------------------------------------------------------- | ------------------------------------------------------------------------------- | ------------------------------------------------------------------------------- |
| <img width = "520" src="https://avatars.githubusercontent.com/u/56537513?v=4"> | <img width = "520" src ="https://avatars.githubusercontent.com/u/71822139?v=4"> | <img width = "520" src ="https://avatars.githubusercontent.com/u/69236634?v=4"> | <img width = "520" src ="https://avatars.githubusercontent.com/u/84527643?v=4"> | <img width = "520" src ="https://avatars.githubusercontent.com/u/100110832?v=4"> | <img width = "520" src ="https://avatars.githubusercontent.com/u/98458302?v=4"> |
| Frontend, WebRTC                                                               | Frontend, WebRTC                                                                        |  Backend                                                                         | Frontend                                                                 | Backend                                                                         | Backend, DevOps                                                                  |

## 💻 Tech Stack
- IDE : IntelliJ, Visual Studio Code
- Front-End: Vite, React, React-Query, JavaScript, SASS
- Backend : SpringBoot, JAVA, Spring-Security, JPA, Gradle
- Database: AWS RDS, MySQL, Redis
- Devops: AWS, Docker, GitHub Actions, Nginx
- API Documentation: Swagger
- VCS: GitHub, Notion, Figma, Swagger 3.0
