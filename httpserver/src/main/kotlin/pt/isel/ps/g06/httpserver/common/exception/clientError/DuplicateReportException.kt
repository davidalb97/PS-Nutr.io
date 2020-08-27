package pt.isel.ps.g06.httpserver.common.exception.clientError

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.common.exception.BaseResponseStatusException

class DuplicateReportException(title: String = "You already reported this submission") : BaseResponseStatusException(
        status = HttpStatus.BAD_REQUEST,
        title = title)