package sparta.nbcamp.wachu.domain.member.dto

import org.springframework.security.crypto.password.PasswordEncoder
import sparta.nbcamp.wachu.domain.member.entity.Member
import java.util.UUID

data class LoginRequest(
    val email: String?,
    val password: String?,
    val profileImageUrl: String?,
    val providerName: String?,
    val providerId: String?,
) {
    companion object {
        fun socialToEntity(request: LoginRequest, passwordEncoder: PasswordEncoder): Member {
            return Member(
                email = null,
                password = passwordEncoder.encode(UUID.randomUUID().toString()),
                nickname = null,
                profileImageUrl = request.profileImageUrl!!,
                provider = request.providerName!!,
                providerId = request.providerId!!,
            )
        }
    }
}