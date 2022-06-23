package io.brick.springmvc.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

// ResponseStatusExceptionResolver 의해서 에러 처리
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")
class BadRequestException: RuntimeException()