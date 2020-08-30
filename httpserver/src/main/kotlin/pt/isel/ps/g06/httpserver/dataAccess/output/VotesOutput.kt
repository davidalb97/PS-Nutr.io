package pt.isel.ps.g06.httpserver.dataAccess.output

import pt.isel.ps.g06.httpserver.model.VoteState
import pt.isel.ps.g06.httpserver.model.Votes

data class VotesOutput(
        val isVotable: Boolean,
        val userVote: VoteState,
        val positive: Int,
        val negative: Int
)

fun toVotesOutput(isVotable: Boolean, votes: Votes, userVote: VoteState): VotesOutput {
    return VotesOutput(
            isVotable = isVotable,
            userVote = userVote,
            positive = votes.positive,
            negative = votes.negative
    )
}

fun toDefaultVotesOutput(isVotable: Boolean): VotesOutput {
    return toVotesOutput(
            isVotable = isVotable,
            userVote = VoteState.NOT_VOTED,
            votes = Votes(0, 0)
    )
}