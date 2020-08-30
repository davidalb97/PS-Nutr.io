package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.model.modular.BaseSubmission
import pt.isel.ps.g06.httpserver.model.modular.INameable

class Cuisine(
        identifier: Int,
        override val name: String
): BaseSubmission<Int>(
        identifier = identifier
), INameable