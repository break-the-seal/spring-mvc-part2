package io.brick.springmvc.domain.item

class Item constructor() {
    var id: Long = 0L
    var itemName: String? = null
    var price: Int? = null
    var quantity: Int? = null

    var open: Boolean? = null
    var regions: List<String> = listOf()
    var itemType: ItemType? = null
    var deliveryCode: String? = null

    constructor(itemName: String, price: Int, quantity: Int): this() {
        this.itemName = itemName
        this.price = price
        this.quantity = quantity
    }
}
