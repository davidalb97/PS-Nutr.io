package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper

import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbReportDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbReportSubmissionDto
import pt.isel.ps.g06.httpserver.model.report.ReportSubmissionDetail
import pt.isel.ps.g06.httpserver.model.report.SubmissionReport

class ReportResponseMapper {

    fun mapToModel(dto: DbReportSubmissionDto): SubmissionReport =
            SubmissionReport(
                    reportId = dto.report_id,
                    reporterId = dto.submitter_id,
                    text = dto.description,
                    submissionDetail = ReportSubmissionDetail(
                            submissionId = dto.submission_id,
                            submitterId = dto.submission_submitter,
                            submissionName = dto._submission_name
                    )
            )

}