package pt.isel.ps.g06.httpserver.dataAccess.output.report

import pt.isel.ps.g06.httpserver.model.report.ReportSubmissionDetail

class ReportSubmissionDetailOutput(
        val submissionId: Int,
        val submitterId: Int,
        val submissionName: String
)

fun toReportSubmissionDetailOutput(submissionDetail: ReportSubmissionDetail) =
        ReportSubmissionDetailOutput(
                submissionId = submissionDetail.submissionId,
                submitterId = submissionDetail.submitterId,
                submissionName = submissionDetail.submissionName
        )