package me.chung.ecommerceapi.web.controller

import me.chung.ecommerceapi.MvcMockTestSupport
import me.chung.ecommerceapi.config.JwtService
import me.chung.ecommerceapi.domain.user.Role
import me.chung.ecommerceapi.domain.user.User
import me.chung.ecommerceapi.domain.user.UserRepos
import me.chung.ecommerceapi.web.dto.AuthenticationResponse
import me.chung.ecommerceapi.web.dto.RegisterRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class AuthControllerTest(
  private val userRepos: UserRepos,
  private val passwordEncoder: BCryptPasswordEncoder,
  private val jwtService: JwtService,
) : MvcMockTestSupport() {

  @BeforeEach
  fun setUp() {
    userRepos.deleteAll()
  }

  @Test
  @DisplayName("회원 가입 테스트")
  fun registerTest() {

    val body = RegisterRequest(
      "user1",
      "홍길동",
      "example@google.com",
      "01012345678",
      "password",
      Role.MEMBER,
    )

    val response = performPost("/api/v1/auth/register", body).andReturn().response
    assertThat(response.status)
      .isEqualTo(200)
    val result = toResult<AuthenticationResponse>(response)

    val user = userRepos.findAll().first()
    assertTrue(jwtService.isTokenValid(result.token, user))
    assertThat(user)
      .extracting("loginId")
      .isEqualTo("user1")

    // 비밀번호 암호화 확인
    val encodedPassword = user.password
    assertThat(encodedPassword)
      .isNotEqualTo("password")
    assertTrue(passwordEncoder.matches("password", encodedPassword))
  }

  @Test
  @DisplayName("중복된 loginId 로 회원 가입 테스트")
  fun registerWithDuplicatedLoginIdTest() {
    userRepos.save(
      User(
        "user1",
        "user",
        "abc@gmail.com",
        "01012345678",
        null,
        "pw",
        Role.MEMBER
      )
    )

    val body = RegisterRequest(
      "user1",
      "홍길동",
      "example@google.com",
      "01012345678",
      "password",
      Role.MEMBER,
    )

    val response = performPost("/api/v1/auth/register", body).andReturn().response
    assertThat(response.status)
      .isEqualTo(HttpStatus.CONFLICT.value())
  }
}
