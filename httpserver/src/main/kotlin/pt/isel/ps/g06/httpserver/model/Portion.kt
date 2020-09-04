package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.model.modular.BaseSubmission

class Portion(
        identifier: Int,
        val amount: Int,
        val unit: String
) : BaseSubmission<Int>(
        identifier = identifier
)