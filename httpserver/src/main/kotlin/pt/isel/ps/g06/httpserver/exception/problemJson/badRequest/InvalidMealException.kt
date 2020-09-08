package pt.isel.ps.g06.httpserver.exception.problemJson.badRequest

class InvalidMealException(detail: String) : BaseBadRequestException(title = detail)