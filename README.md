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