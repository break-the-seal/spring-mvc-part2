# Spring MVC (Part 2)

- Spring Boot `v2.7.0`
- kotlin `v1.6.21`

<br>

## 📌 Section 4. Validation

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
```text
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

<br>

## 📌 Section 5. Bean Validation

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

### Bean Validation - 스프링 적용
```kotlin
@field:NotBlank
@field:NotNull
@field:Range(min = 1_000, max = 1_000_000)
```
- 이러한 어노테이션만으로 Validator 등록 없이 Validation 기능 사용 가능
- `@Validated`, `@Valid`가 붙어 있어야 한다.

#### 검증 순서(중요)
- `@ModelAttribute` 각 필드에 타입 변환 시도
  - 성공하면 다음으로
  - 실패하면 `typeMismatch` -> `FieldError` 추가
- Validator 적용
- (여기서 바인딩에 실패한 필드는 Bean Validation을 적용하지 않는다.)
- **Kotlin에서 확정형으로 하는 경우 NotNull validation 적용이 안되는 이유**
> typeMismatch --> Bean Validation 진행 X  
> typeMismatch X --> Bean Validation 진행

### Bean Validation - 에러 코드
- MessageCodesResolver가 Bean Validation에 대한 코드를 생성해준다.
```text
-> @NotBlank
NotBlank.item.itemName
NotBlank.itemName
NotBlank.java.lang.String
NotBlank
```

### Bean Validation - 오브젝트 오류
```kotlin
@ScriptAssert(
  lang = "javascript", 
  script = "_this.price * _this.quantity >= 10000", 
  message = "총합이 100000원 이상 입력해주세요."
)
class Item {
    //...
}
```
- Script 표현식을 통해 ObjectError를 만들 수 있음
```text
ScriptAssert.item
ScriptAssert
```
- 하지만 실무에서는 비추천(해당 객체 범위를 넘어서는 경우도 존재, 사용하기 복잡)
- **직접 Controller 단 코드에 적용하는 것을 추천**

### Bean Validation - 한계
- 수정시 id not null 조건 & quantity 무제한으로 요건 변경
```kotlin
@NotNull // 수정 요구사항 추가
var id: Long = 0L

@NotNull
// @Max(9_999) // 수정 요구사항 추가
var quantity: Int? = null
```
- 이런 경우 생성시에 문제 발생(id 값이 생성전이라 없음, 생성 때는 quantity max 제한 불가)

### Bean Validation - groups
- 바로 위의 상황을 해결하는 두 가지 방법
  - Bean Validation groups 적용
  - Dto를 생성, 수정시 분리(ItemSaveForm, ItemUpdateForm)

#### groups 적용

```kotlin
// Item
@NotNull(groups = [UpdateCheck::class]) // 수정 요구사항 추가
var id: Long = 0L

// Controller
@Validated(UpdateCheck::class) @ModelAttribute item: Item
```
- 위 방식으로 save, update 상황 따로따로 Bean Validation을 적용할 수 있다.
- 하지만 이러한 방식은 개발하기 까다롭고 복잡해보임 (Dto 분리를 하는 것이 깔끔)