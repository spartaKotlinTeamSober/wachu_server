package sparta.nbcamp.wachu.domain.member.dto

import sparta.nbcamp.wachu.domain.member.entity.Member

data class SignUpResponse(
    val id: Long,
    val email: String,
    val nickname: String,
) {
    companion object {
        fun from(entity: Member): SignUpResponse {
            return SignUpResponse(
                id = entity.id!!,
                email = entity.email,
                nickname = entity.nickname
            )
        }
    }
}