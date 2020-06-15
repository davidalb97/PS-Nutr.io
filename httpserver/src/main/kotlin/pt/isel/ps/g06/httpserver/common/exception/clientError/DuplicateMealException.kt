package pt.isel.ps.g06.httpserver.common.exception.clientError

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.common.exception.BaseResponseStatusException

class DuplicateMealException(detail: String = "Given meal was already inserted!") : BaseResponseStatusException(
        status = HttpStatus.BAD_REQUEST,
        detail = detail
)