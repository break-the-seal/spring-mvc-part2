package io.brick.springmvc.validation

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.validation.DefaultMessageCodesResolver

class MessageCodesResolverTest {

    private val codesResolver = DefaultMessageCodesResolver()

    /**
     * 객체 오류의 경우 다음 순서로 2가지 생성
     *  1. code + "." + object name
     *  2. code
     *
     *  ex) 오류 코드: required, object name: item
     *      1. required.item
     *      2. required
     */
    @Test
    fun messageCodesResolverObject() {
        val messageCodes = codesResolver.resolveMessageCodes("required", "item")
        assertThat(messageCodes).containsExactly("required.item", "required")
    }

    /**
     *  필드 오류의 경우 다음 순서로4가지 메시지 코드 생성
     *  1. code + "." + object name + "." + field
     *  2. code + "." + field
     *  3. code + "." + field type
     *  4. code
     *
     *  ex) 오류 코드: typeMismatch, object name "user", field "age", field type: int
     *      1. "typeMismatch.user.age"
     *      2. "typeMismatch.age"
     *      3. "typeMismatch.int"
     *      4. "typeMismatch"
     */
    @Test
    fun messageCodesResolverField() {
        //  bindingResult.rejectValue("itemName", "required")
        val messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String::class.java)
        assertThat(messageCodes).containsExactly(
            "required.item.itemName",
            "required.itemName",
            "required.java.lang.String",
            "required"
        )
    }
}