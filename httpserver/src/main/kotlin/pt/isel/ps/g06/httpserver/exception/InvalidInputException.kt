package pt.isel.ps.g06.httpserver.exception

/**
 * Exception thrown when a client provides wrong input on a HTTP-request body
 *
 * @param domain let's the user what resource the error links to, e.g: 'project'.
 * @param detail provides extra information about what parameter(s) are missing.
 */
class InvalidInputException(val domain: InvalidInputDomain, val detail: String) : Exception()

enum class InvalidInputDomain(val domain: String) {
    AUTHENTICATION("authentication"),
    SUBMITTER("submitter"),
    SUBMISSION("submission"),
    SUBMISSION_SUBMITTER("submission-submitter"),
    API("api"),
    VOTE("vote"),
    MEAL("meal"),
    CONTRACT("contract"),
    TIMEOUT("timeout"),
    SEARCH_RESTAURANT("search-restaurant"),
    CUISINE("cuisine")
}