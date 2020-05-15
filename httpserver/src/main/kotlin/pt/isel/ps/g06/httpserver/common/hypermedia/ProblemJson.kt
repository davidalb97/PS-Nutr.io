package pt.isel.ps.g06.httpserver.common.hypermedia

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

/**
 * Class used for error models, based on the [Problem Json spec](https://tools.ietf.org/html/rfc7807)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
open class ProblemJson(
        val status: HttpStatus,
        val type: String = "about:blank",
        val title: String = status.reasonPhrase,
        val detail: String?
) {
    fun toResponseEntity(): ResponseEntity<ProblemJson> = ResponseEntity
            .status(status)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(this)
}

@JsonInclude(JsonInclude.Include.NON_NULL)
class InvalidMethodArgumentProblemJson(
        detail: String = "Read the field 'invalidParams' to check which fields are invalid.",
        val invalidParams: Collection<MissingArgument>
) : ProblemJson(
        status = HttpStatus.BAD_REQUEST,
        title = "Your request parameters didn't validate.",
        detail = detail
)

data class MissingArgument(val name: String, val reason: String?)