package sparta.nbcamp.wachu.security.jwt

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class UserPrincipal(
    val memberId: Long,
    val nickname: String,
    val email: String,
    val authorities: Collection<GrantedAuthority>
) {

    constructor(memberId: Long, nickname: String, email: String, userRole: Set<String>) : this(
        memberId,
        nickname,
        email,
        userRole.map { SimpleGrantedAuthority("ROLE_$it") }
    )
}