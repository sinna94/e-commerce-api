package me.chung.ecommerceapi.domain.category

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CategoryRepos : JpaRepository<Category, Long> {

  @Query(
    """
      select a
      from Category a left join fetch a.childCategory
      where a.categoryLevel = :categoryLevel
    """
  )
  fun findByCategoryLevelAndOrderByOrder(categoryLevel: Short): Collection<Category>

  @Query(
    """
      select new me.chung.ecommerceapi.domain.category.CategoryRelation(a.id, a.name, a.categoryLevel, a.isLeaf, a.order)
      from Category a
      where a.parentCategory.id = :id
    """
  )
  fun findByCategoryRelationById(id: Long): List<CategoryRelation>
}

data class CategoryRelation(
  val id: Long,
  val name: String,
  val categoryLevel: Short,
  val isLeaf: Boolean,
  val order: Short,
  var parentId: Long? = null,
) {
  constructor(
    id: Long,
    name: String,
    categoryLevel: Short,
    isLeaf: Boolean,
    order: Short,
  ) : this(id, name, categoryLevel, isLeaf, order, null)

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as CategoryRelation

    if (id != other.id) return false
    if (name != other.name) return false
    if (categoryLevel != other.categoryLevel) return false
    if (isLeaf != other.isLeaf) return false
    if (order != other.order) return false

    return true
  }

  override fun hashCode(): Int {
    var result = id.hashCode()
    result = 31 * result + name.hashCode()
    result = 31 * result + categoryLevel
    result = 31 * result + isLeaf.hashCode()
    result = 31 * result + order
    return result
  }
}
