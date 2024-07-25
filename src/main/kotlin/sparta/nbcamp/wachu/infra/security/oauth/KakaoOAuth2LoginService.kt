package sparta.nbcamp.wachu.infra.security.oauth

import org.springframework.stereotype.Service
import sparta.nbcamp.wachu.infra.security.oauth.dto.OAuthResponse

@Service
class KakaoOAuth2LoginService(
    private val kakaoOAuth2LoginClient: KakaoOAuth2LoginClient,
) {

    fun generateLoginPageUrl(): String {
        return kakaoOAuth2LoginClient.generateLoginPageUrl()
    }

    fun retrieveUserInfo(code: String): OAuthResponse {
        return kakaoOAuth2LoginClient.getAccessToken(code)
            .let { kakaoOAuth2LoginClient.retrieveUserInfo(it) }
            .let {
                OAuthResponse(
                    nickname = it.properties.nickname,
                    providerId = it.id.toString(),
                    profileImageUrl = it.properties.profileImage,
                    provider = "KAKAO"
                )
            }
    }
}