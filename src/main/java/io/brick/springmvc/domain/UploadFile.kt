package io.brick.springmvc.domain

data class UploadFile(
    // 고객이 업로드한 파일명
    var uploadFileName: String,
    // 서버 내부에서 관리하는 파일명
    var storeFileName: String
)
