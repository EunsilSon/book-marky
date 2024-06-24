# 북마키 (BookMarky)
마지막 업데이트 일자는 **24.06.24**이며, 현재 개발 진행 중인 프로젝트 입니다.  

<br>

## 목차
1. [프로젝트 소개](#프로젝트-소개)
2. [기술 스택](#기술-스택)
3. [개발 완료된 기능](#개발-완료된-기능)
4. [API 명세](#API-명세)
5. [ERD](#ERD)
6. [디렉터리 구조](#디렉터리-구조)


<br>

## 프로젝트 소개
**북마키**는 독서 중 마음에 드는 구절을 편리하게 기록하고 쉽게 접근할 수 있는 독서 도우미 서비스입니다.

- 책을 읽다가 마음에 드는 구절을 표시하고 싶지만, 책에 줄을 긋기는 꺼려질때
- 메모장에 적어두기엔 번거롭고 내용이 많아 찾기 힘들때
- 다른 장소에서 독서하기 위해 노트, 필기구 등 챙기기 번거로울때

북마키는 이러한 불편을 해소해줍니다.

<br>

> **“BookMarky”의 의미**  
책(Book)에서 마음에 드는 부분을 표시(Mark)하여 기억하고 보관할 수 있다는 뜻을 담은 이름입니다.  
"Marky"는 표시하다(Mark)와 친근한 느낌을 주는 접미사(-y)를 결합해, 사용자가 쉽게 기억하고 친숙하게 느낄 수 있도록 했습니다.
> 

<br>

## 기술 스택
※ 현재 사용 중인 기술입니다.

| 구분 | 기술 | 설명 |
| --- | --- | --- |
| Back-end | Spring Boot 3 | MVC 패턴의 REST API 서버 |
|  | Spring Data JPA |  |
|  | Spring Security | 사용자 인증 |
|  | JavaMailSender | 이메일 생성 및 전송 |
|  | Redis | 토큰 발급 및 관리 |
|  | MySQL |  |

<br>

## 개발 완료된 기능
※ ‘비고’에 기재된 포스팅에서 구현 과정을 확인할 수 있습니다.

| 기능 | 설명 | 비고 |
| --- | --- | --- |
| 사용자 인증 | 로그인 | [[Spring Security] Form Login 인증 구현하기](https://velog.io/@eunsilson/Spring-Security-Form-Login%EC%9D%84-%EC%BB%A4%EC%8A%A4%ED%85%80%ED%95%B4-%EC%9D%B8%EC%A6%9D-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0) |
|  |  | [[Security] Form Login 인증 Handler 구현하기](https://velog.io/@eunsilson/Security-Form-Login-%EC%9D%B8%EC%A6%9D-Handler-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0) |
|  | 회원가입 |  |
|  | 비밀번호 변경 | [[Spring] 비밀번호 재설정 링크를 이메일로 전송하기 (Redis, UUID 토큰)](https://velog.io/@eunsilson/Spring-%EB%B9%84%EB%B0%80%EB%B2%88%ED%98%B8-%EC%9E%AC%EC%84%A4%EC%A0%95-%EB%A7%81%ED%81%AC%EB%A5%BC-%EC%9D%B4%EB%A9%94%EC%9D%BC%EB%A1%9C-%EC%A0%84%EC%86%A1%ED%95%98%EA%B8%B0-Redis-UUID-%ED%86%A0%ED%81%B0) |

<br>

## API 명세
![image](https://github.com/EunsilSon/book-marky/assets/46162801/28336ddd-4be5-4f36-b52d-9cc68b4119b9)

<br>

## ERD
![erd](https://github.com/EunsilSon/book-marky/assets/46162801/c19a1a21-28dd-4adb-b0ac-7663912c4074)

<br>

## 디렉터리 구조
```
┌── config/   
│ ├── auth/   
│ │ ├── CustomUserDetails  
│ │ └── CustomUserDetailsService   
│ ├──handler/  
│ │ ├── CustomAuthenticationSuccessHandler  
│ │ └── CustomAuthenticationFailureHandler  
│ └── SecurityConfig  
├── controller/   
│ └── UserController  
├── domain/   
│ ├── dto/   
│ │ ├── PasswordResetResq  
│ │ ├── PasswordResetRes  
│ │ └── UserDTO  
│ └──entity/   
│    └── User  
├── repository/   
│ └── UserRepository  
└── service/  
  ├── UserService  
  ├── MailService  
  └── ResetTokenService  
```