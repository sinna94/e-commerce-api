package me.chung.ecommerceapi.domain.category

import jakarta.persistence.*
import me.chung.ecommerceapi.domain.BaseEntity

@Entity
@Table(
  name = "category_level_2",
  uniqueConstraints = [UniqueConstraint(name = "category_level2_uq", columnNames = ["name", "category_level_1"])]
)
class CategoryLevel2(
  @Column(length = 100, nullable = false)
  val name: String,

  @ManyToOne(targetEntity = CategoryLevel1::class, fetch = FetchType.LAZY)
  @JoinColumn(name = "category_level_1", foreignKey = ForeignKey(name = "fk_category_level_1"))
  val categoryLevel1: CategoryLevel1,

  @OneToMany(mappedBy = "categoryLevel2")
  val categoryLevel3List: List<CategoryLevel3>
) : BaseEntity() {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  var id: Long? = null
}
