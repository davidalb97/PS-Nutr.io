package pt.isel.ps.g06.httpserver.argumentResolver

import org.springframework.context.annotation.Lazy
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import pt.isel.ps.g06.httpserver.common.MOD_USER
import pt.isel.ps.g06.httpserver.common.exception.authorization.NotAuthorizedException
import pt.isel.ps.g06.httpserver.model.User
import pt.isel.ps.g06.httpserver.service.AuthenticationService
import pt.isel.ps.g06.httpserver.service.UserService

/**
 * Intercepts each request, verifying if the user is authenticated by checking its Json Web Token
 * and if it is a platform moderator
 */
@Component
class ModAuthorizationArgumentResolver(
        @Lazy
        private val authenticationService: AuthenticationService,
        private val userService: UserService
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean =
            parameter.parameterType == User::class.java

    override fun resolveArgument(
            parameter: MethodParameter,
            mavContainer: ModelAndViewContainer?,
            webRequest: NativeWebRequest,
            binderFactory: WebDataBinderFactory?
    ): Any? {
        val requester = webRequest
                .getHeader(HttpHeaders.AUTHORIZATION)
                ?.let(authenticationService::getEmailFromJwt)
                ?.let(userService::getUserFromEmail)

        if (requester?.userRole != MOD_USER) {
            throw NotAuthorizedException()
        }

        return requester
    }
}