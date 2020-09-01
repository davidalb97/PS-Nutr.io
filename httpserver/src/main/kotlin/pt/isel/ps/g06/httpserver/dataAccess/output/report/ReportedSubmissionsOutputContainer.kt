package pt.isel.ps.g06.httpserver.dataAccess.output.report

import pt.isel.ps.g06.httpserver.model.report.ReportedSubmission
import java.util.stream.Stream
import kotlin.streams.toList

class ReportedSubmissionsOutputContainer(
        val reportedSubmissions: Collection<ReportedSubmissionOutput>
)

fun toReportedSubmissionsOutputContainer(models: Stream<ReportedSubmission>) = ReportedSubmissionsOutputContainer(
        reportedSubmissions = models.map(::toReportedSubmissionOutput).toList()
)

