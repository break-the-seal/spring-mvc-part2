package io.brick.springmvc.web.validation

import io.brick.springmvc.domain.item.Item
import io.brick.springmvc.domain.item.ItemRepository
import mu.KLogging
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.validation.ValidationUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/validation/v2/items")
class ValidationItemControllerV2(
    private val itemRepository: ItemRepository,
    private val itemValidator: ItemValidator
) {
    companion object : KLogging()

    @GetMapping
    fun items(model: Model): String {
        val items: List<Item> = itemRepository.findAll()
        model.addAttribute("items", items)
        return "validation/v2/items"
    }

    @GetMapping("/{itemId}")
    fun item(@PathVariable itemId: Long, model: Model): String {
        val item: Item = itemRepository.findById(itemId)
            ?: throw RuntimeException("item[${itemId}] NOT FOUND")

        model.addAttribute("item", item)
        return "validation/v2/item"
    }

    @GetMapping("/add")
    fun addForm(model: Model): String {
        model.addAttribute("item", Item())
        return "validation/v2/addForm"
    }

    //    @PostMapping("/add")
    fun addItemV1(
        @ModelAttribute item: Item,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes,
        model: Model
    ): String {
        if (item.itemName.isNullOrBlank()) {
            bindingResult.addError(
                FieldError("item", "itemName", "상품 이름은 필수 입력값입니다.")
            )
        }
        if (item.price == null || item.price!! < 1_000 || item.price!! > 1_000_000) {
            bindingResult.addError(
                FieldError("item", "price", "상품 가격은 1,000 ~ 1,000,000 범위 내의 입력값입니다.")
            )

        }
        if (item.quantity == null || item.quantity!! > 9_999) {
            bindingResult.addError(FieldError("item", "quantity", "상품 수량은 9,999개 까지 허용됩니다."))
        }

        if (item.price != null && item.quantity != null) {
            val resultPrice = item.price!! * item.quantity!!
            if (resultPrice < 10_000) {
                bindingResult.addError(
                    ObjectError("item", "가격 * 수량은 10,000원 이상이어야 합니다. (현재 값 = ${resultPrice})")
                )
            }
        }

        if (bindingResult.hasErrors()) {
            logger.info { "errors: ${bindingResult}" }
            return "validation/v2/addForm"
        }

        val savedItem = itemRepository.save(item)

        redirectAttributes.addAttribute("itemId", savedItem.id)
        redirectAttributes.addAttribute("status", true)
        return "redirect:/validation/v2/items/{itemId}"
    }

//    @PostMapping("/add")
    fun addItemV2(
        @ModelAttribute item: Item,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes,
        model: Model
    ): String {
        if (item.itemName.isNullOrBlank()) {
            bindingResult.addError(
                FieldError("item", "itemName", "상품 이름은 필수 입력값입니다.")
            )
            bindingResult.addError(
                FieldError(
                    "item",
                    "itemName",
                    item.itemName,
                    false,
                    null,
                    null,
                    "상품 이름은 필수 입력값입니다."
                )
            )
        }
        if (item.price == null || item.price!! < 1_000 || item.price!! > 1_000_000) {
            bindingResult.addError(
                FieldError(
                    "item",
                    "price",
                    item.price,
                    false,
                    null,
                    null,
                    "상품 가격은 1,000 ~ 1,000,000 범위 내의 입력값입니다."
                )
            )

        }
        if (item.quantity == null || item.quantity!! > 9_999) {
            bindingResult.addError(
                FieldError(
                    "item",
                    "quantity",
                    item.quantity,
                    false,
                    null,
                    null,
                    "상품 수량은 9,999개 까지 허용됩니다."
                )
            )
        }

        if (item.price != null && item.quantity != null) {
            val resultPrice = item.price!! * item.quantity!!
            if (resultPrice < 10_000) {
                bindingResult.addError(
                    ObjectError("item", null, null, "가격 * 수량은 10,000원 이상이어야 합니다. (현재 값 = ${resultPrice})")
                )
            }
        }

        if (bindingResult.hasErrors()) {
            logger.info { "errors: ${bindingResult}" }
            return "validation/v2/addForm"
        }

        val savedItem = itemRepository.save(item)

        redirectAttributes.addAttribute("itemId", savedItem.id)
        redirectAttributes.addAttribute("status", true)
        return "redirect:/validation/v2/items/{itemId}"
    }

//    @PostMapping("/add")
    fun addItemV3(
        @ModelAttribute item: Item,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes,
        model: Model
    ): String {
        if (item.itemName.isNullOrBlank()) {
            bindingResult.addError(
                FieldError(
                    "item",
                    "itemName",
                    item.itemName,
                    false,
                    arrayOf("required.item.itemName"),
                    null,
                    null
                )
            )
        }
        if (item.price == null || item.price!! < 1_000 || item.price!! > 1_000_000) {
            bindingResult.addError(
                FieldError(
                    "item",
                    "price",
                    item.price,
                    false,
                    arrayOf("range.item.price"),
                    arrayOf(1_000, 1_000_000),
                    null
                )
            )

        }
        if (item.quantity == null || item.quantity!! > 9_999) {
            bindingResult.addError(
                FieldError(
                    "item",
                    "quantity",
                    item.quantity,
                    false,
                    arrayOf("max.item.quantity"),
                    arrayOf(9_999),
                    null
                )
            )
        }

        if (item.price != null && item.quantity != null) {
            val resultPrice = item.price!! * item.quantity!!
            if (resultPrice < 10_000) {
                bindingResult.addError(
                    ObjectError(
                        "item",
                        arrayOf("totalPriceMin.item"),
                        arrayOf(10_000, resultPrice),
                        null
                    )
                )
            }
        }

        if (bindingResult.hasErrors()) {
            logger.info { "errors: ${bindingResult}" }
            return "validation/v2/addForm"
        }

        val savedItem = itemRepository.save(item)

        redirectAttributes.addAttribute("itemId", savedItem.id)
        redirectAttributes.addAttribute("status", true)
        return "redirect:/validation/v2/items/{itemId}"
    }

//    @PostMapping("/add")
    fun addItemV4(
        @ModelAttribute item: Item,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes,
        model: Model
    ): String {
        logger.info { "objectName = ${bindingResult.objectName}" }
        logger.info { "target = ${bindingResult.target}" }

        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required")
//        if (item.itemName.isNullOrBlank()) {
//            bindingResult.rejectValue("itemName", "required")
//        }
        if (item.price == null || item.price!! < 1_000 || item.price!! > 1_000_000) {
            bindingResult.rejectValue("price", "range", arrayOf(1_000, 1_000_000), null)
        }
        if (item.quantity == null || item.quantity!! > 9_999) {
            bindingResult.rejectValue("quantity", "max", arrayOf(9_999), null)
        }

        if (item.price != null && item.quantity != null) {
            val resultPrice = item.price!! * item.quantity!!
            if (resultPrice < 10_000) {
                bindingResult.reject("totalPriceMin", arrayOf(10_000, resultPrice), null)
            }
        }

        if (bindingResult.hasErrors()) {
            logger.info { "errors: ${bindingResult}" }
            return "validation/v2/addForm"
        }

        val savedItem = itemRepository.save(item)

        redirectAttributes.addAttribute("itemId", savedItem.id)
        redirectAttributes.addAttribute("status", true)
        return "redirect:/validation/v2/items/{itemId}"
    }

    @PostMapping("/add")
    fun addItemV5(
        @ModelAttribute item: Item,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes,
        model: Model
    ): String {
        itemValidator.validate(item, bindingResult)

        if (bindingResult.hasErrors()) {
            logger.info { "errors: ${bindingResult}" }
            return "validation/v2/addForm"
        }

        val savedItem = itemRepository.save(item)

        redirectAttributes.addAttribute("itemId", savedItem.id)
        redirectAttributes.addAttribute("status", true)
        return "redirect:/validation/v2/items/{itemId}"
    }

    @GetMapping("/{itemId}/edit")
    fun editForm(@PathVariable itemId: Long, model: Model): String {
        val item: Item = itemRepository.findById(itemId)
            ?: throw RuntimeException("item[${itemId}] NOT FOUND")

        model.addAttribute("item", item)
        return "validation/v2/editForm"
    }

    @PostMapping("/{itemId}/edit")
    fun edit(@PathVariable itemId: Long, @ModelAttribute item: Item): String {
        itemRepository.update(itemId, item)
        return "redirect:/validation/v2/items/{itemId}"
    }
}