package sparta.nbcamp.wachu.domain.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.kotest.matchers.shouldBe
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import sparta.nbcamp.wachu.security.jwt.JwtTokenManager
import java.nio.charset.StandardCharsets

@SpringBootTest
@ActiveProfiles("test")
class JwtTest {

    @Value("\${auth.jwt.secret}")
    private lateinit var secret: String

    @Autowired
    private lateinit var jwtTokenManager: JwtTokenManager

    @Transactional
    @Test
    fun `토큰 안에 값이 제대로 들어왔는지 테스트하는 함수`() {
        val `토큰 값` =
            jwtTokenManager.generateToken(
                memberId = 1,
                userRole = UserRole.MEMBER
            )

        val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

        val `토큰 꺼내기` = Jwts.parser().verifyWith(key).build().parseSignedClaims(`토큰 값`)
        `토큰 꺼내기`.payload.get("memberId") shouldBe 1
        `토큰 꺼내기`.payload.get("userRole", String::class.java) shouldBe "MEMBER"
    }

    @Test
    fun `토큰이 정상적으로 검증이 되는지 테스트하는 함수`() {

        val `토큰 값` =
            jwtTokenManager.generateToken(
                memberId = 2,
                userRole = UserRole.ADMIN
            )

        val `검증된 토큰` = jwtTokenManager.validateToken(`토큰 값`)

        `검증된 토큰`.isSuccess shouldBe true
    }
}