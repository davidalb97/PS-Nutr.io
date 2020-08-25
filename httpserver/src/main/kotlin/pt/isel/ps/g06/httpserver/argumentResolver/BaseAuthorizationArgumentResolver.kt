package pt.isel.ps.g06.httpserver.argumentResolver

import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import pt.isel.ps.g06.httpserver.common.exception.authentication.NotAuthenticatedException
import pt.isel.ps.g06.httpserver.security.JwtValidator
import pt.isel.ps.g06.httpserver.service.AuthenticationService
import pt.isel.ps.g06.httpserver.service.UserService
import javax.servlet.http.HttpServletRequest

abstract class BaseAuthorizationArgumentResolver<T>(
        private val authenticationService: AuthenticationService,
        private val userService: UserService,
        private val jwtValidator: JwtValidator
) : HandlerMethodArgumentResolver {

    override fun resolveArgument(
            parameter: MethodParameter,
            mavContainer: ModelAndViewContainer?,
            webRequest: NativeWebRequest,
            binderFactory: WebDataBinderFactory?
    ): Any? {
        val httpServletRequest = webRequest.getNativeRequest(HttpServletRequest::class.java)!!

        return webRequest
                .getHeader(HttpHeaders.AUTHORIZATION)
                ?.also { jwtValidator.authenticate(httpServletRequest) }
                ?.let(::getTarget)
                .also { validate(parameter, it) }
    }

    abstract fun getTarget(jwt: String) : T?

    abstract fun validate(parameter: MethodParameter, target: T?)
}