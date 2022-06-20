package io.brick.springmvc.web

import io.brick.springmvc.domain.member.Member
import io.brick.springmvc.domain.member.MemberRepository
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

    /**
     * 쿠키를 통한 로그인
     */
//    @GetMapping("/")
    fun homeLogin(
        @CookieValue(name = "memberId", required = false) memberId: Long,
        model: Model
    ): String {

        if(memberId == null) {
            return "home"
        }

        // 로그인
        val loginMember = memberRepository.findById(memberId) ?: return "home"

        model.addAttribute("member", loginMember)
        return "loginHome"
    }

    /**
     * 세션을 통한 로그인
     */
//    @GetMapping("/")
    fun homeLoginV2(
        request: HttpServletRequest,
        model: Model
    ): String {

        val member = sessionManager.getSession(request) ?: return "home"

        model.addAttribute("member", member)
        return "loginHome"
    }

    /**
     * HttpServlet 세션을 통한 로그인
     */
//    @GetMapping("/")
    fun homeLoginV3(
        request: HttpServletRequest,
        model: Model
    ): String {

        val session = request.getSession(false) ?: return "home"

        val loginMember = session.getAttribute(SessionConstant.LOGIN_MEMBER)
        model.addAttribute("member", loginMember)

        return "loginHome"
    }

    /**
     * Spring에서 제공하는 세션을 통한 로그인
     */
    @GetMapping("/")
    fun homeLoginV3Spring(
        @SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false) loginMember: Member,
        model: Model
    ): String {

        if(loginMember == null) {
            return "home"
        }
        model.addAttribute("member", loginMember)

        return "loginHome"
    }
}