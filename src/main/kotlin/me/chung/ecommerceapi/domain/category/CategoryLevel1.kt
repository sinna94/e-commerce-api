package me.chung.ecommerceapi.domain.category

import jakarta.persistence.*
import me.chung.ecommerceapi.domain.BaseEntity

@Entity
@Table(
  name = "category_level_1", uniqueConstraints = [UniqueConstraint(name = "category_level1_uq", columnNames = ["name"])]
)
class CategoryLevel1(
  @Column(length = 100, nullable = false)
  var name: String,

  @OneToMany(mappedBy = "categoryLevel1")
  val categoryLevel2List: List<CategoryLevel2>
) : BaseEntity() {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  var id: Long? = null
}
