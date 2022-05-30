package io.brick.springmvc.message

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import java.util.*

@SpringBootTest
class MessageSourceTest {
    @Autowired
    lateinit var ms: MessageSource

    @Test
    fun helloMessage() {
        val result = ms.getMessage("hello", null, Locale.KOREA)
        assertEquals(result, "안녕")
    }

    @Test
    fun notFoundMessageCode() {
        assertThrows<NoSuchMessageException> {
            val result = ms.getMessage("no_code", null, null)
        }
    }

    @Test
    fun notFoundMessageCodeDefaultMessage() {
        val result = ms.getMessage("no_code", null, "default message", null)
        assertEquals(result, "default message")
    }

    @Test
    fun argumentMessage() {
        val message = ms.getMessage("hello.name", arrayOf("Spring"), Locale.KOREA)
        assertEquals(message, "안녕 Spring")
    }

    @Test
    fun defaultLang() {
        assertEquals(ms.getMessage("hello", null, null), "hello")
        assertEquals(ms.getMessage("hello", null, Locale.FRANCE), "hello")
    }

    @Test
    fun koLang() {
        assertEquals(ms.getMessage("hello", null, Locale.KOREA), "안녕")
    }
}