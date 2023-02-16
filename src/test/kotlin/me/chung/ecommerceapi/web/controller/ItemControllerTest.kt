package me.chung.ecommerceapi.web.controller

import me.chung.ecommerceapi.TestSupport
import me.chung.ecommerceapi.domain.category.Category
import me.chung.ecommerceapi.domain.category.CategoryRepos
import me.chung.ecommerceapi.domain.item.Item
import me.chung.ecommerceapi.domain.item.ItemRepos
import me.chung.ecommerceapi.domain.user.Role
import me.chung.ecommerceapi.domain.user.User
import me.chung.ecommerceapi.domain.user.UserRepos
import me.chung.ecommerceapi.web.service.EditItemRequest
import me.chung.ecommerceapi.web.service.ItemResponse
import me.chung.ecommerceapi.web.service.NewItemRequest
import org.assertj.core.api.Assertions.assertThat
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
  }

  @Test
  @WithMockUser("seller", authorities = ["SELLER"])
  fun addItemWithSeller() {
    userRepos.save(
      User("seller", "sellerName", "email", "phone", null, "password", Role.SELLER)
    )

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

  @Test
  @WithMockUser("seller", authorities = ["SELLER"])
  fun editItemTest() {
    val user = userRepos.save(
      User("seller", "sellerName", "email", "phone", null, "password", Role.SELLER)
    )

    val category1 = categoryRepos.save(
      Category("category1", 0, true, 0)
    )

    val category2 = categoryRepos.save(
      Category("category2", 0, true, 1)
    )

    val item = itemRepos.save(Item("item", BigInteger.TEN, "contents", user, false, category1))

    val editItemRequest = EditItemRequest(
      "editItem",
      BigInteger.valueOf(200L),
      "editContents",
      category2.id
    )

    val response = performPut("/api/v1/items/${item.id}", editItemRequest).andReturn().response
    assertEquals(200, response.status)
    val itemResponse = toResult<ItemResponse>(response)
    val expected = ItemResponse(0L, "editItem", BigInteger.valueOf(200L), "editContents", category2.id)
    assertThat(itemResponse)
      .usingRecursiveComparison()
      .ignoringFields("id")
      .isEqualTo(expected)
  }

  @Test
  @WithMockUser("seller", authorities = ["SELLER"])
  fun editOtherUsersItemTest() {
    val user = userRepos.save(
      User("seller2", "sellerName", "email", "phone", null, "password", Role.SELLER)
    )

    userRepos.save(
      User("seller", "sellerName", "email", "phone", null, "password", Role.SELLER)
    )

    val category = categoryRepos.save(
      Category("category1", 0, true, 0)
    )

    val item = itemRepos.save(Item("item", BigInteger.TEN, "contents", user, false, category))

    val editItemRequest = EditItemRequest(
      "editItem",
      BigInteger.valueOf(200L),
      "editContents",
    )

    val response = performPut("/api/v1/items/${item.id}", editItemRequest).andReturn().response
    assertEquals(403, response.status)
  }
}
