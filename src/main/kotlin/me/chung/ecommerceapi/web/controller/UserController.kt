package me.chung.ecommerceapi.web.controller

import me.chung.ecommerceapi.service.UserService
import me.chung.ecommerceapi.web.dto.SignUpDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/user")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/signup")
    fun signUp(
        @RequestBody body: SignUpDto
    ) {
        userService.signUp(body)
    }
}