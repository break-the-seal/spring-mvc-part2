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

### 스프링 부트 기본 오류 처리
- 스프링 부트에서 `BasicErrorController`에서 기본적으로 API에 대한 오류 처리도 자동으로 수행해준다.
- `Accept` header에 따라 오류 응답을 다르게 받아 볼 수 있다.
  - `text/html`, `application/json`
```java
// text/html 경우 (/templates/error/... 경로에 있는 html을 보여준다.)
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {...}
// 그외의 경우 ResponseEntity에 여러 에러 관련 정보들을 넣어서 응답(status, timestamp, error, ...)
@RequestMapping
public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {...}
```
- `BasicErrorController`를 확장해서 JSON 메시지를 변경할 수도 있다는 것만 알아두자.
- `BasicErrorController` 기능은 HTML 페이지 제공하는 경우에만 사용
- `@ExceptionHandler` API 오류 처리할 때는 어노테이션 방식으로 사용

### HandlerExceptionResolver 시작
- 컨트롤러에서 `IllegalArgumentException` 던지면 `500` 에러 응답(서버에서 발생한 에러이기에)
- 이것을 `4xx` 에러로 변경할 수 있다.

![Screen Shot 2022-06-23 at 12 04 08 AM](https://user-images.githubusercontent.com/41675375/175072463-cb4b7713-646b-4556-8df5-11362b8b76e8.png)

```kotlin
if (ex is IllegalArgumentException) {
    logger.info { "IllegalArgumentException resolver to 400" }
    response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.message)
    return ModelAndView()
}
```
- HandlerExceptionResolver 인터페이스 구현체 내용
- `ModelAndView` 반환하는 것은 try ~ catch 하듯이 Exception을 처리해서 정상 흐름처럼 변경  
(WAS에는 정상 응답, `sendError` 보고 다시 `/error` 호출)
- 반환 내용에 따라 달라짐
  - `ModelAndView()`: 뷰 랜더링 X, WAS에 정상 흐름 응답
  - `ModelAndView(...)`: 뷰 랜더링
  - `null`: 다음 ExceptionResolver를 찾음, 없는 경우 예외 처리 X, 서블릿 바깥으로 예외 던지게 됨
- `ExceptionResolver`에서 다양하게 처리 가능
  - 예외 상태코드 변환  
  `response.sendError(...)`를 통해 서블릿에서 상태 코드에 따른 오류 처리하도록 위임
  - 뷰 템플릿 처리  
  바로 오류 화면을 사용자에게 제공
  - API 응답 처리  
  `response.writer.println("message")` HTTP 응답 바디에 데이터 직접 넣어주는 것도 가능  
    (json 형태로 담아서 응답 가능)
- `WebConfig`
  - `extendHandlerExceptionResolvers` 이걸 사용하자(`configureHandlerExceptionResolvers` X)

### HandlerExceptionResolver 활용
- 예외 발생 > WAS까지 예외 전달 > WAS 오류 페이지 정보 조회 > `/error` 호출
- 이 과정 자체가 복잡
- `ExceptionResolver`을 통해 여기서 깔끔하게 예외 처리를 끝낼 수 있다.  
(`UserHandlerExceptionResolver` 참고)
- 하지만 복잡하게 구현을 해야되는데 **스프링이 제공하는** `ExceptionHandler`를 사용하면 간편하게 적용가능

### 스프링이 제공하는 ExceptionResolver 1
- 스프링 부트가 기본적으로 제공하는 `ExceptionHandler`(우선순위 기준)
  - `ExceptionHandlerExceptionResolver`
    - `@ExceptionHandler` 처리(대부분 이 기능 사용)
  - `ResponseStatusExceptionResolver`
    - HTTP 상태코드 지정(`@ResponseStatus(value = HttpStatus.NOT_FOUND)`)
  - `DefaultHandlerExceptionResolver` -> 우선순위 가장 낮다

#### ResponseStatusExceptionResolver
- `@ResponseStatus`가 있는 예외
  - `response.sendError`를 통해 처리
  - yaml 설정파일에 `include-message: always` 하면 설정한 메시지도 응답으로 보내준다.
  - `BadRequestException` 참고(`MessageSource` 기능도 사용)
- `ResponseStatusException` 예외

### 스프링이 제공하는 ExceptionResolver 2

#### DefaultHandlerExceptionResolver
- 스프링 내부에서 발생한 예외를 처리해주는 `ExceptionResolver`
- 대표적으로 `TypeMismatchException`
  - `500` 에러로 처리가 되는데 스프링은 이것을 `400(Bad Request)` 에러로 변환해서 반환해준다.
- `response.sendError(...)`로 해결

### @ExceptionHandler
#### API 예외 처리에 있어서 어려운 점
- `HandlerExceptionResolver`에서 `ModelAndView`를 반환해야 한다는 점(API에선 불필요)
- response에 직접 응답 데이터를 넣어주는 작업 필요(`UserHandlerExceptionResolver` 참고)
- 특정 컨트롤러에서만 발생하는 예외 별도 처리가 어려움(도메인 별 에러를 따로 관리하고 싶을 때)
- `@ExceptionHandler`가 이러한 어려운 점들을 해결해줌

```kotlin
@ExceptionHandler(IllegalArgumentException::class)
fun illegalExHandler(e: IllegalArgumentException): ErrorResult {
    logger.info { "[exceptionHandler] ex $e" }
    return ErrorResult("BAD", e.message)
}
```
- 이것만 설정하면 `200 OK` 정상 응답이 되어버림(예외를 잡아서 response 해준 것이기에)
- `@ResponseStatus` 를 붙여야 한다.

```kotlin
@ExceptionHandler
fun userExHandler(e: UserException): ResponseEntity<ErrorResult> {
    logger.error { "[exceptionHandler] ex $e" }
    val errorResult = ErrorResult("USER-EX", e.message)
    return ResponseEntity(errorResult, HttpStatus.BAD_REQUEST)
}
```
- `@ExceptionHandler` 지정한 예외를 생략하면 인자에 지정한 예외를 핸들링 해준다.(`UserException`)
- `@ExceptionHandler` 여기에 잡은 Exception의 자식 클래스까지 전부 핸들링 가능
```kotlin
@ExceptionHandler(AException::class, BException::class)
```
- 여러 개의 예외를 한꺼번에 핸들링 할 수 있다.

### @ControllerAdvice
- 여러 컨트롤러에서 발생하는 에러들을 한꺼번에 처리해준다.

#### 특정 컨트롤러만 지정
```kotlin
@ControllerAdvice(annotations = [RestController::class]) // 특정 어노테이
@ControllerAdvice("org.example.controllers") // 특정 패키지(하위 패키지 포함)
@ControllerAdvice(assignableTypes = [ControllerInterface::class, AbstracController::class]) // 특정 컨트롤러 타입(하위 클래스 포함)
```