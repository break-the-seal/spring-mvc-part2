package io.brick.springmvc.web.basic.form

import org.hibernate.validator.constraints.Range
import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ItemUpdateForm(

    @NotNull
    var id: Long,

    @NotBlank
    var itemName: String,

    @NotNull
    @Range(min = 1000, max = 1000000)
    var price: Int,

    var quantity: Int
) {
}