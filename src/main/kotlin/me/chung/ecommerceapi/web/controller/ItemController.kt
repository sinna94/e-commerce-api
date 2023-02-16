package me.chung.ecommerceapi.web.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import me.chung.ecommerceapi.web.service.EditItemRequest
import me.chung.ecommerceapi.web.service.ItemResponse
import me.chung.ecommerceapi.web.service.ItemService
import me.chung.ecommerceapi.web.service.NewItemRequest
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
        description = "판매 상품 추가 성공",
        content = [Content(schema = Schema(implementation = ItemResponse::class))]
      ),
      ApiResponse(
        responseCode = "400",
        description = "존재하지 않는 카테고리 아이디가 포함된 판매 상품 추가 시도",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
      ),
    ],
    description = "새로운 판매 상품 추가"
  )
  @PostMapping
  fun addItem(
    @RequestBody newItemRequest: NewItemRequest,
    @RequestAttribute loginId: String,
  ): ResponseEntity<ItemResponse> {
    try {
      return ResponseEntity.ok(itemService.addItem(newItemRequest, loginId))
    } catch (e: IllegalArgumentException) {
      throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
    }
  }

  @Operation(
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "판매 상품 내용 수정 성공",
        content = [Content(schema = Schema(implementation = ItemResponse::class))]
      ),
      ApiResponse(
        responseCode = "400",
        description = "존재하지 않는 판매 상품 아이디로 수정 시도",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
      ),
      ApiResponse(
        responseCode = "403",
        description = "로그인된 사용자의 상품이 아닌 상품을 수정 시도",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
      ),
    ],
    description = "판매 상품 내용 수정"
  )
  @PutMapping("{id}")
  fun editItem(
    @PathVariable id: Long,
    @RequestAttribute loginId: String,
    @RequestBody editItemRequest: EditItemRequest,
  ): ResponseEntity<ItemResponse> {
    try {
      return ResponseEntity.ok(itemService.editItem(id, loginId, editItemRequest))
    } catch (e: IllegalArgumentException) {
      throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
    } catch (e: SecurityException) {
      throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
    }
  }

  @Operation(
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "상품 판매 여부 수정 성공, 변경된 판매 여부 상태 반환",
        content = [Content(schema = Schema(implementation = Boolean::class))]
      ),
      ApiResponse(
        responseCode = "400",
        description = "존재하지 않는 판매 상품 아이디로 수정 시도",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
      ),
      ApiResponse(
        responseCode = "403",
        description = "로그인된 사용자의 상품이 아닌 상품을 수정 시도",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
      ),
    ],
    description = "상품 판매 여부 수정"
  )
  @PatchMapping("{id}")
  fun changeStopSelling(
    @PathVariable id: Long,
    @RequestAttribute loginId: String,
    @RequestParam stopSelling: Boolean,
  ): ResponseEntity<Boolean> {
    try {
      return ResponseEntity.ok(itemService.changeStopSelling(id, loginId, stopSelling))
    } catch (e: IllegalArgumentException) {
      throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
    } catch (e: SecurityException) {
      throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
    }
  }
}
