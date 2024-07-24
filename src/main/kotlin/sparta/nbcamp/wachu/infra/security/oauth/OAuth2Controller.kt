package sparta.nbcamp.wachu.infra.security.oauth

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class OAuth2Controller {

    @GetMapping("/login/oauth2/code/naver")
    fun handleNaverCallback(
        @RequestParam code: String,
        @RequestParam state: String,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseEntity<String> {
        // TODO: 인증 코드로 액세스 토큰 요청
        // TODO: 액세스 토큰으로 사용자 정보 요청
        // TODO: 사용자 정보를 통해 로그인 처리 및 JWT 발급

        // 인증 성공 후, 예를 들어 홈 페이지로 리디렉션
        response.sendRedirect("/")
        return ResponseEntity.ok("로그인 성공")
    }
}