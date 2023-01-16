package me.chung.ecommerceapi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

  @Bean
  fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
    return httpSecurity.csrf()
      .disable()
      .authorizeHttpRequests { auth ->
        auth
          .requestMatchers(
            HttpMethod.GET,
            "/swagger-ui/**", "/v3/api-docs/**"
          ).permitAll()
          .requestMatchers(
            HttpMethod.POST,
            "/v1/user/signup",
          ).permitAll()
          .anyRequest().authenticated()
      }.build()
  }

  @Bean
  fun passwordEncoder(): BCryptPasswordEncoder {
    return BCryptPasswordEncoder()
  }
}
