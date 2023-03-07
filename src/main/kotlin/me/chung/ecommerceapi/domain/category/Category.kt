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
  val isLeaf: Boolean? = false,

  @Column(nullable = false)
  val order: Short,
) : BaseEntity() {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  var id: Long = 0L

  @ManyToOne(targetEntity = Category::class, fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id", foreignKey = ForeignKey(name = "parent_category_fk"))
  var parentCategory: Category? = null
    private set

  @OneToMany(mappedBy = "parentCategory")
  val childCategory: List<Category> = emptyList()

  @Column(length = 10)
  var path: String? = null
    private set

  fun addParentCategory(category: Category) {
    parentCategory = category
    path = if (category.path == null) "${category.id}." else "${category.path}${category.id}."
  }
}
