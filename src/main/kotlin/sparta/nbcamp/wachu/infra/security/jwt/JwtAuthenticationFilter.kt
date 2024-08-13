package sparta.nbcamp.wachu.security.jwt

import io.jsonwebtoken.ExpiredJwtException
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
import sparta.nbcamp.wachu.infra.security.jwt.dto.TokenType

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

                val tokenType = it.payload.get(JwtTokenManager.TOKEN_TYPE_KEY, String::class.java)
                val memberRole = it.payload.get(JwtTokenManager.MEMBER_ROLE_KEY, String::class.java)
                val memberId: Long = it.payload.subject.toLong()

                if (tokenType == TokenType.REFRESH_TOKEN_TYPE.name) {
                    return@onSuccess
                }

                val userPrincipal = UserPrincipal(memberId = memberId, memberRole = setOf(memberRole))
                val authentication = JwtAuthenticationToken(
                    userPrincipal = userPrincipal, details = WebAuthenticationDetailsSource().buildDetails(request)
                )

                SecurityContextHolder.getContext().authentication = authentication
            }.onFailure {
                logger.debug("Token validation failed", it)
                if (it is ExpiredJwtException) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired")

                    return
                }
            }
        }

        filterChain.doFilter(request, response)
    }
}