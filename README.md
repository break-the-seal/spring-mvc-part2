# Spring MVC (Part 2)

- Spring Boot `v2.7.0`
- kotlin `v1.6.21`

<br>

## Section 4. Validation

### BindingResult
- `BindingResult`는 view에 같이 넘어간다. 

```html
<!-- 기존 -->
th:if="${errors?.containsKey('itemName')}" th:text="${errors['itemName']}"
<!-- 변경 -->
th:errors="*{itemName}"
```

```html
<!-- 기존 -->
th:classappend="${errors?.containsKey('itemName')} ? 'field-error' : _"
<!-- 변경 -->
th:errorclass="field-error"
```
- `th:field`에 지정한 내용에 에러가 있으면 알아서 class로 지정해준다.

### BindingResult 2
- 스프링은 타입 오류로 바인딩에 실패하면 `FieldError`를 생성해서 사용자가 입력한 값을 넣는다. (`rejectedValue`)
- `FieldError`에는 두가지 생성자가 존재
  - 그 중에는 `rejectedValue`, `bindingFailure`, `codes`, `arguments` 등의 인자를 넣는 생성자 존재

### 오류 코드와 메시지 처리1
- `FieldError`, `ObjectError`에 `errors.properties`에서 지정한 값으로 메시지 처리를 할 수 있다.
```yaml
spring:
  messages:
    basename: messages,errors
```
- 설정에도 `errors`가 있으면 된다.
```kotlin
bindingResult.addError(
  FieldError(
    "item",
    "price",
    item.price,
    false,
    arrayOf("range.item.price"),    // codes
    arrayOf(1_000, 1_000_000),      // arguments
    null
  )
)
```

### 오류 코드와 메시지 처리 2
- `reject()`, `rejectValue()`를 통한 오류 처리 단순화
```kotlin
bindingResult.rejectValue("price", "range", arrayOf(1_000, 1_000_000), null)
```
- `field`, `errorCode` 두 개만 입력해줘도 알아서 설정내용을 찾아준다.
  - `range.item.price`
  - 어떻게 자동으로 완성되는지에 대해서는 `MessageCodesResolver`의 기능을 이해해야 한다.

### 오류 코드와 메시지 처리 3
```properties
required=필수 값 입니다.
required.item.itemName=상품 이름은 필수입니다.
```
- 범용적으로 사용하다가, 더 구체적인 오류코드가 있으면 구체적인 것을 사용  
(우선순위가 구체적인 것에 있음)
```kotlin
arrayOf("required.item.itemName", "required")
```
- 요런 식으로 맨 앞부터 오류코드를 찾게 됨(이걸 `rejectValue()`에서 알아서 해준다.)
- `MessageCodesResolver` 기능이 이러한 것들을 지원

### 오류 코드와 메시지 처리 4
```kotlin
bindingResult.rejectValue("itemName", "required")
```
- 내부적으로 `MessageCodesResolver` 사용

1. 객체 에러 (ObjectError)
```kotlin
/**
 * messageCode = required.item
 * messageCode = required
 */
val messageCodes = codesResolver.resolveMessageCodes("required", "item")
```
2. 필드 에러 (FieldError)
```kotlin
/**
 * messageCode = required.item.itemName
 * messageCode = required.itemName
 * messageCode = required.java.lang.String
 * messageCode = required
 */
val messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String::class.java)
```
- `rejectValue`를 하게되면 각각 `MessageCodesResolver`에서 찾은 codes를 `ObjectError`, `FieldError`에 담는다.

### 오류 코드와 메시지 처리 5
- rejectValue() 호출 -> MessageCodesResolver codes 사용
- 우선순위는 구체적(최우선) -> 범용적  
(개발에 있어서 범용적으로 개발하고 구체적인 부분은 따로 개발하도록 편의성 제공. 비즈니스 코드는 변경을 하지 않아도 에러 코드, 메시지에 대한 수정만 적용 가능)

### 오류 코드와 메시지 처리 6 (스프링이 직접 만든 error)
- Integer field에 String 입력시 `typeMismatch` codes를 자동으로 지정해준다.
- 여기서 중요한 것은 kotlin data class 사용해서 주생성자에 필드를 지정한 순간 kotlin default constructor가 작동을 안한다.  
  (왜 그런지는 이유 찾아봐야 할듯)
- class 사용해서 default constructor만 지정해둬야 한다.
```
typeMismatch.item.price
typeMismatch.price
typeMismatch.java.lang.Integer
typeMismatch
```

### Validator 분리 1
- Validator 인터페이스 구현을 통해 bindingResult 검증하는 부분을 따로 분리할 수 있다.
```java
public interface Validator {
  boolean supports(Class<?> clazz);
  void validate(Object target, Errors errors);
}
```

### Validator 분리 2
- 직접 생성자 생성을 하지 않고 Validator 인터페이스를 제공하는 이유는 스프링에서 체계적인 검증 기능 도입하기 위함
- `WebDataBinder`를 통해 검증 기능을 포함시킬 수 있음
```kotlin
@InitBinder
fun init(dataBinder: WebDataBinder) {
    dataBinder.addValidators(itemValidator)
}

fun addItemV6(
@Validated @ModelAttribute item: Item,
// ...
)
```
- **요청 때마다** initBinder가 항상 불려져서 검증기를 dataBinder에 넣어둔다.
- 해당 binding 되는 부분에 `@Validated` 적용
  - 검증기를 실행하라는 어노테이션
```kotlin
// config file
class AppConfig: WebMvcConfigurer {
  override fun getValidator() {
      return ItemValidator()
  }
}
```
- global validator 적용 (모든 controller에서 해당 지정한 validator를 추가)
- 참고
  - `@Validated`: 스프링 검증 애노테이션
  - `@Valid`: 자바 표준 검증 애노테이션(`javax`, `spring-boot-starter-validation`)

## Section 5. Bean Validation

### Bean Validation
- **Bean Validation 2.0(JSR-380)** 기술 표준
- 애노테이션과 여러 인터페이스의 모음(like JPA 표준기술 - hibernate)
- Bean Validation - Hibernate Validation 구현체 사용(ORM 관련 X)
```groovy
implementation 'org.springframework.boot:spring-boot-starter-validation'
```
- `jakarta validation` 기술 제공(인터페이스) - `hibernate validator`(구현체)
### Bean Validation 시작
- Validation Annotation
  - `javax.validation` 시작하는 것들은 특정 구현에 상관없이 제공되는 표준 인터페이스
  - `org.hibernate.validator` 시작하는 것들은 하이버네이트 validator 구현체를 사용할 때만 제공
```kotlin
val factory = Validation.buildDefaultValidatorFactory()
val validator = factory.validator

val violations: Set<ConstraintViolation<Item>> = validator.validate(item)
```
