package io.brick.springmvc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.support.ResourceBundleMessageSource

@SpringBootApplication
class SpringMvcPart2Application {

//    @Bean
//    fun messageSource(): MessageSource {
//        val messageSource = ResourceBundleMessageSource()
//        messageSource.setBasenames("messages", "errors")  // messages.properties, errors.properties 인식
//        messageSource.setDefaultEncoding("utf-8")
//
//        return messageSource
//    }
}

fun main(args: Array<String>) {
    runApplication<SpringMvcPart2Application>(*args)
}