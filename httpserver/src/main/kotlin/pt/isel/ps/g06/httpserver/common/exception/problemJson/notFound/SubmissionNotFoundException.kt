package pt.isel.ps.g06.httpserver.common.exception.problemJson.notFound

class SubmissionNotFoundException(title: String = "No submission exists for given identifier!") : BaseNotFoundException(
        title = title
)