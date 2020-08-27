package pt.isel.ps.g06.httpserver.common.exception.forbidden

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.common.exception.BaseResponseStatusException

open class ForbiddenException(
        title: String = "You are not authorized to access this resource."
) : BaseResponseStatusException(
        status = HttpStatus.FORBIDDEN,
        title = title
)