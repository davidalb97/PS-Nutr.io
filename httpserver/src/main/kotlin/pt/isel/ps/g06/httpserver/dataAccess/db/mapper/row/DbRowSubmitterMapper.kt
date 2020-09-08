package pt.isel.ps.g06.httpserver.dataAccess.db.mapper.row

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmitterDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto
import pt.isel.ps.g06.httpserver.util.OffsetDateTimeSupport
import java.sql.ResultSet

class DbRowSubmitterMapper : RowMapper<DbSubmitterDto> {

    override fun map(rs: ResultSet?, ctx: StatementContext?): DbSubmitterDto {
        return DbSubmitterDto(
                submitter_id = rs!!.getInt(SubmitterDao.id),
                creation_date = OffsetDateTimeSupport.parseFromDateTimeWithWithTimeZone(
                        rs.getString(SubmitterDao.date)
                ),
                submitter_type = rs.getString(SubmitterDao.type)
        )
    }
}