package pt.isel.ps.g06.httpserver.exception.problemJson.badRequest

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.exception.problemJson.ProblemJsonException

open class BaseBadRequestException(title: String, detail: String? = null) : ProblemJsonException(
        status = HttpStatus.BAD_REQUEST,
        title = title,
        detail = detail
)