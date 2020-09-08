package pt.isel.ps.g06.httpserver.exception.problemJson.badRequest

/**
 * Exception thrown when a client provides wrong input on a HTTP-request body
 */
class InvalidInputException(title: String) : BaseBadRequestException(title = title)