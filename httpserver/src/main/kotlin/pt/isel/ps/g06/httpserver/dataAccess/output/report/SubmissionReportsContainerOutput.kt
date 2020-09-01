package pt.isel.ps.g06.httpserver.dataAccess.output.report

import pt.isel.ps.g06.httpserver.model.report.BaseReport
import pt.isel.ps.g06.httpserver.model.report.ReportSubmissionDetail
import java.util.stream.Stream
import kotlin.streams.toList

class SubmissionReportsContainerOutput(
        val submissionDetail: ReportSubmissionDetailOutput,
        val reports: Collection<ReportOutput>
)

fun toSubmissionReportsContainerOutput(
        reportedSubmissionDetail: ReportSubmissionDetail,
        submissionReports: Stream<BaseReport>
) = SubmissionReportsContainerOutput(
        submissionDetail = toReportSubmissionDetailOutput(reportedSubmissionDetail),
        reports = submissionReports.map(::toReportOutput).toList()
)