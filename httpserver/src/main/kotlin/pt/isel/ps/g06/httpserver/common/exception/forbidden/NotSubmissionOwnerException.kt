package pt.isel.ps.g06.httpserver.common.exception.forbidden

class NotSubmissionOwnerException : UserForbiddenException(
        detail = "You are not the owner of this submission and thus are unauthorized!"
)