package io.brick.springmvc.resolver

import com.fasterxml.jackson.databind.ObjectMapper
import io.brick.springmvc.exception.UserException
import mu.KLogging
import org.springframework.http.MediaType
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.ModelAndView
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class UserHandlerExceptionResolver: HandlerExceptionResolver {
    companion object: KLogging()

    private val objectMapper = ObjectMapper()

    override fun resolveException(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any?,
        ex: Exception,
    ): ModelAndView? {
        try {
            if (ex is UserException) {
                logger.info { "UserException resolver to 400" }
                val acceptHeader: String? = request.getHeader("accept")
                response.status = HttpServletResponse.SC_BAD_REQUEST

                return if (MediaType.APPLICATION_JSON_VALUE == acceptHeader) {
                    val errorResult = mutableMapOf<String, Any?>()
                    errorResult["ex"] = ex.javaClass
                    errorResult["message"] = ex.message

                    val result = objectMapper.writeValueAsString(errorResult)

                    response.contentType = MediaType.APPLICATION_JSON_VALUE
                    response.characterEncoding = "utf-8"
                    response.writer.write(result)   // 이 때는 예외 처리가 여기서 아예 끝나게 된다. (WAS에서 내부 호출 X)

                    ModelAndView()
                } else {
                    ModelAndView("error/500")   // text/html
                }
            }
        } catch (e: IOException) {
            logger.error { "resolver error: $e" }
        }

        return null
    }
}