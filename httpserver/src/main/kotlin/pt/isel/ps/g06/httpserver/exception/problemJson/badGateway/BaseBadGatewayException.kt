package pt.isel.ps.g06.httpserver.exception.problemJson.badGateway

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.exception.problemJson.ProblemJsonException

open class BaseBadGatewayException(title: String, detail: String? = null) : ProblemJsonException(
        status = HttpStatus.BAD_GATEWAY,
        title = title,
        detail = detail
)