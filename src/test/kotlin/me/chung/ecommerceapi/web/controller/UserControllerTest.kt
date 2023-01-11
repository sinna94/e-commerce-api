package me.chung.ecommerceapi.web.controller

import me.chung.ecommerceapi.TestSupport
import me.chung.ecommerceapi.domain.user.UserRepos
import me.chung.ecommerceapi.web.dto.SignUpDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.snippet.Attributes

class UserControllerTest(
    private val userRepos: UserRepos,
) : TestSupport() {

    @BeforeEach
    fun setUp() {
        userRepos.deleteAll()
    }

    @Test
    fun signUp() {

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

        document(
            "user-signup",
            requestFields(
                fieldWithPath("loginId").description("사용자 로그인 ID"),
                fieldWithPath("name").description("사용자 이름").attributes(Attributes.Attribute("constraints", "길이 20 이하")),
                fieldWithPath("email").description("사용자 email 주소")
                    .attributes(Attributes.Attribute("constraints", "길이 100 이하")),
                fieldWithPath("phone").description("사용자 전화번호"),
                fieldWithPath("password").description("사용자 비밀번호"),
            )
        )
    }
}