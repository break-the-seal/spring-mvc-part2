package io.brick.springmvc.web.basic

import io.brick.springmvc.domain.item.*
import io.brick.springmvc.web.basic.form.ItemSaveForm
import io.brick.springmvc.web.basic.form.ItemUpdateForm
import mu.KLogging
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.StringUtils
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.validation.ValidationUtils
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/validation/v4/items")
class ValidationItemControllerV4(
    private val itemRepository: ItemRepository,
) {
    companion object : KLogging()

    // 자동으로 model 에 담김
    @ModelAttribute("regions")
    fun regions(): Map<String, String> {
        val regions: MutableMap<String, String> = mutableMapOf()
        regions["SEOUL"] = "서울"
        regions["BUSAN"] = "부산"
        regions["JEJU"] = "제주"

        return regions
    }

    @ModelAttribute("itemTypes")
    fun itemTypes(): Array<ItemType> {
        return ItemType.values()
    }

    @ModelAttribute("deliveryCodes")
    fun deliveryCodes(): List<DeliveryCode> {
        val deliveryCodes: MutableList<DeliveryCode> = mutableListOf()
        deliveryCodes.add(DeliveryCode("FAST", "빠른 배송"))
        deliveryCodes.add(DeliveryCode("NORMAL", "일반 배송"))
        deliveryCodes.add(DeliveryCode("SLOW", "느린 배송"))
        return deliveryCodes
    }

    @GetMapping
    fun items(model: Model): String {
        val items: List<Item> = itemRepository.findAll()
        model.addAttribute("items", items)
        return "validation/v4/items"
    }

    @GetMapping("/{itemId}")
    fun item(@PathVariable itemId: Long, model: Model): String {
        val item = itemRepository.findById(itemId)
            ?: throw RuntimeException("item[${itemId}] NOT FOUND")

        model.addAttribute("item", item)
        return "validation/v4/item"
    }

    @GetMapping("/add")
    fun addForm(model: Model): String {
        model.addAttribute("item", Item())
        return "validation/v4/addForm"
    }

    @PostMapping("/add")
    fun addItem(
        // Item 클래스의 SaveCheck가 적용된 부분만 검사
        @Validated @ModelAttribute(name = "item") itemSaveForm: ItemSaveForm,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes,
        model: Model
    ): String {

        // 특정 필드가 아닌 복합 룰 검증
        if (itemSaveForm.price != null && itemSaveForm.quantity != null) {
            val resultPrice = itemSaveForm.price!! * itemSaveForm.quantity!!
            if (resultPrice < 10_000) {
                bindingResult.reject("totalPriceMin", arrayOf(10_000, resultPrice), null)
            }
        }

        // 검증에 실패하면 입력 폼으로 리다이렉트
        // bindingResult는 view에 값을 넘길 수 있음
        if (bindingResult.hasErrors()) {
            logger.info { "errors: $bindingResult" }
            return "validation/v4/addForm"
        }

        // 검증 성공 로직
        val item = Item()
        item.itemName = itemSaveForm.itemName
        item.price = itemSaveForm.price
        item.quantity = itemSaveForm.quantity

        val savedItem: Item = itemRepository.save(item)

        redirectAttributes.addAttribute("itemId", savedItem.id)
        redirectAttributes.addAttribute("status", true)

        return "redirect:/validation/v4/items/{itemId}"
    }

    @GetMapping("/{itemId}/edit")
    fun editForm(@PathVariable itemId: Long, model: Model): String {
        val item: Item? = itemRepository.findById(itemId)
        model.addAttribute("item", item)

        return "validation/v4/editForm"
    }

    @PostMapping("/{itemId}/edit")
    fun edit(
        @PathVariable itemId: Long,
        @Validated @ModelAttribute(name = "item") itemUpdateForm: ItemUpdateForm,
        bindingResult: BindingResult
    ): String {

        // 특정 필드가 아닌 복합 룰 검증
        if (itemUpdateForm.price != null && itemUpdateForm.quantity != null) {
            val resultPrice = itemUpdateForm.price!! * itemUpdateForm.quantity!!
            if (resultPrice < 10_000) {
                bindingResult.reject("totalPriceMin", arrayOf(10_000, resultPrice), null)
            }
        }

        if (bindingResult.hasErrors()) {
            logger.info { "errors: $bindingResult" }
            return "validation/v4/editForm"
        }

        val itemParam = Item()
        itemParam.itemName = itemUpdateForm.itemName
        itemParam.price = itemUpdateForm.price
        itemParam.quantity = itemUpdateForm.quantity

        itemRepository.update(itemId, itemParam)

        return "redirect:/validation/v4/items/{itemId}"
    }
}