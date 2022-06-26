package io.brick.springmvc.converter

import io.brick.springmvc.type.IpPort
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.core.convert.support.DefaultConversionService

class ConversionServiceTest {
    @Test
    fun conversionService() {
        // 등록
        val conversionService = DefaultConversionService()
        conversionService.addConverter(StringToIntegerConverter())
        conversionService.addConverter(IntegerToStringConverter())
        conversionService.addConverter(StringToIpPortConverter())
        conversionService.addConverter(IpPortToStringConverter())

        // 사용
        assertEquals(conversionService.convert("10", Int::class.java) ?: null, 10)
        assertEquals(conversionService.convert(10, String::class.java), "10")

        val ipPort = conversionService.convert("127.0.0.1:8080", IpPort::class.java)
        assertEquals(ipPort, IpPort("127.0.0.1", 8080))

        val ipPortString = conversionService.convert(IpPort("127.0.0.1", 8080), String::class.java)
        assertEquals(ipPortString, "127.0.0.1:8080")

    }
}