package pt.isel.ps.g06.httpserver.common.interceptor

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pt.isel.ps.g06.httpserver.common.exception.ForbiddenInsertionException
import pt.isel.ps.g06.httpserver.common.hypermedia.ProblemJson
import pt.isel.ps.g06.httpserver.common.hypermedia.toResponseEntity

@ControllerAdvice
class ForbiddenMealInsertInterceptor {
    @ExceptionHandler
    fun handleForbiddenMealInsert(ex: ForbiddenInsertionException): ResponseEntity<ProblemJson> {
        return toResponseEntity(status = HttpStatus.FORBIDDEN, detail = ex.detail)
    }
}