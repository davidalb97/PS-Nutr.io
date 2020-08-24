package pt.isel.ps.g06.httpserver.common.exception.clientError

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.common.exception.BaseResponseStatusException

/**
 * Exception thrown when a client provides wrong input on a HTTP-request body
 *
 * @param domain let's the user what resource the error links to, e.g: 'project'.
 * @param detail provides extra information about what parameter(s) are missing.
 */
class InvalidInputException(
        title: String
) : BaseResponseStatusException(
        status = HttpStatus.BAD_REQUEST,
        title = title
)

enum class InvalidInputDomain(val domain: String) {
    AUTHENTICATION("authentication"),
    SUBMITTER("submitter"),
    RESTAURANT("restaurant"),
    SUBMISSION("submission"),
    SUBMISSION_SUBMITTER("submission-submitter"),
    API("api"),
    VOTE("vote"),
    MEAL("meal"),
    CONTRACT("contract"),
    TIMEOUT("timeout"),
    SEARCH_RESTAURANT("search-restaurant"),
    CUISINE("cuisine"),
    RESTAURANT_MEAL("restaurant meal"),
    INGREDIENT("ingredient")
}