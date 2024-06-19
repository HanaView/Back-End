# ✨ 하나뷰 (Hana View)
<p align="center">시간, 장소 제약 없이<br />화상 창구를 통해 <b>비대면 창구 업무 수행</b>
  
<div align="center">
<img width="768" alt="하나뷰 메인 화면" src="https://github.com/HanaView/.github/assets/71822139/055a9ae2-49e6-4ef1-a6a5-b2feef84176a">
</div>

## ✏️ 프로젝트 소개
Hanaview는 비대면 화상 창구 서비스를 통해 대면 업무에서의 불편사항들을 해결합니다.
은행 업무의 시간적, 공간적 제약을 해결함으로써 사용자의 만족도를 높이고자 합니다.

## 🎬 시연 영상
https://youtu.be/tNko3bdyv00?si=3jry193DLumVXeOw

## 🔥 작업 기간

- 2024.05.14 - 2024.06.12

## 💻 Tech Stack

#### Environment

<img src="https://img.shields.io/badge/intellijidea-000000?style=flat&logo=intellijidea&logoColor=white">

#### Development

<img src="https://img.shields.io/badge/Java-007396?style=flat&logo=Java&logoColor=white"> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat&logo=Spring Boot&logoColor=white">
![image](https://img.shields.io/badge/JWT-000000?style=flat-square&logo=JSON%20web%20tokens&logoColor=white) <img src="https://img.shields.io/badge/JPA-007396?style=flat-square&logo=Java&logoColor=green"> 

#### DataBase

<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white"> <img src="https://img.shields.io/badge/Redis-DC382D?style=flat&logo=Redis&logoColor=white">

#### Deployment
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=flat-square&logo=amazon-aws&logoColor=white)

#### Communication

<img src="https://img.shields.io/badge/Slack-4A154B?style=flat&logo=Slack&logoColor=white"> <img src="https://img.shields.io/badge/Notion-000000?style=flat&logo=Notion&logoColor=white"> <img src = "https://img.shields.io/badge/Swagger-6DB33F?style=flat-square&logo=Swagger&logoColor=white">


## 📀 ERD
![image](https://github.com/HanaView/.github/assets/71822139/bdde02af-8def-4616-b8f6-eb63a522a0a6)
- 요구사항 명세서를 기반으로 ERD를 작성했습니다.
- 서비스의 주체인 User와 Teller의 정보를 관리하는 2개의 테이블을 비롯한 총 16개의 테이블로 HanaView의 DataBase를 설계했습니다.


## ⚙️ 시스템 아키텍처
![image](https://github.com/HanaView/.github/assets/71822139/5acd52e3-9892-435b-96dc-744710b9e400)
- 요구사항의 빠른 반영을 위해 Docker와 GitHub Actions를 이용해 CI/CD 환경을 구축했습니다.
- 도커를 통해 서버의 이미지를 생성하고 이를 Hub에 등록한 후, Docker-Compose와 Github Actions를 이용하여 각각의 EC2 인스턴스에 서버를 배포했습니다.
- hanaview.shop이라는 도메인을 이용하며, Route53에 AWS Certificate Manager를 통해 발급받은 인증서를 연결하여 https로의 접근을 허용하였습니다.
- 더불어, Http 접근의 경우에는 로드밸런서를 통해 https로 리다이렉션 됨과 동시에 모든 접근은 로드밸런서를 통해 target-group 으로의 트래픽이 분산됩니다.


## 📃 API
![localhost_swagger-ui_index html_urls primaryName=USER%20API](https://github.com/HanaView/Back-End/assets/98458302/29369a7d-8ccb-41b8-a7f4-565b1e4de0ab)


