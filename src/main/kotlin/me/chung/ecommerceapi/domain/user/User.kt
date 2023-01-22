package me.chung.ecommerceapi.domain.user

import jakarta.persistence.*
import me.chung.ecommerceapi.domain.BaseEntity
import me.chung.ecommerceapi.domain.address.Address
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(
  name = "user",
  uniqueConstraints = [
    UniqueConstraint(name = "user_uq", columnNames = arrayOf("loginId")),
  ]
)
class User(
  @Column(nullable = false)
  val loginId: String,

  @Column(length = 20, nullable = false)
  val name: String,

  @Column(length = 100, nullable = false)
  val email: String,

  @Column(nullable = false)
  val phone: String,

  @Column
  @OneToMany(mappedBy = "user")
  val address: List<Address>?,

  @Column(nullable = false)
  private val password: String,

  @Enumerated(EnumType.STRING)
  val role: Role,

) : UserDetails, BaseEntity() {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id", nullable = false)
  var id: Long? = null

  override fun getAuthorities(): List<SimpleGrantedAuthority> {
    return listOf(SimpleGrantedAuthority(role.name))
  }

  override fun getUsername(): String {
    return loginId
  }

  override fun getPassword(): String {
    return password
  }

  override fun isAccountNonExpired(): Boolean {
    return true
  }

  override fun isAccountNonLocked(): Boolean {
    return true
  }

  override fun isCredentialsNonExpired(): Boolean {
    return true
  }

  override fun isEnabled(): Boolean {
    return true
  }
}
