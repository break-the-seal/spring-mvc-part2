package io.brick.springmvc.domain.item

import org.hibernate.validator.constraints.Range
import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

//@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000", message = "총합이 100000원 이상 입력해주세요.")
class Item {
    @NotNull(groups = [UpdateCheck::class]) // 수정 요구사항 추가
    var id: Long = 0L

    // @field: 안써도 됨
    @NotBlank(groups = [SaveCheck::class, UpdateCheck::class])
    var itemName: String? = null

//    Validation {
//        1. typeMismatch --> X
//        2. Bean Validation (@ 기법)
//    }

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
