package io.brick.springmvc.api

import io.brick.springmvc.exception.BadRequestException
import io.brick.springmvc.exception.UserException
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class ApiExceptionController {
    companion object: KLogging()

    @GetMapping("/api/members/{id}")
    fun getMember(@PathVariable("id") id: String): MemberDto {
        when (id) {
            "ex" -> throw RuntimeException("잘못된 사용자")
            "bad" -> throw IllegalArgumentException("잘못된 입력 값")
            "user-ex" -> throw UserException("사용자 오류")
        }

        return MemberDto(id, "hello  $id")
    }

    @GetMapping("/api/response-status-ex1")
    fun responseStatusEx1(): String {
        throw BadRequestException()
    }

    @GetMapping("/api/response-status-ex2")
    fun responseStatusEx2(): String {
        throw ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", IllegalArgumentException())
    }

}

data class MemberDto(
    val memberId: String? = null,
    val name: String? = null
)