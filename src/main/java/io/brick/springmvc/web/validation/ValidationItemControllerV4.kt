package io.brick.springmvc.web.validation

import io.brick.springmvc.domain.item.Item
import io.brick.springmvc.domain.item.ItemRepository
import io.brick.springmvc.domain.item.SaveCheck
import io.brick.springmvc.domain.item.UpdateCheck
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

//    @PostMapping("/add")
    fun addItem(
        @Validated @ModelAttribute item: Item,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): String {
        // 오브젝트 에러에 대해서는 직접 코드로 구현하는 것이 좋다.
        if (item.price != null && item.quantity != null) {
            val resultPrice = item.price!! * item.quantity!!
            if (resultPrice < 10_000) {
                bindingResult.reject("totalPriceMin", arrayOf(10_000, resultPrice), null)
            }
        }

        if (bindingResult.hasErrors()) {
            logger.info { "errors: ${bindingResult}" }
            return "validation/v4/addForm"
        }

        val savedItem = itemRepository.save(item)

        redirectAttributes.addAttribute("itemId", savedItem.id)
        redirectAttributes.addAttribute("status", true)
        return "redirect:/validation/v4/items/{itemId}"
    }

    @PostMapping("/add")
    fun addItem2(
        @Validated(SaveCheck::class) @ModelAttribute item: Item,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): String {
        // 오브젝트 에러에 대해서는 직접 코드로 구현하는 것이 좋다.
        if (item.price != null && item.quantity != null) {
            val resultPrice = item.price!! * item.quantity!!
            if (resultPrice < 10_000) {
                bindingResult.reject("totalPriceMin", arrayOf(10_000, resultPrice), null)
            }
        }

        if (bindingResult.hasErrors()) {
            logger.info { "errors: ${bindingResult}" }
            return "validation/v4/addForm"
        }

        val savedItem = itemRepository.save(item)

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
        @Validated(UpdateCheck::class) @ModelAttribute item: Item,
        bindingResult: BindingResult
    ): String {
        if (item.price != null && item.quantity != null) {
            val resultPrice = item.price!! * item.quantity!!
            if (resultPrice < 10_000) {
                bindingResult.reject("totalPriceMin", arrayOf(10_000, resultPrice), null)
            }
        }

        if (bindingResult.hasErrors()) {
            logger.info { "errors: ${bindingResult}" }
            return "validation/v4/editForm"
        }

        itemRepository.update(itemId, item)
        return "redirect:/validation/v4/items/{itemId}"
    }
}