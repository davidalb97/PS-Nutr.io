package pt.isel.ps.g06.httpserver.exception.problemJson.forbidden

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.exception.problemJson.ProblemJsonException

open class BaseForbiddenException(title: String = "You are not authorized to access this resource.") : ProblemJsonException(
        status = HttpStatus.FORBIDDEN,
        title = title
)