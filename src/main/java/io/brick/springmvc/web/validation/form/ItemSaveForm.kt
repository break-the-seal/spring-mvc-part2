package io.brick.springmvc.web.validation.form

import org.hibernate.validator.constraints.Range
import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ItemSaveForm(
    @field:NotBlank
    var itemName: String? = null,

    @field:NotNull
    @Range(min = 1_000, max = 1_000_000)
    var price: Int? = null,

    @field:NotNull
    @Max(9_999)
    var quantity: Int? = null
)