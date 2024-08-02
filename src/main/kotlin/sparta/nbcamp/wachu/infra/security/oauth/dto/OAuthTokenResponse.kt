package sparta.nbcamp.wachu.infra.security.oauth.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OAuthTokenResponse(
    val accessToken: String,
    val tokenType: String?,
    val refreshToken: String?,
    val expiresIn: Long?,
    val scope: String?
)