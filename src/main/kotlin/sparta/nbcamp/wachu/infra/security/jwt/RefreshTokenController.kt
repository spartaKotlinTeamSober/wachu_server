package sparta.nbcamp.wachu.infra.security.jwt

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sparta.nbcamp.wachu.domain.member.dto.TokenResponse
import sparta.nbcamp.wachu.infra.security.jwt.dto.RefreshTokenRequest

@RestController
@RequestMapping("/refresh-token")
class RefreshTokenController(
    private val refreshTokenService: RefreshTokenService,
) {
    @PostMapping
    fun refreshAccessToken(
        @RequestBody request: RefreshTokenRequest,
        response: javax.servlet.http.HttpServletResponse
    ): ResponseEntity<TokenResponse> {
        val tokenResponse = refreshTokenService.refreshAccessToken(request)

        val refreshTokenHttpOnly = javax.servlet.http.Cookie("refreshToken", tokenResponse.refreshToken).apply {
            isHttpOnly = true
            secure = true // HTTPS를 사용하는 경우 true
            path = "/"
            maxAge = 7 * 24 * 60 * 60
        }
        response.addCookie(refreshTokenHttpOnly)
        return ResponseEntity.status(HttpStatus.OK).body(tokenResponse)
    }
}