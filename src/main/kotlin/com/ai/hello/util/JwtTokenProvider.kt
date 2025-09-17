package com.ai.hello.util

import com.ai.hello.model.UserRole
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.*



@Component
class JwtTokenProvider{

    @Value("\${jwt.secret-key}")
    private lateinit var secretKey: String

    private val validityInMilliseconds: Long = 3600000 // 1시간


    fun getAuthentication(token: String): Authentication {
        val claims: Claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .body

        val userEmail = claims.subject
        val role = claims["auth"].toString()
        val authorities = listOf(SimpleGrantedAuthority("ROLE_$role"))

        val principal = User(userEmail, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }
    // 토큰 생성
    fun createToken(userEmail: String, role: UserRole): String {
        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)

        return Jwts.builder()
                .setSubject(userEmail)
                .claim("auth", role.name)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(Keys.hmacShaKeyFor(secretKey.toByteArray()),
                        io.jsonwebtoken.SignatureAlgorithm.HS256)
                .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
            true
        } catch (e: Exception) {
            // Log the exception (e.g., ExpiredJwtException, SignatureException)
            false
        }
    }

    fun resolveToken(req: HttpServletRequest): String? {
        val bearerToken = req.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }

}

data class JwtToken(
        val grantType: String,
        val accessToken: String,
        val refreshToken: String
)
