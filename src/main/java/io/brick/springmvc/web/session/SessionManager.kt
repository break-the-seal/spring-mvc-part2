package io.brick.springmvc.web.session

import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 세션 관리
 */
@Component
class SessionManager {
    companion object {
        private const val SESSION_COOKIE_NAME = "mySessionId"
    }

    val sessionStore: MutableMap<String, Any> = ConcurrentHashMap()

    /**
     * 세션 생성
     */
    fun createSession(value: Any, response: HttpServletResponse) {
        // 세션 id 생성, 값을 세션에 저장
        val sessionId = UUID.randomUUID().toString()
        sessionStore[sessionId] = value

        val mySessionCookie = Cookie(SESSION_COOKIE_NAME, sessionId)
        response.addCookie(mySessionCookie)
    }

    /**
     * 세션 조회
     */
    fun getSession(request: HttpServletRequest): Any? {
        return findCookie(request, SESSION_COOKIE_NAME)?.let { sessionStore[it.value] }
    }

    private fun findCookie(request: HttpServletRequest, cookieName: String): Cookie? {
        val cookies = request.cookies
        if (cookies.isNullOrEmpty())
            return null

        return cookies.find { it.name == cookieName }
    }

    /**
     * 세션 만료
     */
    fun expire(request: HttpServletRequest) {
        val sessionCookie = findCookie(request, SESSION_COOKIE_NAME)?.let {
            sessionStore.remove(it.value)
        }
    }
}