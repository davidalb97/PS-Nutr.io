package pt.isel.ps.g06.httpserver.common.interceptor

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pt.isel.ps.g06.httpserver.common.exception.ResourceNotFoundException
import pt.isel.ps.g06.httpserver.common.hypermedia.ProblemJson
import pt.isel.ps.g06.httpserver.common.hypermedia.toResponseEntity

@ControllerAdvice
class ResourceNotFoundInterceptor {
    @ExceptionHandler
    fun handleResourceNotFound(ex: ResourceNotFoundException): ResponseEntity<ProblemJson> {
        return toResponseEntity(status = ex.status, detail = ex.reason ?: "Given resource cannot be found.")
    }
}