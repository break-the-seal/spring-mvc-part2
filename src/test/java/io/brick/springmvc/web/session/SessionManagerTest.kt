package io.brick.springmvc.web.session

import io.brick.springmvc.domain.member.Member
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse

internal class SessionManagerTest {
    private val sessionManager = SessionManager()

    @Test
    fun sessionTest() {
        // 세션 설정
        val response = MockHttpServletResponse()
        val member = Member()
        sessionManager.createSession(
            value = member,
            response = response
        )

        // 요청에 응답 쿠키 저장
        val request = MockHttpServletRequest().apply {
            setCookies(*response.cookies)
        }

        // 세션 조회
        val result = sessionManager.getSession(request)
        assertEquals(result, member)

        // 세션 만료
        sessionManager.expire(request)
        val expired = sessionManager.getSession(request)
        assertNull(expired)
    }
}