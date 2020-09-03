package pt.isel.ps.g06.httpserver.common.exception.problemJson.conflict

class DuplicateMealException(title: String = "Given meal was already inserted!") : BaseConflictException(
        title = title
)