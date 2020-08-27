package pt.isel.ps.g06.httpserver.common.exception.problemJson.unauthorized

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.common.exception.problemJson.ProblemJsonException

class UnauthorizedException : ProblemJsonException(
        status = HttpStatus.UNAUTHORIZED,
        title = "You are not authenticated and as thus cannot access this resource."
)