package pt.isel.ps.g06.httpserver.common.interceptor

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pt.isel.ps.g06.httpserver.common.hypermedia.InvalidMethodArgumentProblemJson
import pt.isel.ps.g06.httpserver.common.hypermedia.MissingArgument
import pt.isel.ps.g06.httpserver.common.hypermedia.ProblemJson

@ControllerAdvice
class InvalidMethodArgumentInterceptor {
    @ExceptionHandler
    fun handleInvalidMethodArgument(ex: MethodArgumentNotValidException): ResponseEntity<ProblemJson> {
        val invalidParams = ex.bindingResult
                .fieldErrors
                .map { MissingArgument(it.field, it.defaultMessage) }

        return InvalidMethodArgumentProblemJson(invalidParams = invalidParams).toResponseEntity()
    }
}