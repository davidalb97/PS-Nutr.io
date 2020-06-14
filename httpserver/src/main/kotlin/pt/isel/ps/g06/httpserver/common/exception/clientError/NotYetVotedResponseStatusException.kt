package pt.isel.ps.g06.httpserver.common.exception.clientError

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.common.exception.BaseResponseStatusException

class NotYetVotedResponseStatusException(detail: String = "You have not yet voted for this submission!") : BaseResponseStatusException(
        status = HttpStatus.BAD_REQUEST,
        detail = detail
)