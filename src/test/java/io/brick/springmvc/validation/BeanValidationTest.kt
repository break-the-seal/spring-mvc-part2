package io.brick.springmvc.validation

import io.brick.springmvc.domain.item.Item
import org.junit.jupiter.api.Test
import javax.validation.Validation

class BeanValidationTest {

    @Test
    fun beanValidation() {
        val factory = Validation.buildDefaultValidatorFactory()
        val validator = factory.validator

        val item = Item()
        item.itemName = " " // 공백
        item.price = 0
        item.quantity = 10_000

        val violations = validator.validate(item)
        for (violation in violations) {
            println("violation = $violation")
            println("violation = ${violation.message}")
        }
    }
}