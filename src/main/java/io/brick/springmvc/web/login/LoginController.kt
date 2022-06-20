package io.brick.springmvc.web.login

import io.brick.springmvc.domain.login.LoginService
import io.brick.springmvc.web.login.form.LoginForm
import io.brick.springmvc.web.session.SessionManager
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
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

    /**
     * 쿠키를 사용한 로그인 처리
     */
//    @PostMapping("/login")
    fun login(
        @Valid @ModelAttribute form: LoginForm,
        bindingResult: BindingResult,
        response: HttpServletResponse
    ): String {
        if(bindingResult.hasErrors()) {
            return "/login/loginForm"
        }

        val loginMember = loginService.login(form.loginId!!, form.password!!)
        if(loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.")
            return "login/loginForm"
        }

        // 로그인 성공 처리
        // 쿠키에 시간 정보를 주지 않으면 -> 세션 쿠키(브라우저 종료시 모두 종료)
        val idCookie = Cookie("memberId", loginMember.id.toString())
        response.addCookie(idCookie)

        return "redirect:/"
    }

    /**
     * 세션을 사용한 로그인 처리
     */
    @PostMapping("/login")
    fun loginV2(
        @Valid @ModelAttribute form: LoginForm,
        bindingResult: BindingResult,
        response: HttpServletResponse
    ): String {
        if(bindingResult.hasErrors()) {
            return "/login/loginForm"
        }

        val loginMember = loginService.login(form.loginId!!, form.password!!)
        if(loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.")
            return "login/loginForm"
        }

        // 로그인 성공 처리
        // 세션 관리자를 통해 세션을 생성하고, 회원 데이터 보관
        sessionManager.createSession(loginMember, response)

        return "redirect:/"
    }

//    @PostMapping("/logout")
    fun logout(response: HttpServletResponse): String {
        expireCookie(response, "memberId")
        return "redirect:/"
    }

    @PostMapping("/logout")
    fun logoutV2(request: HttpServletRequest): String {
        sessionManager.expire(request)
        return "redirect:/"
    }

    private fun expireCookie(response: HttpServletResponse, cookieName: String) {
        val cookie = Cookie(cookieName, null)
        cookie.maxAge = 0
        response.addCookie(cookie)
    }
}