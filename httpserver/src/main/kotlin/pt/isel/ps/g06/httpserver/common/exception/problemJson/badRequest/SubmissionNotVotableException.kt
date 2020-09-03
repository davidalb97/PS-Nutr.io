package pt.isel.ps.g06.httpserver.common.exception.problemJson.badRequest

class SubmissionNotVotableException(title: String = "Given submission is not votable!") : BaseBadRequestException(
        title = title
)