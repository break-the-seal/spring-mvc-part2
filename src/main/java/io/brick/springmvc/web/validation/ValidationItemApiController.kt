package io.brick.springmvc.web.validation

import io.brick.springmvc.web.validation.form.ItemSaveForm
import mu.KLogging
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/validation/api/items")
class ValidationItemApiController {
    companion object : KLogging()

    @PostMapping("/add")
    fun addItem(
        @Validated @RequestBody form: ItemSaveForm,
        bindingResult: BindingResult
    ): Any {
        logger.info { "API 컨트롤러 호출" }

        if (bindingResult.hasErrors()) {
            logger.error { "검증 오류 발생 errors = $bindingResult" }
            return bindingResult.allErrors
        }

        logger.info { "성공 로직 실행" }
        return form
    }
}