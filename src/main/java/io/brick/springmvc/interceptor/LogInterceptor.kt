package io.brick.springmvc.interceptor

import mu.KLogging
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LogInterceptor: HandlerInterceptor {
    companion object: KLogging() {
        const val LOG_ID = "logId"
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val requestURI = request.requestURI
        val logId = UUID.randomUUID().toString()

        request.setAttribute(LOG_ID, logId)
        logger.info { "REQUEST [${logId}][${request.dispatcherType}][${requestURI}][${handler}]" }

        return true
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?,
    ) {
        logger.info { "postHandle [${modelAndView}]" }
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?,
    ) {
        val requestURI = request.requestURI
        val logId = request.getAttribute(LOG_ID)
        logger.info { "RESPONSE [${logId}][${request.dispatcherType}][${requestURI}][${handler}]" }

        ex?.let {
            logger.error { "afterCompletion error! ${ex}" }
        }
    }
}