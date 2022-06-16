package io.brick.springmvc.web.filter

import io.brick.springmvc.web.SessionConstant
import mu.KLogging
import org.springframework.util.PatternMatchUtils
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoginCheckFilter: Filter {
    companion object: KLogging() {
        val whiteList = arrayOf("/", "/members/add", "/login", "/logout", "/css/*")
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val requestURI = httpRequest.requestURI

        val httpResponse = response as HttpServletResponse

        try {
            logger.info { "인증 체크 필터 시작 ${requestURI}" }

            if (isLoginCheckPath(requestURI)) {
                logger.info { "인증 체크 로직 실행 ${requestURI}" }
                val session = httpRequest.getSession(false)

                if (session?.getAttribute(SessionConstant.LOGIN_MEMBER) == null) {
                    logger.info { "미인증 사용자 요청 ${requestURI}" }
                    // 로그인으로 redirect
                    httpResponse.sendRedirect("/login?redirectURL=${requestURI}")
                    return
                }
            }

            chain.doFilter(request, response)
        } catch (e: Exception) {
            throw e // 예외 로깅 가능, but tomcat까지 예외를 보내주어야 함
        } finally {
            logger.info { "인증 체크 필터 종료 ${requestURI}" }
        }
    }

    /**
     * 화이트 리스트의 경우 인증 체크 제외
     */
    private fun isLoginCheckPath(requestURI: String): Boolean {
        return PatternMatchUtils.simpleMatch(whiteList, requestURI).not()
    }
}