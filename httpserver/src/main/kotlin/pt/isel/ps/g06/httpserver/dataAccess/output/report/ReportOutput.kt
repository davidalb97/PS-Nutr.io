package pt.isel.ps.g06.httpserver.dataAccess.output.report

import pt.isel.ps.g06.httpserver.model.report.BaseReport

class ReportOutput(
        val reporterId: Int,
        val reportId: Int,
        val text: String
)

fun toReportOutput(report: BaseReport) = ReportOutput(
        reporterId = report.reporterId,
        reportId = report.reportId,
        text = report.text
)