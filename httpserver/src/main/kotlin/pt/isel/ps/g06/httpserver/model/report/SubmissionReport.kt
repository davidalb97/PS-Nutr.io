package pt.isel.ps.g06.httpserver.model.report

class SubmissionReport(
        val submissionDetail: ReportSubmissionDetail,
        reportId: Int,
        reporterId: Int,
        text: String
) : BaseReport(
        reportId = reportId,
        reporterId = reporterId,
        text = text
)