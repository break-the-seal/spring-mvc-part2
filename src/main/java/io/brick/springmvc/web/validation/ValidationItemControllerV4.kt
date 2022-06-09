package io.brick.springmvc.web.validation

import io.brick.springmvc.domain.item.Item
import io.brick.springmvc.domain.item.ItemRepository
import io.brick.springmvc.web.validation.form.ItemSaveForm
import io.brick.springmvc.web.validation.form.ItemUpdateForm
import mu.KLogging
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/validation/v4/items")
class ValidationItemControllerV4(
    private val itemRepository: ItemRepository
) {
    companion object : KLogging()

    @GetMapping
    fun items(model: Model): String {
        val items: List<Item> = itemRepository.findAll()
        model.addAttribute("items", items)
        return "validation/v4/items"
    }

    @GetMapping("/{itemId}")
    fun item(@PathVariable itemId: Long, model: Model): String {
        val item: Item = itemRepository.findById(itemId)
            ?: throw RuntimeException("item[${itemId}] NOT FOUND")

        model.addAttribute("item", item)
        return "validation/v4/item"
    }

    @GetMapping("/add")
    fun addForm(model: Model): String {
        model.addAttribute("item", Item())
        return "validation/v4/addForm"
    }

    // ModelAttribute <- item 지정을 해야 한다.
    @PostMapping("/add")
    fun addItem2(
        @Validated @ModelAttribute("item") form: ItemSaveForm,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): String {
        // 오브젝트 에러에 대해서는 직접 코드로 구현하는 것이 좋다.
        if (form.price != null && form.quantity != null) {
            val resultPrice = form.price!! * form.quantity!!
            if (resultPrice < 10_000) {
                bindingResult.reject("totalPriceMin", arrayOf(10_000, resultPrice), null)
            }
        }

        if (bindingResult.hasErrors()) {
            logger.info { "errors: ${bindingResult}" }
            return "validation/v4/addForm"
        }

        val itemParam = Item().apply {
            itemName = form.itemName
            price = form.price
            quantity = form.quantity
        }
        val savedItem = itemRepository.save(itemParam)

        redirectAttributes.addAttribute("itemId", savedItem.id)
        redirectAttributes.addAttribute("status", true)
        return "redirect:/validation/v4/items/{itemId}"
    }

    @GetMapping("/{itemId}/edit")
    fun editForm(@PathVariable itemId: Long, model: Model): String {
        val item: Item = itemRepository.findById(itemId)
            ?: throw RuntimeException("item[${itemId}] NOT FOUND")

        model.addAttribute("item", item)
        return "validation/v4/editForm"
    }

    @PostMapping("/{itemId}/edit")
    fun edit(
        @PathVariable itemId: Long,
        @Validated @ModelAttribute("item") form: ItemUpdateForm,
        bindingResult: BindingResult
    ): String {
        if (form.price != null && form.quantity != null) {
            val resultPrice = form.price!! * form.quantity!!
            if (resultPrice < 10_000) {
                bindingResult.reject("totalPriceMin", arrayOf(10_000, resultPrice), null)
            }
        }

        if (bindingResult.hasErrors()) {
            logger.info { "errors: ${bindingResult}" }
            return "validation/v4/editForm"
        }

        val itemParam = Item().apply {
            itemName = form.itemName
            price = form.price
            quantity = form.quantity
        }
        itemRepository.update(itemId, itemParam)
        return "redirect:/validation/v4/items/{itemId}"
    }
}