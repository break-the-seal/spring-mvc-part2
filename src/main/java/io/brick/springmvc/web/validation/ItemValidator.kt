package io.brick.springmvc.web.validation

import io.brick.springmvc.domain.item.Item
import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.ValidationUtils
import org.springframework.validation.Validator

@Component
class ItemValidator: Validator {
    override fun supports(clazz: Class<*>): Boolean {
        return Item::class.java.isAssignableFrom(clazz)
        // item == clazz
        // item == subItem (자식 클래스까지 커버)
    }

    override fun validate(target: Any, errors: Errors) {
        val item = target as Item

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "itemName", "required")
//        if (item.itemName.isNullOrBlank()) {
//            errors.rejectValue("itemName", "required")
//        }
        if (item.price == null || item.price!! < 1_000 || item.price!! > 1_000_000) {
            errors.rejectValue("price", "range", arrayOf(1_000, 1_000_000), null)
        }
        if (item.quantity == null || item.quantity!! > 9_999) {
            errors.rejectValue("quantity", "max", arrayOf(9_999), null)
        }

        if (item.price != null && item.quantity != null) {
            val resultPrice = item.price!! * item.quantity!!
            if (resultPrice < 10_000) {
                errors.reject("totalPriceMin", arrayOf(10_000, resultPrice), null)
            }
        }
    }
}