package pt.isel.ps.g06.httpserver.common.interceptor

import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import pt.isel.ps.g06.httpserver.common.exception.BaseResponseStatusException
import pt.isel.ps.g06.httpserver.common.exception.authentication.NotAuthenticatedException
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant.DbSubmitterResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.UserDbRepository
import pt.isel.ps.g06.httpserver.model.Submitter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

const val USER_ATTRIBUTE = "pt.isel.ps.g06.httpServer.model.user"
const val SUBMITTER_ID = "submitter"

@Component
class AuthenticationInterceptor(
        private val dbUserRepository: UserDbRepository,
        private val submitterResponseMapper: DbSubmitterResponseMapper
) : HandlerInterceptor {

    /**
     * Intercepts incoming user requests and checks if they are authenticated.
     *
     * If the request is authenticated, a [Submitter] is given to following interceptors/controllers
     * in attribute [USER_ATTRIBUTE]
     */
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val submitterId = request
                .getParameter(SUBMITTER_ID)
                ?.toIntOrNull()
                ?: return true

        val submitter = dbUserRepository
                .getBySubmitterId(submitterId)
                ?.let { submitterResponseMapper.mapTo(it) }
                ?: return true

        request.setAttribute(USER_ATTRIBUTE, submitter)
        return true
    }
}

/**
 * Helper method to avoid the boilerplate code of obtaining [Submitter]
 * for given Http request.
 *
 * @throws NotAuthenticatedException if no submitter exists;
 * which is a [BaseResponseStatusException] and as such will be properly handled by [BaseExceptionHandler]
 */
fun HttpServletRequest.ensureSubmitter(): Submitter {
    val submitter = this.getAttribute(USER_ATTRIBUTE) ?: throw NotAuthenticatedException()
    return submitter as Submitter
}