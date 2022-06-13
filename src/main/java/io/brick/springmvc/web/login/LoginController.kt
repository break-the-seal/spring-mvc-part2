package io.brick.springmvc.web.login

import io.brick.springmvc.domain.login.LoginService
import io.brick.springmvc.web.SessionConstant
import io.brick.springmvc.web.session.SessionManager
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession
import javax.validation.Valid

@Controller
class LoginController(
    private val loginService: LoginService,
    private val sessionManager: SessionManager
) {
    @GetMapping("/login")
    fun loginForm(@ModelAttribute("loginForm") form: LoginForm): String {
        return "login/loginForm"
    }

//    @PostMapping("/login")
    fun login(
        @Valid @ModelAttribute form: LoginForm,
        bindingResult: BindingResult,
        response: HttpServletResponse
    ): String {
        if (bindingResult.hasErrors()) {
            return "login/loginForm"
        }

        val loginMember = loginService.login(form.loginId!!, form.password!!)
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.")
            return "login/loginForm"
        }

        val idCookie = Cookie("memberId", loginMember.id.toString())
        response.addCookie(idCookie)

        return "redirect:/"
    }

//    @PostMapping("/login")
    fun loginV2(
        @Valid @ModelAttribute form: LoginForm,
        bindingResult: BindingResult,
        response: HttpServletResponse
    ): String {
        if (bindingResult.hasErrors()) {
            return "login/loginForm"
        }

        val loginMember = loginService.login(form.loginId!!, form.password!!)
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.")
            return "login/loginForm"
        }

        // 로그인 성공 처리
        // 세션 관리자를 통해 세션을 생성하고, 회원 데이터 보관
        sessionManager.createSession(loginMember, response)

        return "redirect:/"
    }

    @PostMapping("/login")
    fun loginV3(
        @Valid @ModelAttribute form: LoginForm,
        bindingResult: BindingResult,
        request: HttpServletRequest
    ): String {
        if (bindingResult.hasErrors()) {
            return "login/loginForm"
        }

        val loginMember = loginService.login(form.loginId!!, form.password!!)
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.")
            return "login/loginForm"
        }

        // 로그인 성공 처리
        // 세션이 있으면 기존 세션 반환, 없으면 신규 세션 생성
        val session = request.getSession(true) // default: true
        // 세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConstant.LOGIN_MEMBER, loginMember)

        return "redirect:/"
    }

//    @PostMapping("/logout")
    fun logout(response: HttpServletResponse): String {
        expireCookie(response, "memberId")
        return "redirect:/"
    }

//    @PostMapping("/logout")
    fun logoutV2(request: HttpServletRequest, response: HttpServletResponse): String {
        sessionManager.expire(request)
        return "redirect:/"
    }

    @PostMapping("/logout")
    fun logoutV3(request: HttpServletRequest, response: HttpServletResponse): String {
        request.getSession(false)?.invalidate()

        return "redirect:/"
    }

    private fun expireCookie(response: HttpServletResponse, cookieName: String) {
        val cookie = Cookie(cookieName, null)
        cookie.maxAge = 0
        response.addCookie(cookie)
    }
}