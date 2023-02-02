package me.chung.ecommerceapi.domain.item

import jakarta.persistence.*
import me.chung.ecommerceapi.domain.BaseEntity
import me.chung.ecommerceapi.domain.user.User
import java.math.BigInteger

@Entity
@Table(
  name = "item"
)
class Item(
  @Column(nullable = false, length = 100)
  var title: String,

  @Column(nullable = false)
  var price: BigInteger,

  @Column
  var contents: String,

  @ManyToOne(targetEntity = User::class, fetch = FetchType.LAZY)
  @JoinColumn(name = "seller", foreignKey = ForeignKey(name = "fk_seller"))
  var seller: Long,

  @Column
  var stopSelling: Boolean?,

) : BaseEntity() {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  var id: Long? = null
}
