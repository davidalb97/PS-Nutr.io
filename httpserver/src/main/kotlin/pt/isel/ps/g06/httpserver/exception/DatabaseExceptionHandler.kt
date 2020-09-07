package pt.isel.ps.g06.httpserver.common.interceptor

import org.postgresql.util.PSQLException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pt.isel.ps.g06.httpserver.common.hypermedia.ProblemJson
import pt.isel.ps.g06.httpserver.common.hypermedia.toResponseEntity

private val log = LoggerFactory.getLogger(DatabaseExceptionHandler::class.java)

@ControllerAdvice
class DatabaseExceptionHandler {

    @ExceptionHandler
    fun handleUnsupportedJwtException(ex: PSQLException): ResponseEntity<ProblemJson> =
            processDbError(ex)

    @ExceptionHandler
    fun handleUnsupportedJwtException(ex: IllegalArgumentException): ResponseEntity<ProblemJson> =
            processDbError(ex)

    private fun processDbError(ex: Exception): ResponseEntity<ProblemJson> {
        log.error(ex.message, ex)
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        return toResponseEntity(
                status = status,
                type = "about:blank",
                detail = null,
                title = "An unexpected database error occurred."
        )
    }
}