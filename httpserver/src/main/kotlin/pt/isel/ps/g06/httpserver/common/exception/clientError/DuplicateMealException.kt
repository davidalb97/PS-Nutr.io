package pt.isel.ps.g06.httpserver.common.exception.clientError

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.common.exception.BaseResponseStatusException

class DuplicateMealException(title: String = "Given meal was already inserted!") : BaseResponseStatusException(
        status = HttpStatus.BAD_REQUEST,
        title = title
)