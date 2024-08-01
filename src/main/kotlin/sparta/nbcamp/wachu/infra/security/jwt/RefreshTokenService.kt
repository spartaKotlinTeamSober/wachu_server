package sparta.nbcamp.wachu.infra.security.jwt

import org.springframework.stereotype.Service
import sparta.nbcamp.wachu.domain.member.dto.TokenResponse
import sparta.nbcamp.wachu.domain.member.entity.MemberRole
import sparta.nbcamp.wachu.infra.security.jwt.dto.RefreshTokenRequest

@Service
class RefreshTokenService(
    private val jwtTokenManager: JwtTokenManager,

    ) {

    fun refreshAccessToken(request: RefreshTokenRequest): TokenResponse {
        val refreshToken = request.refreshToken

        return jwtTokenManager.validateToken(refreshToken).fold(
            onSuccess = {
                val tokens = jwtTokenManager.generateToken(
                    memberId = it.payload.subject.toLong(),
                    memberRole = MemberRole.valueOf(it.payload.get("memberRole", String::class.java))
                )
                tokens
            },
            onFailure = { throw IllegalStateException(" 토큰이 검증되지않음") }
        )
    }
}
