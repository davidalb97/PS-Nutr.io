package pt.isel.ps.g06.httpserver.common.exception.clientError

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.common.exception.BaseResponseStatusException

class DuplicateSubmitterException (detail: String = "Given submitter already exists with that name!")
    : BaseResponseStatusException(
        status = HttpStatus.BAD_REQUEST,
        title = detail
)