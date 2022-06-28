package io.brick.springmvc.controller

import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.util.StreamUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/spring")
class SpringUploadController(
    @Value("\${file.dir}")
    private val fileDir: String
) {
    companion object : KLogging()

    @GetMapping("/upload")
    fun newFile(): String {
        return "upload-form"
    }

    @PostMapping("/upload")
    fun saveFileV2(
        @RequestParam itemName: String,
        @RequestParam file: MultipartFile,
        request: HttpServletRequest
    ): String {
        logger.info { "request = ${request}" }
        logger.info { "itemName = ${itemName}" }
        logger.info { "multipartFile = ${file}" }

        if (file.isEmpty().not()) {
            val fullPath = fileDir + file.originalFilename
            logger.info { "save file - fullPath = ${fullPath}" }
            file.transferTo(File(fullPath))
        }

        return "upload-form"
    }
}