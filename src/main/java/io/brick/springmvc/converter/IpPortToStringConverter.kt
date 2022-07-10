package io.brick.springmvc.converter

import io.brick.springmvc.type.IpPort
import mu.KLogging
import org.springframework.core.convert.converter.Converter

class IpPortToStringConverter : Converter<IpPort, String> {
    companion object : KLogging()

    override fun convert(source: IpPort): String? {
        logger.info { "convert source $source" }
        return "${source.ip}:${source.port}"
    }
}