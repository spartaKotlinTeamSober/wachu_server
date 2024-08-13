package sparta.nbcamp.wachu.domain.member.dto

import sparta.nbcamp.wachu.domain.member.entity.Member

data class ProfileResponse(
    val id: Long?,
    val email: String?,
    val nickname: String?,
    val profileUrl: String?
) {
    companion object {
        fun from(profile: Member): ProfileResponse {
            return ProfileResponse(
                id = profile.id,
                email = profile.email,
                nickname = profile.nickname,
                profileUrl = profile.profileImageUrl
            )
        }
    }
}