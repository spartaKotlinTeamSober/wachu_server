package sparta.nbcamp.wachu.infra.security.oauth

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import sparta.nbcamp.wachu.domain.member.repository.MemberRepository

@Service
class CustomOAuth2UserService : OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val accessToken = userRequest.accessToken.tokenValue
        val restTemplate = RestTemplate()
        val response = restTemplate.getForObject(
            "https://openapi.naver.com/v1/nid/me",
            Map::class.java,
            "Bearer $accessToken"
        ) as Map<String, Any>

        val userAttributes = response["response"] as Map<String, Any>
        val userName = userAttributes["name"] as String
        val userEmail = userAttributes["email"] as String
        val userNickname = userAttributes["nickname"] as String

        return DefaultOAuth2User(
            listOf(OAuth2UserAuthority(userAttributes)),
            userAttributes,
            "name"
        )
    }
}