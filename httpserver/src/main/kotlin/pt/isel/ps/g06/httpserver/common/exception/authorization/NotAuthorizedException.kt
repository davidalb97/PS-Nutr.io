package pt.isel.ps.g06.httpserver.common.exception.authorization

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.common.exception.BaseResponseStatusException

class NotAuthorizedException : BaseResponseStatusException(
        status = HttpStatus.FORBIDDEN,
        detail = "You are not authorized to access this resource."
)