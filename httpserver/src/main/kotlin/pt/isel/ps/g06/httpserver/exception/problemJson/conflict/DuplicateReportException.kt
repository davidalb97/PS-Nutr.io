package pt.isel.ps.g06.httpserver.exception.problemJson.conflict

class DuplicateReportException(title: String = "You already reported this submission") : BaseConflictException(title = title)