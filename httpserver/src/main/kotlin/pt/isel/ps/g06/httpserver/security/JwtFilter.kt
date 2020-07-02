package pt.isel.ps.g06.httpserver.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private const val AUTH_HEADER = "Authorization"

@Component
class JwtFilter(
        @Autowired private val myUserDetailsService: MyUserDetailsService,
        @Autowired private val jwtUtil: JwtUtil
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authHeader: String? = request.getHeader(AUTH_HEADER)

        var username: String? = null
        var jwt: String? = null

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7)
            username = jwtUtil.getUsername(jwt)
        }

        if (jwt != null && username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = this.myUserDetailsService.loadUserByUsername(username)

            if (jwtUtil.validadeToken(jwt, userDetails)) {
                val userPasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities
                )

                userPasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                SecurityContextHolder.getContext().authentication = userPasswordAuthenticationToken
            }

            filterChain.doFilter(request, response)
        }
    }
}
