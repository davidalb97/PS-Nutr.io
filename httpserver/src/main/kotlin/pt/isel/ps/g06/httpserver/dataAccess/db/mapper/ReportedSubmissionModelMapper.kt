package pt.isel.ps.g06.httpserver.dataAccess.db.mapper

import pt.isel.ps.g06.httpserver.dataAccess.common.mapper.ModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbReportedSubmissionDto
import pt.isel.ps.g06.httpserver.model.report.ReportSubmissionDetail
import pt.isel.ps.g06.httpserver.model.report.ReportedSubmission

class ReportedSubmissionModelMapper : ModelMapper<DbReportedSubmissionDto, ReportedSubmission> {

    override fun mapTo(dto: DbReportedSubmissionDto): ReportedSubmission =
            ReportedSubmission(
                    submissionDetail = ReportSubmissionDetail(
                            submissionId = dto._submission_id,
                            submitterId = dto._submitter_id,
                            submissionName = dto._name
                    ),
                    count = dto._count
            )
}