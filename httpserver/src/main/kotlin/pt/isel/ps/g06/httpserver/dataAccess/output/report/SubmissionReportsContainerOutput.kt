package pt.isel.ps.g06.httpserver.dataAccess.output.report

import pt.isel.ps.g06.httpserver.model.report.BaseReport
import pt.isel.ps.g06.httpserver.model.report.ReportSubmissionDetail

class SubmissionReportsContainerOutput(
        val submissionDetail: ReportSubmissionDetailOutput,
        val reports: Collection<ReportOutput>
)

fun toSubmissionReportsContainerOutput(
        reportedSubmissionDetail: ReportSubmissionDetail,
        submissionReports: Collection<BaseReport>
) = SubmissionReportsContainerOutput(
        submissionDetail = toReportSubmissionDetailOutput(reportedSubmissionDetail),
        reports = submissionReports.map(::toReportOutput)
)