package io.brick.springmvc.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

// reason 은 messages.properties 값 조회 가능
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")
class BadRequestException : RuntimeException(){
}