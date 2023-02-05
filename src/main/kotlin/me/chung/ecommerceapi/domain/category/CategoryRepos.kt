package me.chung.ecommerceapi.domain.category

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CategoryRepos : JpaRepository<Category, Long> {

  @Query(
    """
      select new me.chung.ecommerceapi.domain.category.CategoryRelation(a.id, a.name, a.categoryLevel, a.isLeaf, a.order, b.id)
      from Category a left join Category b on a.id = b.parentId
      where a.categoryLevel = :categoryLevel
    """
  )
  fun findByCategoryLevelAndOrderByOrder(categoryLevel: Short): Collection<CategoryRelation>
}

data class CategoryRelation(
  val id: Long,
  val name: String,
  val categoryLevel: Short,
  val isLeaf: Boolean,
  val order: Short,
  var parentId: Long?,
)
