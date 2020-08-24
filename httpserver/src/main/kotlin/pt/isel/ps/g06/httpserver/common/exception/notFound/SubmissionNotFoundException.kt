package pt.isel.ps.g06.httpserver.common.exception.notFound

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.common.exception.BaseResponseStatusException

open class SubmissionNotFoundException(title: String = "No submission exists for given identifier!") : BaseResponseStatusException(
        status = HttpStatus.NOT_FOUND,
        title = title
)