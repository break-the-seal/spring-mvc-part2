package io.brick.springmvc.controller

import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.util.StreamUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/servlet/v2")
class ServletUploadControllerV2(
    @Value("\${file.dir}")
    private val fileDir: String
) {
    companion object: KLogging()

    @GetMapping("/upload")
    fun newFile(): String {
        return "upload-form"
    }

    @PostMapping("/upload")
    fun saveFileV2(request: HttpServletRequest): String {
        logger.info { "request = ${request}" }

        val itemName = request.getParameter("itemName")
        logger.info { "itemName = ${itemName}" }

        val parts = request.parts
        logger.info { "parts = ${parts}" }

        for (part in parts) {
            logger.info { "==== PART ====" }
            logger.info { "name = ${part.name}" }

            // 헤더 정보 가져오기
            val headerNames = part.headerNames
            for (headerName in headerNames) {
                logger.info { "header ${headerName}: ${part.getHeader(headerName)}" }
            }

            // 편의 메서드
            // Content-Disposition: ...; filename="image.png"
            logger.info { "submittedFilename = ${part.submittedFileName}" }
            logger.info { "size = ${part.size}" }

            // 데이터 읽기
            val inputStream = part.inputStream
            val body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8)
            logger.info { "body = ${body}" }

            val submittedFileName = part.submittedFileName ?: ""
            if (submittedFileName.isNotBlank()) {
                val fullPath = fileDir + submittedFileName
                logger.info { "save file - fullPath = ${fullPath}" }

                part.write(fullPath)
            }
        }

        return "upload-form"
    }
}