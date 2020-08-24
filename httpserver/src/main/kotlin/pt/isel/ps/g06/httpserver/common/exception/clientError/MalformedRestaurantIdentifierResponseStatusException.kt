package pt.isel.ps.g06.httpserver.common.exception.clientError

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.common.exception.BaseResponseStatusException

class MalformedRestaurantIdentifierResponseStatusException(detail: String) : BaseResponseStatusException(
        status = HttpStatus.BAD_REQUEST,
        title = detail
)