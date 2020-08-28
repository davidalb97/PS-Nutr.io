package pt.isel.ps.g06.httpserver.dataAccess.output.report

import pt.isel.ps.g06.httpserver.model.DetailedReport
import pt.isel.ps.g06.httpserver.model.ReportSubmissionName

class DetailedReportContainer(
        val submissionName: String?,
        val reports: Collection<DetailedReport>
)

fun toDetailedReportContainer(
        submissionName: String?,
        reports: Collection<DetailedReport>
) = DetailedReportContainer(submissionName, reports)