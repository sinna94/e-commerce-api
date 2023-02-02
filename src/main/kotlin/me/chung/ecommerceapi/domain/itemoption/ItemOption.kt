package me.chung.ecommerceapi.domain.itemoption

import jakarta.persistence.*
import me.chung.ecommerceapi.domain.BaseEntity
import me.chung.ecommerceapi.domain.user.User
import java.math.BigInteger

@Entity
@Table(
  name = "item_option"
)
class ItemOption(
  @ManyToOne(targetEntity = User::class, fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id", foreignKey = ForeignKey(name = "fk_item"))
  var itemId: Long,

  @Column(nullable = false, length = 100)
  var optionTitle: String,

  @Column(nullable = false, length = 50)
  var optionDescription: String,

  @Column
  var optionPrice: BigInteger?,

  @Column
  var parentOptionId: Long?,
) : BaseEntity() {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  var id: Long? = null
}
