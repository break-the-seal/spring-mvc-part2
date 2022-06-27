package io.brick.springmvc.controller

import io.brick.springmvc.type.IpPort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping

@Controller
class ConverterController {
    @GetMapping("/converter-view")
    fun converterView(model: Model): String {
        model.addAttribute("number", 10000)
        model.addAttribute("ipPort", IpPort("127.0.0.1", 8080))

        return "converter-view"
    }

    @GetMapping("/converter/edit")
    fun converterForm(model: Model): String {
        val ipPort = IpPort("127.0.0.1", 8080)
        val form = Form(ipPort)
        model.addAttribute("form", form)

        return "converter-form"
    }

    @PostMapping("/converter/edit")
    fun converterEdit(
        @ModelAttribute form: Form,
        model: Model,
    ): String {
        val ipPort = form.ipPort
        model.addAttribute("ipPort", ipPort)
        return "converter-view"
    }
}

data class Form(
    val ipPort: IpPort
)