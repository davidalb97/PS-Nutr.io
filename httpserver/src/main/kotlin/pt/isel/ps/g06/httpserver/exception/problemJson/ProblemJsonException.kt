package pt.isel.ps.g06.httpserver.exception.problemJson

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import pt.isel.ps.g06.httpserver.common.hypermedia.ProblemJson

/**
 * Represents a base exception that will always be caught by [BaseExceptionHandler],
 * mapped to [ProblemJson] and sent to the user as an HTTP Response
 */
open class ProblemJsonException(
        status: HttpStatus,
        val type: String = "about:blank",
        val detail: String? = null,
        val title: String
) : ResponseStatusException(status, title)