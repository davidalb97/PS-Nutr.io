package pt.isel.ps.g06.httpserver.common.exception.notFound

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.common.exception.BaseResponseStatusException

class UserNotFoundException(title: String = "No user exists for given identifier!") : BaseResponseStatusException(
        status = HttpStatus.NOT_FOUND,
        title = title
)