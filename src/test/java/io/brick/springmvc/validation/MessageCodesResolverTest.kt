package io.brick.springmvc.validation

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.validation.DefaultMessageCodesResolver
import org.springframework.validation.MessageCodesResolver

internal class MessageCodesResolverTest {
    val codesResolver: MessageCodesResolver = DefaultMessageCodesResolver()

    @Test
    fun messageCodesResolverObject() {
        val messageCodes = codesResolver.resolveMessageCodes("required", "item")
        for (messageCode in messageCodes) {
            /**
             * messageCode = required.item
             * messageCode = required
             */
            println("messageCode = $messageCode")
        }
        // ObjectError("item", arrayOf("required.item", "required"), ...)

        assertTrue(messageCodes.contains("required.item"))
        assertTrue(messageCodes.contains("required"))
    }

    @Test
    fun messageCodesResolverField() {
        val messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String::class.java)
        for (messageCode in messageCodes) {
            /**
             * messageCode = required.item.itemName
             * messageCode = required.itemName
             * messageCode = required.java.lang.String
             * messageCode = required
             */
            println("messageCode = $messageCode")
        }
        /**
         * bindingResult.rejectValue("itemName", "required")
         * - 내부적으로 MessageCodesResolver 사용
         * - FieldError("item", "itemName", null, false, messageCodes, null, ...) < MessageCodesResolver에서 찾은 codes를 넣는다.
         */

    }
}