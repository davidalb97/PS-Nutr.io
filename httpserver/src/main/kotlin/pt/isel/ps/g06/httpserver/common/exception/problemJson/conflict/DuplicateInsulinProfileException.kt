package pt.isel.ps.g06.httpserver.common.exception.problemJson.conflict

class DuplicateInsulinProfileException(title: String = "Given insulin profile already exists!") : BaseConflictException(
        title = title
)