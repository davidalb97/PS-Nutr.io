package pt.isel.ps.g06.httpserver.dataAccess.output

import pt.isel.ps.g06.httpserver.model.VoteState

data class VotesOutput(
        val userVote: VoteState,
        val positive: Int,
        val negative: Int
)