package sparta.nbcamp.wachu.domain.member.dto

data class LoginRequest(
    val email: String,
    val password: String,
)