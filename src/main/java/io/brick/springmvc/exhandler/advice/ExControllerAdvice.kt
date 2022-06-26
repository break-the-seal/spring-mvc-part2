package io.brick.springmvc.exhandler.advice

import io.brick.springmvc.exception.UserException
import io.brick.springmvc.exhandler.ErrorResult
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["io.brick.springmvc.api"])
class ExControllerAdvice {
    companion object: KLogging()

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalExHandler(e: IllegalArgumentException): ErrorResult {
        logger.error { "[exceptionHandler] ex $e" }
        return ErrorResult("BAD", e.message)
    }

    // 이런 식으로도 사용 가능
    @ExceptionHandler
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