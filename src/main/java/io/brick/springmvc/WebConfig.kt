package io.brick.springmvc

import io.brick.springmvc.web.filter.LogFilter
import io.brick.springmvc.web.filter.LoginCheckFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.servlet.Filter

@Configuration
class WebConfig {

    /**
     * @return FilterRegistrationBean -> 스프링 부트 내부에 WAS를 띄울 때 Filter 등록해준다.
     */
    @Bean
    fun logFilter(): FilterRegistrationBean<*> {
        val filterRegistrationBean: FilterRegistrationBean<Filter> = FilterRegistrationBean()
        filterRegistrationBean.filter = LogFilter()
        filterRegistrationBean.order = 1    // filter가 체인으로 돌 때 순서
        filterRegistrationBean.addUrlPatterns("/*")

        return filterRegistrationBean
    }

    @Bean
    fun loginCheckFilter(): FilterRegistrationBean<*> {
        val filterRegistrationBean: FilterRegistrationBean<Filter> = FilterRegistrationBean()
        filterRegistrationBean.filter = LoginCheckFilter()
        filterRegistrationBean.order = 2
        filterRegistrationBean.addUrlPatterns("/*")

        return filterRegistrationBean
    }
}