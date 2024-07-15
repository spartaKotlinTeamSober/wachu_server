package sparta.nbcamp.wachu.domain.member.dto

data class SignUpRequest(
    val email: String,
    val password: String,
    val nickname: String,
    val confirmPassword: String,
)