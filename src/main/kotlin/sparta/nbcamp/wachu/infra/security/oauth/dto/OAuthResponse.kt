package sparta.nbcamp.wachu.infra.security.oauth.dto

data class OAuthResponse(
    var nickname: String?,
    val profileImageUrl: String,
    val provider: String,
    val providerId: String,
    var email: String?,
)
