package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.model.modular.BaseSubmission

class Portion(
        identifier: Int,
        val amount: Float,
        val unit: String
) : BaseSubmission<Int>(
        identifier = identifier
)