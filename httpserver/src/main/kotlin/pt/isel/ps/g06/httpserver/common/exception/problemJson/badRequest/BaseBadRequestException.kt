package pt.isel.ps.g06.httpserver.common.exception.problemJson.badRequest

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.common.exception.problemJson.ProblemJsonException

open class BaseBadRequestException(title: String): ProblemJsonException(
        status = HttpStatus.BAD_REQUEST,
        title = title
)