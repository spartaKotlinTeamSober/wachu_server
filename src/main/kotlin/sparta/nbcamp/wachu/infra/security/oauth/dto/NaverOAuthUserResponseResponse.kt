package sparta.nbcamp.wachu.infra.security.oauth.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class NaverOAuthUserResponseResponse(
    val id: String,
    val nickname: String,
    val email: String?,
    val name: String?,
    @JsonProperty("profile_image")
    val profileImageUrl: String
)