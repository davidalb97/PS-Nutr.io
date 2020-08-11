package pt.isel.ps.g06.httpserver.dataAccess.db.mapper

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.InsulinProfileDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserInsulinProfileDto
import pt.isel.ps.g06.httpserver.util.OffsetDateTimeSupport
import java.sql.ResultSet

class DbRowInsulinProfileMapper: RowMapper<DbUserInsulinProfileDto> {

    override fun map(rs: ResultSet?, ctx: StatementContext?): DbUserInsulinProfileDto {
        return DbUserInsulinProfileDto(
                submitterId = rs!!.getInt(InsulinProfileDao.submitterId),
                profileName = rs.getString(InsulinProfileDao.profileName),
                startTime = rs.getString(InsulinProfileDao.startTime),
                endTime = rs.getString(InsulinProfileDao.endTime),
                glucoseObjective = rs.getInt(InsulinProfileDao.glucoseObjective),
                insulinSensitivityFactor = rs.getInt(InsulinProfileDao.sensitivityFactor),
                carbohydrateRatio = rs.getInt(InsulinProfileDao.carbRatio),
                modificationDate = OffsetDateTimeSupport.parseFromDateTimeWithWithTimeZone(
                        rs.getString(InsulinProfileDao.modificationDate)
                )
        )
    }
}