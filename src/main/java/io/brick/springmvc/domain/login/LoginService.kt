package io.brick.springmvc.domain.login

import io.brick.springmvc.domain.member.Member
import io.brick.springmvc.domain.member.MemberRepository
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val memberRepository: MemberRepository
) {

    /**
     * @param loginId String
     * @param password String
     * @return null 로그인 실패
     */
    fun login(loginId: String, password: String): Member? {
        return memberRepository.findByLoginId(loginId)?.takeIf {
            it.password == password
        }
    }
}