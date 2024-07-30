package sparta.nbcamp.wachu.domain.member.dto

import sparta.nbcamp.wachu.infra.security.oauth.dto.OAuthResponse

data class CompleteSocialSignUp(
    val oauthRequest: OAuthResponse,
    val socialSignUpRequest: SocialSignUpRequest,
)
