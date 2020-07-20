package pt.isel.ps.g06.httpserver.dataAccess.db.dto

import org.jdbi.v3.core.mapper.reflect.ColumnName
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.InsulinProfileDao
import java.time.OffsetDateTime

class DbUserInsulinProfileDto(
        @ColumnName(InsulinProfileDao.submitterId)
        val submitterId: Int,
        @ColumnName(InsulinProfileDao.profileName)
        val profileName: String,
        @ColumnName(InsulinProfileDao.startTime)
        val startTime: String,
        @ColumnName(InsulinProfileDao.endTime)
        val endTime: String,
        @ColumnName(InsulinProfileDao.glucoseObjective)
        val glucoseObjective: Int,
        @ColumnName(InsulinProfileDao.sensitivityFactor)
        val insulinSensitivityFactor: Int,
        @ColumnName(InsulinProfileDao.carbRatio)
        val carbohydrateRatio: Int,
        @ColumnName(InsulinProfileDao.modificationDate)
        val modificationDate: OffsetDateTime
)