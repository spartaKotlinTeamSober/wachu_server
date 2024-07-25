package sparta.nbcamp.wachu.infra.security.oauth

import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import sparta.nbcamp.wachu.infra.security.oauth.dto.OAuthResponse

@RestController
@CrossOrigin(origins = ["http://localhost:8080"])
class KakaoOAuth2LoginController(
    private val kakaoOAuth2LoginService: KakaoOAuth2LoginService,
) {
    private val logger = LoggerFactory.getLogger(KakaoOAuth2LoginController::class.java)

    @GetMapping("/oauth2/login/kakao")
    fun redirectLoginPage(response: HttpServletResponse) {
        val loginPageUrl = kakaoOAuth2LoginService.generateLoginPageUrl()
        logger.info("Generated login page URL: $loginPageUrl")
        response.sendRedirect(loginPageUrl)
    }

    @GetMapping("/oauth2/callback/kakao")
    fun callback(
        @RequestParam code: String
    ): ResponseEntity<OAuthResponse> {
        return ResponseEntity.ok(kakaoOAuth2LoginService.retrieveUserInfo(code))
    }
}