package me.chung.ecommerceapi.web.service

import me.chung.ecommerceapi.domain.user.User
import me.chung.ecommerceapi.domain.user.UserRepos
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
  private val userRepos: UserRepos,
) {
  fun findUserByLoginId(loginId: String): User {
    return userRepos.findByLoginId(loginId) ?: throw IllegalArgumentException("Could not find user : $loginId")
  }
}
