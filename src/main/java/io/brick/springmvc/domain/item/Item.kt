package io.brick.springmvc.domain.item

import org.hibernate.validator.constraints.Range
import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class Item {
    @NotNull(groups = [UpdateCheck::class]) // 수정 요구사항 추가
    var id: Long = 0L

    @NotBlank(groups = [SaveCheck::class, UpdateCheck::class])
    var itemName: String? = null

    @NotNull(groups = [SaveCheck::class, UpdateCheck::class])
    @Range(min = 1_000, max = 1_000_000, groups = [SaveCheck::class, UpdateCheck::class])
    var price: Int? = null

    @NotNull(groups = [SaveCheck::class, UpdateCheck::class])
    @Max(9_999, groups = [SaveCheck::class]) // 수정 요구사항 추가
    var quantity: Int? = null

    override fun toString(): String {
        return "Item(id=$id, itemName=$itemName, price=$price, quantity=$quantity)"
    }
}
