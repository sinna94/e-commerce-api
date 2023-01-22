package me.chung.ecommerceapi.config

import me.chung.ecommerceapi.domain.user.UserRepos
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

@Configuration
@EnableJpaAuditing
class AppConfig(
  private val userRepos: UserRepos,
) {
  @Bean
  fun userDetailsService(): UserDetailsService {
    return UserDetailsService { username ->
      userRepos.findByLoginId(username) ?: throw UsernameNotFoundException("User not found")
    }
  }
}
