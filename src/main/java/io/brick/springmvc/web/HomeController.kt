package io.brick.springmvc.web

import io.brick.springmvc.domain.member.Member
import io.brick.springmvc.domain.member.MemberRepository
import io.brick.springmvc.web.argumentresolver.Login
import io.brick.springmvc.web.session.SessionManager
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.SessionAttribute
import javax.servlet.http.HttpServletRequest

@Controller
class HomeController(
    private val memberRepository: MemberRepository,
    private val sessionManager: SessionManager
) {
//    @GetMapping("/")
    fun home(): String {
        return "home"
    }

//    @GetMapping("/")
    fun homeLogin(
        @CookieValue(name = "memberId", required = false) memberId: Long?,
        model: Model
    ): String {
        if (memberId == null) {
            return "home"
        }

        val loginMember = memberRepository.findById(memberId)
            ?: return "home"

        model.addAttribute("member", loginMember)
        return "loginHome"
    }

//    @GetMapping("/")
    fun homeLoginV2(
        request: HttpServletRequest,
        model: Model
    ): String {
        // 세션 관리자에 저장된 회원 정보 조회
        val member = sessionManager.getSession(request) as? Member
            ?: return "home"

        model.addAttribute("member", member)
        return "loginHome"
    }

//    @GetMapping("/")
    fun homeLoginV3(
        request: HttpServletRequest,
        model: Model
    ): String {
        // home 화면에서는 의도적으로 세션을 생성할 필요가 없기에 false
        val session = request.getSession(false)
            ?: return "home"

        val member = session.getAttribute(SessionConstant.LOGIN_MEMBER) as? Member
            ?: return "home"

        model.addAttribute("member", member)
        return "loginHome"
    }

//    @GetMapping("/")
    fun homeLoginV3Spring(
        @SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false) loginMember: Member?,
        model: Model
    ): String {
        if (loginMember == null) {
            return "home"
        }

        model.addAttribute("member", loginMember)
        return "loginHome"
    }

    @GetMapping("/")
    fun homeLoginV3ArgumentResolver(
        @Login loginMember: Member?,
        model: Model
    ): String {
        if (loginMember == null) {
            return "home"
        }

        model.addAttribute("member", loginMember)
        return "loginHome"
    }
}