package me.chung.ecommerceapi.web.controller

import me.chung.ecommerceapi.TestSupport
import me.chung.ecommerceapi.domain.category.Category
import me.chung.ecommerceapi.domain.category.CategoryRepos
import me.chung.ecommerceapi.web.service.CategoryResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CategoryControllerTest(
  private val categoryRepos: CategoryRepos,
) : TestSupport() {

  @BeforeEach
  fun setUp() {
    categoryRepos.deleteAll()
  }

  @Test
  @DisplayName("카테고리 조회 테스트")
  fun getTopLevelCategories() {
    val topCategory1 = categoryRepos.save(
      Category("c1", 0, false, 0)
    )
    val topCategory2 = categoryRepos.save(
      Category("c2", 0, false, 1)
    )
    categoryRepos.save(
      Category("c3", 0, false, 2)
    )
    categoryRepos.saveAll(
      listOf(
        Category("c1-1", 1, true, 0, topCategory1.id),
        Category("c1-2", 1, true, 1, topCategory1.id),
        Category("c2-1", 1, true, 0, topCategory2.id),
        Category("c2-2", 1, true, 1, topCategory2.id),
      )
    )

    val response = performGet("/api/v1/category").andReturn().response
    assertEquals(200, response.status)

    val result = toResult<List<CategoryResponse>>(response)
    assertThat(result)
      .hasSize(3)
    assertThat(result)
      .extracting("name")
      .containsExactlyInAnyOrder("c1", "c2", "c3")
  }

  @Test
  @DisplayName("카테고리 ID 로 조회 테스트")
  fun getCategoriesById() {
    val topCategory = categoryRepos.save(
      Category("c1", 0, false, 0)
    )
    val subCategory = categoryRepos.save(Category("c1-1", 1, false, 0, topCategory.id))

    categoryRepos.saveAll(
      listOf(
        Category("c1-1-1", 2, true, 1, subCategory.id),
        Category("c1-1-2", 2, true, 2, subCategory.id),
        Category("c1-1-3", 2, true, 3, subCategory.id),
        Category("c1-1-4", 2, true, 4, subCategory.id),
      )
    )

    val response = performGet("/api/v1/category", mapOf("categoryId" to subCategory.id.toString())).andReturn().response
    assertEquals(200, response.status)

    val result = toResult<List<CategoryResponse>>(response)
    assertThat(result)
      .hasSize(4)
    assertThat(result)
      .extracting("name")
      .containsExactlyInAnyOrder(
        "c1-1-1",
        "c1-1-2",
        "c1-1-3",
        "c1-1-4",
      )
  }
}
