package sparta.nbcamp.wachu.security.jwt

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class UserPrincipal(
    val nickname:String,
    val email:String,
    val authorities: Collection<GrantedAuthority>
){

    constructor(nickname: String, email:String, userRole: Set<String>) : this(
        nickname,
        email,
        userRole.map{ SimpleGrantedAuthority("ROLE_$it") }
    )
}