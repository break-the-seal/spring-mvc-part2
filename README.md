# Spring MVC (Part 2)

- Spring Boot `v2.7.0`
- kotlin `v1.6.21`

## 📌 섹션 8. 예외 처리와 오류 페이지

### 서블릿 예외 처리 - 시작
- 먼저 애플리케이션에서 발생한 예외가 처리되지 못하고(`try ~ catch`) 서블릿 바깥까지 전달되는 경우를 알아야 한다.
- 서블릿 바깥은 WAS까지 예외가 도달하는 상황

```properties
server.error.whitelabel.enabled=false
```

- 스프링 부트가 제공하는 기본 예외 페이지 기능 `false` 처리
- 톰캣이 예외를 처리하게 된다.
- `response.sendError(http_status_code, error_description)`
  - 이런 방식으로 서블릿 컨테이너에게 상태코드, 에러메시지를 전달해줄 수 있다.
  - Exception이 실제 발생한 것은 아님
  - 서블릿 컨테이너가 마지막에 `sendError`를 확인하고 그에 맞는 오류 페이지를 랜더링 해줌
- 결론) 서블릿은 `Exception` 발생 or `response.sendError` 를 통해 오류를 감지해서 사용자에게 그에 맞는 오류 화면을 보여준다.

### 서블릿 예외 처리 - 오류 페이지 작동 원리
- `WebServerCustomizer`: 스프링부트가 톰캣에 설정한 내용대로 에러페이지를 등록해준다.
```kotlin
val errorPageEx = ErrorPage(RuntimeException::class.java, "/error-page/500")
```
- `RuntimeException` 에 대해서 에러 경로를 설정해준 내용

#### 작동 순서
1. 컨트롤러 RuntimeException 발생 -> 인터셉터 -> 서블릿 -> 필터 -> WAS(tomcat)  
2. WAS(tomcat) `/error-page/500` 다시 요청 -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러 `/error-page/500` 호출 -> View render

#### Request를 통한 에러 내용 확인
```kotlin
const val ERROR_EXCEPTION = "javax.servlet.error.exception"
const val ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type"
const val ERROR_MESSAGE = "javax.servlet.error.message"
const val ERROR_REQUEST_URI = "javax.servlet.error.request_uri"
const val ERROR_SERVLET_NAME = "javax.servlet.error.servlet_name"
const val ERROR_STATUS_CODE = "javax.servlet.error.status_code"
```
- 위의 request 속성 키값을 이용해 에러 내용을 확인할 수 있다.