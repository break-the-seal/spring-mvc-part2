package io.brick.springmvc.controller

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.NumberFormat
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import java.time.LocalDateTime

@Controller
class FormatterController {

    @GetMapping("/formatter/edit")
    fun formatterForm(model: Model): String {
        val form = FormatterForm(number = 10000, localDateTime = LocalDateTime.now())
        model.addAttribute("form", form)
        return "formatter-form"
    }

    @PostMapping("/formatter/edit")
    fun formatterEdit(@ModelAttribute(value = "form") form: FormatterForm): String {
        return "formatter-view"
    }
}

data class FormatterForm(
    @field:NumberFormat(pattern = "###,###")
    val number: Int,

    @field:DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val localDateTime: LocalDateTime
)