package pt.isel.ps.g06.httpserver.model

import java.time.OffsetDateTime

class InsulinProfile(
        val submitterId: Int,
        val profileName: String,
        val startTime: String,
        val endTime: String,
        val glucoseObjective: Int,
        val insulinSensitivityFactor: Int,
        val carbohydrateRatio: Int,
        val modificationDate: OffsetDateTime
)