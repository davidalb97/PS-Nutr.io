package pt.isel.ps.g06.httpserver.dataAccess.model.mapper

import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmissionReportDto
import pt.isel.ps.g06.httpserver.model.report.BaseReport

class BaseReportResponseMapper : ResponseMapper<DbSubmissionReportDto, BaseReport> {

    override fun mapTo(dto: DbSubmissionReportDto): BaseReport =
            BaseReport(
                    reporterId = dto.submitter_id,
                    reportId = dto.report_id,
                    text = dto.description
            )
}