package pt.isel.ps.g06.httpserver.dataAccess.output.vote

import pt.isel.ps.g06.httpserver.model.Submitter

data class SimplifiedUserOutput(
        val id: Int
)

fun toSimplifiedUserOutput(submitter: Submitter): SimplifiedUserOutput {
    return SimplifiedUserOutput(
            id = submitter.identifier
    )
}