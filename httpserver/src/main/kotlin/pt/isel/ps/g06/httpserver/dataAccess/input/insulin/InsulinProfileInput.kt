package pt.isel.ps.g06.httpserver.dataAccess.input.insulin

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalTime
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class InsulinProfileInput(
        @field:NotBlank(message = "A profile name must be given!")
        val profileName: String?,

        @field:NotNull(message = "A start time must be given!")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        val startTime: LocalTime?,

        @field:NotNull(message = "An end time must be given!")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        val endTime: LocalTime?,

        @field:NotNull(message = "A glucose objective must be given!")
        @field:Min(value = 0, message = "Glucose objective must be positive!")
        val glucoseObjective: Int?,

        @field:NotNull(message = "A positive insulin sensitivity factor must be given!")
        @field:Min(value = 0, message = "Insulin sensitivity factor must be positive!")
        val insulinSensitivityFactor: Int?,

        @field:NotNull(message = "A carbohydrate ratio must be given!")
        @field:Min(value = 0, message = "Carbohydrate ratio must be positive!")
        val carbohydrateRatio: Int?
)