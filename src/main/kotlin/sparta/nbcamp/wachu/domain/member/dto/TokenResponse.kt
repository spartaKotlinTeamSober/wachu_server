package sparta.nbcamp.wachu.domain.member.dto

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String?,
)