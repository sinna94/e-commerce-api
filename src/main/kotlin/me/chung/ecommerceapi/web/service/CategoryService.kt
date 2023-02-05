package me.chung.ecommerceapi.web.service

import com.fasterxml.jackson.annotation.JsonProperty
import me.chung.ecommerceapi.domain.category.CategoryRepos
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CategoryService(
  private val categoryRepos: CategoryRepos,
) {

  companion object {
    const val TOP_LEVEL: Short = 1
  }

  fun getTopLevelCategories(): List<CategoryResponse> {
    return findCategoryResponseByLevel(TOP_LEVEL)
  }

  private fun findCategoryResponseByLevel(categoryLevel: Short): List<CategoryResponse> {
    return categoryRepos.findByCategoryLevelAndOrderByOrder(categoryLevel)
      .groupBy({ c ->
        c.parentId = null
        c
      }) { it.parentId }
      .entries
      .map {
        val (id, name, level, isLeaf, order, _) = it.key
        CategoryResponse(id, name, level, isLeaf, order, it.value.filterNotNull())
      }
  }
}

/**
 * 카테고리 정보
 * @param id 카테고리 아이디
 *
 * @param name 카테고리 이름
 *
 * @param level 카테고리 레벨
 *
 * @param isLeaf 최하단 카테고리 여부
 *
 * @param order 카테고리 표시 순서
 *
 * @param categories 하위 카테고리 ID 리스트
 */
data class CategoryResponse(
  val id: Long,
  val name: String,
  val level: Short,
  @JsonProperty("isLeaf")
  val isLeaf: Boolean,
  val order: Short,
  val categories: List<Long>,
)
