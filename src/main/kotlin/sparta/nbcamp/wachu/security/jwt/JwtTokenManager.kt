package sparta.nbcamp.wachu.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import sparta.nbcamp.wachu.domain.member.dto.UserRole
import java.nio.charset.StandardCharsets
import java.util.Date

@Component
class JwtTokenManager(
    @Value("\${auth.jwt.issuer") private val issuer: String,
    @Value("\${auth.jwt.secret}") private val secret: String,

    ) {

    fun generateToken(memberId: Long, userRole: UserRole): String {

        val claims: Claims =
            Jwts.claims()
                .add(mapOf("memberId" to memberId, "userRole" to userRole))
                .build()

        val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

        return Jwts.builder().claims(claims).issuer(issuer)
            .expiration(Date(System.currentTimeMillis() + 3600 * 24)).signWith(key).compact()
    }

    fun validateToken(token: String): Result<Jws<Claims>> {

        return kotlin.runCatching {
            val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

            Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
        }
    }
}