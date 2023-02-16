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
import org.junit.jupiter.api.DisplayName
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
  @DisplayName("판매자 계정으로 판매 상품 추가 테스트")
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
    val result = toResult<ItemResponse>(response)
    val expected = ItemResponse(itemRepos.findById(result.id).get())
    assertThat(result)
      .usingRecursiveComparison()
      .ignoringFields("id")
      .isEqualTo(expected)
  }

  @Test
  @WithMockUser("seller", authorities = ["MEMBER"])
  @DisplayName("일반 계정으로 판매 상품 추가 테스트")
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
  @DisplayName("판매 상품 정보 수정 테스트")
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
    val expected = ItemResponse(itemRepos.findById(itemResponse.id).get())
    assertThat(itemResponse)
      .usingRecursiveComparison()
      .ignoringFields("id")
      .isEqualTo(expected)
  }

  @Test
  @WithMockUser("seller", authorities = ["SELLER"])
  @DisplayName("다른 판매자의 판매 상품 정보 수정 테스트")
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

  @Test
  @WithMockUser("seller", authorities = ["SELLER"])
  @DisplayName("상품 판매 여부 수정 테스트")
  fun changeStopSellingTest() {
    val user = userRepos.save(
      User("seller", "sellerName", "email", "phone", null, "password", Role.SELLER)
    )

    val category = categoryRepos.save(
      Category("category1", 0, true, 0)
    )

    val item = itemRepos.save(Item("item", BigInteger.TEN, "contents", user, false, category))

    val response = performPatch("/api/v1/items/${item.id}", mapOf("stopSelling" to "true")).andReturn().response
    assertEquals(200, response.status)
    val result = toResult<Boolean>(response)
    val expected = itemRepos.findById(item.id).get().stopSelling
    assertEquals(expected, result)
  }

  @Test
  @WithMockUser("seller", authorities = ["SELLER"])
  @DisplayName("다른 판매자의 상품 판매 여부 수정 테스트")
  fun changeStopSellingOtherUsersItemTest() {
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

    val response = performPatch("/api/v1/items/${item.id}", mapOf("stopSelling" to "true")).andReturn().response
    assertEquals(403, response.status)
  }
}
