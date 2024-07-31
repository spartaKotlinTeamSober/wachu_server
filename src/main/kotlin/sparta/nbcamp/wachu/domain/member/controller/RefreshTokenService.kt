package sparta.nbcamp.wachu.domain.member.controller

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import sparta.nbcamp.wachu.domain.member.dto.RefreshTokenRequest
import sparta.nbcamp.wachu.domain.member.dto.TokenResponse
import sparta.nbcamp.wachu.domain.member.entity.MemberRole
import sparta.nbcamp.wachu.infra.security.jwt.JwtTokenManager
import java.util.concurrent.TimeUnit

@Service
class RefreshTokenService(
    private val jwtTokenManager: JwtTokenManager,
    private val redisTemplate: RedisTemplate<String, String>
) {

    fun refreshAccessToken(request: RefreshTokenRequest): TokenResponse {
        val refreshToken = request.refreshToken

        return jwtTokenManager.validateToken(refreshToken).fold(
            onSuccess = {
                val tokens = jwtTokenManager.generateToken(
                    memberId = it.payload.subject.toLong(),
                    memberRole = MemberRole.valueOf(it.payload.get("memberRole", String::class.java))
                )
                redisTemplate.delete("refreshToken::${it.payload.subject.toLong()}")
                redisTemplate.opsForValue()
                    .set("refreshToken::${it.payload.subject.toLong()}", tokens.refreshToken, 7, TimeUnit.DAYS)
                tokens
            },
            onFailure = { throw IllegalStateException(" 토큰이 검증되지않음") }
        )
    }
}
