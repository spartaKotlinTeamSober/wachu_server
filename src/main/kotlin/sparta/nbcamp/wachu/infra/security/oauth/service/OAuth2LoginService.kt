package sparta.nbcamp.wachu.infra.security.oauth.service

import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Service
import sparta.nbcamp.wachu.domain.member.dto.TokenResponse
import sparta.nbcamp.wachu.domain.member.service.MemberService
import sparta.nbcamp.wachu.infra.security.oauth.KakaoOAuth2LoginClient
import sparta.nbcamp.wachu.infra.security.oauth.NaverOAuth2LoginClient
import sparta.nbcamp.wachu.infra.security.oauth.dto.OAuthResponse

@Service
class OAuth2LoginService(
    private val kakaoOAuth2LoginClient: KakaoOAuth2LoginClient,
    private val naverOAuth2LoginClient: NaverOAuth2LoginClient,
    private val memberService: MemberService
) {

    fun kakaoGenerateLoginPageUrl(): String {
        return kakaoOAuth2LoginClient.generateLoginPageUrl()
    }

    fun naverGenerateLoginPageUrl(session: HttpSession): String {
        return naverOAuth2LoginClient.generateLoginPageUrl(session)
    }

    fun kakaoRetrieveUserInfo(code: String): TokenResponse {
        return kakaoOAuth2LoginClient.getAccessToken(code)
            .let { kakaoOAuth2LoginClient.retrieveUserInfo(it) }
            .let {
                memberService.socialLogin(
                    OAuthResponse(
                        nickname = it.properties.nickname,
                        providerId = it.id.toString(),
                        profileImageUrl = it.properties.profileImage,
                        provider = "KAKAO",
                        email = null,
                    )
                )
            }
    }

    fun naverRetrieveUserInfo(code: String, state: String): TokenResponse {
        return naverOAuth2LoginClient.getAccessToken(code, state)
            .let { naverOAuth2LoginClient.retrieveUserInfo(it) }
            .let {
                memberService.socialLogin(
                    OAuthResponse(
                        nickname = it.response.nickname,
                        providerId = it.response.id,
                        profileImageUrl = it.response.profileImageUrl,
                        provider = "NAVER",
                        email = it.response.email,
                    )
                )
            }
    }
}