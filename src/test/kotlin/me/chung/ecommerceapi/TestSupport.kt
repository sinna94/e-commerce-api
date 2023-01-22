package me.chung.ecommerceapi

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
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

  fun performPost(url: String, body: Any? = null): ResultActions {
    val builder = MockMvcRequestBuilders.post(URI.create(url))
      .contentType(MediaType.APPLICATION_JSON)

    body?.let {
      builder.content(parseJson(body))
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
