package sparta.nbcamp.wachu.infra.security.oauth.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class KakaoUserPropertiesResponse(
    val nickname: String,
    val profileImage: String,
)
