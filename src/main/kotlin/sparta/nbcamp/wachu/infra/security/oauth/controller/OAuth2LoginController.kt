package sparta.nbcamp.wachu.infra.security.oauth.controller

import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import sparta.nbcamp.wachu.domain.member.dto.TokenResponse
import sparta.nbcamp.wachu.infra.security.oauth.service.OAuth2LoginService

@RestController
class OAuth2LoginController(
    private val OAuth2LoginService: OAuth2LoginService,
    @Value("\${frontend.url}") private val frontendUrl: String
) {
    private val logger = LoggerFactory.getLogger(OAuth2LoginController::class.java)

    @GetMapping("/oauth2/login/kakao")
    fun kakaoRedirectLoginPage(response: HttpServletResponse): ResponseEntity<String> {
        val loginPageUrl = OAuth2LoginService.kakaoGenerateLoginPageUrl()
        logger.info("Generated login page URL: $loginPageUrl")
        return ResponseEntity.ok(loginPageUrl)
    }

    @GetMapping("/oauth2/login/naver")
    fun naverRedirectLoginPage(session: HttpSession): ResponseEntity<String> {
        val loginPageUrl = OAuth2LoginService.naverGenerateLoginPageUrl(session = session)
        logger.info("Generated login page URL: $loginPageUrl")
        return ResponseEntity.ok(loginPageUrl)
    }

    @GetMapping("/oauth2/callback/kakao")
    fun kakaoCallback(
        @RequestParam code: String,
        response: HttpServletResponse
    ): ResponseEntity<Void> {
        val token: TokenResponse = OAuth2LoginService.kakaoRetrieveUserInfo(code)

        val refreshTokenCookie = ResponseCookie.from("refreshToken", token.refreshToken)
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .maxAge(7 * 24 * 60 * 60)
            .path("/")
            .build()

        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())

        val redirectUrl = "$frontendUrl/login/oauth-redirect?accessToken=${token.accessToken}"
        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, redirectUrl).build()
    }

    @GetMapping("/oauth2/callback/naver")
    fun naverCallback(
        @RequestParam code: String,
        @RequestParam state: String,
        response: HttpServletResponse
    ): ResponseEntity<Void> {
        val token: TokenResponse = OAuth2LoginService.naverRetrieveUserInfo(code, state)

        val refreshTokenCookie = ResponseCookie.from("refreshToken", token.refreshToken)
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .maxAge(7 * 24 * 60 * 60)
            .path("/")
            .build()

        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())

        val redirectUrl = "$frontendUrl/login/oauth-redirect?accessToken=${token.accessToken}"
        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, redirectUrl).build()
    }
}