package sparta.nbcamp.wachu.domain.member.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sparta.nbcamp.wachu.domain.member.dto.RefreshTokenRequest
import sparta.nbcamp.wachu.domain.member.dto.TokenResponse

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
            isHttpOnly = true // 요것이 httponly설정 javascript로 허튼짓을 못하게함
            secure = true // HTTPS를 사용하는 경우 설정!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            path = "/" //쿠키가 웹사이트의 모든 경로에서 사용되게함
            maxAge = 7 * 24 * 60 * 60 // 쿠키의 유효기간
        }
        response.addCookie(refreshTokenHttpOnly)
        return ResponseEntity.status(HttpStatus.OK).body(tokenResponse)
    }
}