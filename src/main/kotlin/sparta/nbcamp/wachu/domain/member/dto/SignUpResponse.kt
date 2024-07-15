package sparta.nbcamp.wachu.domain.member.dto

data class SignUpResponse(
    val id: Long,
    val email: String,
    val nickname: String,
)