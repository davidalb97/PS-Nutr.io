package pt.isel.ps.g06.httpserver.exception.problemJson.badRequest

class NoSuchApiResponseStatusException(
        title: String = "Could not find an API dependency for given submitter."
) : BaseBadRequestException(
        title = title
)