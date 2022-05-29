package io.brick.springmvc.web.basic

import io.brick.springmvc.domain.item.DeliveryCode
import io.brick.springmvc.domain.item.Item
import io.brick.springmvc.domain.item.ItemRepository
import io.brick.springmvc.domain.item.ItemType
import mu.KLogging
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/form/items")
class FormItemController(
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
        return "form/items"
    }

    @GetMapping("/{itemId}")
    fun item(@PathVariable itemId: Long, model: Model): String {
        val item = itemRepository.findById(itemId)
            ?: throw RuntimeException("item[${itemId}] NOT FOUND")

        model.addAttribute("item", item)
        return "form/item"
    }

    @GetMapping("/add")
    fun addForm(model: Model): String {
        model.addAttribute("item", Item())
        return "form/addForm"
    }

    @PostMapping("/add")
    fun addItem(@ModelAttribute item: Item, redirectAttributes: RedirectAttributes): String {
        logger.info { "item.open=${item.open}" }
        logger.info { "item.regions=${item.regions}" }
        logger.info { "item.itemType=${item.itemType}" }

        val savedItem: Item = itemRepository.save(item)

        redirectAttributes.addAttribute("itemId", savedItem.id)
        redirectAttributes.addAttribute("status", true)

        return "redirect:/form/items/{itemId}"
    }

    @GetMapping("/{itemId}/edit")
    fun editForm(@PathVariable itemId: Long, model: Model): String {
        val item: Item? = itemRepository.findById(itemId)
        model.addAttribute("item", item)

        return "form/editForm"
    }

    @PostMapping("/{itemId}/edit")
    fun edit(@PathVariable itemId: Long, @ModelAttribute item: Item): String {
        itemRepository.update(itemId, item)

        return "redirect:/form/items/{itemId}"
    }
}