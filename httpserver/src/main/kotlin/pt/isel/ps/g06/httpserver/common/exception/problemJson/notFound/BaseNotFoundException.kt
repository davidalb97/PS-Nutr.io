package pt.isel.ps.g06.httpserver.common.exception.problemJson.notFound

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.common.exception.problemJson.ProblemJsonException

open class BaseNotFoundException(title: String): ProblemJsonException(
        status = HttpStatus.NOT_FOUND,
        title = title
)