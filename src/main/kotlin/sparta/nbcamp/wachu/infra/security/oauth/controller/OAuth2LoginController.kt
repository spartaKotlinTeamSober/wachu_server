package sparta.nbcamp.wachu.infra.security.oauth.controller

import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import sparta.nbcamp.wachu.infra.security.oauth.dto.OAuthResponse
import sparta.nbcamp.wachu.infra.security.oauth.service.OAuth2LoginService

@RestController
@CrossOrigin(origins = ["http://localhost:8080"])
class OAuth2LoginController(
    private val OAuth2LoginService: OAuth2LoginService,
) {
    private val logger = LoggerFactory.getLogger(OAuth2LoginController::class.java)

    @GetMapping("/oauth2/login/kakao")
    fun kakaoRedirectLoginPage(response: HttpServletResponse) {
        val loginPageUrl = OAuth2LoginService.kakaoGenerateLoginPageUrl()
        logger.info("Generated login page URL: $loginPageUrl")
        response.sendRedirect(loginPageUrl)
    }

    @GetMapping("/oauth2/login/naver")
    fun naverRedirectLoginPage(response: HttpServletResponse) {
        val loginPageUrl = OAuth2LoginService.naverGenerateLoginPageUrl()
        logger.info("Generated login page URL: $loginPageUrl")
        response.sendRedirect(loginPageUrl)
    }

    @GetMapping("/oauth2/callback/kakao")
    fun kakaoCallback(
        @RequestParam code: String
    ): ResponseEntity<OAuthResponse> {
        return ResponseEntity.ok(OAuth2LoginService.kakaoRetrieveUserInfo(code))
    }

    @GetMapping("/oauth2/callback/naver")
    fun naverCallback(
        @RequestParam code: String
    ): ResponseEntity<OAuthResponse> {
        return ResponseEntity.ok(OAuth2LoginService.naverRetrieveUserInfo(code))
    }
}