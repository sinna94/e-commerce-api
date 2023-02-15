package me.chung.ecommerceapi.web.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import me.chung.ecommerceapi.web.service.ItemService
import me.chung.ecommerceapi.web.service.NewItemRequest
import me.chung.ecommerceapi.web.service.NewItemResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/v1/items")
class ItemController(
  private val itemService: ItemService,
) {

  @Operation(
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "아이템 추가 성공",
        content = [Content(schema = Schema(implementation = NewItemResponse::class))]
      ),
      ApiResponse(
        responseCode = "400",
        description = "존재하지 않는 카테고리 아이디가 포함된 아이템 추가 시도",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
      ),
    ],
    description = "새로운 아이템 추가"
  )
  @PostMapping
  fun addItem(
    @RequestBody newItemRequest: NewItemRequest,
    @RequestAttribute loginId: String,
  ): ResponseEntity<NewItemResponse> {
    try {
      return ResponseEntity.ok(itemService.addItem(newItemRequest, loginId))
    } catch (e: IllegalArgumentException) {
      throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
    }
  }
}
