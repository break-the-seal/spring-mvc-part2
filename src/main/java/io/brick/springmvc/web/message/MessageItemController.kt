package io.brick.springmvc.web.message

import io.brick.springmvc.domain.item.Item
import io.brick.springmvc.domain.item.ItemRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/message/items")
class MessageItemController(
    private val itemRepository: ItemRepository
) {
    @GetMapping
    fun items(model: Model): String {
        val items: List<Item> = itemRepository.findAll()
        model.addAttribute("items", items)
        return "message/items"
    }

    @GetMapping("/{itemId}")
    fun item(@PathVariable itemId: Long, model: Model): String {
        val item: Item = itemRepository.findById(itemId)
            ?: throw RuntimeException("item[${itemId}] NOT FOUND")

        model.addAttribute("item", item)
        return "message/item"
    }

    @GetMapping("/add")
    fun addForm(model: Model): String {
        model.addAttribute("item", Item())
        return "message/addForm"
    }

    @PostMapping("/add")
    fun addItem(@ModelAttribute item: Item, redirectAttributes: RedirectAttributes): String {
        val savedItem: Item = itemRepository.save(item)
        redirectAttributes.addAttribute("itemId", savedItem.id)
        redirectAttributes.addAttribute("status", true)

        return "redirect:/message/items/{itemId}"
    }

    @GetMapping("/{itemId}/edit")
    fun editForm(@PathVariable itemId: Long, model: Model): String {
        val item: Item = itemRepository.findById(itemId)
            ?: throw RuntimeException("item[${itemId}] NOT FOUND")

        model.addAttribute("item", item)
        return "message/editForm"
    }

    @PostMapping("/{itemId}/edit")
    fun edit(@PathVariable itemId: Long, @ModelAttribute item: Item): String {
        itemRepository.update(itemId, item)
        return "redirect:/message/items/{itemId}"
    }
}