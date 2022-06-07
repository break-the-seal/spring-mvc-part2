package io.brick.springmvc.domain.item

import org.hibernate.validator.constraints.Range
import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class Item {
    var id: Long = 0L

    @field:NotBlank
    var itemName: String? = null

    @field:NotNull
    @field:Range(min = 1_000, max = 1_000_000)
    var price: Int? = null

    @field:NotNull
    @field:Max(9_999)
    var quantity: Int? = null

    override fun toString(): String {
        return "Item(id=$id, itemName=$itemName, price=$price, quantity=$quantity)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        if (id != other.id) return false
        if (itemName != other.itemName) return false
        if (price != other.price) return false
        if (quantity != other.quantity) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (itemName?.hashCode() ?: 0)
        result = 31 * result + (price ?: 0)
        result = 31 * result + (quantity ?: 0)
        return result
    }


}
