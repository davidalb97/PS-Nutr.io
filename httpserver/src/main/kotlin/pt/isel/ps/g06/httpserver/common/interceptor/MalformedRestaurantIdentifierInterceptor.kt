package pt.isel.ps.g06.httpserver.common.interceptor

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pt.isel.ps.g06.httpserver.common.exception.MalformedRestaurantIdentifierException
import pt.isel.ps.g06.httpserver.common.hypermedia.ProblemJson
import pt.isel.ps.g06.httpserver.common.hypermedia.toResponseEntity

@ControllerAdvice
class MalformedRestaurantIdentifierInterceptor {
    @ExceptionHandler
    fun handleMalformedRestaurantIdentifier(ex: MalformedRestaurantIdentifierException): ResponseEntity<ProblemJson> {
        return toResponseEntity(status = HttpStatus.BAD_REQUEST, detail = ex.detail)
    }
}