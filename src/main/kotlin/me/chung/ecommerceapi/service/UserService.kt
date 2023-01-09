package me.chung.ecommerceapi.service

import me.chung.ecommerceapi.domain.user.UserRepos
import me.chung.ecommerceapi.web.dto.SignUpDto
import org.springframework.stereotype.Service


@Service
class UserService(
    private val userRepos: UserRepos
) {
    fun signUp(body: SignUpDto) {

    }

}