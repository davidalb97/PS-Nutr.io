package pt.isel.ps.g06.httpserver.common.exception.clientError

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.common.exception.BaseResponseStatusException

class InvalidReportSubmissionException (title: String = "This submission is not reportable")
    : BaseResponseStatusException(
        status = HttpStatus.BAD_REQUEST,
        title = title
)