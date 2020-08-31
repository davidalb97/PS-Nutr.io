package pt.isel.ps.g06.httpserver.dataAccess.output.report

import pt.isel.ps.g06.httpserver.model.report.SubmissionReport

data class GenericReportsOutputContainer(
        val reports: Collection<GenericReportOutput>
)

fun toGenericReportsOutputContainer(reports: Collection<SubmissionReport>) = GenericReportsOutputContainer(
        reports = reports.map(::toGenericReportOutput)
)