package pt.isel.ps.g06.httpserver.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pt.isel.ps.g06.httpserver.common.hypermedia.ProblemJson
import pt.isel.ps.g06.httpserver.common.hypermedia.toResponseEntity
import javax.validation.ConstraintViolationException

@ControllerAdvice
class ConstraintViolationExceptionHandler {

    @ExceptionHandler
    fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<ProblemJson> {
        val status = HttpStatus.BAD_REQUEST
        return toResponseEntity(
                status = status,
                type = "about:blank",
                detail = "Constraint violators: ${ex.constraintViolations.joinToString(", ")}",
                title = "Value violates constraint"
        )
    }
}