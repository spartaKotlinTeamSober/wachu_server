package sparta.nbcamp.wachu.security.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import sparta.nbcamp.wachu.infra.security.jwt.JwtAuthenticationToken
import sparta.nbcamp.wachu.infra.security.jwt.JwtTokenManager
import sparta.nbcamp.wachu.infra.security.jwt.UserPrincipal

@Component
class JwtAuthenticationFilter(
    private val jwtTokenManager: JwtTokenManager,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        var pureToken: String? = null

        if (request.getHeader(AUTHORIZATION) != null && request.getHeader(AUTHORIZATION).startsWith("Bearer ")) {
            pureToken = request.getHeader("Authorization").substring(7)
        }
        if (pureToken != null) {

            jwtTokenManager.validateToken(pureToken).onSuccess {

                val memberRole = it.payload.get("memberRole", String::class.java)
                val memberId: Long = it.payload.subject.toLong()

                val userPrincipal = UserPrincipal(memberId = memberId, memberRole = setOf(memberRole))
                val authentication = JwtAuthenticationToken(
                    userPrincipal = userPrincipal, details = WebAuthenticationDetailsSource().buildDetails(request)
                )
                SecurityContextHolder.getContext().authentication = authentication
            }.onFailure {
                logger.debug("Token validation failed", it)
            }
        }
        filterChain.doFilter(request, response)
    }
}