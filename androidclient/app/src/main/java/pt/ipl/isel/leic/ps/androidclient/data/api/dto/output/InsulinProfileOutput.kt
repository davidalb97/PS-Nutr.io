package pt.ipl.isel.leic.ps.androidclient.data.api.dto.output

class InsulinProfileOutput(
    val profileName: String,
    val startTime: String,
    val endTime: String,
    val glucoseObjective: Float,
    val insulinSensitivityFactor: Float,
    val carbohydrateRatio: Float
)