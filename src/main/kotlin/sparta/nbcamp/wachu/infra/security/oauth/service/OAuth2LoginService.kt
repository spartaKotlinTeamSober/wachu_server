package sparta.nbcamp.wachu.infra.security.oauth.service

import org.springframework.stereotype.Service
import sparta.nbcamp.wachu.infra.security.oauth.KakaoOAuth2LoginClient
import sparta.nbcamp.wachu.infra.security.oauth.NaverOAuth2LoginClient
import sparta.nbcamp.wachu.infra.security.oauth.dto.OAuthResponse

@Service
class OAuth2LoginService(
    private val kakaoOAuth2LoginClient: KakaoOAuth2LoginClient,
    private val naverOAuth2LoginClient: NaverOAuth2LoginClient,
) {

    fun kakaoGenerateLoginPageUrl(): String {
        return kakaoOAuth2LoginClient.generateLoginPageUrl()
    }

    fun naverGenerateLoginPageUrl(): String {
        return naverOAuth2LoginClient.generateLoginPageUrl()
    }

    fun kakaoRetrieveUserInfo(code: String): OAuthResponse {
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

    fun naverRetrieveUserInfo(code: String): OAuthResponse {
        return naverOAuth2LoginClient.getAccessToken(code)
            .let { naverOAuth2LoginClient.retrieveUserInfo(it) }
            .let {
                OAuthResponse(
                    nickname = it.properties.nickname,
                    providerId = it.id.toString(),
                    profileImageUrl = it.properties.profileImage,
                    provider = "NAVER"
                )
            }
    }
}