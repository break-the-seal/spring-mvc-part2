package io.brick.springmvc.converter

import mu.KLogging
import org.springframework.core.convert.converter.Converter

class IntegerToStringConverter : Converter<Int, String> {
    companion object : KLogging()

    override fun convert(source: Int): String? {
        logger.info { "convert source = $source" }
        return source.toString()
    }
}