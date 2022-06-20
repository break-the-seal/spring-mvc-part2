package io.brick.springmvc.domain.member

import javax.validation.constraints.NotEmpty

data class Member(
    var id: Long = 0L,

    @field:NotEmpty
    var loginId: String,

    @field:NotEmpty
    var name: String,

    @field:NotEmpty
    var password: String
)