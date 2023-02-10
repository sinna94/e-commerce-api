package me.chung.ecommerceapi.web.controller

import me.chung.ecommerceapi.domain.item.Item
import me.chung.ecommerceapi.web.service.ItemService
import me.chung.ecommerceapi.web.service.NewItemRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/items")
class ItemController(
  private val itemService: ItemService,
) {

  @PostMapping
  fun addItem(
    @RequestBody newItemRequest: NewItemRequest,
    @RequestAttribute loginId: String,
  ): ResponseEntity<Item> {
    return ResponseEntity.ok(itemService.addItem(newItemRequest, loginId))
  }
}
