package pt.isel.ps.g06.httpserver.common.exception.clientError

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.common.exception.BaseResponseStatusException

class SubmissionNotVotableException(detail: String = "Given submission is not votable!") : BaseResponseStatusException(
        status = HttpStatus.BAD_REQUEST,
        detail = detail
)