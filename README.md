<br>
<h1 align="center"> KPU 2021 컴퓨터 공학과 졸업 작품 </h1>
<h2 align="center"> 시각장애인을 위한 편의점 제품 인식 애플리케이션 🏪  </h2>
<br>

---
## ⭐️ **팀원 역할 및 소개** ⭐️
<table>
  <tr>
    <td align="center"><a href="https://github.com/contea95"><img src="https://avatars1.githubusercontent.com/u/64491659?s=400&u=3c39d4f9b95c74c1474c8cc90706155d600f00b8&v=4" width="100px;" alt=""/><br /><sub><b>한상혁</b></sub></a><br />서버/머신러닝</td>
    <td align="center"><a href="https://github.com/dasoopark"><img src="https://avatars3.githubusercontent.com/u/51106039?s=400&u=5c86308a2fa6e33b84bd1623658ffbb3cb19b960&v=4" width="100px;" alt=""/><br /><sub><b>박다수</b></sub></a><br />애플리케이션</td>
    <td align="center"><a href="https://github.com/1007ChaeMin"><img src="https://avatars0.githubusercontent.com/u/19782958?s=400&v=4" width="100px;" alt=""/><br /><sub><b>임채민</b></sub></a><br />애플리케이션</td>
    <td align="center"><a href="https://github.com/Jiwoon22"><img src="https://avatars1.githubusercontent.com/u/51106092?s=400&u=0e2a843114fc9ea40a85f702b4d56657bf4ea481&v=4" width="100px;" alt=""/><br /><sub><b>정지운</b></sub></a><br />데이터셋</td>
  </tr>
</table>

---
## 개발 방법
### **Agile Framework - Scrum 모델** 사용

#### 사용 협업 툴

1. [Github](https://github.com/kpuce2021/Dandelion)
2. [Trello](https://trello.com/b/BMWAK0oT)
3. [SpreadSheet](https://docs.google.com/spreadsheets/d/1auyVjzKxb7iPgwjhEjFhaKm2PBqUoI8BcHxVw9EPz6k/edit?usp=sharing)
4. Slack

---
## 시스템 설계
![system_design](./Document/210123시스템설계_목표버전.png)

---
## 서비스 WorkFlow
- 이미지 추가 예정

---
## Foldering
```

```
---
## 기능 개발 여부 / 담당자
|기능|상세 기능|담당자|구현 여부|
|:--:|:------:|:---:|:-------:|
|데이터셋|딥러닝을 위한 제품 사진 준비|정지운|O|
|딥러닝|데이터셋을 토대로 모델 학습|한상혁|구현 중|
|카메라|촬영한 사진을 네트워크로 전송 가능하도록 resize|임채민|O|
|전송|reszie된 사진을 서버로 전송|임채민|O|
|수신|서버에서 수신된 값을 액티비티에 표출|임채민|O|
|UI|장애인 접근성을 토대로한 UI 제작|박다수|X|

---

## 사용 환경 및 사용 라이브러리
|라이브러리|목적|사용환경|
|:--------:|:---:|:---:|
|Retrofit2|HTTP 통신|Android|
|Glide|Image 호출|Android|
|Flask|Web Server|Server|
|TensorFlow|Deep Learning|Server|

---
