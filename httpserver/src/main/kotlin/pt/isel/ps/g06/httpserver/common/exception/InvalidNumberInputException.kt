package pt.isel.ps.g06.httpserver.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pt.isel.ps.g06.httpserver.common.hypermedia.ProblemJson
import pt.isel.ps.g06.httpserver.common.hypermedia.toResponseEntity

@ControllerAdvice
class InvalidNumberInputException {
    @ExceptionHandler
    fun handleInvalidNumberInput(ex: NumberFormatException): ResponseEntity<ProblemJson> {
        return toResponseEntity(
                HttpStatus.BAD_REQUEST,
                title = "Bad input! Expected a numeric value but got something else instead.",
                detail = null
        )
    }
}