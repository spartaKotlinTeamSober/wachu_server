package sparta.nbcamp.wachu.infra.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import sparta.nbcamp.wachu.domain.member.entity.MemberRole
import java.nio.charset.StandardCharsets
import java.util.Date

@Component
class JwtTokenManager(
    @Value("\${auth.jwt.issuer}") private val issuer: String,
    @Value("\${auth.jwt.secret}") private val secret: String,

    ) {

    private val accessTokenValidity = 3600 * 1000 // 1 hour
    private val refreshTokenValidity = 7 * 24 * 3600 * 1000 // 1 week

    private val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

    fun generateToken(memberId: Long, memberRole: MemberRole): Map<String, String> {
        val claims: Claims = Jwts.claims().add(mapOf("memberRole" to memberRole)).build()

        val accessToken = Jwts.builder()
            .subject(memberId.toString())
            .claims(claims)
            .issuer(issuer)
            .expiration(Date(System.currentTimeMillis() + accessTokenValidity))
            .signWith(key)
            .compact()

        val refreshToken = Jwts.builder()
            .subject(memberId.toString())
            .issuer(issuer)
            .expiration(Date(System.currentTimeMillis() + refreshTokenValidity))
            .signWith(key)
            .compact()

        return mapOf("accessToken" to accessToken, "refreshToken" to refreshToken)
    }

    fun validateToken(token: String): Result<Jws<Claims>> {

        return kotlin.runCatching {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
        }
    }

    fun parseClaims(token: String): Claims {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).body
    }
}