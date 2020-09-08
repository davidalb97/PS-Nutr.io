package pt.isel.ps.g06.httpserver.exception.problemJson.conflict

class DuplicateSubmitterException(title: String = "Given submitter already exists with that name!") : BaseConflictException(
        title = title
)