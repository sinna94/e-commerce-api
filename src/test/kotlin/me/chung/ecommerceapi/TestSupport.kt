package me.chung.ecommerceapi

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.net.URI

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
abstract class TestSupport {

  @Autowired
  private lateinit var mockMvc: MockMvc

  @Autowired
  lateinit var objectMapper: ObjectMapper

  fun parseJson(value: Any): String {
    return objectMapper.writeValueAsString(value)
  }

  fun performGet(url: String, params: Map<String, String>? = null, role: String? = null): ResultActions {
    var builder = getBuilder(MockMvcRequestBuilders::get, url)

    params?.forEach { (key, value) ->
      builder = builder.param(key, value)
    }

    role?.let {
      builder = builder.with(user("user").roles(role))
    }

    return mockMvc.perform(builder)
  }

  private fun getBuilder(
    method: (URI) -> MockHttpServletRequestBuilder,
    url: String,
  ): MockHttpServletRequestBuilder {
    return method(URI.create(url))
      .contentType(MediaType.APPLICATION_JSON)
  }

  fun performPost(url: String, body: Any? = null): ResultActions {
    var builder = getBuilder(MockMvcRequestBuilders::post, url)

    body?.let {
      builder = builder.content(parseJson(body))
    }
    return mockMvc.perform(builder)
  }

  final inline fun <reified T> toResult(response: MockHttpServletResponse): T {
    return toResult(response.contentAsString)
  }

  final inline fun <reified T> toResult(json: String): T {
    val typeReference = object : TypeReference<T>() {}
    return objectMapper.readValue(json, typeReference)
  }
}
