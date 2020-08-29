package pt.isel.ps.g06.httpserver.common.exception.problemJson.badRequest

class MalformedRestaurantIdentifierResponseStatusException(detail: String) : BaseBadRequestException(
        title = detail
)