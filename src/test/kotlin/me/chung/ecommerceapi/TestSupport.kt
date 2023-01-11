package me.chung.ecommerceapi

import com.fasterxml.jackson.databind.ObjectMapper
import me.chung.ecommerceapi.config.RestDocsConfig
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler
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
@Import(RestDocsConfig::class)
@ExtendWith(RestDocumentationExtension::class)
abstract class TestSupport {
    companion object {
        protected const val CONSTRAINTS = "constraints"
    }

    @Autowired
    protected lateinit var restDocs: RestDocumentationResultHandler

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    fun parseJson(value: Any): String {
        return objectMapper.writeValueAsString(value)
    }

    fun performPost(url: String, body: Any? = null): ResultActions {
        val builder = MockMvcRequestBuilders.post(URI.create("/v1/user/signup"))
            .contentType(MediaType.APPLICATION_JSON)

        body?.let {
            builder.content(parseJson(body))
        }
        return mockMvc.perform(builder)
    }
}