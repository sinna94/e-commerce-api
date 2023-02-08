package me.chung.ecommerceapi.web.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import me.chung.ecommerceapi.web.service.CategoryResponse
import me.chung.ecommerceapi.web.service.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/v1/category")
class CategoryController(
  private val categoryService: CategoryService,
) {

  @Operation(
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "카테고리 정보 조회 성공",
        content = [Content(schema = Schema(implementation = CategoryResponse::class))]
      ),
      ApiResponse(
        responseCode = "400",
        description = "존재하지 않는 카테고리 아이디로 조회 시도",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
      ),
    ],
    description = "카테고리 정보와 함께 하위 카테고리 아이디 리스트 조회"
  )
  @GetMapping
  fun getCategories(
    @RequestParam(required = false) categoryId: Long?,
  ): ResponseEntity<List<CategoryResponse>> {
    try {
      return ResponseEntity.ok(categoryService.getCategories(categoryId))
    } catch (e: IllegalArgumentException) {
      throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
    }
  }
}
