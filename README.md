# Spring MVC (Part 2)

- Spring Boot `v2.7.0`
- kotlin `v1.6.21`

<br>

## 📌 Section 6. 로그인 처리1 - 쿠키, 세션

### 로그인 기능
```kotlin
// LoginService.kt
fun login(loginId: String, password: String): Member? {
    return memberRepository.findByLoginId(loginId)?.takeIf {
        it.password == password
    }
}
```
- kotlin takeIf 확장함수를 통한 조건식 적용