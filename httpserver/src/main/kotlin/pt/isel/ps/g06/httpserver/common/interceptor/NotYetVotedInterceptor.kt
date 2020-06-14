package pt.isel.ps.g06.httpserver.common.interceptor

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pt.isel.ps.g06.httpserver.common.exception.clientError.NotYetVotedException
import pt.isel.ps.g06.httpserver.common.hypermedia.ProblemJson
import pt.isel.ps.g06.httpserver.common.hypermedia.toResponseEntity

@ControllerAdvice
class NotYetVotedInterceptor {
    @ExceptionHandler
    fun handleResourceNotFound(ex: NotYetVotedException): ResponseEntity<ProblemJson> {
        return toResponseEntity(
                status = HttpStatus.BAD_REQUEST,
                detail = ex.message ?: "You have not yet voted for this submission!"
        )
    }
}