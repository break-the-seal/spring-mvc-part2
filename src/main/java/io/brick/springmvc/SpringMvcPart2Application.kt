package io.brick.springmvc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringMvcPart2Application {
    // WebMvcConfigurer
//    override fun getValidator(): Validator {
//        return ItemValidator()
//    }
}

fun main(args: Array<String>) {
    runApplication<SpringMvcPart2Application>(*args)
}

