package me.chung.ecommerceapi.web.dto

import me.chung.ecommerceapi.domain.user.Role

data class RegisterRequest(
  val loginId: String,
  val name: String,
  val email: String,
  val phone: String,
  val password: String,
  val role: Role,
)
