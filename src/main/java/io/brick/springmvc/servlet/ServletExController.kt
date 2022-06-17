package io.brick.springmvc.servlet

import mu.KLogging
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import javax.servlet.http.HttpServletResponse

@Controller
class ServletExController {
    companion object: KLogging()

    @GetMapping("/error-ex")
    fun errorEx() {
        throw RuntimeException("예외 발생!")
    }

    @GetMapping("/error-404")
    fun error404(response: HttpServletResponse) {
        response.sendError(404, "404 error!")
    }

    @GetMapping("/error-500")
    fun error500(response: HttpServletResponse) {
        response.sendError(500, "500 error!")
    }
}