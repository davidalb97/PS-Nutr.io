package pt.isel.ps.g06.httpserver.model

data class InsulinProfile(
        val submitterId: Int,
        val profileName: String,
        val startTime: String,
        val endTime: String,
        val glucoseObjective: Int,
        val insulinSensitivityFactor: Int,
        val carbohydrateRatio: Int
)