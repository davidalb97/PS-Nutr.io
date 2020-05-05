package pt.isel.ps.g06.httpserver.common.hypermedia

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

/**
 * Class used for error models, based on the [Problem Json spec](https://tools.ietf.org/html/rfc7807)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProblemJson(
        val type: String,
        val title: String,
        val detail: String,
        val status: Int
)

fun toResponseEntity(
        status: HttpStatus,
        type: String = "about:blank",
        title: String = status.reasonPhrase,
        detail: String
): ResponseEntity<ProblemJson> {
    val problem = ProblemJson(
            type = type,
            title = title,
            detail = detail,
            status = status.value()
    )

    return ResponseEntity
            .status(status)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(problem)
}