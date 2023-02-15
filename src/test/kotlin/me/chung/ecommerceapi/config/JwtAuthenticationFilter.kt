package me.chung.ecommerceapi.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.test.context.TestComponent
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.web.filter.OncePerRequestFilter

@TestComponent
class JwtAuthenticationFilter : OncePerRequestFilter() {

  override fun doFilterInternal(
    request: HttpServletRequest,
    response: HttpServletResponse,
    filterChain: FilterChain,
  ) {
    SecurityContextHolder.getContext().authentication?.let {
      val principal = it.principal
      val loginId = (principal as User).username
      request.setAttribute("loginId", loginId)
    }

    filterChain.doFilter(request, response)
  }
}
