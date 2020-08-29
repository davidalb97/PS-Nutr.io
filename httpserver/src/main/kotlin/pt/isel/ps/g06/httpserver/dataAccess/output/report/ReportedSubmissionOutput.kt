package pt.isel.ps.g06.httpserver.dataAccess.output.report

import pt.isel.ps.g06.httpserver.model.report.ReportedSubmission

class ReportedSubmissionOutput(
        val submissionDetail: ReportSubmissionDetailOutput,
        val reportCount: Int
)

fun toReportedSubmissionOutput(model: ReportedSubmission) = ReportedSubmissionOutput(
        submissionDetail = toReportSubmissionDetailOutput(model.submissionDetail),
        reportCount = model.count
)