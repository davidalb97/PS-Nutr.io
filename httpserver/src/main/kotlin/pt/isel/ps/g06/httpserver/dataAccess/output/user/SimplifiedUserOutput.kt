package pt.isel.ps.g06.httpserver.dataAccess.output.user

import pt.isel.ps.g06.httpserver.model.Submitter

data class SimplifiedUserOutput(
        val id: Int,
        val name: String
)

fun toSimplifiedUserOutput(submitter: Submitter): SimplifiedUserOutput {
    return SimplifiedUserOutput(
            id = submitter.identifier,
            name = submitter.name
    )
}