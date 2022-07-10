package io.brick.springmvc.controller

import mu.KLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class HelloController {
    companion object : KLogging()

    @GetMapping("/hello-v1")
    fun helloV1(request: HttpServletRequest): String {
        val data: String? = request.getParameter("data")
        val intValue = data?.toInt()

        logger.info { "data $intValue" }
        return "ok"
    }

    @GetMapping("/hello-v2")
    fun helloV2(@RequestParam data: Int): String {
        logger.info { "data $data" }
        return "ok"
    }
}