package pt.isel.ps.g06.httpserver.common.interceptor

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pt.isel.ps.g06.httpserver.common.hypermedia.ProblemJson
import pt.isel.ps.g06.httpserver.common.hypermedia.toResponseEntity
import javax.validation.ConstraintViolationException

@ControllerAdvice
class InvalidCountExceptionHandler {

    @ExceptionHandler
    fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<ProblemJson> {
        val status = HttpStatus.BAD_REQUEST
        return toResponseEntity(
                status = status,
                type = status.reasonPhrase,
                detail = "Invalid count value",
                title = "about:blank"
        )
    }
}