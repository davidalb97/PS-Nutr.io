package pt.isel.ps.g06.httpserver.dataAccess.db.mapper

import pt.isel.ps.g06.httpserver.dataAccess.common.mapper.ModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmissionReportDto
import pt.isel.ps.g06.httpserver.model.report.BaseReport

class BaseReportModelMapper : ModelMapper<DbSubmissionReportDto, BaseReport> {

    override fun mapTo(dto: DbSubmissionReportDto): BaseReport =
            BaseReport(
                    reporterId = dto.submitter_id,
                    reportId = dto.report_id,
                    text = dto.description
            )
}