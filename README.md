# Spring MVC (Part 2)

- Spring Boot `v2.7.0`
- kotlin `v1.6.21`

## 📌 섹션 11. 파일 업로드

### 파일 업로드 소개

- form 형태
  - `application/x-www-form-urlencoded`
    - `&`로 이어서 parameter 형태로 보냄
    - 파일 같은 바이너리 데이터를 전송하기에 부적합
    - 텍스트와 바이너리 데이터를 동시에 보낼 때에도 부적합
  - `multipart/form-data`
    - 문자와 바이너리 데이터를 동시에 전송
```text
POST /save HTTP/1.1
Host: localhost:8080
Content-Type: multipart/form-data;boundary=------XXX
Content-Length: .....

------XXX
Content-Disposition: form-data; name="username"

lee
------XXX
Content-Disposition: form-data; name="age"

20
------XXX
Content-Disposition: form-data; name="file1"; filename="intro.png"
Content-Type: image/png

12ughyd988o1yjdhsba89129idhkf189yfdjkbf893yr9igkd8912...
------XXX--
```
- multipart는 여러 개의 형태 데이터를 동시에 보낼 수 있음

### 서블릿과 파일 업로드 1
- form을 multipart로 지정하면 컨트롤러 request는 다음 클래스로 들어온다.  
  `org.springframework.web.multipart.support.StandardMultipartHttpServletRequest`

#### 멀티파트 사용 옵션
- 업로드 사이즈 제한
```properties
# 파일 하나의 최대 사이즈, 기본 1MB
spring.servlet.multipart.max-file-size=1MB
# 멀티파트 요청 하나에 여러 파일을 업로드, 그 전체 합에 대한 설정
spring.servlet.multipart.max-request-size=10MB
```
- 사이즈를 넘으면 `SizeLimitExceededException` 발생
```properties
spring.servlet.multipart.enabled=false
```
- 기본은 true, false 설정시 multipart 기능 비활성화
- `DispatcherServlet`에서 `MultipartResolver`를 통해 `HttpServletRequest` -> `MultipartHttpServletRequest`로 변환
- `MultipartFile`이 더 편해서 이걸 더 자주 사용

### 서블릿과 파일 업로드 2
- `ServletUploadControllerV2` 참고

### 스프링과 파일 업로드
- 스프링은 `MultipartFile` 인터페이스로 멀티파트 파일을 편리하게 지원

### 예제로 구현하는 파일 업로드, 다운로드
- `ItemForm`: 컨트롤러에서 `@ModelAttribute`로 받는 form parameter 데이터
  - 여기에 `MultipartFile`로 받을 수 있음

```html
<input type="file" multiple="multiple" name="imageFiles" >
```
- multiple 옵션을 넣으면 여러 개 파일을 한꺼번에 업로드 가능