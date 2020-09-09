package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.model.modular.BaseSubmission
import pt.isel.ps.g06.httpserver.model.modular.INameable
import java.time.OffsetDateTime

class InsulinProfile(
        identifier: Int,
        val startTime: String,
        val endTime: String,
        val glucoseObjective: Float,
        val insulinSensitivityFactor: Float,
        val carbohydrateRatio: Float,
        val modificationDate: OffsetDateTime,
        override val name: String
) : BaseSubmission<Int>(
        identifier = identifier
), INameable