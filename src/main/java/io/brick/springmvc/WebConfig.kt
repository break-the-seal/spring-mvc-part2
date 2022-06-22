package io.brick.springmvc

import io.brick.springmvc.filter.LogFilter
import io.brick.springmvc.interceptor.LogInterceptor
import io.brick.springmvc.resolver.MyHandlerExceptionResolver
import io.brick.springmvc.resolver.UserHandlerExceptionResolver
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.DispatcherType
import javax.servlet.Filter

@Configuration
class WebConfig: WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(LogInterceptor())
            .order(1)
            .addPathPatterns("/**")
            // filter 같이 dispatcherType으로 설정 불가능
            // exclude할 path로 지정
            .excludePathPatterns("/css/**", "*.ico", "/error", "/error-page/**")
    }

    override fun extendHandlerExceptionResolvers(resolvers: MutableList<HandlerExceptionResolver>) {
        resolvers.add(MyHandlerExceptionResolver())
        resolvers.add(UserHandlerExceptionResolver())
    }

    @Bean
    fun logFilter(): FilterRegistrationBean<*> {
        val filterRegistrationBean = FilterRegistrationBean<Filter>()
        filterRegistrationBean.filter = LogFilter()
        filterRegistrationBean.order = 1
        filterRegistrationBean.addUrlPatterns("/*")
        // 여기서 filter에 DispatcherType 적용 가능(어떤 타입에 필터를 적용할 것인지 설정)
        // 기본값은 "REQUEST"만 설정
        // filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR)

        return filterRegistrationBean
    }
}