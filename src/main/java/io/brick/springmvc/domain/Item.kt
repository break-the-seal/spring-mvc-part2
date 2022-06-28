package io.brick.springmvc.domain

data class Item(
    var id: Long = 0L,
    var itemName: String,
    var attachFile: UploadFile?,
    val imageFiles: List<UploadFile> = emptyList()
)
