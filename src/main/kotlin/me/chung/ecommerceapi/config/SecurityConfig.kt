package me.chung.ecommerceapi.config

import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun configure(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web ->
            web.ignoring()
                .requestMatchers(HttpMethod.POST, "/api/v/user/signup")
        }
    }


    @Bean
    fun filterChain(httpSecurity: HttpSecurity):SecurityFilterChain {
        httpSecurity.authorizeHttpRequests()
        httpSecurity.csrf()
            .disable()
        return httpSecurity.build()
    }
}

