package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper

import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbReportSubmissionNameDto
import pt.isel.ps.g06.httpserver.model.ReportSubmissionName

class ReportSubmissionNameMapper : ResponseMapper<DbReportSubmissionNameDto?, ReportSubmissionName?> {

    override fun mapTo(dto: DbReportSubmissionNameDto?): ReportSubmissionName? =
            ReportSubmissionName(restaurantName = dto?._restaurant_name)

}