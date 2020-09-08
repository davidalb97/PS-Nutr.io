package pt.isel.ps.g06.httpserver.exception.problemJson.badRequest

class MalformedRestaurantIdentifierResponseStatusException(detail: String) : BaseBadRequestException(
        title = detail
)