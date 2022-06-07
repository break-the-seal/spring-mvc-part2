package io.brick.springmvc.validation

import io.brick.springmvc.domain.item.Item
import org.junit.jupiter.api.Test
import javax.validation.ConstraintViolation
import javax.validation.Validation

internal class BeanValidationTest {
    @Test
    fun beanValidation() {
        val factory = Validation.buildDefaultValidatorFactory()
        val validator = factory.validator

        val item = Item().apply {
            itemName = " "
            price = 0
            quantity = 10_000
        }

        val violations: Set<ConstraintViolation<Item>> = validator.validate(item)
        for (violation in violations) {
            println("violation = ${violation}")
            println("violation.message = ${violation.message}")
        }
    }
}