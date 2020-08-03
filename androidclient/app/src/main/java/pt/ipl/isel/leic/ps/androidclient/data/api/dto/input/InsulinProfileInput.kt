package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input

class InsulinProfileInput(
    val name: String,
    val startTime: String,
    val endTime: String,
    val glucoseObjective: Int,
    val sensitivityFactor: Int,
    val carbohydrateRatio: Int,
    val modificationTime: String?
)