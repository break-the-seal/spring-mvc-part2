package io.brick.springmvc

import io.brick.springmvc.domain.item.Item
import io.brick.springmvc.domain.item.ItemRepository
import io.brick.springmvc.domain.member.Member
import io.brick.springmvc.domain.member.MemberRepository
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class TestDataInit(
    private val itemRepository: ItemRepository,
    private val memberRepository: MemberRepository
) {
    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    fun init() {
        itemRepository.save(Item().apply {
            itemName = "itemA"
            price = 10_000
            quantity = 10
        })

        itemRepository.save(Item().apply {
            itemName =  "itemB"
            price = 20_000
            quantity = 20
        })

        memberRepository.save(Member().apply {
            loginId = "test"
            password = "test!"
            name = "테스터"
        })
    }
}