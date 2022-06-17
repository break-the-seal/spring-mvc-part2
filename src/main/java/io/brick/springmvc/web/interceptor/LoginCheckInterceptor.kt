package io.brick.springmvc.web.interceptor

import io.brick.springmvc.web.SessionConstant
import mu.KLogging
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoginCheckInterceptor: HandlerInterceptor {
    companion object: KLogging()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val requestURI = request.requestURI

        logger.info { "인증 체크 인터셉터 실행 ${requestURI}" }

        logger.info { "인증 체크 로직 실행 ${requestURI}" }
        val session = request.getSession(false)

        if (session?.getAttribute(SessionConstant.LOGIN_MEMBER) == null) {
            logger.info { "미인증 사용자 요청 ${requestURI}" }
            // 로그인으로 redirect
            response.sendRedirect("/login?redirectURL=${requestURI}")
            return false
        }

        return true
    }
}