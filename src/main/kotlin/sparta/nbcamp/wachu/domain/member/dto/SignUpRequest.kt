package sparta.nbcamp.wachu.domain.member.dto

import sparta.nbcamp.wachu.domain.member.entity.MemberRole

data class SignUpRequest(
    val email: String,
    val password: String,
    val nickname: String,
    val profileImageUrl: String?,
    val role: MemberRole,
)
