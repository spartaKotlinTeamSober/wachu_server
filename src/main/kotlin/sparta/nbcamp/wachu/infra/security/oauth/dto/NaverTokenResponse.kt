package sparta.nbcamp.wachu.infra.security.oauth.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class NaverTokenResponse(
    val accessToken: String,
    val tokenType: String?,
    val refreshToken: String?,
    val expiresIn: Long?,
    val scope: String?
)