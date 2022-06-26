package io.brick.springmvc.converter

import io.brick.springmvc.type.IpPort
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ConverterTest {
    @Test
    fun stringToInteger() {
        val converter = StringToIntegerConverter()
        val result = converter.convert("10")

        assertEquals(result, 10)
    }

    @Test
    fun integerToString() {
        val converter = IntegerToStringConverter()
        val result = converter.convert(10)

        assertEquals(result, "10")
    }

    @Test
    fun stringToIpPort() {
        val converter = IpPortToStringConverter()
        val result = converter.convert(IpPort(ip = "127.0.0.1", port = 8080))

        assertEquals(result, "127.0.0.1:8080")
    }

    @Test
    fun ipPortToString() {
        val converter = StringToIpPortConverter()
        val result = converter.convert("127.0.0.1:8080")

        assertEquals(result, IpPort(ip = "127.0.0.1", port = 8080))
    }
}