package me.chung.ecommerceapi.web.controller

import me.chung.ecommerceapi.MvcMockTestSupport
import me.chung.ecommerceapi.domain.category.Category
import me.chung.ecommerceapi.domain.category.CategoryRepos
import me.chung.ecommerceapi.domain.item.Item
import me.chung.ecommerceapi.domain.item.ItemRepos
import me.chung.ecommerceapi.domain.user.Role
import me.chung.ecommerceapi.domain.user.User
import me.chung.ecommerceapi.domain.user.UserRepos
import me.chung.ecommerceapi.web.service.ItemResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.math.BigInteger

class SearchControllerTest(
  private val itemRepos: ItemRepos,
  private val categoryRepos: CategoryRepos,
  private val userRepos: UserRepos,
) : MvcMockTestSupport() {

  @BeforeEach
  fun setUp() {
    itemRepos.deleteAllInBatch()
    categoryRepos.deleteAllInBatch()
    userRepos.deleteAllInBatch()
  }

  @Test
  fun searchAll() {
    val user = userRepos.save(User("u", "n", "e", "p", null, "", Role.SELLER))
    val categoryTop = categoryRepos.save(Category("category", 0, false, 0))
    val categoryMiddle =
      categoryRepos.save(Category("category", 1, false, 0).also { it.addParentCategory(categoryTop) })
    val categoryLeaf =
      categoryRepos.save(Category("category", 2, true, 0).also { it.addParentCategory(categoryMiddle) })

    itemRepos.saveAll(
      listOf(
        Item("itemTop1", BigInteger.valueOf(10000L), "", user, false, categoryTop),
        Item("itemTop2", BigInteger.valueOf(50000L), "", user, false, categoryTop),
        Item("itemMiddle1", BigInteger.valueOf(10000L), "", user, false, categoryMiddle),
        Item("itemMiddle2", BigInteger.valueOf(5000L), "", user, false, categoryMiddle),
        Item("itemLeaf1", BigInteger.valueOf(20000L), "", user, false, categoryLeaf),
        Item("itemLeaf2", BigInteger.valueOf(30000L), "", user, false, categoryLeaf),
        Item("itemLeaf3", BigInteger.valueOf(50000L), "", user, false, categoryLeaf),
        Item("itemLeaf4", BigInteger.valueOf(40000L), "", user, false, categoryLeaf),
        Item("itemLeaf5", BigInteger.valueOf(50000L), "", user, false, categoryLeaf),
        Item("itemLeaf6", BigInteger.valueOf(30000L), "", user, false, categoryLeaf),
      )
    )

    val response = performGet(
      "/api/v1/search",
      mapOf(
        "minPrice" to "10000",
        "maxPrice" to "40000",
        "categoryId" to "${categoryMiddle.id}",
        "query" to "Leaf",
        "sortType" to "PRICE_DESC",
        "offset" to "0",
        "limit" to "3"
      )
    ).andReturn().response
    assertEquals(HttpStatus.OK.value(), response.status)
    val result = toResult<List<ItemResponse>>(response)
    assertThat(result)
      .extracting("title")
      .containsExactly("itemLeaf4", "itemLeaf6", "itemLeaf2")
      .hasSize(3)
  }
}
