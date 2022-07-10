package io.brick.springmvc.formatter

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.Locale

internal class MyNumberFormatterTest {

    private val formatter = MyNumberFormatter()

    @Test
    fun parse() {
        val result = formatter.parse("1000", Locale.KOREA)
        assertEquals(result, 1000L)
    }

    @Test
    fun print() {
        val result = formatter.print(1000, Locale.KOREA)
        assertEquals(result, "1,000")
    }
}