package pt.isel.ps.g06.httpserver.exception.problemJson.badRequest

class SubmissionNotFavorableException(title: String = "Given submission cannot be favored!") : BaseBadRequestException(
        title = title
)