package io.brick.springmvc.web.validation

import io.brick.springmvc.domain.item.Item
import io.brick.springmvc.domain.item.ItemRepository
import mu.KLogging
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/validation/v1/items")
class ValidationItemControllerV1(
    private val itemRepository: ItemRepository
) {
    companion object: KLogging()

    @GetMapping
    fun items(model: Model): String {
        val items: List<Item> = itemRepository.findAll()
        model.addAttribute("items", items)
        return "validation/v1/items"
    }

    @GetMapping("/{itemId}")
    fun item(@PathVariable itemId: Long, model: Model): String {
        val item: Item = itemRepository.findById(itemId)
            ?: throw RuntimeException("item[${itemId}] NOT FOUND")

        model.addAttribute("item", item)
        return "validation/v1/item"
    }

    @GetMapping("/add")
    fun addForm(model: Model): String {
        model.addAttribute("item", Item())
        return "validation/v1/addForm"
    }

    @PostMapping("/add")
    fun addItem(
        @ModelAttribute item: Item,
        redirectAttributes: RedirectAttributes,
        model: Model
    ): String {
        val errors: MutableMap<String, String> = mutableMapOf()

        if (item.itemName.isNullOrBlank()) {
            errors["itemName"] = "상품 이름은 필수 입력값입니다."
        }
        if (item.price == null || item.price!! < 1_000 || item.price!! > 1_000_000) {
            errors["price"] = "상품 가격은 1,000 ~ 1,000,000 범위 내의 입력값입니다."
        }
        if (item.quantity == null || item.quantity!! > 9_999) {
            errors["quantity"] = "상품 수량은 9,999개 까지 허용됩니다."
        }

        if (item.price != null && item.quantity != null) {
            val resultPrice = item.price!! * item.quantity!!
            if (resultPrice < 10_000) {
                errors["globalError"] = "가격 * 수량은 10,000원 이상이어야 합니다. (현재 값 = ${resultPrice})"
            }
        }

        if (errors.isNotEmpty()) {
            logger.info { "errors: ${errors}" }
            model.addAttribute("errors", errors)
            return "validation/v1/addForm"
        }

        val savedItem = itemRepository.save(item)

        redirectAttributes.addAttribute("itemId", savedItem.id)
        redirectAttributes.addAttribute("status", true)
        return "redirect:/validation/v1/items/{itemId}"
    }

    @GetMapping("/{itemId}/edit")
    fun editForm(@PathVariable itemId: Long, model: Model): String {
        val item: Item = itemRepository.findById(itemId)
            ?: throw RuntimeException("item[${itemId}] NOT FOUND")

        model.addAttribute("item", item)
        return "validation/v1/editForm"
    }

    @PostMapping("/{itemId}/edit")
    fun edit(@PathVariable itemId: Long, @ModelAttribute item: Item): String {
        itemRepository.update(itemId, item)
        return "redirect:/validation/v1/items/{itemId}"
    }
}