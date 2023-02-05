package me.chung.ecommerceapi.web.controller

import me.chung.ecommerceapi.TestSupport
import me.chung.ecommerceapi.domain.category.Category
import me.chung.ecommerceapi.domain.category.CategoryRepos
import me.chung.ecommerceapi.web.service.CategoryResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CategoryControllerTest(
  private val categoryRepos: CategoryRepos,
) : TestSupport() {

  @BeforeEach
  fun setUp() {
    categoryRepos.deleteAll()
  }

  @Test
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
        Category("c1-2", 2, true, 1, topCategory1.id),
        Category("c2-1", 1, true, 0, topCategory2.id),
        Category("c2-2", 2, true, 1, topCategory2.id),
      )
    )

    val response = performGet("/api/v1/category").andReturn().response
    assertEquals(200, response.status)

    val result = toResult<List<CategoryResponse>>(response)
    assertThat(result)
      .hasSize(3)
  }
}
