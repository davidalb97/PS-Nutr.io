package pt.isel.ps.g06.httpserver.common.exception.clientError

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.common.exception.BaseResponseStatusException

class NonReportableSubmissionException (title: String = "This submission can not be reported")
    : BaseResponseStatusException(
        status = HttpStatus.BAD_REQUEST,
        title = title
)