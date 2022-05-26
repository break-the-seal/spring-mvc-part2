package io.brick.springmvc.basic

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/basic")
class BasicController {

    @GetMapping("/text-basic")
    fun textBasic(model: Model): String {
        model.addAttribute("data", "Hello Spring!")
        return "basic/text-basic"
    }

    @GetMapping("/text-unescaped")
    fun textUnescaped(model: Model): String {
        model.addAttribute("data", "Hello <b>Spring!</b>")
        return "basic/text-unescaped"
    }
}