package me.chung.ecommerceapi.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import java.util.function.Function

@Service
class JwtService {
  companion object {
    private const val SECRET_KEY = "614E645267556B58703272357538782F413F4428472B4B6250655368566D5971"
  }

  fun extractLoginId(token: String): String {
    return extractClaim(token, Claims::getSubject)
  }

  fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
    val claims = extractAllClaims(token)
    return claimsResolver.apply(claims)
  }

  fun generateToken(userDetails: UserDetails): String {
    return generateToken(emptyMap(), userDetails)
  }

  fun generateToken(
    extraClaims: Map<String, Any>,
    userDetails: UserDetails,
  ): String {
    return Jwts
      .builder()
      .setClaims(extraClaims)
      .setSubject(userDetails.username)
      .setIssuedAt(Date(System.currentTimeMillis()))
      .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
      .signWith(getSignInKey(), SignatureAlgorithm.ES256)
      .compact()
  }

  fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
    val loginId = extractLoginId(extractLoginId(token))
    return loginId == userDetails.username && !isTokenExpired(token)
  }

  private fun isTokenExpired(token: String): Boolean {
    return extractExpiration(token).before(Date())
  }

  private fun extractExpiration(token: String): Date {
    return extractClaim(token, Claims::getExpiration)
  }

  private fun extractAllClaims(token: String): Claims {
    return Jwts
      .parserBuilder()
      .setSigningKey(getSignInKey())
      .build()
      .parseClaimsJws(token)
      .body
  }

  private fun getSignInKey(): Key {
    val keyBytes = Decoders.BASE64.decode(SECRET_KEY)
    return Keys.hmacShaKeyFor(keyBytes)
  }
}
