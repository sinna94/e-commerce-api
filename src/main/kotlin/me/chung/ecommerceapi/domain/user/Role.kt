package me.chung.ecommerceapi.domain.user

enum class Role(key: String, title: String) {
  MEMBER("MEMBER", "회원"),
  SELLER("SELLER", "판매자")
}
