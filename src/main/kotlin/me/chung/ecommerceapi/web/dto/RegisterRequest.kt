package me.chung.ecommerceapi.web.dto

data class RegisterRequest(
  val loginId: String,
  val name: String,
  val email: String,
  val phone: String,
  val password: String,
)