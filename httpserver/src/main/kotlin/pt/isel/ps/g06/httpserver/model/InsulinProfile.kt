package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.model.modular.BaseSubmission
import pt.isel.ps.g06.httpserver.model.modular.INameable
import java.time.OffsetDateTime

class InsulinProfile(
        identifier: Int,
        val startTime: String,
        val endTime: String,
        val glucoseObjective: Int,
        val insulinSensitivityFactor: Int,
        val carbohydrateRatio: Int,
        val modificationDate: OffsetDateTime,
        override val name: String
) : BaseSubmission<Int>(
        identifier = identifier
), INameable