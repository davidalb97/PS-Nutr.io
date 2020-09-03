package pt.isel.ps.g06.httpserver.common.exception.problemJson.badRequest

class InvalidReportSubmissionException(
        title: String = "This submission is not reportable"
) : BaseBadRequestException(
        title = title
)