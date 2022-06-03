package io.brick.springmvc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.support.ResourceBundleMessageSource

@SpringBootApplication
class SpringMvcPart2Application

fun main(args: Array<String>) {
    runApplication<SpringMvcPart2Application>(*args)
}