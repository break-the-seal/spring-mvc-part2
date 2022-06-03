package io.brick.springmvc.web.basic

import io.brick.springmvc.domain.item.DeliveryCode
import io.brick.springmvc.domain.item.Item
import io.brick.springmvc.domain.item.ItemRepository
import io.brick.springmvc.domain.item.ItemType
import mu.KLogging
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/validation/v1/items")
class ValidationItemControllerV1(
    private val itemRepository: ItemRepository
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
        return "validation/v1/items"
    }

    @GetMapping("/{itemId}")
    fun item(@PathVariable itemId: Long, model: Model): String {
        val item = itemRepository.findById(itemId)
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
        logger.info { "item.open=${item.open}" }
        logger.info { "item.regions=${item.regions}" }
        logger.info { "item.itemType=${item.itemType}" }

        // 검증 오류 결과를 보관
        val errors = hashMapOf<String, String>()

        // 검증 로직
        if(!StringUtils.hasText(item.itemName)) {
            errors["itemName"] = "상품 이름은 필수 입력값입니다."
        }

        if(item.price == null || item.price!! < 1_000 || item.price!! > 1_000_000) {
            errors["price"] = "상품 가격은 1,000 ~ 1,000,000 범위 내의 입력값입니다."
        }

        if (item.quantity == null || item.quantity!! > 9_999) {
            errors["quantity"] = "상품 수량은 9,999개 까지 허용됩니다."
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.price != null && item.quantity != null) {
            val resultPrice = item.price!! * item.quantity!!
            if (resultPrice < 10_000) {
                errors["globalError"] = "가격 * 수량은 10,000원 이상이어야 합니다. (현재 값 = ${resultPrice})"
            }
        }

        // 검증에 실패하면 입력 폼으로 리다이렉트
        // @ModelAttribute item: Item 을 통해 기존에 입력한 값들은 그대로 model에 존재
        if (errors.isNotEmpty()) {
            logger.info { "errors: $errors" }
            model.addAttribute("errors", errors)
            return "validation/v1/addForm"
        }

        // 검증 성공 로직
        val savedItem: Item = itemRepository.save(item)

        redirectAttributes.addAttribute("itemId", savedItem.id)
        redirectAttributes.addAttribute("status", true)

        return "redirect:/validation/v1/items/{itemId}"    }

    @GetMapping("/{itemId}/edit")
    fun editForm(@PathVariable itemId: Long, model: Model): String {
        val item: Item? = itemRepository.findById(itemId)
        model.addAttribute("item", item)

        return "validation/v1/editForm"
    }

    @PostMapping("/{itemId}/edit")
    fun edit(@PathVariable itemId: Long, @ModelAttribute item: Item): String {
        itemRepository.update(itemId, item)

        return "redirect:/validation/v1/items/{itemId}"
    }
}