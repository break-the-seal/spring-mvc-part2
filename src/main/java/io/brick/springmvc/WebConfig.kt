package io.brick.springmvc

import io.brick.springmvc.converter.IpPortToStringConverter
import io.brick.springmvc.converter.StringToIpPortConverter
import io.brick.springmvc.formatter.MyNumberFormatter
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig: WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        // 컨버터가 포멧터보다 우선순위가 높기에 NumberFormatter 관련 컨버터는 주석처리
//        registry.addConverter(StringToIntegerConverter())
//        registry.addConverter(IntegerToStringConverter())
        registry.addConverter(StringToIpPortConverter())
        registry.addConverter(IpPortToStringConverter())

        registry.addFormatter(MyNumberFormatter())
    }
}