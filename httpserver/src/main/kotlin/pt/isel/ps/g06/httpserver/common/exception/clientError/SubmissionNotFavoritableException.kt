package pt.isel.ps.g06.httpserver.common.exception.clientError

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.common.exception.BaseResponseStatusException

class SubmissionNotFavoritableException(detail: String = "Given submission cannot be favored!") : BaseResponseStatusException(
        status = HttpStatus.BAD_REQUEST,
        detail = detail
)