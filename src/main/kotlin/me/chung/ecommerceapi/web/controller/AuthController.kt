package me.chung.ecommerceapi.web.controller

import me.chung.ecommerceapi.service.AuthService
import me.chung.ecommerceapi.web.dto.AuthenticationRequest
import me.chung.ecommerceapi.web.dto.AuthenticationResponse
import me.chung.ecommerceapi.web.dto.RegisterRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
  private val service: AuthService,
) {

  @PostMapping("/register")
  fun register(
    @RequestBody request: RegisterRequest,
  ): ResponseEntity<AuthenticationResponse> {
    val result = service.register(request)
    return ResponseEntity.ok(result)
  }

  @PostMapping("/authenticate")
  fun authenticate(
    @RequestBody request: AuthenticationRequest,
  ): ResponseEntity<AuthenticationResponse> {
    val result = service.authenticate(request)
    return ResponseEntity.ok(result)
  }
}
