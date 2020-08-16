package pt.isel.ps.g06.httpserver.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import pt.isel.ps.g06.httpserver.common.BEARER
import pt.isel.ps.g06.httpserver.service.UserService
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private const val AUTH_HEADER = "Authorization"

/***
 * The JwtUtil filters the request, checking if the jwt in the authorization header is valid.
 */
@Component
class JwtFilter(
        private val jwtUtil: JwtUtil,
        private val userService: UserService
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authHeader: String? = request.getHeader(AUTH_HEADER)

        var userEmail: String? = null
        var jwt: String? = null

        if (authHeader != null && authHeader.startsWith(BEARER)) {
            jwt = authHeader.substring(7)
            userEmail = jwtUtil.getUserEmail(jwt)
        }

        if (jwt != null && userEmail != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = this.userService.loadUserByUsername(userEmail)

            if (jwtUtil.validateToken(jwt, userDetails)) {
                val userPasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities
                )

                userPasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                SecurityContextHolder.getContext().authentication = userPasswordAuthenticationToken
            }

        }
        filterChain.doFilter(request, response)
    }
}