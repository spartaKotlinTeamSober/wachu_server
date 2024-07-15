package sparta.nbcamp.wachu.security.jwt

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class UserPrincipal(
    val memberId: Long,
    val authorities: Collection<GrantedAuthority>
) {

    constructor(memberId: Long, memberRole: Set<String>) : this(
        memberId,
        memberRole.map { SimpleGrantedAuthority("ROLE_$it") }
    )
}