package ch.ak.ITS.Lab._5.Auth.Backend.service

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import java.util.Date

@Service
class JwtService(
    @Value("\${app.jwt.secret}") private val secretBase64: String,
    @Value("\${app.jwt.expiration-minutes}") private val expirationMinutes: Long
) {
    companion object {
        private const val MILLIS_IN_A_MINUTE = 60 * 1000L
    }

    private fun signingKey() = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretBase64))

    fun generateToken(user: UserDetails): String {
        val now = Date()
        val expiry = Date(now.time + expirationMinutes * MILLIS_IN_A_MINUTE)
        return Jwts.builder()
            .subject(user.username)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(signingKey(), Jwts.SIG.HS256)
            .compact()
    }

    fun extractEmail(token: String) = Jwts.parser()
        .verifyWith(signingKey())
        .build()
        .parseSignedClaims(token)
        .payload
        .subject

    fun isTokenValid(token: String, user: UserDetails): Boolean {
        return try {
            val claims = Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .payload

            claims.subject == user.username && claims.expiration.after(Date())
        } catch (_: JwtException) {
            false
        }
    }
}
