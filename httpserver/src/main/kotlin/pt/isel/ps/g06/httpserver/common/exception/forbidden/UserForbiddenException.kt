package pt.isel.ps.g06.httpserver.common.exception.forbidden

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.common.exception.BaseResponseStatusException

open class UserForbiddenException(title: String) : BaseResponseStatusException(
        status = HttpStatus.FORBIDDEN,
        title = title
)