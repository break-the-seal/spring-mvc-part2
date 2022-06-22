package io.brick.springmvc

import org.springframework.boot.web.server.ConfigurableWebServerFactory
import org.springframework.boot.web.server.ErrorPage
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

/**
 * 스프링 부트가 run 할 때 톰캣에게 아래 설정된 내용대로 에러페이지를 등록
 */
@Component
class WebServerCustomizer : WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    override fun customize(factory: ConfigurableWebServerFactory) {
        val errorPage404 = ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404")
        val errorPage500 = ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500")
        val errorPageEx = ErrorPage(RuntimeException::class.java, "/error-page/500")

        factory.addErrorPages(errorPage404, errorPage500, errorPageEx)
    }
}