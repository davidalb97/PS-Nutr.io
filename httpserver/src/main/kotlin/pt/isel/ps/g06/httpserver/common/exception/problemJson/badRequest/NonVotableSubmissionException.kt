package pt.isel.ps.g06.httpserver.common.exception.problemJson.badRequest

class NonVotableSubmissionException(title: String = "This submission can not be voted") : BaseBadRequestException(
        title = title
)