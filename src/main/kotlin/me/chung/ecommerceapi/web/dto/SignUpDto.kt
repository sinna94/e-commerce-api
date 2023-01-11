package me.chung.ecommerceapi.web.dto

import me.chung.ecommerceapi.domain.user.User

data class SignUpDto(
    val loginId: String,
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
)
