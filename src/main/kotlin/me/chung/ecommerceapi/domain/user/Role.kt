package me.chung.ecommerceapi.domain.user

enum class Role(key: String, title: String) {
  GUEST("ROLE_GUEST", "손님"),
  MEMBER("ROLE_MEMBER", "회원"),
  SELLER("ROLE_SELLER", "판매자")
}
