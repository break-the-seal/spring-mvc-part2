package io.brick.springmvc.domain.item

class Item() {

    var id: Long = 0L
    var itemName: String? = null
    var price: Int? = null
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