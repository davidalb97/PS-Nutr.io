package pt.isel.ps.g06.httpserver.dataAccess.output.report

import pt.isel.ps.g06.httpserver.model.report.ReportedSubmission

class ReportedSubmissionsOutputContainer(
        val reportedSubmissions: Collection<ReportedSubmissionOutput>
)

fun toReportedSubmissionsOutputContainer(models: Sequence<ReportedSubmission>) = ReportedSubmissionsOutputContainer(
        reportedSubmissions = models.map(::toReportedSubmissionOutput).toList()
)

