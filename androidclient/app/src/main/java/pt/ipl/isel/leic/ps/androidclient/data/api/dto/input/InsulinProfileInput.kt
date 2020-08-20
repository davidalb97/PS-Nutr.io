package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input

class InsulinProfileInput(
    val name: String,
    val startTime: String,
    val endTime: String,
    val glucoseObjective: Float,
    val sensitivityFactor: Float,
    val carbohydrateRatio: Float,
    val modificationTime: String?
)