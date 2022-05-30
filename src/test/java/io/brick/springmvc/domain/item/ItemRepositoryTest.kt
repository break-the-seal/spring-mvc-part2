package io.brick.springmvc.domain.item

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ItemRepositoryTest {
    private val itemRepository: ItemRepository = ItemRepository()

    @AfterEach
    fun afterEach() {
        itemRepository.clearStore()
    }

    @Test
    fun save() {
        // given
        val item = Item(itemName = "itemA", price = 10000, quantity = 10)

        // when
        val savedItem = itemRepository.save(item)

        // then
        val findItem = itemRepository.findById(item.id)
        assertSame(findItem, savedItem)
    }

    @Test
    fun findAll() {
        // given
        val item1 = Item(itemName = "item1", price = 10000, quantity = 10)
        val item2 = Item(itemName = "item2", price = 20000, quantity = 20)

        itemRepository.save(item1)
        itemRepository.save(item2)

        // when
        val result = itemRepository.findAll()

        // then
        assertEquals(result.size, 2)
        assertTrue(result.contains(item1))
        assertTrue(result.contains(item2))
    }

    @Test
    fun updateItem() {
        // given
        val item = Item(itemName = "item1", price = 10000, quantity = 10)

        val savedItem = itemRepository.save(item)
        val itemId = savedItem.id

        // when
        val updateParam = Item(itemName = "item2", price = 20000, quantity = 20)
        itemRepository.update(itemId, updateParam)

        // then
        val findItem = itemRepository.findById(itemId)

        assertEquals(findItem?.itemName ?: "", updateParam.itemName)
        assertEquals(findItem?.price ?: -1, updateParam.price)
    }
}