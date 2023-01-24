package me.chung.ecommerceapi.web.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/hello")
class HelloController {

  @GetMapping
  fun hello(): ResponseEntity<String> {
    return ResponseEntity.status(200).body("Hello")
  }
}
