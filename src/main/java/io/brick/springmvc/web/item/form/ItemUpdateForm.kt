package io.brick.springmvc.web.item.form

import org.hibernate.validator.constraints.Range
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ItemUpdateForm(
    @field:NotNull
    var id: Long? = null,

    @field:NotBlank
    var itemName: String? = null,

    @field:NotNull
    @field:Range(min = 1_000, max = 1_000_000)
    var price: Int? = null,

    var quantity: Int? = null
)