package io.brick.springmvc.api

import mu.KLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ApiExceptionController {
    companion object : KLogging()

    @GetMapping("/api/members/{id}")
    fun getMember(@PathVariable("id") id: String): MemberDto {
        if(id == "ex") {
            throw RuntimeException("잘못된 사용자")
        }

        return MemberDto(id, "hello $id")
    }
}

data class MemberDto(
    val memberId: String? = null,
    val name: String? = null
)