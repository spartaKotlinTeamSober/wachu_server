package sparta.nbcamp.wachu.infra.security.oauth.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoOAuthUserPropertiesResponse(
    val nickname: String,
    val profileImage: String,
)
