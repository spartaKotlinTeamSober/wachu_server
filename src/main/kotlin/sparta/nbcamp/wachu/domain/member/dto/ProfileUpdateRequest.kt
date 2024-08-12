package sparta.nbcamp.wachu.domain.member.dto

data class ProfileUpdateRequest(
    val email: String?,
    val password: String?,
    val confirmPassword: String?,
    val nickname: String?,
)
