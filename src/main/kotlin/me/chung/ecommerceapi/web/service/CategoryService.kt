package me.chung.ecommerceapi.web.service

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import me.chung.ecommerceapi.domain.category.Category
import me.chung.ecommerceapi.domain.category.CategoryRelation
import me.chung.ecommerceapi.domain.category.CategoryRepos
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
@Transactional
class CategoryService(
  private val categoryRepos: CategoryRepos,
) {

  companion object {
    const val TOP_LEVEL: Short = 0
  }

  fun getCategories(categoryId: Long?): List<CategoryResponse> {
    if (categoryId == null) {
      return findTopLevelCategoryResponse()
    }

    val hasCategory = categoryRepos.existsById(categoryId)

    if (!hasCategory) {
      throw IllegalArgumentException("invalid category id : $categoryId")
    }

    return categoryRepos.findByCategoryRelationById(categoryId)
      .map {
        val (id, name, level, isLeaf, order) = it
        CategoryResponse(id, name, level, isLeaf, order)
      }
  }

  private fun findTopLevelCategoryResponse(): List<CategoryResponse> {
    return toCategoryResponseList(categoryRepos.findByCategoryLevelAndOrderByOrder(TOP_LEVEL))
  }

  private fun toCategoryResponseList(categoryRelations: Collection<CategoryRelation>) =
    categoryRelations
      .groupBy({ it }) { it.parentId }
      .entries
      .map {
        val (id, name, level, isLeaf, order) = it.key
        CategoryResponse(id, name, level, isLeaf, order, it.value.filterNotNull())
      }

  @OptIn(ExperimentalStdlibApi::class)
  fun findCategoryById(categoryId: Long): Category {
    return categoryRepos.findById(categoryId).getOrNull()
      ?: throw IllegalArgumentException("invalid category id : $categoryId")
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
@Schema(description = "카테고리 정보")
data class CategoryResponse(
  val id: Long,
  val name: String,
  val level: Short,
  @JsonProperty("isLeaf")
  val isLeaf: Boolean,
  val order: Short,
  val categories: List<Long>? = null,
)
