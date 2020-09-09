package pt.isel.ps.g06.httpserver.exception.problemJson.badRequest


class CannotDeletePublicSubmissionException(
        title: String = "A public submission is not deletable"
) : BaseBadRequestException(
        title = title
)