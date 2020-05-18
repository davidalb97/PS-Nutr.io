package pt.isel.ps.g06.httpserver.dataAccess.db.mapper

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmissionDto
import pt.isel.ps.g06.httpserver.util.parsePostgresql
import java.sql.ResultSet

class SubmissionMapper: RowMapper<DbSubmissionDto> {

    override fun map(rs: ResultSet?, ctx: StatementContext?): DbSubmissionDto {
        return DbSubmissionDto(
                rs!!.getInt(SubmissionDao.id),
                rs.getString(SubmissionDao.type),
                parsePostgresql(rs.getString(SubmissionDao.date))
        )
    }
}