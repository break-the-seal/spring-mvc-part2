package io.brick.springmvc.domain

import org.springframework.stereotype.Repository

@Repository
class ItemRepository {
    companion object {
        private val store: MutableMap<Long, Item> = mutableMapOf()
        private var sequence: Long = 0L
    }

    fun save(item: Item): Item {
        item.id = ++sequence
        store[item.id] = item
        return item
    }

    fun findById(id: Long): Item? {
        return store[id]
    }
}