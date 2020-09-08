package pt.isel.ps.g06.httpserver.dataAccess.db.mapper.row

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmissionDto
import pt.isel.ps.g06.httpserver.util.OffsetDateTimeSupport
import java.sql.ResultSet

class DbRowSubmissionMapper : RowMapper<DbSubmissionDto> {

    override fun map(rs: ResultSet?, ctx: StatementContext?): DbSubmissionDto {
        return DbSubmissionDto(
                submission_id = rs!!.getInt(SubmissionDao.id),
                submission_type = rs.getString(SubmissionDao.type),
                submission_date = OffsetDateTimeSupport.parseFromDateTimeWithWithTimeZone(
                        rs.getString(SubmissionDao.date)
                )
        )
    }
}