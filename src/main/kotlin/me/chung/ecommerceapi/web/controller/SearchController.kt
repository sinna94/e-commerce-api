package me.chung.ecommerceapi.web.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import me.chung.ecommerceapi.web.service.ItemResponse
import me.chung.ecommerceapi.web.service.ItemService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigInteger

@RestController
@RequestMapping("/api/v1/search")
class SearchController(
  private val service: ItemService,
) {

  @Operation(
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "판매 상품 검색 성공",
        content = [Content(schema = Schema(implementation = ItemResponse::class))]
      )
    ],
    description = "판매 상품 검색"
  )
  @GetMapping
  fun searchAll(
    searchQuery: SearchQuery,
    @RequestParam sortType: SortType,
    @RequestParam offset: Long,
    @RequestParam limit: Long,
  ): ResponseEntity<List<ItemResponse>> {
    return ResponseEntity.ok(service.searchAll(searchQuery, sortType, offset, limit))
  }
}

data class SearchQuery(
  val categoryId: Long? = null,
  val minPrice: BigInteger? = null,
  val maxPrice: BigInteger? = null,
  val query: String? = null,
)

enum class SortType(val type: String) {
  PRICE_ASC("price_acs"),
  PRICE_DESC("price_desc"),
  REVIEW("review"),
  DATE("date"),
}
