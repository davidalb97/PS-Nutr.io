package pt.isel.ps.g06.httpserver.common.exception.clientError

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.common.exception.BaseResponseStatusException

class NonVotableSubmissionException(title: String = "This submission can not be voted")
    : BaseResponseStatusException(
        status = HttpStatus.BAD_REQUEST,
        title = title
)