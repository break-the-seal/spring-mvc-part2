package io.brick.springmvc.servlet

import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import javax.servlet.http.HttpServletResponse

@Controller
class ServletExController {

    companion object : KLogging()

    @GetMapping("/error-ex")
    fun errorEx() {
        throw java.lang.RuntimeException("예외 발생!")
    }

    @GetMapping("/error-404")
    fun error404(response: HttpServletResponse) {
        response.sendError(HttpStatus.NOT_FOUND.value(), "404 error!")
    }

    @GetMapping("/error-400")
    fun error400(response: HttpServletResponse) {
        response.sendError(HttpStatus.BAD_REQUEST.value() , "400 error!")
    }

    @GetMapping("/error-500")
    fun error500(response: HttpServletResponse) {
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "500 error!")
    }
}