package me.chung.ecommerceapi.web.controller

import me.chung.ecommerceapi.TestSupport
import me.chung.ecommerceapi.domain.user.Role
import me.chung.ecommerceapi.domain.user.User
import me.chung.ecommerceapi.domain.user.UserRepos
import me.chung.ecommerceapi.web.dto.SignUpDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class UserControllerTest(
    private val userRepos: UserRepos,
    private val passwordEncoder: BCryptPasswordEncoder,
) : TestSupport() {

    @BeforeEach
    fun setUp() {
        userRepos.deleteAll()
    }

    @Test
    @DisplayName("회원 가입 테스트")
    fun signUpTest() {

        val body = SignUpDto(
            "user1",
            "홍길동",
            "example@google.com",
            "01012345678",
            "password"
        )

        val response = performPost("/v1/user/signup", body).andReturn().response
        assertThat(response.status)
            .isEqualTo(200)
        val responseEntity = toResponseEntity<Boolean>(response)


        val users = userRepos.findAll()
        assertThat(users)
            .hasSize(1)
            .extracting("loginId")
            .isEqualTo(listOf("user1"))

        // 비밀번호 암호화 확인
        val encodedPassword = users.first().password
        assertThat(encodedPassword)
            .isNotEqualTo("password")
        assertTrue(passwordEncoder.matches("password", encodedPassword))
    }

    @Test
    @DisplayName("중복된 loginId 로 회원 가입 테스트")
    fun signUpWithDuplicatedLoginIdTest() {
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

        val body = SignUpDto(
            "user1",
            "홍길동",
            "example@google.com",
            "01012345678",
            "password"
        )


        val response = performPost("/v1/user/signup", body).andReturn().response
        assertThat(response.status)
            .isEqualTo(HttpStatus.BAD_REQUEST.value())
    }
}