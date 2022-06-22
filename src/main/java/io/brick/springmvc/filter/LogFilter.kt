package io.brick.springmvc.filter

import mu.KLogging
import java.util.*
import javax.servlet.*
import javax.servlet.http.HttpServletRequest

class LogFilter : Filter {
    companion object: KLogging()

    override fun init(filterConfig: FilterConfig) {
        logger.info { "LogFilter init" }
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        logger.info { "LogFilter doFilter" }

        val httpRequest = request as HttpServletRequest
        val requestURI = httpRequest.requestURI
        val dispatcherType = request.dispatcherType

        val uuid = UUID.randomUUID().toString()

        try {
            logger.info { "REQUEST [${uuid}][${dispatcherType}][${requestURI}]" }
            chain.doFilter(request, response)   // 여기가 중요(여러 Filter -> DispatcherServlet 으로 가는 관문)
        } catch (e: Exception) {
            throw e
        } finally {
            logger.info { "RESPONSE [${uuid}][${dispatcherType}][${requestURI}]" }
        }
    }

    override fun destroy() {
        logger.info { "LogFilter destroy" }
    }
}