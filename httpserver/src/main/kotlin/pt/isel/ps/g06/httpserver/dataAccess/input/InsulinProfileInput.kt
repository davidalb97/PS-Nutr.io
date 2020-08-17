package pt.isel.ps.g06.httpserver.dataAccess.input

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class InsulinProfileInput(
        @field:NotBlank(message = "A profile name must be given!")
        val profileName: String?,

        @field:NotNull(message = "A start time must be given!")
        @field:Pattern(
                regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]\$",
                message = "Your start time must follow a HH:MM format!"
        )
        val startTime: String?,

        @field:NotNull(message = "An end time must be given!")
        @field:Pattern(
                regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]\$",
                message = "Your end time must follow a HH:MM format!"
        )
        val endTime: String?,

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