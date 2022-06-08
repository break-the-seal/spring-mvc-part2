package io.brick.springmvc.web.basic

import io.brick.springmvc.web.basic.form.ItemSaveForm
import mu.KLogging
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/validation/api/items"])
class ValidationItemApiController {

    companion object : KLogging()

    /**
     * @ModelAttribute
     *  - 각각의 필드 단위로 세밀하게 적용
     *  - 특정 필드가 바인딩되지 않아도 나머지 필드는 정상 바인딩 -> Validator를 사용한 검증도 적용 가능
     *
     * @RequestBody
     *  - 객체 단위로 적용
     *  - HttpMessageConverter 단계에서 JSON 데이터를 객체로 변경하지 못하면, 이후 단계가 진행되지 않고 예외 발
     *  - controller 호출 x, validator 검증 x
     */
    @PostMapping("/add")
    fun addItem(
        @RequestBody @Validated itemSaveForm: ItemSaveForm,
        bindingResult: BindingResult
    ): Any {

        logger.info("API 컨트롤러 호출")

        if(bindingResult.hasErrors()) {
            logger.info("검증 오류 발생 errors={}", bindingResult)
            return bindingResult.allErrors
        }

        logger.info("성공 로직 실행")
        return itemSaveForm
    }
}