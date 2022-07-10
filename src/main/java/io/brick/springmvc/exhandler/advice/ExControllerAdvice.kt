package io.brick.springmvc.exhandler.advice

import io.brick.springmvc.exception.UserException
import io.brick.springmvc.exhandler.ErrorResult
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * 여러 컨트롤러에서 발생하는 에러들을 한꺼번에 처리해준다.
 */
//@RestControllerAdvice(basePackages = ["io.brick.springmvc.api"])
@RestControllerAdvice
class ExControllerAdvice {

    companion object : KLogging()

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalExHandler(e: IllegalArgumentException): ErrorResult {
        logger.error { "[exceptionHandler] ex $e" }
        return ErrorResult("BAD", e.message)
    }

    @ExceptionHandler(UserException::class)
    fun userExHandler(e: UserException): ResponseEntity<ErrorResult> {
        logger.error { "[exceptionHandler] ex $e" }
        val errorResult = ErrorResult("USER-EX", e.message)
        return ResponseEntity(errorResult, HttpStatus.BAD_REQUEST)
    }

    // 위에서 처리되지 못한 예외들을 여기서 처리
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    fun exHandler(e: Exception): ErrorResult {
        logger.error { "[exceptionHandler] ex $e" }
        return ErrorResult("EX", e.message)
    }
}