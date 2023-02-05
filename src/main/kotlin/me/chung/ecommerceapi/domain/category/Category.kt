package me.chung.ecommerceapi.domain.category

import jakarta.persistence.*
import me.chung.ecommerceapi.domain.BaseEntity

@Entity
@Table(
  name = "category",
  uniqueConstraints = [UniqueConstraint(name = "category_uq", columnNames = ["name", "categoryLevel"])]
)
class Category(
  @Column(length = 100, nullable = false)
  val name: String,

  @Column(nullable = false)
  val categoryLevel: Short,

  @Column
  val isLeaf: Boolean = false,

  @Column(nullable = false)
  val order: Short,

  @Column
  val parentId: Long? = null,
) : BaseEntity() {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  var id: Long = 0L
}
