package pt.isel.ps.g06.httpserver.dataAccess.output.report

import pt.isel.ps.g06.httpserver.model.report.SubmissionReport

data class GenericReportOutput(
        val report: ReportOutput,
        val submissionDetail: ReportSubmissionDetailOutput
)

fun toGenericReportOutput(report: SubmissionReport) = GenericReportOutput(
        report = ReportOutput(
                reporterId = report.reporterId,
                reportId = report.reportId,
                text = report.text
        ),
        submissionDetail = ReportSubmissionDetailOutput(
                submissionId = report.submissionDetail.submissionId,
                submitterId = report.submissionDetail.submitterId,
                submissionName = report.submissionDetail.submissionName
        )
)