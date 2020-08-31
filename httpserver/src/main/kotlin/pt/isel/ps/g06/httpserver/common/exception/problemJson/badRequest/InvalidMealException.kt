package pt.isel.ps.g06.httpserver.common.exception.problemJson.badRequest

class InvalidMealException(detail: String) : BaseBadRequestException(title = detail)