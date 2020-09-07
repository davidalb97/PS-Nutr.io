package pt.isel.ps.g06.httpserver.exception.problemJson.badRequest

/**
 * Exception thrown when a client provides wrong input on a HTTP-request body
 *
 * @param domain let's the user what resource the error links to, e.g: 'project'.
 * @param detail provides extra information about what parameter(s) are missing.
 */
class InvalidInputException(title: String) : BaseBadRequestException(title = title)