package sparta.nbcamp.wachu.infra.security.oauth

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import sparta.nbcamp.wachu.domain.member.entity.MemberRole
import sparta.nbcamp.wachu.infra.security.jwt.JwtTokenManager

@Component
class OAuth2LoginSuccessHandler(
    private val jwtTokenManager: JwtTokenManager
) : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val oAuth2User = authentication.principal as OAuth2User
        val name = oAuth2User.attributes["name"] as String
        val email = oAuth2User.attributes["email"] as String

        // 사용자 정보를 사용하여 JWT 토큰 생성
        val token = jwtTokenManager.generateToken(memberId =999  , memberRole = MemberRole.MEMBER)

        response.addHeader("Authorization", "Bearer $token")
        response.sendRedirect("/")
    }
}
