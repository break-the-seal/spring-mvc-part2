package io.brick.springmvc.web.argumentresolver

import io.brick.springmvc.domain.member.Member
import io.brick.springmvc.web.SessionConstant
import mu.KLogging
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest

class LoginMemberArgumentResolver : HandlerMethodArgumentResolver {

    companion object: KLogging()

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        logger.info { "supportsParameter 실행" }
        val hasLoginAnnotation = parameter.hasParameterAnnotation(Login::class.java)
        val hasMemberType = Member::class.java.isAssignableFrom(parameter.parameterType)

        return hasLoginAnnotation && hasMemberType
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any? {
        logger.info { "resolveArgument 실행" }
        val request = webRequest.nativeRequest as HttpServletRequest
        val session = request.getSession(false)
            ?: return null

        return session.getAttribute(SessionConstant.LOGIN_MEMBER)
    }
}