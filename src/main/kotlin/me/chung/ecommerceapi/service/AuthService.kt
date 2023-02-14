package me.chung.ecommerceapi.service

import me.chung.ecommerceapi.config.JwtService
import me.chung.ecommerceapi.domain.user.User
import me.chung.ecommerceapi.domain.user.UserRepos
import me.chung.ecommerceapi.web.dto.AuthenticationRequest
import me.chung.ecommerceapi.web.dto.AuthenticationResponse
import me.chung.ecommerceapi.web.dto.RegisterRequest
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class AuthService(
  private val userRepos: UserRepos,
  private val passwordEncoder: PasswordEncoder,
  private val jwtService: JwtService,
  private val authenticationManager: AuthenticationManager,
) {
  fun register(request: RegisterRequest): AuthenticationResponse {
    val (loginId, name, email, phone, password, role) = request

    val user = userRepos.findByLoginId(loginId)

    if (user != null) {
      throw ResponseStatusException(HttpStatus.CONFLICT, "loginId is duplicated")
    }

    val encodedPassword = passwordEncoder.encode(password)
    val newUser = User(loginId, name, email, phone, null, encodedPassword, role)
    userRepos.save(newUser)
    val jwtToken = jwtService.generateToken(newUser)
    return AuthenticationResponse(jwtToken)
  }

  fun authenticate(request: AuthenticationRequest): AuthenticationResponse {
    val (loginId, password) = request
    authenticationManager.authenticate(
      UsernamePasswordAuthenticationToken(loginId, password)
    )
    val user =
      userRepos.findByLoginId(loginId) ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found")
    val token = jwtService.generateToken(user)
    return AuthenticationResponse(token)
  }
}
