package io.brick.springmvc.message

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import java.util.*

@SpringBootTest
class MessageSourceTest {

    @Autowired
    lateinit var messageSource: MessageSource

    @Test
    fun helloMessage() {
        val result = messageSource.getMessage("hello", null, null)
        assertThat(result).isEqualTo("안녕")
    }

    @Test
    fun notFoundMessageCode() {
        assertThatThrownBy { messageSource.getMessage("no-code", null, null) }
            .isInstanceOf(NoSuchMessageException::class.java)
    }

    @Test
    fun notFoundMessageCodeDefaultMessage() {
        val result = messageSource.getMessage("no-code", null, "default message", null)
        assertThat(result).isEqualTo("default message")
    }

    @Test
    fun argumentMessage() {
        val message = messageSource.getMessage("hello.name", arrayOf("Spring"), null)
        assertThat(message).isEqualTo("안녕 Spring")
    }

    @Test
    fun defaultLang() {
        assertThat(messageSource.getMessage("hello", null, null)).isEqualTo("안녕")
        assertThat(messageSource.getMessage("hello", null, Locale.FRANCE)).isEqualTo("안녕")
    }

    @Test
    fun koLang() {
        assertThat(messageSource.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕")
    }

}