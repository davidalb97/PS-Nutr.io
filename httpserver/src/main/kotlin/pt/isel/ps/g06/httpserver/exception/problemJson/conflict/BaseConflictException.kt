package pt.isel.ps.g06.httpserver.exception.problemJson.conflict

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.exception.problemJson.ProblemJsonException

open class BaseConflictException(title: String) : ProblemJsonException(
        status = HttpStatus.CONFLICT,
        title = title
)