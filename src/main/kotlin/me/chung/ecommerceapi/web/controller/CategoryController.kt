package me.chung.ecommerceapi.web.controller

import me.chung.ecommerceapi.web.service.CategoryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/category")
class CategoryController(
  private val categoryService: CategoryService
) {

  @GetMapping
  fun getTopLevelCategories() {
    categoryService.getTopLevelCategories()
  }
}
