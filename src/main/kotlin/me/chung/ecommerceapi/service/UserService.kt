package me.chung.ecommerceapi.service

import me.chung.ecommerceapi.domain.user.Role
import me.chung.ecommerceapi.domain.user.User
import me.chung.ecommerceapi.domain.user.UserRepos
import me.chung.ecommerceapi.web.dto.SignUpDto
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(
  private val userRepos: UserRepos,
  private val passwordEncoder: BCryptPasswordEncoder,
) {
  fun signUp(body: SignUpDto): Boolean {
    val (loginId, name, email, phone, password) = body

    if (userRepos.findByLoginId(loginId) != null) {
      throw ResponseStatusException(HttpStatus.BAD_REQUEST, "loginId is duplicated")
    }

    val encodedPassword = passwordEncoder.encode(password)
    val newUser = User(loginId, name, email, phone, null, encodedPassword, Role.MEMBER)
    userRepos.save(newUser)
    return true
  }
}
