package sparta.nbcamp.wachu.infra.security.oauth

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class KakaoOAuth2LoginController(
    private val kakaoOAuth2LoginService : KakaoOAuth2LoginService,
) {

    //1 로그인 요청 왔을때 로그인 페이지로 Redirect
    @GetMapping("/oauth2/login/kakao")
    fun redirectLoginPage(response: HttpServletResponse){
        val loginPageUrl = kakaoOAuth2LoginService.generateLoginPageUrl()
        response.sendRedirect(loginPageUrl)
    }

    // authorization code 받아서 로그인 완료처리(토큰 뱉는거)
    @GetMapping("/oauth2/login/callback")
    fun callback(
        @RequestParam code:String
    ): ResponseEntity<String> {
        val accessToken:String = kakaoOAuth2LoginService.login(code)
        return ResponseEntity.ok(accessToken)
    }
}