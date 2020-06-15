package pt.isel.ps.g06.httpserver.dataAccess.output.vote

import pt.isel.ps.g06.httpserver.model.VoteState
import pt.isel.ps.g06.httpserver.model.Votes

data class VotesOutput(
        val userVote: VoteState,
        val positive: Int,
        val negative: Int
)

fun toVotesOutput(votes: Votes?, userVote: VoteState): VotesOutput? {
    return votes?.let {
        VotesOutput(
                userVote = userVote,
                positive = it.positive,
                negative = it.negative
        )
    }
}