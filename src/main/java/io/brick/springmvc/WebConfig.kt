package io.brick.springmvc

import io.brick.springmvc.web.argumentresolver.LoginMemberArgumentResolver
import io.brick.springmvc.web.filter.LogFilter
import io.brick.springmvc.web.filter.LoginCheckFilter
import io.brick.springmvc.web.interceptor.LogInterceptor
import io.brick.springmvc.web.interceptor.LoginCheckInterceptor
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.Filter

@Configuration
class WebConfig: WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(LoginMemberArgumentResolver())
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(LogInterceptor())
            .order(1)
            .addPathPatterns("/**")
            .excludePathPatterns("/css/**", "/*.ico", "/error")

        registry.addInterceptor(LoginCheckInterceptor())
            .order(2)
            .addPathPatterns("/**")
            .excludePathPatterns("/", "/members/add", "/login", "/logout", "/css/**", "/*.ico", "/error")
    }

    /**
     * @return FilterRegistrationBean 스프링 부트 내부에 WAS를 띄울 때 Filter 등록해준다.
     */
    @Bean
    fun logFilter(): FilterRegistrationBean<*> {
        val filterRegistrationBean: FilterRegistrationBean<Filter> = FilterRegistrationBean()
        filterRegistrationBean.filter = LogFilter()
        filterRegistrationBean.order = 1
        filterRegistrationBean.addUrlPatterns("/*")

        return filterRegistrationBean
    }

//    @Bean
    fun loginCheckFilter(): FilterRegistrationBean<*> {
        val filterRegistrationBean: FilterRegistrationBean<Filter> = FilterRegistrationBean()
        filterRegistrationBean.filter = LoginCheckFilter()
        filterRegistrationBean.order = 2
        filterRegistrationBean.addUrlPatterns("/*")

        return filterRegistrationBean
    }
}