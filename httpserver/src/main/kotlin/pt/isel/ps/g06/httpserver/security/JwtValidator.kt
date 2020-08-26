package pt.isel.ps.g06.httpserver.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.common.BEARER
import pt.isel.ps.g06.httpserver.common.exception.authorization.NotAuthorizedException
import pt.isel.ps.g06.httpserver.service.UserService
import javax.servlet.http.HttpServletRequest

const val AUTH_HEADER = "Authorization"

@Component
class JwtValidator(
        private val jwtUtil: JwtUtil,
        private val userService: UserService
) {

    fun authenticate(request: HttpServletRequest) {

        val authHeader: String? = request.getHeader(AUTH_HEADER)

        var userEmail: String? = null
        var jwt: String? = null

        if (authHeader != null && authHeader.startsWith(BEARER)) {
            jwt = authHeader.substring(7)
            userEmail = jwtUtil.getUserEmail(jwt)
        }

        if (jwt != null && userEmail != null) {

            val user = userService.requireUserFromEmail(userEmail)

            if (user.isUserBanned) {
                throw NotAuthorizedException()
            }

            if (SecurityContextHolder.getContext().authentication == null) {

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

        }
    }
}