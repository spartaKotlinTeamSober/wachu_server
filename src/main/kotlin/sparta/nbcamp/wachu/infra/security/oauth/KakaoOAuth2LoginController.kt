package sparta.nbcamp.wachu.infra.security.oauth

import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["http://localhost:8080"])
class KakaoOAuth2LoginController(
    private val kakaoOAuth2LoginService: KakaoOAuth2LoginService,
) {
    private val logger = LoggerFactory.getLogger(KakaoOAuth2LoginController::class.java)

    //1 로그인 요청 왔을때 로그인 페이지로 Redirect
    @GetMapping("/oauth2/login/kakao")
    fun redirectLoginPage(response: HttpServletResponse) {
        val loginPageUrl = kakaoOAuth2LoginService.generateLoginPageUrl()
        logger.info("Generated login page URL: $loginPageUrl")
        response.sendRedirect(loginPageUrl)
    }

    // authorization code 받아서 로그인 완료처리(토큰 뱉는거)
    @GetMapping("/oauth2/callback/kakao")
    fun callback(
        @RequestParam code: String
    ): ResponseEntity<String> {
        val accessToken: String = kakaoOAuth2LoginService.login(code)
        return ResponseEntity.ok(accessToken)
    }
}