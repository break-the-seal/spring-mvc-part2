package io.brick.springmvc.api

import io.brick.springmvc.exception.UserException
import mu.KLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ApiExceptionV3Controller {
    companion object : KLogging()

    @GetMapping("/api3/members/{id}")
    fun getMember(@PathVariable("id") id: String): MemberDto {
        when (id) {
            "ex" -> throw RuntimeException("잘못된 사용자")
            "bad" -> throw IllegalArgumentException("잘못된 입력 값")
            "user-ex" -> throw UserException("사용자 오류")
        }

        return MemberDto(id, "hello  $id")
    }
}