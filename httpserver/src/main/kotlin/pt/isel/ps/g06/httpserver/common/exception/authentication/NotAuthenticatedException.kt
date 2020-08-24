package pt.isel.ps.g06.httpserver.common.exception.authentication

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.common.exception.BaseResponseStatusException

class NotAuthenticatedException : BaseResponseStatusException(
        status = HttpStatus.UNAUTHORIZED,
        title = "You are not authenticated and as thus cannot access this resource."
)