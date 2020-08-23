package pt.isel.ps.g06.httpserver.argumentResolver

import org.springframework.context.annotation.Lazy
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import pt.isel.ps.g06.httpserver.common.exception.authentication.NotAuthenticatedException
import pt.isel.ps.g06.httpserver.model.Submitter
import pt.isel.ps.g06.httpserver.service.AuthenticationService
import pt.isel.ps.g06.httpserver.service.UserService

@Component
class AuthenticationArgumentResolver(
        @Lazy
        private val authenticationService: AuthenticationService,
        private val userService: UserService
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.parameterType == Submitter::class.java

    override fun resolveArgument(
            parameter: MethodParameter,
            mavContainer: ModelAndViewContainer?,
            webRequest: NativeWebRequest,
            binderFactory: WebDataBinderFactory?
    ): Any? {
        val submitter = webRequest
                .getHeader(AUTHORIZATION)
                ?.let(authenticationService::getEmailFromJwt)
                ?.let(userService::getSubmitterFromEmail)

        if (!parameter.isOptional && submitter == null) {
            throw NotAuthenticatedException()
        }

        return submitter
    }
}