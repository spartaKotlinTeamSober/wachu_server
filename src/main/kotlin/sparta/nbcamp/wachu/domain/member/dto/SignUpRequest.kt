package sparta.nbcamp.wachu.domain.member.dto

import org.springframework.security.crypto.password.PasswordEncoder
import sparta.nbcamp.wachu.domain.member.entity.Member

data class SignUpRequest(
    val email: String,
    val password: String,
    val nickname: String,
    val confirmPassword: String,
)

fun SignUpRequest.toEntity(passwordEncoder: PasswordEncoder): Member {
    return Member(
        email = this.email,
        password = passwordEncoder.encode(this.password),
        nickname = this.nickname,
        profileImageUrl = null,
    )
}
