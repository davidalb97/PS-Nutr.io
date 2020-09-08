package pt.isel.ps.g06.httpserver.exception.problemJson.forbidden

class NotSubmissionOwnerException(
        title: String = "You are not the owner of this submission and thus are unauthorized!"
) : BaseForbiddenException(
        title = title
)