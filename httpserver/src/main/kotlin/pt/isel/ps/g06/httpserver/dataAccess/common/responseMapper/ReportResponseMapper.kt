package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper

import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbReportDto
import pt.isel.ps.g06.httpserver.model.Report

class ReportResponseMapper {

    fun mapToModel(dto: DbReportDto): Report =
            Report(
                    reportIdentifier = dto.report_id,
                    submitterIdentifier = dto.submitter_id,
                    submissionIdentifier = dto.submission_id,
                    text = dto.description
            )

}