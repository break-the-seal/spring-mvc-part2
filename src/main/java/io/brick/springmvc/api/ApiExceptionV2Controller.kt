package io.brick.springmvc.api

import io.brick.springmvc.exception.UserException
import mu.KLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ApiExceptionV2Controller {
    companion object : KLogging()

    // 해당 컨트롤러 안에서 발생한 에러에 대해서만 handling

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(IllegalArgumentException::class)
//    fun illegalExHandler(e: IllegalArgumentException): ErrorResult {
//        logger.error { "[exceptionHandler] ex $e" }
//        return ErrorResult("BAD", e.message)
//    }

    // 이런 식으로도 사용 가능
//    @ExceptionHandler
//    fun userExHandler(e: UserException): ResponseEntity<ErrorResult> {
//        logger.error { "[exceptionHandler] ex $e" }
//        val errorResult = ErrorResult("USER-EX", e.message)
//        return ResponseEntity(errorResult, HttpStatus.BAD_REQUEST)
//    }

    // 위에서 처리되지 못한 예외들을 여기서 처리
//

    @GetMapping("/api2/members/{id}")
    fun getMember(@PathVariable("id") id: String): MemberDto {
        when (id) {
            "ex" -> throw RuntimeException("잘못된 사용자")
            "bad" -> throw IllegalArgumentException("잘못된 입력 값")
            "user-ex" -> throw UserException("사용자 오류")
        }

        return MemberDto(id, "hello  $id")
    }
}