# README

<img width="1024" height="1024" alt="룸버디_로고" src="https://github.com/user-attachments/assets/f9163740-b7f9-49a7-bf1d-77b4137b8ff1" />


---

## ✅ 룸버디

- 스터디룸 간편 예약 시스템
- 개인 토이 프로젝트

---

## 📅 프로젝트 기간

- 2024-08-24 ~ 2024-09-04

---

## 👦🏻 인원

- 개인 토이 프로젝트

---

## 🌐 배포 주소

- http://roombuddy.duckdns.org/

---

## 📽️ 서비스 시연 영상

- https://www.youtube.com/watch?v=lhdbn6R0nEs&t=48s

---

## 🦈 GitHub

BACKEND : https://github.com/crane0731/RoomBuddy 

FRONTEND : https://github.com/crane0731/RoomBuddy-Front

---

## 🖼️ 서비스 이미지

### 메인화면

<img width="1919" height="853" alt="image" src="https://github.com/user-attachments/assets/9f94d3db-be3f-4020-8fa7-2fe803abd917" />

### 로그인 , 회원가입

<img width="492" height="508" alt="image 1" src="https://github.com/user-attachments/assets/80288e4e-aef0-4495-852a-4816b7b2e3c7" />

<img width="492" height="750" alt="image 2" src="https://github.com/user-attachments/assets/8df05fe1-4212-44fb-8b62-9760f8ee4a74" />


### 이메일 인증

<img width="886" height="430" alt="image 3" src="https://github.com/user-attachments/assets/c7d16510-6735-489c-99bd-456ee8997e43" />


### 마이페이지

<img width="1807" height="782" alt="image 4" src="https://github.com/user-attachments/assets/1abc38ed-cd16-408a-a2eb-61789dcd60e2" />


### 관리자 페이지

<img width="1663" height="627" alt="image 5" src="https://github.com/user-attachments/assets/a5246ff6-8cd0-4e7f-afe1-d6b952d84b46" />

<img width="1745" height="712" alt="image 6" src="https://github.com/user-attachments/assets/3f216fb9-ef32-4faf-a25a-b3c0d8eee8b8" />

<img width="1784" height="757" alt="image 7" src="https://github.com/user-attachments/assets/f2661235-0345-4b44-b33a-b2bdff74a3fa" />

<img width="1758" height="764" alt="image 8" src="https://github.com/user-attachments/assets/37aa57e9-feae-4358-86c2-4694d3b037cd" />


---

## 🎯 프로젝트 소개

- 대학교 도서관 , 스터디 카페에서의 스터디룸 예약 시스템을 누구든 간편하게 이용 할 수 있도록 만든 개인 토이 프로젝트
- 회원 가입은 간단한 이메일 인증 작업을 통해 간단히 할 수 있다.
- 사용자들은 쉽게 스터디룸의 현 예약 상황과 예약을 진행/취소 할 수 있다.
- 관리자는 관리자 전용 페이지를 통해 회원 , 스터디룸 , 예약을 관리 하고 블랙아웃(특정 시간 예약 금지)을 제어 할 수 있다.

---

## 💡프로젝트 목적

- Spring Boot  + MyBatis 조합 기술을 익히고 실전에 적용 해보기 위함
- EC2 프리티어 + Git Action 을 통해 간단한 자동 배포 CI/CD 구축해보고 실제 배포까지 진행 해보기 위함

---

## ⚙️ 핵심 기능

- 로그인 (Spring Security + JWT + SMTP)
- 사용자
    - 특정 스터디룸의 예약 현황 확인
    - 특정 스터디룸의 특정 시간 예약 (1시간 단위로 가능 ex. 1 , 2 ,3 , 4) or 예약 취소
    - 마이페이지에서 나의 예약 확인 및 관리
- 관리자
    - 관리자 페이지
    - 회원 관리
    - 스터디룸 관리
    - 예약 진행 관리
    - 블랙 아웃(특정 시간 예약 불가) 제어

---

## 📄ERD

https://www.erdcloud.com/d/QbHWvYEn6iuajcPpj

<img width="639" height="474" alt="image 9" src="https://github.com/user-attachments/assets/d27a7964-29c7-4661-9f63-04ee388e8bca" />


---

## 🛠️기술 스택

### 🛠️ 기술 스택

**FRONTEND**

- Vue 3 (Composition API)
- Axios

**BACKEND**

- Spring Boot 3
- Spring Security
- JWT
- MyBatis
- Spring Mail
- Lombok

**DATABASE**

- MySQL 8
- Redis (JWT 블랙리스트 관리)

**CI/CD & DEPLOY**

- AWS EC2 (Ubuntu, Nginx, Free Tier)
- GitHub & GitHub Actions
- DuckDNS (도메인)

---

## 📝API 명세서

[API 명세서](https://www.notion.so/API-26513d3e3c1b8048aa13db877f78890b?pvs=21) 

---

## 🌐 시스템 아키텍쳐

<img width="1212" height="750" alt="룸버디_시스템_아키텍쳐" src="https://github.com/user-attachments/assets/a023e43b-1b1a-4a54-809c-68c41ac63fbd" />

---
