package pt.ipl.isel.leic.ps.androidclient.data.api.dto.output

class InsulinProfileOutput(
    val profileName: String,
    val startTime: String,
    val endTime: String,
    val glucoseObjective: Int,
    val insulinSensitivityFactor: Int,
    val carbohydrateRatio: Int
)