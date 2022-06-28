package io.brick.springmvc.controller

import org.springframework.web.multipart.MultipartFile

data class ItemForm(
    val itemId: Long? = 0L,
    val itemName: String? = null,
    var attachFile: MultipartFile? = null,
    val imageFiles: List<MultipartFile> = emptyList()
)