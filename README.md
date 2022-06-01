# Spring MVC (Part 2)

- Spring Boot `v2.7.0`
- kotlin `v1.6.21`

<br>

## Section 3. 메시지, 국제화

- 스프링은 기본적인 메시지 관리 기능을 제공한다.

### 스프링 메시지 소스 사용
```kotlin
val result = ms.getMessage("hello", null, Locale.KOREA)
```
- properties key이름과 `Locale`을 정하면 해당 이름을 가져온다.
- `Locale`에 null로 지정하면 default인 `messages.properties` 내용을 가져온다.  
 근데 OS에 따라 default로 지정된 언어를 가지고 `Locale`이 정해지는 경우가 있는 듯 하다.

```kotlin
val result = ms.getMessage("no_code", null, null)
// default value
val result = ms.getMessage("no_code", null, "default message", null)
```
- 없는 key 값을 지정하면 `NoSuchMessageException` 에러 발생
- default value를 지정할 수 있다.

### 웹 어플리케이션에 국제화 적용하기
- `MessageSource`는 `Locale`정보에 기반해서 언어를 선택하고 해당하는 국가 언어의 값을 가져온다.
- Locale 정보를 알아내기 위해 `Accept-Language` 헤더를 참조한다.
- `LocaleResolver`: 스프링은 `Locale` 선택 방식을 변경할 수 있도록 인터페이스 제공
  - 기본적으로 `AcceptHeaderLocaleResolver`를 사용