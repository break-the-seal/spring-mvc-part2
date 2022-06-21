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
> 1. 컨트롤러 RuntimeException 발생 -> 인터셉터 -> 서블릿 -> 필터 -> WAS(tomcat)  
> 2. WAS(tomcat) `/error-page/500` 다시 요청 -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러 `/error-page/500` 호출 -> View render

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

### 서블릿 예외 처리 - 필터
- 위에 작동순서에 따라서 컨트롤러에 오류가 발생하면 오류 페이지를 출력하기 위해 WAS 내부에서 request 호출이 다시 발생
- 불필요한 필터, 인터셉터 과정이 있음(로그인 인증 체크 같은 경우는 에러 발생시 불필요하게 두 번 하는 꼴)
- `DispatcherType` 추가 정보를 제공해서 문제 해결

#### DispatcherType
- filter는 `dispatcherTypes`를 제공해준다.
```kotlin
logger.info { "dispatchType=${request.dispatcherType}" }
```
- 고객 처음 요청시 `REQUEST`, `DispatcherType` enum class 참고

```kotlin
// 필터 등록시 filter 적용할 DispatcherType 설정 가능
filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR)
```
- 아무것도 넣지 않으면 기본으로 `REQUEST`만 설정

```text
1. GET http://localhost:8080/error-ex
2. request: DispatcherType[REQUEST] <<
3. response: DispatcherType[REQUEST] <<
4. GET /error-page/500 (WAS 내부 호출)
5. request: DispatcherType[ERROR] <<
6. response: DispatcherType[ERROR] <<
```
- `DispatcherType`을 `REQUEST`, `ERROR` 두 개 설정한 경우에 해당

### 서블릿 예외 처리 - 인터셉터
```text
> 1. 컨트롤러 예외 발생 -> 인터셉터 -> 서블릿 -> 필터 -> WAS(tomcat): WAS까지 예외 전파  
> 2. WAS(tomcat) `/error-page/500` 다시 요청 -> 필터(x) -> 서블릿 -> 인터셉터(x) -> 컨트롤러 `/error-page/500` 호출 -> View render
```
- 인터셉터에서도 등록시 `excludePathPatterns`에 error-page 경로를 등록하면 인터셉터도 통과한다. 

> 결국 WAS에서 에러 페이지를 다시 내부 호출할 때에는 필터, 인터셉터 둘다 사용하지 않고도 호출이 가능해진다.

### 스프링 부트 - 오류 페이지 1
- 위의 서블릿 예외처리는 번거롭다.
  - `WebServerCustomizer`를 통해 에러와 에러 페이지를 매핑
  - 에러 페이지를 위한 `ErrorPageController`를 따로 구성
- 스프링 부트는 이런 과정을 기본으로 제공
  - `ErrorPage` 자동 등록(`/error`, 기본 설정 에러 경로)
  - 예외 발생 혹은 `response.sendError(...)` 발생시 모두 `/error` 호출
  - `BasicErrorController` 자동 등록: `/error`경로를 매핑해서 처리하는 컨트롤러
- `ErrorMvcAutoConfiguration`: 여기서 오류 페이지를 자동 등록

#### 뷰 선택 우선순위(`BasicErrorController`)
1. 뷰 템플릿
   - `resources/templates/error/500.html`
   - `resources/templates/error/5xx.html`
2. 정적 리소스(static, public)
   - `resources/static/error/400.html`
   - `resources/static/error/404.html`
   - `resources/static/error/4xx.html`
3. 적용 대상이 없을 때 뷰 이름(error)
   - `resources/templates/error.html`

### 스프링 부트 - 오류 페이지 2
- `BasicErrorController` 여기에서 model에 담아 에러 관련 여러 정보들을 뷰에 전달해준다.
```html
<li th:text="|timestamp: ${timestamp}|"></li>
<li th:text="|path: ${path}|"></li>
<li th:text="|status: ${status}|"></li>
<li th:text="|message: ${message}|"></li>
<li th:text="|error: ${error}|"></li>
<li th:text="|exception: ${exception}|"></li>
<li th:text="|errors: ${errors}|"></li>
<li th:text="|trace: ${trace}|"></li>
```
- 여기서 몇개는 null로 나온다. (오류 관련 내부 정보들을 사용자에게 노출하는 것은 보안상 문제 발생)
- yaml 설정에서 이를 조절할 수 있다.
```yaml
server:
  error:
    include-exception: true
    include-message: always
    include-stacktrace: always
    include-binding-errors: always
```
- 혹여나 공통 처리 컨트롤러 기능을 customize 하고 싶을 때
  - `ErrorController` 인터페이스 구현
  - `BasicErrorController` 상속 기능 추가

<br>

## 📌 섹션 9. API 예외 처리

### 시작
- @RestController api 전용 컨트롤러 대상
- 위에 상태로 RuntimeException 발생시 `/error-page/500`에서 html 코드 자체를 반환하게 된다.
```kotlin
@RequestMapping(value = ["/error-page/500"], produces = [MediaType.APPLICATION_JSON_VALUE])
```
- `Accept: application/json`을 통해 `ResponseEntity`를 반환받게 한다.
- JSON 형태로 에러 응답을 받게 된다.