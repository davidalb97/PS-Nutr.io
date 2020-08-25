package pt.isel.ps.g06.httpserver.argumentResolver

import org.springframework.context.annotation.Lazy
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.ModelAndViewContainer
import pt.isel.ps.g06.httpserver.common.exception.authentication.NotAuthenticatedException
import pt.isel.ps.g06.httpserver.model.User
import pt.isel.ps.g06.httpserver.security.JwtValidator
import pt.isel.ps.g06.httpserver.service.AuthenticationService
import pt.isel.ps.g06.httpserver.service.UserService
import javax.servlet.http.HttpServletRequest

/**
 * Intercepts each request, verifying if the user is authenticated by checking its Json Web Token
 */
@Component
class UserAuthenticationArgumentResolver(
        @Lazy
        private val authenticationService: AuthenticationService,
        private val userService: UserService,
        private val jwtValidator: JwtValidator
) : BaseAuthorizationArgumentResolver<User>(
        authenticationService = authenticationService,
        userService = userService,
        jwtValidator = jwtValidator
) {

    override fun supportsParameter(parameter: MethodParameter): Boolean =
            parameter.parameterType == User::class.java

    override fun getTarget(jwt: String): User? =
        jwt.let(authenticationService::getEmailFromJwt).let(userService::getUserFromEmail)

    override fun validate(parameter: MethodParameter, target: User?) {
        if (!parameter.isOptional && target == null) {
            throw NotAuthenticatedException()
        }
    }


}