package io.brick.springmvc.formatter

import mu.KLogging
import org.springframework.format.Formatter
import java.text.NumberFormat
import java.util.*

class MyNumberFormatter : Formatter<Number> {
    companion object : KLogging()

    override fun print(`object`: Number, locale: Locale): String {
        logger.info { "text = ${`object`}, locale = ${locale}" }

        // 1000 -> "1,000"
        return NumberFormat.getInstance(locale).format(`object`)
    }

    override fun parse(text: String, locale: Locale): Number {
        logger.info { "text = ${text}, locale = ${locale}" }

        // "1,000" -> 1000
        return NumberFormat.getInstance(locale).parse(text)
    }
}