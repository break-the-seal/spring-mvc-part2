package io.brick.springmvc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringMvcPart2Application {

    // spring에서 MessageSource를 자동으로 등록을 해준다.
/*
    @Bean
    fun messageSource(): MessageSource {
        return ResourceBundleMessageSource().apply {
            setBasenames("messages", "errors")  // messages.properties, errors.properties 인식
            setDefaultEncoding("utf-8")
        }
    }
*/
}

fun main(args: Array<String>) {
    runApplication<SpringMvcPart2Application>(*args)
}

