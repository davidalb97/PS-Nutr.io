package pt.isel.ps.g06.httpserver.common.interceptor

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureException
import io.jsonwebtoken.UnsupportedJwtException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import pt.isel.ps.g06.httpserver.common.hypermedia.ProblemJson
import pt.isel.ps.g06.httpserver.common.hypermedia.toResponseEntity
import pt.isel.ps.g06.httpserver.security.AUTH_HEADER

@ControllerAdvice
class JwtExceptionHandler {

    fun handleUnsupportedJwtException(ex: UnsupportedJwtException): ResponseEntity<ProblemJson> {
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        return toResponseEntity(
                status = status,
                type = status.reasonPhrase,
                detail = "Argument does not represent an Claims JWS.",
                title = "about:blank"
        )
    }

    fun handleMalformedJwtException(ex: MalformedJwtException): ResponseEntity<ProblemJson> {
        val status = HttpStatus.BAD_REQUEST
        return toResponseEntity(
                status = status,
                type = status.reasonPhrase,
                detail = "$AUTH_HEADER value is not a valid JWS.",
                title = "about:blank"
        )
    }

    fun handleSignatureException(ex: SignatureException): ResponseEntity<ProblemJson> {
        val status = HttpStatus.UNAUTHORIZED
        return toResponseEntity(
                status = status,
                type = status.reasonPhrase,
                detail = "JWS signature validation failed.",
                title = "about:blank"
        )
    }

    fun handleExpiredJwtException(ex: ExpiredJwtException): ResponseEntity<ProblemJson> {
        val status = HttpStatus.UNAUTHORIZED
        return toResponseEntity(
                status = status,
                type = "about:blank",
                detail = "The jwt token has expired.",
                title = status.reasonPhrase
        )
    }
}