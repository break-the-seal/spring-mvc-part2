package io.brick.springmvc.domain.item

data class Item(
    var id: Long = 0L,
    var itemName: String? = null,
    var price: Int? = null,
    var quantity: Int? = null,
    var open: Boolean? = null,
    var regions: List<String> = listOf(),
    var itemType: ItemType? = null,
    var deliveryCode: String? = null
)
