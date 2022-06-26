package io.brick.springmvc.controller

import io.brick.springmvc.type.IpPort
import mu.KLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class HelloController {
    companion object: KLogging()

    @GetMapping("/hello-v1")
    fun helloV1(request: HttpServletRequest): String {
        val data: String? = request.getParameter("data")    // 문자열로 처리
        val intValue = data?.toInt()

        logger.info { "data $intValue" }
        return "ok"
    }

    @GetMapping("/hello-v2")
    fun helloV2(@RequestParam data: Int): String {
        logger.info { "data $data" }
        return "ok"
    }

    @GetMapping("/ip-port")
    fun ipPort(@RequestParam ipPort: IpPort): String {
        logger.info { "ipPort IP = ${ipPort.ip}" }
        logger.info { "ipPort PORT = ${ipPort.port}" }

        return "ok"
    }
}