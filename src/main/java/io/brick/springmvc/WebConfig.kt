package io.brick.springmvc

import io.brick.springmvc.filter.LogFilter
import io.brick.springmvc.filter.TestFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.DispatcherType
import javax.servlet.Filter

@Configuration
class WebConfig: WebMvcConfigurer {
    @Autowired
    lateinit var testFilter: TestFilter

    @Bean
    fun logFilter(): FilterRegistrationBean<*> {
        val filterRegistrationBean = FilterRegistrationBean<Filter>()
        filterRegistrationBean.filter = LogFilter()
        filterRegistrationBean.order = 1
        filterRegistrationBean.addUrlPatterns("/*")
        // 여기서 filter에 DispatcherType 적용 가능(어떤 타입에 필터를 적용할 것인지 설정)
        // 기본값은 "REQUEST"만 설정
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR)

        return filterRegistrationBean
    }

//    @Bean
//    fun testFilter(): FilterRegistrationBean<*> {
//        val filterRegistrationBean = FilterRegistrationBean<Filter>()
//        filterRegistrationBean.filter = testFilter
//        filterRegistrationBean.order = 2
//        filterRegistrationBean.addUrlPatterns("/*")
//
//        return filterRegistrationBean
//    }
}