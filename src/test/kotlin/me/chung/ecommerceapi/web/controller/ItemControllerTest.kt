package me.chung.ecommerceapi.web.controller

import me.chung.ecommerceapi.TestSupport
import me.chung.ecommerceapi.domain.category.Category
import me.chung.ecommerceapi.domain.category.CategoryRepos
import me.chung.ecommerceapi.domain.item.ItemRepos
import me.chung.ecommerceapi.domain.user.Role
import me.chung.ecommerceapi.domain.user.User
import me.chung.ecommerceapi.domain.user.UserRepos
import me.chung.ecommerceapi.web.service.NewItemRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.test.context.support.WithMockUser
import java.math.BigInteger

class ItemControllerTest(
  private val itemRepos: ItemRepos,
  private val categoryRepos: CategoryRepos,
  private val userRepos: UserRepos,
) : TestSupport() {

  @BeforeEach
  fun setUp() {
    itemRepos.deleteAll()
    categoryRepos.deleteAll()
    userRepos.deleteAll()
    userRepos.save(
      User("seller", "sellerName", "email", "phone", null, "password", Role.SELLER)
    )
  }

  @Test
  @WithMockUser("seller", authorities = ["SELLER"])
  fun addItemWithSeller() {

    val topCategory1 = categoryRepos.save(
      Category("c1", 0, false, 0)
    )
    val category = categoryRepos.save(
      Category("c3", 1, true, 0, topCategory1.id)
    )

    val newItemRequest = NewItemRequest(
      "new_item",
      BigInteger.valueOf(10000L),
      "new item contents",
      category.id
    )

    val response = performPost("/api/v1/items", newItemRequest).andReturn().response
    assertEquals(200, response.status)
  }

  @Test
  @WithMockUser("seller", authorities = ["MEMBER"])
  fun addItemWithMember() {

    val topCategory1 = categoryRepos.save(
      Category("c1", 0, false, 0)
    )
    val category = categoryRepos.save(
      Category("c3", 1, true, 0, topCategory1.id)
    )

    val newItemRequest = NewItemRequest(
      "new_item",
      BigInteger.valueOf(10000L),
      "new item contents",
      category.id
    )

    val response = performPost("/api/v1/items", newItemRequest).andReturn().response
    assertEquals(403, response.status)
  }
}
