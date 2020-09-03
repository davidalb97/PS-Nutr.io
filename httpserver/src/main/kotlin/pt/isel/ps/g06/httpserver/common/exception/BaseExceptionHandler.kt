package pt.isel.ps.g06.httpserver.common.interceptor

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pt.isel.ps.g06.httpserver.common.exception.problemJson.ProblemJsonException
import pt.isel.ps.g06.httpserver.common.hypermedia.ProblemJson
import pt.isel.ps.g06.httpserver.common.hypermedia.toResponseEntity

@ControllerAdvice
class BaseExceptionHandler {
    @ExceptionHandler
    fun handleBaseException(ex: ProblemJsonException): ResponseEntity<ProblemJson> {
        return toResponseEntity(
                status = ex.status,
                type = ex.type,
                detail = ex.detail,
                title = ex.title
        )
    }
}