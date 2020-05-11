package pt.isel.ps.g06.httpserver.common.interceptor

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pt.isel.ps.g06.httpserver.common.exception.ResourceNotFoundException
import pt.isel.ps.g06.httpserver.common.hypermedia.ProblemJson
import pt.isel.ps.g06.httpserver.common.hypermedia.toResponseEntity
import java.util.*

@ControllerAdvice
class MethodArgumentNotValidInterceptor {
    @ExceptionHandler
    fun handleArgumentNotValid(ex: Exception): ResponseEntity<ProblemJson> {
        println("Argument not valid")
        return ResponseEntity.of(Optional.empty())
//        return toResponseEntity(status = ex.status, detail = ex.reason ?: "Given resource cannot be found.")
    }
}