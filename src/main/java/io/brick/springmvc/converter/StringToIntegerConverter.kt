package io.brick.springmvc.converter

import mu.KLogging
import org.springframework.core.convert.converter.Converter

class StringToIntegerConverter : Converter<String, Int> {
    companion object : KLogging()

    override fun convert(source: String): Int? {
        logger.info { "convert source = $source" }
        return source.toInt()
    }
}