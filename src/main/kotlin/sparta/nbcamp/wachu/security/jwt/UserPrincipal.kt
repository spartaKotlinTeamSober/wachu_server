package sparta.nbcamp.wachu.security.jwt

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class UserPrincipal(
    val memberId: Long,
    val authorities: Collection<GrantedAuthority>
) {

    constructor(memberId: Long, userRole: Set<String>) : this(
        memberId,
        userRole.map { SimpleGrantedAuthority("ROLE_$it") }
    )
}