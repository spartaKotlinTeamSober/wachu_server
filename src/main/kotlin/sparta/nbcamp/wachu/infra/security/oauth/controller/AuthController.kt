package sparta.nbcamp.wachu.infra.security.oauth.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sparta.nbcamp.wachu.domain.member.dto.TokenResponse
import sparta.nbcamp.wachu.domain.member.entity.MemberRole
import sparta.nbcamp.wachu.domain.member.repository.MemberRepository
import sparta.nbcamp.wachu.infra.security.jwt.JwtTokenManager
import sparta.nbcamp.wachu.infra.security.oauth.dto.RefreshTokenRequest
import sparta.nbcamp.wachu.infra.security.oauth.repository.RefreshTokenRepository

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val jwtTokenManager: JwtTokenManager,
    private val memberRepository: MemberRepository,
    private val refreshTokenRepository: RefreshTokenRepository
) {

    @PostMapping("/logout")
    fun logout(@RequestBody refreshTokenRequest: RefreshTokenRequest): ResponseEntity<Void> {
        val refreshToken = refreshTokenRequest.refreshToken
        refreshTokenRepository.findByToken(refreshToken)?.let {
            refreshTokenRepository.delete(it)
        }
        return ResponseEntity.ok().build()
    }

    @PostMapping("/refresh")
    fun refreshAccessToken(@RequestBody refreshTokenRequest: RefreshTokenRequest): ResponseEntity<TokenResponse> {
        val refreshToken = refreshTokenRequest.refreshToken

        return jwtTokenManager.validateToken(refreshToken).fold(
            onSuccess = { claimsJws ->
                val claims = claimsJws.body
                val memberId = claims.subject.toLong()
                val member =
                    memberRepository.findById(memberId) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
                val tokens = jwtTokenManager.generateToken(member.id!!, MemberRole.MEMBER)
                refreshTokenRepository.save(member.id!!, tokens["refreshToken"]!!)
                ResponseEntity.ok(TokenResponse(tokens["accessToken"]!!, tokens["refreshToken"]!!))
            },
            onFailure = {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }
        )
    }
}
