package pt.isel.ps.g06.httpserver.dataAccess.input

data class InsulinProfileInput(
        val profileName: String,
        val startTime: String,
        val endTime: String,
        val glucoseObjective: Int,
        val insulinSensitivityFactor: Int,
        val carbohydrateRatio: Int
)