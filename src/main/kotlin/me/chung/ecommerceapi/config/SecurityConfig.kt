package me.chung.ecommerceapi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
@Configuration
class SecurityConfig {

//    @Bean
//    fun configure(): WebSecurityCustomizer {
//        return WebSecurityCustomizer { web ->
//            web.ignoring()
//                .requestMatchers(HttpMethod.GET, "/api/v/user/signup")
//        }
//    }


    @Bean
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity.csrf()
            .disable()
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/v1/user/signup").permitAll()
                    .anyRequest().authenticated()
            }.build()
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}

