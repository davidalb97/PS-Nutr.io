package pt.isel.ps.g06.httpserver.common.exception.forbidden

class NotSubmissionOwnerException(
        detail: String = "You are not the owner of this submission and thus are unauthorized!"
) : UserForbiddenException(
        detail = detail
)