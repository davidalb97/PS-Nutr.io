package pt.isel.ps.g06.httpserver.dataAccess.input

import javax.validation.constraints.NotNull

data class VoteInput(
        @field:NotNull(message = "You must provide a vote!")
        val vote: Boolean?
)