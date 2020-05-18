package pt.isel.ps.g06.httpserver.dataAccess.db.mapper

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.UserDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserDto
import pt.isel.ps.g06.httpserver.util.parsePostgresql
import java.sql.ResultSet

class UserMapper: RowMapper<DbUserDto> {

    override fun map(rs: ResultSet?, ctx: StatementContext?): DbUserDto {
        return DbUserDto(
                rs!!.getInt(UserDao.id),
                rs.getString(UserDao.email),
                rs.getString(UserDao.sessionSecret),
                parsePostgresql(rs.getString(UserDao.date))
        )
    }
}