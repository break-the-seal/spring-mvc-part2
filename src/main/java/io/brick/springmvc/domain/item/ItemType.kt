package io.brick.springmvc.domain.item

enum class ItemType(
    val description: String
) {
    BOOK("도서"), FOOD("음식"), ETC("기타");
}