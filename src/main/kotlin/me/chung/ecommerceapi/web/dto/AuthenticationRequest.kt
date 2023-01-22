package me.chung.ecommerceapi.web.dto

data class AuthenticationRequest(
  val loginId: String,
  val password: String,
)
