package io.brick.springmvc.domain.item

import org.hibernate.validator.constraints.Range
import org.hibernate.validator.constraints.ScriptAssert
import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

// 권장하지 않음
//@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000", message = "총합이 10000원 넘게 입력해주세요.")
class Item() {

    @NotNull(groups = [UpdateCheck::class])
    var id: Long = 0L

    @NotBlank(groups = [SaveCheck::class, UpdateCheck::class])
    var itemName: String? = null

    @NotNull(groups = [SaveCheck::class, UpdateCheck::class])
    @Range(min = 1_000, max = 1_000_000, groups = [SaveCheck::class, UpdateCheck::class])
    var price: Int? = null

    @NotNull(groups = [SaveCheck::class, UpdateCheck::class])
    @Max(9999, groups = [SaveCheck::class])
    var quantity: Int? = null

    var open: Boolean? = null               // 판매 여부
    var regions: List<String> = listOf()    // 등록 지역
    var itemType: ItemType? = null          // 상품 종류
    var deliveryCode: String? = null        // 배송 방식

    constructor(itemName: String, price: Int, quantity: Int): this() {
        this.itemName = itemName
        this.price = price
        this.quantity = quantity
    }
}