package pt.isel.ps.g06.httpserver.exception.problemJson.badGateway

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.exception.problemJson.ProblemJsonException

class BadGatewayException(detail: String? = null) : ProblemJsonException(
        status = HttpStatus.BAD_GATEWAY,
        title = "Our restaurant providers are currently offline, please try again later",
        detail = detail
)