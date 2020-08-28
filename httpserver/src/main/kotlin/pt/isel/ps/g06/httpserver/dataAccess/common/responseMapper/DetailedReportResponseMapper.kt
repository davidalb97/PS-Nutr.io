package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper

import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbDetailedReportDto
import pt.isel.ps.g06.httpserver.model.DetailedReport

class DetailedReportResponseMapper : ResponseMapper<DbDetailedReportDto, DetailedReport> {

    override fun mapTo(dto: DbDetailedReportDto): DetailedReport =
            DetailedReport(
                    reportIdentifier = dto.report_id,
                    reporterIdentifier = dto.submitter_id,
                    reportDescription = dto.description
            )
}