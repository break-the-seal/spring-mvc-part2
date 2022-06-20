package io.brick.springmvc.domain.member

import javax.validation.constraints.NotEmpty

data class Member(
    var id: Long = 0L,

    @field:NotEmpty
    var loginId: String? = null,

    @field:NotEmpty
    var name: String? = null,

    @field:NotEmpty
    var password: String? = null
)