package io.brick.springmvc.web.interceptor

import mu.KLogging
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.lang.Exception
import java.util.UUID
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LogInterceptor : HandlerInterceptor {

    companion object : KLogging() {
        const val LOG_ID = "logId"
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val requestURI = request.requestURI
        val logId = UUID.randomUUID().toString()

        request.setAttribute(LOG_ID, logId)
        logger.info { "REQUEST [${logId}][${requestURI}][${handler}]" }

        // @RequestMapping: HandlerMethod
        // 정적 리소스: ResourceHttpRequestHandler
        // 호출할 컨트롤러 메서드의 모든 정보가 포함되어 있다.
        if (handler is HandlerMethod) {
            // handler 관련 정보 사용 가능
        }

        return true
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        logger.info { "postHandle [${modelAndView}]" }
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        val requestURI = request.requestURI
        val logId = request.getAttribute(LOG_ID)
        logger.info { "RESPONSE [${logId}][${requestURI}][${handler}]" }

        ex?.let {
            logger.error { "afterCompletion error! ${ex}" }
        }
    }
}