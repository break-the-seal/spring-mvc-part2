package io.brick.springmvc.file

import io.brick.springmvc.domain.UploadFile
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.*

@Component
class FileStore(
    @Value("\${file.dir}")
    private val fileDir: String
) {
    fun getFullPath(filename: String): String {
        return fileDir + filename
    }

    fun storeFiles(multipartFiles: List<MultipartFile>): List<UploadFile> {
        val storeFileResult = mutableListOf<UploadFile>()
        multipartFiles.forEach { multipartFile ->
            var uploadFile: UploadFile? = null
            if (multipartFile.isEmpty().not()) {
                storeFile(multipartFile)?.let {
                    storeFileResult.add(it)
                }
            }
        }

        return storeFileResult
    }

    fun storeFile(multipartFile: MultipartFile?): UploadFile? {
        if (multipartFile?.isEmpty() == true) {
            return null
        }

        // image.png
        val originalFilename = multipartFile!!.originalFilename
            ?: return null

        val storeFileName = createStoreFileName(originalFilename)
        multipartFile.transferTo(File(getFullPath(storeFileName)))

        return UploadFile(originalFilename, storeFileName)
    }

    private fun createStoreFileName(originalFilename: String): String {
        val extension = extractExtension(originalFilename)

        // 서버에 저장하는 파일명
        // abc-abc-123absc.png
        val uuid = UUID.randomUUID().toString()
        return "${uuid}.${extension}"
    }

    private fun extractExtension(originalFilename: String): String {
        val pos = originalFilename.lastIndexOf(".")
        return originalFilename.substring(pos + 1)
    }
}