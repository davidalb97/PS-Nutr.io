package pt.isel.ps.g06.httpserver.dataAccess.db.dto

import org.jdbi.v3.core.mapper.reflect.ColumnName
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.InsulinProfileDao

class DbUserEncInsulinProfileDto(
        @ColumnName(InsulinProfileDao.submitterId)
        val submitterId: Int,
        @ColumnName(InsulinProfileDao.profileName)
        val profileName: String,
        @ColumnName(InsulinProfileDao.startTime)
        val startTime: String,
        @ColumnName(InsulinProfileDao.endTime)
        val endTime: String,
        @ColumnName(InsulinProfileDao.glucoseObjective)
        val glucoseObjective: String,
        @ColumnName(InsulinProfileDao.sensitivityFactor)
        val insulinSensitivityFactor: String,
        @ColumnName(InsulinProfileDao.carbRatio)
        val carbohydrateRatio: String,
        @ColumnName(InsulinProfileDao.modificationDate)
        val modificationDate: String
)