package me.chung.ecommerceapi.service

data class OAuthAttributes(
  private val attributes: Map<String, Any>,
  private val nameAttributeKey: String,
  private val name: String,
  private val email: String,
)
