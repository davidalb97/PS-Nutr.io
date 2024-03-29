package pt.isel.ps.g06.httpserver.dataAccess.output

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import pt.isel.ps.g06.httpserver.model.InsulinProfile
import java.time.OffsetDateTime

data class InsulinProfileOutput(
        val name: String,
        val startTime: String,
        val endTime: String,
        val glucoseObjective: Float,
        val sensitivityFactor: Float,
        val carbohydrateRatio: Float,
        @JsonSerialize(using = ToStringSerializer::class)
        val modificationDate: OffsetDateTime
)

fun toInsulinProfileOutput(insulinProfile: InsulinProfile): InsulinProfileOutput {
    return InsulinProfileOutput(
            name = insulinProfile.name,
            startTime = insulinProfile.startTime,
            endTime = insulinProfile.endTime,
            glucoseObjective = insulinProfile.glucoseObjective,
            sensitivityFactor = insulinProfile.insulinSensitivityFactor,
            carbohydrateRatio = insulinProfile.carbohydrateRatio,
            modificationDate = insulinProfile.modificationDate
    )
}