package io.brick.springmvc.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {
    @GetMapping("/")
    fun home(): String {
        return "home"
    }
}