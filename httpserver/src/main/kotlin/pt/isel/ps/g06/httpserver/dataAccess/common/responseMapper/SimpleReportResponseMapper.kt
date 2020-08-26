package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper

import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSimplifiedReportDto
import pt.isel.ps.g06.httpserver.model.SimplifiedReport

class SimpleReportResponseMapper: ResponseMapper<DbSimplifiedReportDto, SimplifiedReport> {

    override fun mapTo(dto: DbSimplifiedReportDto): SimplifiedReport =
            SimplifiedReport(
                    submissionIdentifier = dto.submission_id,
                    name = dto._name,
                    reportCount = dto._count
            )
}