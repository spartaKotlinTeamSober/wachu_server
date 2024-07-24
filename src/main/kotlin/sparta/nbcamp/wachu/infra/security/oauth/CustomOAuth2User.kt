package sparta.nbcamp.wachu.infra.security.oauth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import sparta.nbcamp.wachu.domain.member.entity.Member

class CustomOAuth2User(
    private val oAuth2User: OAuth2User,
    val memberId: Long
) : OAuth2User {

    override fun getAuthorities() = oAuth2User.authorities

    override fun getAttributes() = oAuth2User.attributes

    override fun getName() = oAuth2User.getAttribute<String>("name")

    fun getEmail() = oAuth2User.getAttribute<String>("email")
}
