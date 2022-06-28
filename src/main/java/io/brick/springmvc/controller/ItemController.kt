package io.brick.springmvc.controller

import io.brick.springmvc.domain.Item
import io.brick.springmvc.domain.ItemRepository
import io.brick.springmvc.file.FileStore
import mu.KLogging
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.util.UriUtils
import java.nio.charset.StandardCharsets

@Controller
class ItemController(
    private val itemRepository: ItemRepository,
    private val fileStore: FileStore
) {
    companion object: KLogging()

    @GetMapping("/items/new")
    fun newItem(@ModelAttribute form: ItemForm): String {
        return "item-form"
    }

    @PostMapping("/items/new")
    fun saveItem(
        @ModelAttribute form: ItemForm,
        redirectAttributes: RedirectAttributes,
    ): String {
        val attachFile = fileStore.storeFile(form.attachFile)
        val storeImageFiles = fileStore.storeFiles(form.imageFiles)

        // 데이터베이스 저장
        val item = Item(
            itemName = form.itemName!!,
            attachFile = attachFile,
            imageFiles = storeImageFiles
        )

        itemRepository.save(item)

        redirectAttributes.addAttribute("itemId", item.id)

        return "redirect:/items/{itemId}"
    }

    @GetMapping("/items/{id}")
    fun item(@PathVariable id: Long, model: Model): String {
        val item = itemRepository.findById(id)
            ?: throw RuntimeException("Item[${id}] Not Found")

        model.addAttribute("item", item)

        return "item-view"
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    fun downloadImage(@PathVariable filename: String): Resource {
        // file:/Users/../....png
        return UrlResource("file:${fileStore.getFullPath(filename)}")
    }

    // 첨부파일 다운로드 기능
    @GetMapping("/attach/{itemId}")
    fun downloadAttach(@PathVariable itemId: Long): ResponseEntity<UrlResource> {
        val item = itemRepository.findById(itemId)
            ?: throw RuntimeException("Item[${itemId}] Not Found")

        val (storeFileName, uploadFileName) = item.attachFile?.let {
            it.storeFileName to it.uploadFileName
        } ?: throw RuntimeException("No Attach File")

        val resource = UrlResource("file:${fileStore.getFullPath(storeFileName)}")

        logger.info { "uploadFileName = ${uploadFileName}" }

        // 한글 깨짐 방지
        val encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8)
        // 다운로드를 위해서 필요한 헤더
        val contentDisposition = "attachment; filename=\"${encodedUploadFileName}\""
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
            .body(resource)
    }
}