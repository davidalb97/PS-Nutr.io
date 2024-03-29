package pt.isel.ps.g06.httpserver.exception.problemJson.notFound

class MissingInsulinProfileException(
        profileName: String?,
        title: String = "The specified user profile '$profileName' does not exist."
) : BaseNotFoundException(
        title = title
)