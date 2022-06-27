package io.brick.springmvc.formatter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

internal class MyNumberFormatterTest {
    private val formatter = MyNumberFormatter()

    @Test
    fun parse() {
        val result = formatter.parse("1,000", Locale.KOREA)
        assertEquals(result, 1000L) // Long 주의
    }

    @Test
    fun print() {
        val result = formatter.print(1000, Locale.KOREA)
        assertEquals(result, "1,000")
    }
}