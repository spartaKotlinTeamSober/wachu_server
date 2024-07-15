package sparta.nbcamp.wachu.security.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtTokenManager: JwtTokenManager,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        val pureToken: String?

        if (request.getHeader(AUTHORIZATION) != null && request.getHeader(AUTHORIZATION).startsWith("Bearer ")) {
            pureToken = request.getHeader("Authorization").substring(7)
        } else {
            throw RuntimeException("토큰이 null이거나 Bearer로 시작하지 않음")
        }


        jwtTokenManager.validateToken(pureToken).onSuccess {

            val memberRole = it.payload.get("memberRole", String::class.java)
            val memberId = it.payload.get("memberId", String::class.java).toLong()

            val userPrincipal = UserPrincipal(memberId = memberId, memberRole = setOf(memberRole))
            val authentication = JwtAuthenticationToken(
                userPrincipal = userPrincipal, details = WebAuthenticationDetailsSource().buildDetails(request)
            )
            SecurityContextHolder.getContext().authentication = authentication
        }.onFailure {
            logger.debug("Token validation failed", it)
        }


        filterChain.doFilter(request, response)
    }
}