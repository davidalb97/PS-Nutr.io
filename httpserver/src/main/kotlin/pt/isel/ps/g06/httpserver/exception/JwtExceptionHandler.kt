package pt.isel.ps.g06.httpserver.exception

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureException
import io.jsonwebtoken.UnsupportedJwtException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pt.isel.ps.g06.httpserver.common.AUTH_HEADER
import pt.isel.ps.g06.httpserver.common.hypermedia.ProblemJson
import pt.isel.ps.g06.httpserver.common.hypermedia.toResponseEntity

@ControllerAdvice
class JwtExceptionHandler {

    @ExceptionHandler
    fun handleUnsupportedJwtException(ex: UnsupportedJwtException): ResponseEntity<ProblemJson> {
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        return toResponseEntity(
                status = status,
                type = "about:blank",
                detail = null,
                title = "Argument does not represent an Claims JWS."
        )
    }

    @ExceptionHandler
    fun handleMalformedJwtException(ex: MalformedJwtException): ResponseEntity<ProblemJson> {
        val status = HttpStatus.BAD_REQUEST
        return toResponseEntity(
                status = status,
                type = "about:blank",
                detail = null,
                title = "$AUTH_HEADER value is not a valid JWS."
        )
    }

    @ExceptionHandler
    fun handleSignatureException(ex: SignatureException): ResponseEntity<ProblemJson> {
        val status = HttpStatus.UNAUTHORIZED
        return toResponseEntity(
                status = status,
                type = "about:blank",
                detail = null,
                title = "JWS signature validation failed."
        )
    }

    @ExceptionHandler
    fun handleExpiredJwtException(ex: ExpiredJwtException): ResponseEntity<ProblemJson> {
        val status = HttpStatus.UNAUTHORIZED
        return toResponseEntity(
                status = status,
                type = "about:blank",
                detail = null,
                title = "The jwt token has expired."
        )
    }
}