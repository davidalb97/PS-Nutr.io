package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper

import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbReportSubmissionDetailDto
import pt.isel.ps.g06.httpserver.model.report.ReportSubmissionDetail

class ReportSubmissionDetailMapper {

    fun mapTo(dto: DbReportSubmissionDetailDto, submissionId: Int): ReportSubmissionDetail =
            ReportSubmissionDetail(
                    submissionName = dto._submission_name,
                    submitterId = dto.submitter_id,
                    submissionId = submissionId
            )
}