package io.brick.springmvc

import io.brick.springmvc.domain.item.Item
import io.brick.springmvc.domain.item.ItemRepository
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class TestDataInit(
    private val itemRepository: ItemRepository
) {
    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    fun init() {
        itemRepository.save(Item(itemName = "itemA", price = 10000, quantity = 10))
        itemRepository.save(Item(itemName = "itemB", price = 20000, quantity = 20))
    }
}