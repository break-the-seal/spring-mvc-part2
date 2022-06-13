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
  - `sessionStore` -> `Map<String, Any>`, key는 UUID로 생성
  - key 값만 Cookie에 저장 (`mySessionId`, key)
- 세션 조회
  - Cookie를 통해 들어온 mySessionId 이름의 쿠키 value 조회
  - key 값을 가지고 `sessionStore` 조회
- 세션 만료
  - `sessionStore`에서 해당 key를 가지고 remove

#### JUnit Test
```kotlin
val response = MockHttpServletResponse()
val request = MockHttpServletRequest().apply {
  setCookies(*response.cookies)
}
```
- mock객체를 이용해 HttpServletResponse, Request를 가져다 사용할 수 있다.

### 로그인 처리하기 - 직접 만든 세션 적용
- 세션이라는 것을 직접 구현해서 만들어봄(`SessionManager`)
- 서블릿에서 이러한 세션 기능을 지원해준다.

### 로그인 처리하기 - 서블릿 HTTP 세션 1 
- 서블릿에서 `HttpSession`을 제공 (`SessionManager`와 유사한 기능을 가짐)
- `HttpSession`을 생성하고 쿠키 생성(`Cookie: JSESSIONID=xxxxxxxx`)
```kotlin
request.getSession(true) // true(default), false 
```
- true: 세션이 있으면 기존 세션 반환, 없으면 새로운 세션 생성
- false: 세션이 있으면 기존 세션 반환, 없으면 null 반환

### 로그인 처리하기 - 서블릿 HTTP 세션 2
```kotlin
@SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false) member: Member?,
```
- 해당 기능은 세션을 생성하지 않는다.

#### TrackingModes
- 새로운 브라우저에서 로그인을 하면 `localhost:8080/;jsessionid=xxxx` url 뒤에 붙게 된다.
- 브라우저가 쿠키를 지원하지 않는 경우 경로를 통해 유지하기 위함(그런데 단점이 더 많음, url 뒤에 계속 유지해야 함)
```properties
server.servlet.session.tracking-modes=cookie
```
- 위 설정을 통해 url로 제공되는 jsessionid 내용을 아예 없앨 수 있다.

### 세션 정보와 타임아웃 설정
- session의 `maxInactiveInterval`, `lastAccessedTime`과 관련이 깊다.

#### 세션의 종료시점
  - 세션 생성시점을 기준으로 하기보다 사용자가 서버에 가장 최근에 요청한 시간 기준으로 타임아웃 설정하는 것이 합리적
```properties
server.servlet.session.timeout=1800 (초 단위)
```
- 글로벌 설정은 분단위(60초 단위)로 하는 것이 좋다.
```kotlin
session.setMaxInactiveInterval(1800)
```

#### 타임아웃 발생
- 세션 타임아웃은 JSESSIONID 전달하는 HTTP 요청이 있으면 현재 시간으로 다시 초기화
- `session.getLastAccessedTime()`
- 마지막 접근 시간 이후로 timeout 시간이 지나면 WAS가 내부에서 해당 세션 제거