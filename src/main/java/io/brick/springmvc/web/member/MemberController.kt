package io.brick.springmvc.web.member

import io.brick.springmvc.domain.member.Member
import io.brick.springmvc.domain.member.MemberRepository
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid

@Controller
@RequestMapping("/members")
class MemberController(
    private val memberRepository: MemberRepository
) {
    @GetMapping("/add")
    fun addForm(@ModelAttribute("member") member: Member): String {
        return "members/addMemberForm"
    }

    @PostMapping("/add")
    fun save(
        @Valid @ModelAttribute member: Member,
        bindingResult: BindingResult
    ): String {
        if (bindingResult.hasErrors()) {
            return "members/addMemberForm"
        }

        memberRepository.save(member)

        return "redirect:/"
    }
}