package sparta.nbcamp.wachu.domain.member.dto

data class UpdateRequest(
    val email: String?,
    val password: String?,
    val nickname: String?,
)
