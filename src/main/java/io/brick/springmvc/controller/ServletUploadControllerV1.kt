package io.brick.springmvc.controller

import mu.KLogging
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/servlet/v1")
class ServletUploadControllerV1 {
    companion object: KLogging()

    @GetMapping("/upload")
    fun newFile(): String {
        return "upload-form"
    }

    @PostMapping("/upload")
    fun saveFileV1(request: HttpServletRequest): String {
        logger.info { "request = ${request}" }

        val itemName = request.getParameter("itemName")
        logger.info { "itemName = ${itemName}" }

        val parts = request.parts   // multipart form 전송 때 body 부분에서 각 part들
        logger.info { "parts = ${parts}" }

        return "upload-form"
    }
}