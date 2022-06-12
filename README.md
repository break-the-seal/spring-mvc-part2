# Spring MVC (Part 2)

- Spring Boot `v2.7.0`
- kotlin `v1.6.21`

<br>

## 📌 Section 6. 로그인 처리1 - 쿠키, 세션

### 로그인 기능
```kotlin
// LoginService.kt
fun login(loginId: String, password: String): Member? {
    return memberRepository.findByLoginId(loginId)?.takeIf {
        it.password == password
    }
}
```
- kotlin takeIf 확장함수를 통한 조건식 적용

### 로그인 처리하기 - 쿠키 사용
- 쿠키
  - 영속 쿠키: 만료 날짜를 입력하면 해당 날짜까지 유지
  - 세션 쿠키: 만료 날짜 생략하면 브라우저 종료시 까지만 유지

### 쿠키와 보안 문제
- 쿠키는 조작이 가능하다.
- 쿠키에 보관된 정보는 훔쳐갈 수 있음(hijacking이 가능)
- 해커가 쿠키를 한 번 훔쳐가면 그 쿠키로 악의적인 요청을 계속 시도 가능

### 로그인 처리하기 - Session 동작 방식
- 로그인 정보를 session에 key-value 형태로 저장
- 클라이언트에 응답할 때 session key 값을 반환해준다. (예측 불가능한 key 값)
- 클라이언트는 해당 session key 값을 쿠키 헤더에 담아서 요청하고 서버는 해당 key 값을 가지고 세션 조회

### 로그인 처리하기 - Session 직접 만들기
- 세션 생성
- 세션 조회
- 세션 만료