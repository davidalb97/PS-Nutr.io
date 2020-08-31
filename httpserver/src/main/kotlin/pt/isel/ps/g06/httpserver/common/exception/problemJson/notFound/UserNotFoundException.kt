package pt.isel.ps.g06.httpserver.common.exception.problemJson.notFound

class UserNotFoundException(title: String = "The specified user was not found"): BaseNotFoundException(
        title = title
)