package io.brick.springmvc.domain.item

class Item {
    var id: Long = 0L
    var itemName: String? = null
    var price: Int? = null
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
