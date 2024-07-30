package sparta.nbcamp.wachu.domain.member.dto

import sparta.nbcamp.wachu.domain.member.entity.Member
import sparta.nbcamp.wachu.infra.security.oauth.dto.OAuthResponse
import java.util.UUID

data class SocialSignUpRequest(
    val email: String,
    val nickname: String,
    val code: String,
) {
    companion object {
        fun toEntity(request: SocialSignUpRequest, oauthRequest: OAuthResponse): Member {
            return Member(
                email = request.email,
                password = UUID.randomUUID().toString(),
                nickname = request.nickname,
                profileImageUrl = oauthRequest.profileImageUrl,
                provider = oauthRequest.provider,
                providerId = oauthRequest.providerId,
            )
        }
    }
}
