package me.chung.ecommerceapi.domain.category

import jakarta.persistence.*
import me.chung.ecommerceapi.domain.BaseEntity

@Entity
@Table(
  name = "category_level_3",
  uniqueConstraints = [UniqueConstraint(name = "category_level3_uq", columnNames = ["name", "category_level_2"])]
)
class CategoryLevel3(
  @Column(length = 100, nullable = false)
  val name: String,

  @ManyToOne(targetEntity = CategoryLevel2::class, fetch = FetchType.LAZY)
  @JoinColumn(name = "category_level_2", foreignKey = ForeignKey(name = "fk_category_level_2"))
  val categoryLevel2: CategoryLevel2,
) : BaseEntity() {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  var id: Long? = null
}
