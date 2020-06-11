package pt.isel.ps.g06.httpserver.common.interceptor

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pt.isel.ps.g06.httpserver.common.exception.NoSuchApiException
import pt.isel.ps.g06.httpserver.common.hypermedia.ProblemJson
import pt.isel.ps.g06.httpserver.common.hypermedia.toResponseEntity

@ControllerAdvice
class NoSuchApiExceptionInterceptor {
    @ExceptionHandler
    fun handleNoSuchApi(ex: NoSuchApiException): ResponseEntity<ProblemJson> {
        return toResponseEntity(
                status = HttpStatus.BAD_REQUEST,
                detail = ex.message ?: "Could not find an API dependency for given submitter."
        )
    }
}