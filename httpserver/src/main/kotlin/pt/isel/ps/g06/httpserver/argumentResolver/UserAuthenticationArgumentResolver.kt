package pt.isel.ps.g06.httpserver.argumentResolver

import org.springframework.core.MethodParameter
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import pt.isel.ps.g06.httpserver.common.AUTH_HEADER
import pt.isel.ps.g06.httpserver.common.BEARER
import pt.isel.ps.g06.httpserver.common.exception.problemJson.unauthorized.UnauthorizedException
import pt.isel.ps.g06.httpserver.common.exception.problemJson.forbidden.BaseForbiddenException
import pt.isel.ps.g06.httpserver.model.User
import pt.isel.ps.g06.httpserver.security.JwtUtil
import pt.isel.ps.g06.httpserver.service.UserService
import javax.servlet.http.HttpServletRequest

/**
 * Intercepts each request, verifying if the user is authenticated by checking its Json Web Token
 */
@Component
class UserAuthenticationArgumentResolver(
        private val userService: UserService,
        private val jwtUtil: JwtUtil
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean =
            parameter.parameterType == User::class.java

    override fun resolveArgument(
            parameter: MethodParameter,
            mavContainer: ModelAndViewContainer?,
            webRequest: NativeWebRequest,
            binderFactory: WebDataBinderFactory?
    ): Any? {

        val user = authenticateAndGetUser(webRequest)

        if (!parameter.isOptional && user == null) {
            throw UnauthorizedException()
        }

        return user
    }

    fun authenticateAndGetUser(webRequest: NativeWebRequest): User? {

        val request = webRequest.getNativeRequest(HttpServletRequest::class.java)!!

        val authHeader: String? = request.getHeader(AUTH_HEADER)

        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            return null
        }

        val jwt = authHeader.substring(7)
        val userEmail = jwtUtil.getUserEmail(jwt)

        val user = userService.getUserFromEmail(userEmail) ?: return null

        if (user.isUserBanned) {
            throw BaseForbiddenException()
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

        return user
    }
}