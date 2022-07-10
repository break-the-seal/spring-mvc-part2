package io.brick.springmvc.formatter

import io.brick.springmvc.converter.IpPortToStringConverter
import io.brick.springmvc.converter.StringToIpPortConverter
import io.brick.springmvc.type.IpPort
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.format.support.DefaultFormattingConversionService

internal class FormattingConversionTest {

    @Test
    fun formattingConversionService() {
        val conversionService = DefaultFormattingConversionService()

        // 컨버터 등록
        conversionService.addConverter(StringToIpPortConverter())
        conversionService.addConverter(IpPortToStringConverter())

        // 포맷터 등록
        conversionService.addFormatter(MyNumberFormatter())

        // 컨버터 사용
        val ipPort = conversionService.convert("127.0.0.1:8080", IpPort::class.java)
        assertEquals(ipPort, IpPort("127.0.0.1", 8080))

        // 포멧터 사용
        assertEquals(conversionService.convert(1000, String::class.java), "1,000")
        assertEquals(conversionService.convert("1,000", Long::class.java) ?: -1L, 1000L)
    }
}