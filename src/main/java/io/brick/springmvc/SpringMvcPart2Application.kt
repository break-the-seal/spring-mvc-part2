package io.brick.springmvc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringMvcPart2Application {
/*
    @Bean
    fun messageSource(): MessageSource {
        return ResourceBundleMessageSource().apply {
            setBasenames("messages", "errors")
            setDefaultEncoding("utf-8")
        }
    }
*/
}

fun main(args: Array<String>) {
    runApplication<SpringMvcPart2Application>(*args)
}

