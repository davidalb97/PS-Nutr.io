package pt.isel.ps.g06.httpserver.dataAccess.output.vote

import pt.isel.ps.g06.httpserver.model.Creator

data class SimplifiedUserOutput(
        val id: Int,
        val name: String
)

fun toSimplifiedUserOutput(creator: Creator): SimplifiedUserOutput {
    return SimplifiedUserOutput(
            id = creator.identifier,
            name = creator.name
    )
}