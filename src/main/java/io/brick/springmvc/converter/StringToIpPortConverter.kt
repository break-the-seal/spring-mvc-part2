package io.brick.springmvc.converter

import io.brick.springmvc.type.IpPort
import mu.KLogging
import org.springframework.core.convert.converter.Converter

class StringToIpPortConverter : Converter<String, IpPort> {
    companion object : KLogging()

    override fun convert(source: String): IpPort {
        logger.info { "convert source = ${source}" }
        // "127.0.0.1:8080"
        val split = source.split(":")
        val ip = split[0]
        val port = split[1].toInt()

        return IpPort(ip, port)
    }
}