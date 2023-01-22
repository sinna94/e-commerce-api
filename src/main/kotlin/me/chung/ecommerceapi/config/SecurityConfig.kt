package me.chung.ecommerceapi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
  private val jwtAuthFilter: JwtAuthenticationFilter,
  private val userDetailsService: UserDetailsService,
) {

  @Bean
  fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
    httpSecurity.csrf()
      .disable()
      .authorizeHttpRequests { auth ->
        auth
          .requestMatchers(
            HttpMethod.GET,
            "/swagger-ui/**", "/v3/api-docs/**"
          ).permitAll()
          .requestMatchers(
            "/api/v1/auth/**",
          ).permitAll()
          .anyRequest()
          .authenticated()
          .and()
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
          .authenticationProvider(authenticationProvider())
          .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
//          .hasAnyRole(Role.MEMBER.name, Role.GUEST.name, Role.SELLER.name)
      }
    return httpSecurity.build()
  }

  @Bean
  fun passwordEncoder(): BCryptPasswordEncoder {
    return BCryptPasswordEncoder()
  }

  @Bean
  fun authenticationProvider(): AuthenticationProvider {
    return DaoAuthenticationProvider()
      .apply {
        this.setUserDetailsService(userDetailsService)
        this.setPasswordEncoder(passwordEncoder())
      }
  }

  @Bean
  fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
    return config.authenticationManager
  }
}
