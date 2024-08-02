package sparta.nbcamp.wachu.domain.member.dto

import org.springframework.security.crypto.password.PasswordEncoder
import sparta.nbcamp.wachu.domain.member.entity.Member

data class SignUpRequest(
    val email: String,
    val password: String,
    val nickname: String,
    val confirmPassword: String,
    val code: String,
) {
    companion object {
        fun toEntity(request: SignUpRequest, passwordEncoder: PasswordEncoder): Member {
            return Member(
                email = request.email,
                password = passwordEncoder.encode(request.password),
                nickname = request.nickname,
                profileImageUrl = null
            )
        }
    }
}
