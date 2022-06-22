package io.brick.springmvc.resolver

import mu.KLogging
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.ModelAndView
import java.io.IOException
import java.lang.Exception
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class MyHandlerExceptionResolver: HandlerExceptionResolver {
    companion object: KLogging()

    override fun resolveException(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any?,
        ex: Exception,
    ): ModelAndView? {
        try {
            if (ex is IllegalArgumentException) {
                logger.info { "IllegalArgumentException resolver to 400" }
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.message)
                return ModelAndView()
            }
        } catch (e: IOException) {
            logger.error { "resolver error: $e" }
        }

        return null
    }
}