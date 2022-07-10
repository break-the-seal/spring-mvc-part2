package io.brick.springmvc

import io.brick.springmvc.converter.IntegerToStringConverter
import io.brick.springmvc.converter.IpPortToStringConverter
import io.brick.springmvc.converter.StringToIntegerConverter
import io.brick.springmvc.converter.StringToIpPortConverter
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(StringToIntegerConverter())
        registry.addConverter(IntegerToStringConverter())
        registry.addConverter(StringToIpPortConverter())
        registry.addConverter(IpPortToStringConverter())
    }
}