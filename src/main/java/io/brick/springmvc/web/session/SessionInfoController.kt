package io.brick.springmvc.web.session

import mu.KLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.time.LocalDateTime
import java.util.*
import javax.servlet.http.HttpServletRequest

@RestController
class SessionInfoController {
    companion object : KLogging()

    @GetMapping("/session-info")
    fun sessionInfo(request: HttpServletRequest): String {
        val session = request.getSession(false) ?: return "세션이 없습니다."

        session.attributeNames.asIterator()
            .forEachRemaining {
                logger.info { "session name = ${it}, value = ${session.getAttribute(it)}" }
            }

        logger.info { "sessionId = ${session.id}" } // "JSESSIONID"의 값
        logger.info { "maxInactiveInterval = ${session.maxInactiveInterval}" }  // 세션 유효시간(초 단위)
        logger.info { "creationTime = ${session.creationTime.toLocalDateTime()}" }  // 생성 일시
        logger.info { "lastAccessedTime = ${session.lastAccessedTime.toLocalDateTime()}" }   // 마지막 접근 시간
        logger.info { "isNew = ${session.isNew}" }  // 새로 생성된 세션 여부

        return "세션 출력"
    }

}

fun Long.toLocalDateTime(): LocalDateTime? {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), TimeZone.getDefault().toZoneId())
}
