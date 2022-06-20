package io.brick.springmvc.web.login.form

import javax.validation.constraints.NotEmpty

data class LoginForm(
    @field:NotEmpty
    val loginId: String? = null,

    @field:NotEmpty
    val password: String? = null
)