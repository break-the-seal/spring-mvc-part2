# Spring MVC (Part 2)

- Spring Boot `v2.7.0`
- kotlin `v1.6.21`

<br>

## Section 2. 스프링 통합과 폼

```groovy
implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
```
- thymeleaf spring boot starter를 설정하면 타임리프 관련 viewResolver와 자잘한 설정들을 자동으로 해준다.

### 체크 박스
- checkbox를 비활성화하고 form을 넘기면 Boolean에 대해서 `null`로 받아와 지는 문제가 발생

#### 해결방법 1
```html
<input type="hidden" name="_open" value="on"/>
```
- `_open`으로 된 hidden input을 지정한다.
- `open=on&_open=on`: `_open` 무시
- `_open=on`: _`open`을 통해 false로 Spring에서 인식
- **이 방식은 hidden 필드를 붙여야 한다는 점에서 번거로움**

#### 해결방법 2
```html
<input type="checkbox" id="open" th:field="*{open}" class="form-check-input">
```
- thymeleaf로 해결
- 위의 `_open` hidden 필드를 thymeleaf가 자동으로 등록해준다.

### 체크 박스 멀티
```html
<!-- item.html -->
<div th:each="region : ${regions}" class="form-check form-check-inline">
    <input type="checkbox" th:field="${item.regions}" th:value="${region.key}" class="form-check-input" disabled>
    <label th:for="${#ids.prev('regions')}"
           th:text="${region.value}" class="form-check-label">서울</label>
</div>
```
- `th:field`에 지정한 `item.regions`안에 들어간 값들을 가지고 `th:value`의 값의 contains 체크 유무에 따라 `checked`를 알아서 넣어준다.
- id 같은 경우 `regions` 필드 이름에 뒤에 숫자를 알아서 붙여준다.
- `label` 같은 경우 `input` id와 일치시키기 위해 `#ids.prev()`, `#ids.next()`를 통해 할당된 id를 자동으로 매칭 시킬 수 있다.

### 라디오 버튼
```html
<div th:each="type: ${T(io.brick.springmvc.domain.item.ItemType).values()}">
```
- enum type인 경우 thymeleaf에서 직접 enum class 호출을 할 수 있다.