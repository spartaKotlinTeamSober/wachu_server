package sparta.nbcamp.wachu.infra.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import sparta.nbcamp.wachu.domain.member.dto.TokenResponse
import sparta.nbcamp.wachu.domain.member.entity.MemberRole
import sparta.nbcamp.wachu.infra.security.jwt.dto.TokenType
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.Date

@Component
class JwtTokenManager(
    @Value("\${auth.jwt.issuer}") private val issuer: String,
    @Value("\${auth.jwt.secret}") private val secret: String,

    ) {

    private val accessTokenValidity = 3600 * 1000
    private val refreshTokenValidity = 7 * 24 * 3600 * 1000

    private val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

    fun generateTokenResponse(memberId: Long, memberRole: MemberRole): TokenResponse {
        return TokenResponse(
            accessToken = generateToken(
                memberId.toString(),
                memberRole.name,
                TokenType.ACCESS_TOKEN_TYPE.toString(),
                accessTokenValidity
            ),
            refreshToken = generateToken(
                memberId.toString(),
                memberRole.name,
                TokenType.REFRESH_TOKEN_TYPE.toString(),
                refreshTokenValidity
            )
        )
    }

    private fun generateToken(subject: String, memberRole: String, tokenType: String, expirationTime: Int): String {
        val claims: Claims = Jwts.claims().add(mapOf("memberRole" to memberRole, "tokenType" to tokenType)).build()

        val now = Instant.now()
        val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

        return Jwts.builder()
            .subject(subject)
            .issuer(issuer)
            .issuedAt(Date.from(now))
            .expiration(Date(System.currentTimeMillis() + expirationTime))
            .claims(claims)
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): Result<Jws<Claims>> {
        return kotlin.runCatching {
            val claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token)

            val tokenType = claims.payload.get("tokenType", String::class.java)
            if (tokenType == TokenType.REFRESH_TOKEN_TYPE.toString()) throw IllegalStateException("토큰 타입이 accessToken이 아님")

            claims
        }
    }
}