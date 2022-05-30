package io.brick.springmvc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringMvcPart2Application {

    // spring에서 MessageSource를 자동으로 등록을 해준다.
    // customizing을 원할시 properties 설정에서 바꿀 수 있음(doc 참고)
/*
    @Bean
    fun messageSource(): MessageSource {
        return ResourceBundleMessageSource().apply {
            setBasenames("messages", "errors")  // messages_en.properties, errors.properties 인식
            setDefaultEncoding("utf-8")
        }
    }
*/
}

fun main(args: Array<String>) {
    runApplication<SpringMvcPart2Application>(*args)
}

