# Spring MVC (Part 2)

- Spring Boot `v2.7.0`
- kotlin `v1.6.21`

## 섹션 10. 스프링 타입 컨버터

### 스프링 타입 컨버터 소개

- 스프링에서는 문자열로 들어오는 파라미터 데이터를 원하는 타입으로 알아서 변환해준다.
- 스프링 타입 변환 적용 예시
  - 스프링 MVC 요청 파라미터(`@RequestParam`, `@ModelAttribute`, `@PathVariable`)
  - `@Value` yml 정보 읽기
  - 뷰 렌더링 할 때
- 스프링은 확장가능한 `Converter` 인터페이스를 제공 
  - 추가적인 타입 변환이 필요할 때 `Converter` 인터페이스를 구현해서 등록하면 된다.
  - X -> Y, Y -> X 구현하면 된다.
- 과거에는 `PropertyEditor`, but 동시성 문제(지금은 사용 X)

### 타입 컨버터 - Converter
- `Converter`, `ConverterFactory`, `GenericConverter`, `ConditionalGenericConverter`
- 스프링은 문자, 숫자, boolean, enum 등 일반적인 타입에 대한 대부분의 컨버터를 기본으로 제공해준다.

### 컨버전 서비스 - ConversionService
- 타입 컨버터를 하나하나 타입 변환에 사용하는 것은 번거로움
- 스프링은 개별 컨버터를 모아두고 그것을 묶어서 편리하게 사용할 수 있는 기능 제공(`ConversionService`)
- 등록과 사용으로 분리
```kotlin
// 등록
val conversionService = DefaultConversionService()
conversionService.addConverter(StringToIntegerConverter())
//...

// 사용
conversionService.convert("10", Int::class.java)
```
- 등록할 땐 converter를 알아야하지만 사용하는 입장에서는 몰라도 된다.

#### ISP 인터페이스 분리 원칙
> ISP: 클라이언트가 자신이 이용하지 않는 메서드에 의존하지 않아야 한다.

- `DefaultConversionService`
  - `ConversionService`: 사용에 초점
  - `ConverterRegistry`: 등록에 초점
- 등록과 사용 인터페이스를 분리하면 관심사를 명확히 분리 가능
- 사용하는 입장에서는 `ConversionService`만 알면 되기 때문에 ISP 준수

### 스프링에 Converter 적용하기
- Config 설정을 통해 converter를 스프링에 등록할 수 있다.
- 스프링은 수많은 기본 컨버터들을 제공, 컨버터를 추가하면 기본 컨버터보다 높은 우선순위를 가지게 됨
- `@RequestParam`: 이것을 처리하는 `ArgumentResolver`(`RequestParamMethodArgumentResolver`) 여기에서 `ConversionService`를 사용해 타입 변환