package sparta.nbcamp.wachu.domain.member.emailcode.dto

data class CheckCodeRequest(
    val email: String,
    val code: String,
)
